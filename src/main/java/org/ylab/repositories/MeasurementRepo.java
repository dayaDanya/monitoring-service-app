package org.ylab.repositories;

import org.ylab.domain.models.Measurement;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Репозиторий измерений, представляющий слой взаимодействия с базой данных измерений.
 * На текущий момент реализован с использованием коллекции.
 */
public class MeasurementRepo {

    /**
     * Список измерений
     */
    private static List<Measurement> measurements;

    /**
     * Счетчик измерений
     */
    private static long mCounter;

    /**
     * Конструктор, инициализирующий список (вызывается один раз)
     */
    public MeasurementRepo() {
        if (measurements == null) {
            mCounter = 0;
            measurements = new ArrayList<>();
        }
    }

    /**
     * Сохранение измерения
     * @param measurement Измерение для сохранения
     */
    public void save(Measurement measurement) {
        measurement.setId(++mCounter);
        measurements.add(measurement);
    }

    /**
     * Поиск последней даты измерения для заданного счетчика
     * @param counterId Идентификатор счетчика
     * @return Последняя дата измерения (Optional)
     */
    public Optional<LocalDateTime> findLastDate(long counterId) {
        return measurements
                .stream()
                .filter(m -> m.getCounterId() == counterId)
                .max(Comparator.comparing(Measurement::getSubmissionDate))
                .map(Measurement::getSubmissionDate);
    }

    /**
     * Поиск последнего измерения для заданного счетчика
     * @param counterId Идентификатор счетчика
     * @return Последнее измерение (Optional)
     */
    public Optional<Measurement> findLast(long counterId) {
        return measurements
                .stream()
                .filter(m -> m.getCounterId() == counterId)
                .max(Comparator.comparing(Measurement::getSubmissionDate));
    }

    /**
     * Поиск всех измерений для заданного счетчика
     * @param counterId Идентификатор счетчика
     * @return Список всех измерений для заданного счетчика
     */
    public List<Measurement> findAllByCounterId(long counterId) {
        return measurements
                .stream()
                .filter(m -> m.getCounterId() == counterId)
                .collect(Collectors.toList());
    }

    /**
     * Поиск всех измерений
     * @return Список всех измерений
     */
    public List<Measurement> findAll() {
        return measurements;
    }

    /**
     * Поиск измерения для заданного счетчика и месяца
     * @param counterId Идентификатор счетчика
     * @param month Месяц (1-12)
     * @return Измерение для заданного счетчика и месяца (Optional)
     */
    public Optional<Measurement> findByMonth(long counterId, int month) {
        return measurements
                .stream()
                .filter(m -> m.getCounterId() == counterId)
                .filter(m -> m.getSubmissionDate().getMonth().equals(Month.of(month)))
                .findFirst();
    }
}
