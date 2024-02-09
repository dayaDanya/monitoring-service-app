package org.ylab.domain.usecases;

import org.ylab.domain.models.Measurement;
import org.ylab.exceptions.CounterNotFoundException;
import org.ylab.exceptions.MeasurementNotFoundException;
import org.ylab.exceptions.WrongDateException;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Интерфейс, представляющий использование сущности Measurement в рамках бизнес-логики.
 * Реализует методы для сохранения измерений, поиска и отображения данных о измерениях.
 */
public interface MeasurementUseCase {


    /**
     * Метод для сохранения измерения, выполняя проверки и выбрасывая исключение в случае ошибки.
     *
     * @param measurement Измерение, которое требуется сохранить.
     * @param personId    Идентификатор человека, к которому привязано измерение.
     * @throws WrongDateException       Выбрасывается в случае, если дата измерения не соответствует ожидаемой.
     * @throws CounterNotFoundException Выбрасывается в случае, если связанный счетчик не найден.
     */
    void save(Measurement measurement, long personId);

    /**
     * Метод для получения списка всех измерений.
     *
     * @return Список всех измерений.
     */
    Map<Long, Measurement> findAllMeasurements();

    /**
     * Метод для получения списка всех измерений по идентификатору человека.
     *
     * @param personId Идентификатор человека, для которого осуществляется поиск измерений.
     * @return Список измерений, принадлежащих указанному человеку.
     */
    Map<Long, Measurement> findAllById(long personId);

    /**
     * Метод для получения последнего измерения по типу счетчика и идентификатору человека.
     *
     * @param type     Тип счетчика.
     * @param personId Идентификатор человека.
     * @return Последнее измерение указанного типа счетчика, принадлежащее указанному человеку.
     * @throws MeasurementNotFoundException Выбрасывается в случае, если измерение не найдено.
     */
    Measurement findLast(String type, long personId);

    /**
     * Метод для получения измерения за указанный месяц по типу счетчика и идентификатору человека.
     *
     * @param personId Идентификатор человека.
     * @param month    Месяц, за который осуществляется поиск измерения.
     * @param type     Тип счетчика.
     * @return Измерение за указанный месяц, принадлежащее указанному человеку и типу счетчика.
     * @throws MeasurementNotFoundException Выбрасывается в случае, если измерение не найдено.
     */
    Measurement findByMonth(long personId, int month, String type);

    /**
     * Метод для проверки, что дата текущего измерения не превышает следующий месяц после последнего измерения.
     *
     * @param savedDateTime   Дата последнего измерения.
     * @param currentDateTime Текущая дата измерения.
     * @return {@code true}, если текущая дата не превышает следующий месяц, иначе {@code false}.
     */
    private boolean isNotNextMonth(LocalDateTime savedDateTime, LocalDateTime currentDateTime) {
        return false;
    }

}
