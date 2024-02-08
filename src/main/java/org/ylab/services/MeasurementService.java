package org.ylab.services;

import org.ylab.domain.models.CounterType;
import org.ylab.domain.models.Measurement;
import org.ylab.domain.models.Operation;
import org.ylab.domain.models.enums.Action;
import org.ylab.domain.usecases.CounterUseCase;
import org.ylab.domain.usecases.MeasurementUseCase;
import org.ylab.domain.usecases.OperationUseCase;
import org.ylab.exceptions.BadMeasurementAmountException;
import org.ylab.exceptions.CounterNotFoundException;
import org.ylab.exceptions.MeasurementNotFoundException;
import org.ylab.exceptions.WrongDateException;
import org.ylab.repositories.IMeasurementRepo;
import org.ylab.repositories.implementations.MeasurementRepo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Класс, представляющий использование сущности Measurement в рамках бизнес-логики.
 * Реализует методы для сохранения измерений, поиска и отображения данных о измерениях.
 */
public class MeasurementService implements MeasurementUseCase {
    private final IMeasurementRepo measurementRepo;

    private final OperationUseCase operationUseCase;

    private final CounterUseCase counterUseCase;

    /**
     * Конструктор класса MeasurementUseCase, инициализирующий репозиторий измерений, использование операций и использование счетчиков.
     */
    public MeasurementService(MeasurementRepo measurementRepo,
                              OperationService operationUseCase,
                              CounterService counterUseCase) {
        this.measurementRepo = measurementRepo;
        this.operationUseCase = operationUseCase;
        this.counterUseCase = counterUseCase;
    }

    /**
     * Метод для сохранения измерения, выполняя проверки и выбрасывая исключение в случае ошибки.
     *
     * @param measurement Измерение, которое требуется сохранить.
     * @param personId    Идентификатор человека, к которому привязано измерение.
     * @throws WrongDateException       Выбрасывается в случае, если дата измерения не соответствует ожидаемой.
     * @throws CounterNotFoundException Выбрасывается в случае, если связанный счетчик не найден.
     */
    public void save(Measurement measurement, long personId) throws WrongDateException, CounterNotFoundException {
        long counterId = counterUseCase
                .findIdByPersonIdAndCounterType(personId, measurement.getCounterType())
                .orElseThrow(CounterNotFoundException::new);
        Optional<LocalDateTime> lastMeasurementDate = measurementRepo.findLastDate(counterId);
        if (lastMeasurementDate.isPresent() && isNotNextMonth(lastMeasurementDate.get(), measurement.getSubmissionDate())) {
            throw new WrongDateException(lastMeasurementDate.get());
        } else if (lastMeasurementDate.isPresent()
                && findLast( measurement.getCounterType(), personId)
                .getAmount() >= measurement.getAmount() ) {
            throw new BadMeasurementAmountException();
        } else {
            measurement.setCounterId(counterId);
            measurementRepo.save(measurement);
            operationUseCase.save(new Operation(personId, Action.ADD_MEASUREMENT, LocalDateTime.now()));
        }
    }

    /**
     * Метод для получения списка всех измерений.
     *
     * @return Список всех измерений.
     */
    public Map<Long, Measurement> findAll() {
        return measurementRepo.findAll();
    }

    /**
     * Метод для получения списка всех измерений по идентификатору человека.
     *
     * @param personId Идентификатор человека, для которого осуществляется поиск измерений.
     * @return Список измерений, принадлежащих указанному человеку.
     */
    public Map<Long, Measurement> findAllById(long personId) {
        Map<Long, Measurement> resultSet = new HashMap<>();
        List<Long> counterIds = counterUseCase.findIdsByPersonId(personId);
        counterIds.forEach(id -> resultSet.putAll(measurementRepo.findAllByCounterId(id)));
        operationUseCase.save(new Operation(personId, Action.WATCH_HISTORY, LocalDateTime.now()));
        return resultSet;
    }

    /**
     * Метод для получения последнего измерения по типу счетчика и идентификатору человека.
     *
     * @param type     Тип счетчика.
     * @param personId Идентификатор человека.
     * @return Последнее измерение указанного типа счетчика, принадлежащее указанному человеку.
     * @throws MeasurementNotFoundException Выбрасывается в случае, если измерение не найдено.
     */
    public Measurement findLast(String type, long personId) throws MeasurementNotFoundException {
        long counterId = counterUseCase.findIdByPersonIdAndCounterType(personId, type)
                .orElseThrow(CounterNotFoundException::new);
        operationUseCase.save(new Operation(personId, Action.WATCH_LAST, LocalDateTime.now()));
        return measurementRepo.findLast(counterId).orElseThrow(MeasurementNotFoundException::new);
    }

    /**
     * Метод для получения измерения за указанный месяц по типу счетчика и идентификатору человека.
     *
     * @param personId Идентификатор человека.
     * @param month     Месяц, за который осуществляется поиск измерения.
     * @param type      Тип счетчика.
     * @return Измерение за указанный месяц, принадлежащее указанному человеку и типу счетчика.
     * @throws MeasurementNotFoundException Выбрасывается в случае, если измерение не найдено.
     */
    public Measurement findByMonth(long personId, int month, String type) throws MeasurementNotFoundException {
        long counterId = counterUseCase.findIdByPersonIdAndCounterType(personId, type)
                .orElseThrow(CounterNotFoundException::new);
        operationUseCase.save(new Operation(personId, Action.WATCH_BY_MONTH, LocalDateTime.now()));
        return measurementRepo.findByMonth(counterId, month).orElseThrow(MeasurementNotFoundException::new);
    }

    /**
     * Метод для проверки, что дата текущего измерения не превышает следующий месяц после последнего измерения.
     *
     * @param savedDateTime   Дата последнего измерения.
     * @param currentDateTime Текущая дата измерения.
     * @return {@code true}, если текущая дата не превышает следующий месяц, иначе {@code false}.
     */
    private boolean isNotNextMonth(LocalDateTime savedDateTime, LocalDateTime currentDateTime) {
        return currentDateTime.isBefore(savedDateTime) ||
                (currentDateTime.getMonthValue() == savedDateTime.getMonthValue() &&
                        currentDateTime.getYear() == savedDateTime.getYear());
    }

}
