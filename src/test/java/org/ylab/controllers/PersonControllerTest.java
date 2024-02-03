package org.ylab.controllers;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.ylab.domain.dto.PersonInDto;
import org.ylab.infrastructure.in.migrations.MigrationUtil;
import org.ylab.repositories.*;
import org.ylab.usecases.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class PersonControllerTest {

    PersonController personController;
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

    @BeforeEach
    void setUp() {
        PersonRepo personRepo = new PersonRepo(postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        OperationRepo operationRepo = new OperationRepo(postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        CounterTypeUseCase counterTypeUseCase = new CounterTypeUseCase(new CounterTypeRepo());
        CounterUseCase counterUseCase = new CounterUseCase(new CounterRepo(), counterTypeUseCase);
        OperationUseCase operationUseCase = new OperationUseCase(operationRepo);
        PersonUseCase personUseCase = new PersonUseCase(new PasswordUseCase(), personRepo, operationUseCase,
                new TokenService(new TokenRepo(), personRepo), counterUseCase, counterTypeUseCase);
        personController = new PersonController(personUseCase);
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
            //todo connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void afterAll() {

        postgres.stop();
    }

    @Test
    void register_200() {
        PersonInDto person = new PersonInDto("email", "password");
        Assertions.assertEquals("201 created", personController.register(person));
    }

    @Test
    void register_400() {
        PersonInDto person = new PersonInDto("email", "password");
        personController.register(person);
        Assertions.assertEquals("400 bad request",
                personController.register(person));
    }

    @Test
    void authenticate_200() {
        PersonInDto person = new PersonInDto("email", "password");
        personController.register(person);
        String result = personController.authenticate(person);
        Assertions.assertEquals("200 OK. Your authorization token is:",
                result.substring(0, result.indexOf(":") + 1));
    }

    @Test
    void authenticate_400() {
        PersonInDto person = new PersonInDto("email", "password");
        String result = personController.authenticate(person);
        Assertions.assertEquals("400 bad request:",
                result.substring(0, result.indexOf(":") + 1));
    }
}