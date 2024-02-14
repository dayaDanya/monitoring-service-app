package org.ylab.domain.usecases;

import org.ylab.domain.models.Counter;
import org.ylab.domain.models.CounterType;
import org.ylab.domain.models.Measurement;
import org.ylab.domain.models.Operation;
import org.ylab.exceptions.CounterTypeNotFoundException;

import java.util.Map;

/**
 * Интерфейс, представляющий действия админа в рамках бизнес логики.
 */
public interface AdminUseCase {

    /**
     * Метод для сохранения счетчика с проверкой существования связанного типа счетчика.
     *
     * @param counter Счетчик, который требуется сохранить.
     * @throws CounterTypeNotFoundException Возникает, если связанный тип счетчика не найден.
     */
    void saveCounter(Counter counter);

    /**
     * Метод для сохранения типа счетчика.
     * Сохранение производится только в случае, если тип счетчика с таким именем еще не существует.
     *
     * @param counterType Тип счетчика, который требуется сохранить.
     */
    void saveCounterType(CounterType counterType);

    /**
     * Метод для получения списка всех операций.
     *
     * @return Список всех операций.
     */
    Map<Long, Operation> findAllOperations();

    /**
     * Метод для получения списка всех измерений.
     *
     * @return Список всех измерений.
     */
    Map<Long, Measurement> findAllMeasurements();

    /**
     * Метод для проверки является ли полученный id пользователя
     * - id админа
     * @param principal id
     * @return true if admin, false if not
     */
    boolean isAdmin(long principal);

}
