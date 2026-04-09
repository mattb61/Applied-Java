package cna.nl.ca.mariadb_example;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

public class RelationalDataAccessApplication implements CommandLineRunner {
    
    public static final Logger log =
        LoggerFactory.getLogger(RelationalDataAccessApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(
            RelationalDataAccessApplication.class,
            args);
    }

    private final JdbcTemplate jdbcTemplate;

    public RelationalDataAccessApplication(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Creating tables");

        jdbcTemplate.execute("DROP TABLE IF EXISTS customers");
        jdbcTemplate.execute("CREATE TABLE customers("
            + "id SERIAL"
            + ", first_name VARCHAR(255)"
            + ", last_name VARCHAR(255)"
            + ")"
        );

        List<Object[]> splitUpNames = Stream.of("John Woo")
    }
}
