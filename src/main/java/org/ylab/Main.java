package org.ylab;

import org.ylab.cli.ConsoleCLI;
import org.ylab.controllers.AdminController;
import org.ylab.controllers.MeasurementController;
import org.ylab.controllers.PersonController;
import org.ylab.infrastructure.in.ConsoleReader;
import org.ylab.infrastructure.in.migrations.MigrationUtil;
import org.ylab.repositories.*;
import org.ylab.usecases.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        PersonRepo personRepo = new PersonRepo();
        OperationUseCase operationUseCase = new OperationUseCase(new OperationRepo());
        CounterTypeUseCase counterTypeUseCase = new CounterTypeUseCase(new CounterTypeRepo());
        CounterUseCase counterUseCase = new CounterUseCase(new CounterRepo() ,counterTypeUseCase);
        TokenService tokenService = new TokenService();
        MeasurementUseCase measurementUseCase = new MeasurementUseCase(
                new MeasurementRepo(), operationUseCase, counterUseCase);
        PersonUseCase personUseCase = new PersonUseCase(
                new PasswordUseCase(), personRepo, operationUseCase,
                tokenService, counterUseCase, counterTypeUseCase);
        ConsoleCLI cli  = new ConsoleCLI(new PersonController(personUseCase),
                new MeasurementController(measurementUseCase, counterUseCase, tokenService),
                new AdminController(measurementUseCase, operationUseCase, personUseCase,
                        tokenService, counterTypeUseCase, counterUseCase),
                new ConsoleReader());

        MigrationUtil migrationUtil = new MigrationUtil();
        try {
            migrationUtil.migrate();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            cli.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}