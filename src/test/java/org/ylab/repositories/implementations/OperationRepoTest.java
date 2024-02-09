package org.ylab.repositories.implementations;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.ylab.domain.models.Operation;
import org.ylab.domain.enums.Action;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.infrastructure.in.db.migrations.MigrationUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class OperationRepoTest {
    private OperationRepo repo;

    @Container
    private static PostgreSQLContainer postgres =
            new PostgreSQLContainer<>("postgres:13.3");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        MigrationUtil migrationConfig = new MigrationUtil(
                new ConnectionAdapter(postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword())
        );
        migrationConfig.migrate();
    }


    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        repo = new OperationRepo(new ConnectionAdapter(postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword())
                );


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
    @DisplayName("Проверяет результат поиска операций по айди пользователя")
    void findAllById() {
        Operation ofUser1 = new Operation(1, Action.AUTHENTICATION, LocalDateTime.now());
        Operation ofUser2 = new Operation(1, Action.LOGOUT, LocalDateTime.now());
        repo.save(ofUser1);
        repo.save(ofUser2);
        List<Operation> expected = Arrays.asList(ofUser1, ofUser2);
        Map<Long, Operation> actual = repo.findAll();
        Assertions.assertEquals(expected.size(), actual.size());
    }
}