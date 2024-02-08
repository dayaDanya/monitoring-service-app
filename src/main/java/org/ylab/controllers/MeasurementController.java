package org.ylab.controllers;

import org.ylab.domain.dto.*;
import org.ylab.domain.models.CounterType;
import org.ylab.domain.models.Measurement;
import org.ylab.exceptions.*;
import org.ylab.services.CounterService;
import org.ylab.services.MeasurementService;
import org.ylab.services.TokenService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс, представляющий контроллер для операций с измерениями.
 */
public class MeasurementController {
    private final MeasurementService measurementUseCase;
    private final CounterService counterUseCase;
    private final TokenService tokenService;

    /**
     * Конструктор класса MeasurementController, инициализирующий используемые компоненты.
     */
    public MeasurementController(MeasurementService measurementUseCase,
                                 CounterService counterUseCase,
                                 TokenService tokenService) {
        this.measurementUseCase = measurementUseCase;
        this.counterUseCase = counterUseCase;
        this.tokenService = tokenService;
    }

    /**
     * Метод для добавления нового измерения.
     *
     * @param dto   DTO с информацией о новом измерении.
     * @param token Токен пользователя, выполняющего операцию.
     * @return Строка с результатом операции.
     */
    public String add(MeasurementInDto dto, String token) {
        try {
//            measurementUseCase.save(new Measurement(dto, LocalDateTime.now()),
//                    tokenService.getPersonId(token));
        } catch (WrongDateException | TokenNotActualException | BadMeasurementAmountException e) {
            return "400 bad request: " + e.getMessage();
        }
        return "201 created";
    }

    /**
     * Метод для получения списка всех измерений пользователя.
     *
     * @param token Токен пользователя, для которого требуется получить измерения.
     * @return Ответ с результатом операции и списком измерений.
     */
    public MeasurementOutResponse allMeasurements(String token) {
        try {
            Map<Long, MeasurementOutDto> dtoMap = measurementUseCase.findAllById(tokenService.getPersonId(token))
                    .entrySet()
                    .stream()
                    .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), new MeasurementOutDto(entry.getValue())),
                            HashMap::putAll);
            return new MeasurementOutResponse("200 OK", dtoMap);
        } catch (TokenNotActualException e) {
            return new MeasurementOutResponse("400 bad request: " + e.getMessage(), Collections.emptyMap());
        }
    }


    /**
     * Метод для получения последнего измерения указанного типа счетчика пользователя.
     *
     * @param dto   DTO с информацией о типе счетчика.
     * @param token Токен пользователя, для которого требуется получить последнее измерение.
     * @return Ответ с результатом операции и списком измерений (одним последним измерением).
     */
    public MeasurementOutResponse lastMeasurement(CounterTypeDto dto, String token) {
        try {
            Measurement found = measurementUseCase.findLast(new CounterType(dto.getName()), tokenService.getPersonId(token));
            return new MeasurementOutResponse("200 OK", Map.of(found.getId(), new MeasurementOutDto(found)));
        } catch (TokenNotActualException | CounterNotFoundException e) {
            return new MeasurementOutResponse("400 bad request: " + e.getMessage(), Collections.emptyMap());
        } catch (MeasurementNotFoundException e) {
            return new MeasurementOutResponse("200 OK", Collections.emptyMap());
        }
    }

    /**
     * Метод для получения измерения за указанный месяц указанного типа счетчика пользователя.
     *
     * @param token Токен пользователя, для которого требуется получить измерение.
     * @param month Номер месяца.
     * @param dto   DTO с информацией о типе счетчика.
     * @return Ответ с результатом операции и списком измерений (одним измерением за указанный месяц).
     */
    public MeasurementOutResponse measurementByMonth(String token, int month, CounterTypeDto dto) {
        try {
            Measurement found = measurementUseCase.findByMonth(tokenService.getPersonId(token), month, new CounterType(dto.getName()));
            return new MeasurementOutResponse("200 OK", Map.of(found.getId(), new MeasurementOutDto(found)));
        } catch (TokenNotActualException | CounterNotFoundException e) {
            return new MeasurementOutResponse("400 bad request: " + e.getMessage(), Collections.emptyMap());
        } catch (MeasurementNotFoundException e) {
            return new MeasurementOutResponse("200 OK", Collections.emptyMap());
        }
    }

    /**
     * Метод для получения списка счетчиков пользователя.
     *
     * @param token Токен пользователя, для которого требуется получить список счетчиков.
     * @return Ответ с результатом операции и map счетчиков пользователя.
     */
    public CounterOutResponse personCounters(String token) {
        try {
            Map<Long, CounterDto> dtoMap = counterUseCase.findByPersonId(
                            tokenService.getPersonId(token))
                    .entrySet().stream()
                    .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), new CounterDto(entry.getValue())),
                            HashMap::putAll);

            return new CounterOutResponse("200 OK", dtoMap);
        } catch (TokenNotActualException e) {
            return new CounterOutResponse("400 bad request: " + e.getMessage(), Collections.emptyMap());
        }
    }
}