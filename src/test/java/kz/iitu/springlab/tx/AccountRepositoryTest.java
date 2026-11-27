package kz.iitu.springlab.tx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired AccountRepository repository;
    @Autowired TestEntityManager em;

    @Test
    void findsAccountByOwnerName() {
        em.persist(new Account("lab12-alice", new BigDecimal("100.00")));
        em.persist(new Account("lab12-bob", new BigDecimal("200.00")));
        em.flush();
        em.clear();          // force the query to hit the database

        var found = repository.findByOwnerName("lab12-alice");

        assertThat(found).isPresent();
        assertThat(found.get().getOwnerName()).isEqualTo("lab12-alice");
        assertThat(found.get().getBalance()).isEqualByComparingTo("100.00");
    }

    @Test
    void doesNotReturnAccountOfAnotherOwner() {
        em.persist(new Account("lab12-bob", new BigDecimal("200.00")));
        em.flush();
        em.clear();

        var found = repository.findByOwnerName("lab12-alice");

        assertThat(found).isEmpty();
    }
}
