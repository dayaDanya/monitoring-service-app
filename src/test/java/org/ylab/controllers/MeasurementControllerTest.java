package org.ylab.controllers;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.ylab.domain.dto.MeasurementInDto;
import org.ylab.domain.models.Person;
import org.ylab.infrastructure.in.migrations.MigrationUtil;
import org.ylab.repositories.*;
import org.ylab.usecases.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class MeasurementControllerTest {
    MeasurementController measurementController;

    Person person;
    @Container
    private static PostgreSQLContainer postgres =
            new PostgreSQLContainer<>("postgres:13.3");
     PersonRepo personRepo;

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
        personRepo = new PersonRepo(postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        MeasurementRepo measurementRepo = new MeasurementRepo(postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        OperationRepo operationRepo = new OperationRepo(postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        CounterTypeUseCase counterTypeUseCase = new CounterTypeUseCase(new CounterTypeRepo());
        CounterUseCase counterUseCase = new CounterUseCase(new CounterRepo(), counterTypeUseCase);
        OperationUseCase operationUseCase = new OperationUseCase(operationRepo);
        PersonUseCase personUseCase = new PersonUseCase(new PasswordUseCase(), personRepo, operationUseCase,
                new TokenService(personRepo), counterUseCase, counterTypeUseCase);
        MeasurementUseCase measurementUseCase = new MeasurementUseCase(measurementRepo, operationUseCase, counterUseCase);
        measurementController = new MeasurementController(measurementUseCase, counterUseCase, new TokenService(personRepo));
        person = new Person("user", "password");
        personUseCase.register(person);
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
    void add() {

        Assertions.assertEquals("201 created", measurementController.add(
                new MeasurementInDto(34345.5, "HOT"), personRepo.
        ));
    }

    @Test
    void allMeasurements() {
    }

    @Test
    void lastMeasurement() {
    }

    @Test
    void measurementByMonth() {
    }

    @Test
    void personCounters() {
    }
}