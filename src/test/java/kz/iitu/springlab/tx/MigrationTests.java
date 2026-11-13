package kz.iitu.springlab.tx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MigrationTests {
    @Autowired
    JdbcTemplate jdbc;

    @Test
    void allMigrationsAppliedOnce() {
        List<Map<String, Object>> rows = jdbc.queryForList(
                "SELECT \"installed_rank\", \"version\", \"description\", \"success\" " +
                        "FROM \"flyway_schema_history\" WHERE \"version\" IN ('1','2','3','4') " +
                        "ORDER BY \"installed_rank\"");
        rows.forEach(r -> System.out.println(">>> flyway_schema_history: " + r));
        assertEquals(4, rows.size());
        rows.forEach(r -> assertEquals(true, r.get("success")));
    }
}