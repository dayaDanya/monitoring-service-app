package org.ylab;

import org.ylab.cli.ConsoleCLI;
import org.ylab.controllers.AdminController;
import org.ylab.controllers.MeasurementController;
import org.ylab.controllers.PersonController;
import org.ylab.infrastructure.in.console.ConsoleReader;
import org.ylab.infrastructure.in.db.MigrationUtil;
import org.ylab.repositories.implementations.*;
import org.ylab.services.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        PersonRepo personRepo = new PersonRepo();
        OperationService operationUseCase = new OperationService(new OperationRepo());
        CounterTypeService counterTypeUseCase = new CounterTypeService(new CounterTypeRepo());
        CounterService counterUseCase = new CounterService(new CounterRepo() ,counterTypeUseCase);
        TokenService tokenService = new TokenService(new TokenRepo());
        MeasurementService measurementUseCase = new MeasurementService(
                new MeasurementRepo(), operationUseCase, counterUseCase);
        PersonService personUseCase = new PersonService(
                new PasswordService(), personRepo, operationUseCase,
                tokenService);
        ConsoleCLI cli  = new ConsoleCLI(new PersonController(personUseCase),
                new MeasurementController(measurementUseCase, counterUseCase, tokenService),
                new AdminController(measurementUseCase, operationUseCase, personUseCase,
                        tokenService, counterTypeUseCase, counterUseCase),
                new ConsoleReader());

        MigrationUtil migrationUtil = new MigrationUtil();

            migrationUtil.migrate();

        try {
            cli.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}