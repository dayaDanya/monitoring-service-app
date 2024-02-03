package org.ylab.controllers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.ylab.infrastructure.in.migrations.MigrationUtil;
import org.ylab.repositories.OperationRepo;
import org.ylab.repositories.PersonRepo;
import org.ylab.usecases.PasswordUseCase;
import org.ylab.usecases.PersonUseCase;
import org.ylab.usecases.TokenService;

class PersonControllerTest {

    PersonUseCase personUseCase;
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
       personUseCase = new PersonUseCase(new PasswordUseCase(), personRepo, operationRepo, new TokenService());
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void register() {

    }

    @Test
    void authenticate() {
    }
}