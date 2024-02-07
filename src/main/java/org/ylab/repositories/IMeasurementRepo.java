package org.ylab.repositories;

import org.ylab.domain.models.Measurement;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * Репозиторий измерений, представляющий слой взаимодействия с базой данных измерений.
 *
 */
public interface IMeasurementRepo {

    /**
     * Сохранение измерения
     * @param measurement Измерение для сохранения
     */
    void save(Measurement measurement);

    /**
     * Поиск последней даты измерения для заданного счетчика
     * @param counterId Идентификатор счетчика
     * @return Последняя дата измерения (Optional)
     */
    Optional<LocalDateTime> findLastDate(long counterId);

    /**
     * Поиск последнего измерения для заданного счетчика
     * @param counterId Идентификатор счетчика
     * @return Последнее измерение (Optional)
     */
    Optional<Measurement> findLast(long counterId);

    /**
     * Поиск всех измерений для заданного счетчика
     * @param counterId Идентификатор счетчика
     * @return Список всех измерений для заданного счетчика
     */
     Map<Long,Measurement> findAllByCounterId(long counterId);


    /**
     * Поиск всех измерений
     * @return Список всех измерений
     */
   Map<Long, Measurement> findAll();

    /**
     * Поиск измерения для заданного счетчика и месяца
     * @param counterId Идентификатор счетчика
     * @param month Месяц (1-12)
     * @return Измерение для заданного счетчика и месяца (Optional)
     */
    Optional<Measurement> findByMonth(long counterId, int month);
}
