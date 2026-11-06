package kz.iitu.springlab.tx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

// Тесттер әдейі @Transactional емес: әйтпесе тест өз транзакциясымен
// commit/rollback нәтижесін жасырып жібереді.
@SpringBootTest
class TxLabTests {

    @Autowired AccountService service;
    @Autowired AccountRepository repo;
    @Autowired OuterService outer;
    @Autowired AuditRepository auditRepo;
    @Autowired JdbcTemplate jdbc;
    @Autowired PlatformTransactionManager txManager;

    @BeforeEach
    void reset() {
        jdbc.update("DELETE FROM audit_log");
        jdbc.update("DELETE FROM accounts");
        jdbc.update("INSERT INTO accounts (owner_name, balance) VALUES ('Alice', 1000.00), ('Bob', 500.00)");
    }

    private Account alice() { return repo.findByOwnerName("Alice").orElseThrow(); }
    private Account bob() { return repo.findByOwnerName("Bob").orElseThrow(); }
    private static BigDecimal bd(String s) { return new BigDecimal(s); }
    private static void assertBal(String expected, Account a) {
        assertEquals(0, bd(expected).compareTo(a.getBalance()),
                a.getOwnerName() + " expected " + expected + " but was " + a.getBalance());
    }

    // ---------- Task A ----------
    @Test
    void a1_successfulTransfer() {
        service.transfer(alice().getId(), bob().getId(), bd("100.00"));
        assertBal("900.00", alice());
        assertBal("600.00", bob());
    }

    @Test
    void a2_insufficientFunds_noChange() {
        Long aliceId = alice().getId();
        Long bobId = bob().getId();
        assertThrows(IllegalStateException.class,
                () -> service.transfer(aliceId, bobId, bd("5000.00")));
        assertBal("1000.00", alice());
        assertBal("500.00", bob());
    }

    @Test
    void a3_uncheckedExceptionAfterDebit_rollsBack() {
        assertThrows(RuntimeException.class,
                () -> service.transferFailAfterDebit(alice().getId(), bob().getId(), bd("100.00")));
        assertBal("1000.00", alice());
        assertBal("500.00", bob());
    }

    @Test
    void a4_checkedException_defaultCommits() {
        assertThrows(Exception.class,
                () -> service.transferCheckedDefault(alice().getId(), bob().getId(), bd("100.00")));
        assertBal("900.00", alice()); // debit COMMIT болды -> ақша жоғалды!
    }

    @Test
    void a4_checkedException_withRollbackFor_rollsBack() {
        assertThrows(Exception.class,
                () -> service.transferCheckedRollbackFor(alice().getId(), bob().getId(), bd("100.00")));
        assertBal("1000.00", alice());
    }

    // ---------- Task B ----------
    @Test
    void b1_required_auditRolledBackWithOuter() {
        assertThrows(IllegalStateException.class, () -> outer.outerWithRequired(true));
        assertEquals(0, auditRepo.count());
    }

    @Test
    void b2_requiresNew_auditSurvivesOuterRollback() {
        assertThrows(IllegalStateException.class, () -> outer.outerWithRequiresNew(true));
        assertEquals(1, auditRepo.count());
    }

    // ---------- Task C ----------
    private BigDecimal[] readTwice(int isolation) throws Exception {
        CountDownLatch firstReadDone = new CountDownLatch(1);
        CountDownLatch writerDone = new CountDownLatch(1);
        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            TransactionTemplate readerTx = new TransactionTemplate(txManager);
            readerTx.setIsolationLevel(isolation);
            Future<BigDecimal[]> f = pool.submit(() -> readerTx.execute(status -> {
                BigDecimal first;
                try {
                    first = jdbc.queryForObject(
                            "SELECT balance FROM accounts WHERE owner_name='Alice'", BigDecimal.class);
                } finally {
                    firstReadDone.countDown();
                }
                try { assertTrue(writerDone.await(10, TimeUnit.SECONDS)); }
                catch (InterruptedException e) { throw new RuntimeException(e); }
                BigDecimal second = jdbc.queryForObject(
                        "SELECT balance FROM accounts WHERE owner_name='Alice'", BigDecimal.class);
                return new BigDecimal[]{first, second};
            }));
            assertTrue(firstReadDone.await(10, TimeUnit.SECONDS));
            new TransactionTemplate(txManager).executeWithoutResult(s ->
                    jdbc.update("UPDATE accounts SET balance = balance + 50 WHERE owner_name='Alice'"));
            writerDone.countDown();
            return f.get(15, TimeUnit.SECONDS);
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    void c1_readCommitted_secondReadSeesCommittedChange() throws Exception {
        BigDecimal[] r = readTwice(TransactionDefinition.ISOLATION_READ_COMMITTED);
        System.out.println(">>> READ_COMMITTED  (H2): first=" + r[0] + " second=" + r[1]);
        assertNotEquals(0, r[0].compareTo(r[1]), "non-repeatable read expected under READ_COMMITTED");
    }

    @Test
    void c2_repeatableRead_observe() throws Exception {
        BigDecimal[] r = readTwice(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        System.out.println(">>> REPEATABLE_READ (H2): first=" + r[0] + " second=" + r[1]);
        // Қатаң тексермейміз: нәтиже дерекқорға байланысты. Көргеніңізді есепке жазыңыз.
    }

    // ---------- Task D ----------
    @Test
    void d_optimisticLock_staleUpdateRejected() {
        Account copy1 = alice();
        Account copy2 = alice();
        assertEquals(copy1.getVersion(), copy2.getVersion());

        copy1.credit(bd("10.00"));
        repo.save(copy1);          // commit, version өседі

        copy2.credit(bd("20.00"));
        assertThrows(OptimisticLockingFailureException.class, () -> repo.save(copy2));

        assertBal("1010.00", alice()); // бірінші өзгеріс үстінен жазылмады
    }
}