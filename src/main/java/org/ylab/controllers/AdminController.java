package org.ylab.controllers;

import org.ylab.domain.dto.*;
import org.ylab.domain.models.Counter;
import org.ylab.domain.models.CounterType;
import org.ylab.domain.models.Operation;
import org.ylab.domain.models.Person;
import org.ylab.domain.models.enums.Action;
import org.ylab.domain.models.enums.Role;
import org.ylab.usecases.*;
import org.ylab.util.CounterTypeNotFoundException;
import org.ylab.util.PersonNotFoundException;
import org.ylab.util.TokenNotActualException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Класс, представляющий контроллер для административных операций.
 */
public class AdminController {
    private final MeasurementUseCase measurementUseCase;
    private final OperationUseCase operationUseCase;
    private final PersonUseCase personUseCase;
    private final TokenService tokenService;
    private final CounterTypeUseCase counterTypeUseCase;
    private final CounterUseCase counterUseCase;

    /**
     * Конструктор класса AdminController, инициализирующий используемые компоненты.
     */
    public AdminController() {
        this.measurementUseCase = new MeasurementUseCase();
        this.operationUseCase = new OperationUseCase();
        this.tokenService = TokenService.getInstance();
        this.personUseCase = new PersonUseCase();
        counterTypeUseCase = new CounterTypeUseCase();
        counterUseCase = new CounterUseCase();
    }

    /**
     * Метод для получения списка всех операций (для администратора).
     *
     * @param token Токен администратора.
     * @return Ответ с результатом операции и списком операций.
     */
    public OperationOutResponse allOperations(String token) {
        try {
            long personId = tokenService.getPersonId(token);
            Person person = personUseCase.findById(personId);

            if (person.getRole() == Role.ADMIN) {
                List<OperationOutDto> dtoList = operationUseCase.findAll()
                        .stream()
                        .map(OperationOutDto::new)
                        .toList();
                return new OperationOutResponse("200 OK.", dtoList);
            }
            return new OperationOutResponse("400 bad request", Collections.emptyList());
        } catch (TokenNotActualException e) {
            return new OperationOutResponse("400 bad request", Collections.emptyList());
        }
    }

    /**
     * Метод для получения списка всех измерений (для администратора).
     *
     * @param token Токен администратора.
     * @return Ответ с результатом операции и списком измерений.
     */
    public MeasurementOutResponse allMeasurements(String token) {
        try {
            long personId = tokenService.getPersonId(token);
            Person person = personUseCase.findById(personId);

            if (person.getRole() == Role.ADMIN) {
                List<MeasurementOutDto> dtoList = measurementUseCase.findAll()
                        .stream()
                        .map(MeasurementOutDto::new)
                        .toList();
                return new MeasurementOutResponse("200 OK.", dtoList);
            }
            return new MeasurementOutResponse("400 bad request", Collections.emptyList());
        } catch (TokenNotActualException e) {
            return new MeasurementOutResponse("400 bad request", Collections.emptyList());
        }
    }

    /**
     * Метод для добавления нового типа счетчика (для администратора).
     *
     * @param dto   DTO с информацией о новом типе счетчика.
     * @param token Токен администратора.
     * @return Строка с результатом операции.
     */
    public String addCounterType(CounterTypeDto dto, String token) {
        try {
            long personId = tokenService.getPersonId(token);
            Person person = personUseCase.findById(personId);

            if (person.getRole() == Role.ADMIN) {
                counterTypeUseCase.save(new CounterType(dto.getName()));
                return "201 created.";
            }
            return "400 bad request";
        } catch (TokenNotActualException e) {
            return "400 bad request " + e.getMessage();
        }
    }

    /**
     * Метод для передачи счетчика другому пользователю (для администратора).
     *
     * @param dto         DTO с информацией о типе счетчика.
     * @param receiverId  Идентификатор пользователя-получателя счетчика.
     * @param token       Токен администратора.
     * @return Строка с результатом операции.
     */
    public String giveCounter(CounterTypeDto dto, long receiverId, String token) {
        try {
            long personId = tokenService.getPersonId(token);
            Person person = personUseCase.findById(personId);

            if (person.getRole() == Role.ADMIN) {
                counterUseCase.save(new Counter(receiverId, new CounterType(dto.getName())));
                operationUseCase.save(new Operation(personId, Action.GIVE_COUNTER, LocalDateTime.now()));
                return "201 created.";
            }
            return "400 bad request";
        } catch (TokenNotActualException | PersonNotFoundException | CounterTypeNotFoundException e) {
            return "400 bad request " + e.getMessage();
        }
    }
}
