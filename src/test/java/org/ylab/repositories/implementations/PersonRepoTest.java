package org.ylab.repositories.implementations;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.ylab.domain.models.Person;
import org.ylab.domain.models.enums.Role;
import org.ylab.infrastructure.in.db.MigrationUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class PersonRepoTest {
    private PersonRepo personRepo;
    @Container
    private static PostgreSQLContainer postgres =
            new PostgreSQLContainer<>("postgres:13.3");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        MigrationUtil migrationConfig = new MigrationUtil(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        migrationConfig.migrate();
    }


    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        personRepo = new PersonRepo(postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());


    }
    @AfterEach
    void tearDown() {
        try (Connection connection = DriverManager
                .getConnection(postgres.getJdbcUrl(),
                        postgres.getUsername(),
                        postgres.getPassword())) {
            connection.setAutoCommit(false);
            String insertDataSQL = "delete from entities.person";
            PreparedStatement deleteDataStatement = connection.prepareStatement(insertDataSQL);
            deleteDataStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Проверяет правильность поиска по  id")
    void findById_equal() {
        Person expected = new Person("email", "password");
        personRepo.save(expected);
        long id = personRepo.findIdByEmail("email").get();
        expected.setId(id);
        expected.setRole(Role.USER);
        Assertions.assertEquals(expected.getEmail(), personRepo.findById(id).get().getEmail());
    }
}