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

            switch (num) {
                case 1:
                    registration();

                    break;
                case 2:
                    authorization();
                    break;
                case 3:
                    addMeasurement();

                    break;
                case 4:
                    getLastMeasurement();

                    break;
                case 5:
                    getMeasurementByMonth();

                    break;
                case 6:
                    additionHistory();
                    break;
                case 7:
                    countersList();
                    break;
                case 8:
                    globalHistory();
                    break;
                case 9:
                    watchAudit();
                    break;
                case 10:
                    addNewCounterType();
                    break;
                case 11:
                    giveCounterType();
                    break;
                default:
                    System.out.println("Wrong number");
            }
        }
    }

    /**
     * Запрашивает у пользователя ввод аутентификационного токена, названия типа счетчика и идентификатора лица,
     * чтобы присвоить тип счетчика с использованием adminController.
     *
     * @throws IOException Если происходит ошибка ввода/вывода
     */
    private void giveCounterType() throws IOException {
        String answer;
        System.out.print("Введите аутентификационный токен: ");
        answer = consoleReader.readString();
        System.out.print("Введите название типа счетчика: ");
        String counterType = consoleReader.readString();
        System.out.print("Введите идентификатор лица: ");
        int number = consoleReader.readInt();
        System.out.println(adminController.giveCounter(
                new CounterTypeDto(counterType), number, answer));
    }

    /**
     * Запрашивает у пользователя ввод аутентификационного токена и названия нового типа счетчика,
     * чтобы добавить новый тип счетчика с использованием adminController.
     *
     * @throws IOException Если происходит ошибка ввода/вывода
     */
    private void addNewCounterType() throws IOException {
        String answer;
        System.out.print("Введите аутентификационный токен: ");
        answer = consoleReader.readString();
        System.out.print("Введите название нового типа счетчика: ");
        String counterType = consoleReader.readString();
        System.out.println(adminController.addCounterType(
                new CounterTypeDto(counterType), answer));
    }

    /**
     * Запрашивает у пользователя ввод аутентификационного токена, чтобы просмотреть информацию об аудите
     * с использованием adminController.
     *
     * @throws IOException Если происходит ошибка ввода/вывода
     */
    private void watchAudit() throws IOException {
        String answer;
        System.out.print("Введите аутентификационный токен: ");
        answer = consoleReader.readString();
        OperationOutResponse oResponse = adminController.allOperations(answer);
        System.out.println(oResponse.getMessage());
        System.out.println(oResponse.printDtos());
    }

    /**
     * Запрашивает у пользователя ввод аутентификационного токена, чтобы просмотреть глобальную историю измерений
     * с использованием adminController.
     *
     * @throws IOException Если происходит ошибка ввода/вывода
     */
    private void globalHistory() throws IOException {
        String answer;
        System.out.print("Введите аутентификационный токен: ");
        answer = consoleReader.readString();
        MeasurementOutResponse response = adminController.allMeasurements(answer);
        System.out.println(response.getMessage());
        System.out.println(response.printDtos());
    }

    /**
     * Запрашивает у пользователя ввод аутентификационного токена, чтобы получить список счетчиков,
     * связанных с лицом, с использованием measurementController.
     *
     * @throws IOException Если происходит ошибка ввода/вывода
     */
    private void countersList() throws IOException {
        String answer;
        System.out.print("Введите аутентификационный токен: ");
        answer = consoleReader.readString();
        CounterOutResponse counterOutResponse = measurementController.personCounters(answer);
        System.out.println(counterOutResponse.getMessage());
        System.out.println(counterOutResponse.printDtos());
    }

    /**
     * Запрашивает у пользователя ввод аутентификационного токена, чтобы просмотреть историю добавления измерений
     * с использованием measurementController.
     *
     * @throws IOException Если происходит ошибка ввода/вывода
     */
    private void additionHistory() throws IOException {
        MeasurementOutResponse response;
        String answer;
        System.out.print("Введите аутентификационный токен: ");
        answer = consoleReader.readString();
        response = measurementController.allMeasurements(answer);
        System.out.println(response.getMessage());
        System.out.println(response.printDtos());
    }

    /**
     * Запрашивает у пользователя ввод аутентификационного токена, названия счетчика и номер месяца
     * для получения измерений за конкретный месяц с использованием measurementController.
     *
     * @throws IOException Если происходит ошибка ввода/вывода
     */
    private void getMeasurementByMonth() throws IOException {
        String counterType;
        String answer;
        MeasurementOutResponse response;
        System.out.print("Введите аутентификационный токен: ");
        answer = consoleReader.readString();
        System.out.print("Введите название счетчика: ");
        counterType = consoleReader.readString();
        System.out.print("Введите номер месяца (1-12): ");
        int number;
        try {
            number = consoleReader.readInt();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return;
        }
        response = measurementController.measurementByMonth(
                answer, number, new CounterTypeDto(counterType));
        System.out.println(response.getMessage());
        System.out.println(response.printDtos());
    }

    /**
     * Запрашивает у пользователя ввод аутентификационного токена и названия счетчика,
     * чтобы получить последнее измерение с использованием measurementController.
     *
     * @throws IOException Если происходит ошибка ввода/вывода
     */
    private void getLastMeasurement() throws IOException {
        String counterType;
        String answer;
        System.out.print("Введите аутентификационный токен: ");
        answer = consoleReader.readString();
        System.out.print("Введите название счетчика: ");
        counterType = consoleReader.readString();
        MeasurementOutResponse response = measurementController
                .lastMeasurement(new CounterTypeDto(counterType), answer);
        System.out.println(response.getMessage());
        System.out.println(response.printDtos());
    }

    /**
     * Запрашивает у пользователя ввод аутентификационного токена, названия счетчика и текущего значения,
     * чтобы добавить новое измерение с использованием measurementController.
     *
     * @throws IOException Если происходит ошибка ввода/вывода
     */
    private void addMeasurement() throws IOException {
        String answer;
        System.out.print("Введите аутентификационный токен: ");
        answer = consoleReader.readString();
        System.out.print("Введите название счетчика: ");
        String counterType = consoleReader.readString();
        System.out.print("Введите текущее значение: ");
        double value;
        try {
            value = consoleReader.readDouble();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println(measurementController.add(new MeasurementInDto(value, counterType), answer));
    }

    /**
     * Запрашивает у пользователя ввод электронной почты и пароля для аутентификации
     * с использованием personController.
     *
     * @throws IOException Если происходит ошибка ввода/вывода
     */
    private void authorization() throws IOException {
        PersonInDto dto;
        dto = new PersonInDto();
        System.out.print("Введите адрес электронной почты, пожалуйста: ");
        dto.setEmail(consoleReader.readString());
        System.out.print("Введите пароль, пожалуйста: ");
        dto.setPassword(consoleReader.readString());
        System.out.println(personController.authenticate(dto));
    }

    /**
     * Запрашивает у пользователя ввод электронной почты и пароля для регистрации
     * с использованием personController.
     *
     * @throws IOException Если происходит ошибка ввода/вывода
     */
    private void registration() throws IOException {
        String answer;
        PersonInDto dto = new PersonInDto();
        System.out.print("Введите адрес электронной почты, пожалуйста: ");
        answer = consoleReader.readString();
        dto.setEmail(answer);
        System.out.print("Введите пароль, пожалуйста: ");
        answer = consoleReader.readString();
        dto.setPassword(answer);
        System.out.println(personController.register(dto));
    }

}
