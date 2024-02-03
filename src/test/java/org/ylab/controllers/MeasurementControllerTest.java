package org.ylab.controllers;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.ylab.domain.dto.MeasurementInDto;
import org.ylab.domain.dto.PersonInDto;
import org.ylab.domain.models.Person;
import org.ylab.infrastructure.in.db.MigrationUtil;
import org.ylab.repositories.implementations.*;
import org.ylab.services.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

class MeasurementControllerTest {
    MeasurementController measurementController;

    private Person person;
    @Container
    private static PostgreSQLContainer postgres =
            new PostgreSQLContainer<>("postgres:13.3");
    private PersonService personUseCase;

    private PersonController personController;

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
        MeasurementRepo measurementRepo = new MeasurementRepo(postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        OperationRepo operationRepo = new OperationRepo(postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        CounterTypeRepo repo = new CounterTypeRepo(postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        CounterRepo counterRepo = new CounterRepo(postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        TokenRepo tokenRepo = new TokenRepo(postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        CounterTypeService counterTypeUseCase = new CounterTypeService(repo);
        CounterService counterService = new CounterService(counterRepo, counterTypeUseCase);
        OperationService operationService = new OperationService(operationRepo);
        personUseCase = new PersonService(new PasswordService(), personRepo, operationService,
                new TokenService(tokenRepo));
        personController = new PersonController(personUseCase);
        MeasurementService measurementService = new MeasurementService(measurementRepo, operationService, counterService);
        measurementController = new MeasurementController(measurementService, counterService, new TokenService(tokenRepo));
        person = new Person("person",
                "password");
        personController.register(new PersonInDto(person.getEmail(), person.getPassword()));
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
    @DisplayName("Добавление измерения: успешно добавляет")
    void add_adds() {
        personController.register(new PersonInDto(person.getEmail(), person.getPassword()));
        String token = personUseCase.authenticate(person);
        Assertions.assertEquals("201 created", measurementController.add(
                new MeasurementInDto(34345.5, "HOT"),
                token
        ));
    }

    @Test
    @DisplayName("Получение всех измерений: возвращает правильное количество записей")

    void allMeasurements() {
        String token = personUseCase.authenticate(person);
        MeasurementInDto m1 = new MeasurementInDto(3244, "HOT");
        MeasurementInDto m2 = new MeasurementInDto(334244, "COLD");
        MeasurementInDto m3 = new MeasurementInDto(32424, "HEAT");
        measurementController.add(m1, token);
        measurementController.add(m2, token);
        measurementController.add(m3, token);
        Map<Long, MeasurementInDto> expected = Map.of(1L, m1, 2L, m2, 3L, m3);
        Assertions.assertEquals(expected.size(), measurementController.allMeasurements(token).getDtoMap().size());
    }

    @Test
    @DisplayName("Получение счетчиков пользователя: возвращает правильное количество счетчиков")
    void personCounters() {
        String token = personUseCase.authenticate(person);
        Assertions.assertEquals(3, measurementController.personCounters(token).getDtoMap().size());
    }
}