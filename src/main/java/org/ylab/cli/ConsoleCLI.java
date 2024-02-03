package org.ylab.cli;


import org.ylab.controllers.AdminController;
import org.ylab.controllers.MeasurementController;
import org.ylab.controllers.PersonController;
import org.ylab.domain.dto.*;
import org.ylab.infrastructure.in.console.ConsoleReader;

import java.io.IOException;

/**
 * Класс, представляющий консольный интерфейс пользователя.
 */
public class ConsoleCLI {
    private final PersonController personController;
    private final MeasurementController measurementController;
    private final AdminController adminController;
    private final ConsoleReader consoleReader;

    /**
     * Конструктор класса ConsoleCLI, инициализирующий используемые компоненты.
     */
    public ConsoleCLI(PersonController personController, MeasurementController measurementController,
                      AdminController adminController, ConsoleReader consoleReader) {
        this.personController = personController;
        this.measurementController = measurementController;
        this.adminController = adminController;
        this.consoleReader = consoleReader;
    }

    /**
     * Метод для запуска консольного интерфейса.
     *
     * @throws IOException Исключение, если произошла ошибка ввода-вывода.
     */
    public void run() throws IOException {
        while (true) {
            System.out.println("1. Sign up");
            System.out.println("2. Log in");
            System.out.println("3. Add measurement");
            System.out.println("4. Get last measurement");
            System.out.println("5. Get measurements by month");
            System.out.println("6. Watch own addition history");
            System.out.println("7. Get counters list");
            System.out.println("8. Watch global history (admin)");
            System.out.println("9. Watch audit (admin)");
            System.out.println("10. Add new counter type (admin)");
            System.out.println("11. Give new counter to a person (admin)");
            int num;
            num = consoleReader.readInt();

            String answer;
            switch (num) {
                case 1:
                    PersonInDto dto = new PersonInDto();
                    System.out.print("Enter email, please: ");
                    answer = consoleReader.readString();
                    dto.setEmail(answer);
                    System.out.print("Enter password, please: ");
                    answer = consoleReader.readString();
                    dto.setPassword(answer);
                    System.out.println(personController.register(dto));
                    break;
                case 2:
                    dto = new PersonInDto();
                    System.out.print("Enter email, please: ");
                    dto.setEmail(consoleReader.readString());
                    System.out.print("Enter password, please: ");
                    dto.setPassword(consoleReader.readString());
                    System.out.println(personController.authenticate(dto));
                    break;
                case 3:
                    System.out.print("Enter authentication token: ");
                    answer = consoleReader.readString();
                    System.out.print("Enter counter type: ");
                    String counterType = consoleReader.readString();
                    System.out.print("Enter current value: ");
                    double value;
                    try {
                        value = consoleReader.readDouble();
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    System.out.println(measurementController.add(new MeasurementInDto(value, counterType), answer));
                    break;
                case 4:
                    System.out.print("Enter authentication token: ");
                    answer = consoleReader.readString();
                    System.out.print("Enter counter type: ");
                    counterType = consoleReader.readString();
                    MeasurementOutResponse response = measurementController
                            .lastMeasurement(new CounterTypeDto(counterType), answer);
                    System.out.println(response.getMessage());
                    System.out.println(response.printDtos());
                    break;
                case 5:
                    System.out.print("Enter authentication token: ");
                    answer = consoleReader.readString();
                    System.out.print("Enter counter type: ");
                    counterType = consoleReader.readString();
                    System.out.print("Enter number of month (1-12): ");
                    int number;
                    try {
                        number = consoleReader.readInt();
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    response = measurementController.measurementByMonth(
                            answer, number, new CounterTypeDto(counterType));
                    System.out.println(response.getMessage());
                    System.out.println(response.printDtos());
                    break;
                case 6:
                    System.out.print("Enter authentication token: ");
                    answer = consoleReader.readString();
                    response = measurementController.allMeasurements(answer);
                    System.out.println(response.getMessage());
                    System.out.println(response.printDtos());
                    break;
                case 7:
                    System.out.print("Enter authentication token: ");
                    answer = consoleReader.readString();
                    CounterOutResponse counterOutResponse = measurementController.personCounters(answer);
                    System.out.println(counterOutResponse.getMessage());
                    System.out.println(counterOutResponse.printDtos());
                    break;
                case 8:
                    System.out.print("Enter authentication token: ");
                    answer = consoleReader.readString();
                    response = adminController.allMeasurements(answer);
                    System.out.println(response.getMessage());
                    System.out.println(response.printDtos());
                    break;
                case 9:
                    System.out.print("Enter authentication token: ");
                    answer = consoleReader.readString();
                    OperationOutResponse oResponse = adminController.allOperations(answer);
                    System.out.println(oResponse.getMessage());
                    System.out.println(oResponse.printDtos());
                    break;
                case 10:
                    System.out.print("Enter authentication token: ");
                    answer = consoleReader.readString();
                    System.out.print("Enter new counter type name: ");
                    counterType = consoleReader.readString();
                    System.out.println(adminController.addCounterType(
                            new CounterTypeDto(counterType), answer));
                    break;
                case 11:
                    System.out.print("Enter authentication token: ");
                    answer = consoleReader.readString();
                    System.out.print("Enter counter type name: ");
                    counterType = consoleReader.readString();
                    System.out.print("Enter person id: ");
                    number = consoleReader.readInt();
                    System.out.println(adminController.giveCounter(
                            new CounterTypeDto(counterType), number, answer));
                    break;
                default:
                    System.out.println("Wrong number");
            }
        }
    }
}
