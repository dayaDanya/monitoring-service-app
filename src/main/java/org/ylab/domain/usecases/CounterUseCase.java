package org.ylab.domain.usecases;

import org.ylab.domain.models.Counter;
import org.ylab.exceptions.CounterTypeNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Интерфейс, представляющий использование сущности Counter в рамках бизнес-логики.
 * Реализует методы для сохранения счетчиков, поиска по различным критериям и связанных операций.
 */
public interface CounterUseCase {


    /**
     * Метод для сохранения счетчика с проверкой существования связанного типа счетчика.
     *
     * @param counter Счетчик, который требуется сохранить.
     * @throws CounterTypeNotFoundException Возникает, если связанный тип счетчика не найден.
     */
    void saveCounter(Counter counter);

    /**
     * Метод для поиска списка счетчиков по идентификатору человека.
     *
     * @param personId Идентификатор человека, для которого осуществляется поиск счетчиков.
     * @return Map счетчиков, принадлежащих указанному человеку.
     */
    Map<Long, Counter> findByPersonId(long personId);

    /**
     * Метод для поиска идентификатора счетчика по идентификатору человека и типу счетчика.
     *
     * @param personId Идентификатор человека.
     * @param type     Тип счетчика.
     * @return {@code Optional<Long>} содержащий идентификатор счетчика, если счетчик существует.
     */
    Optional<Long> findIdByPersonIdAndCounterType(long personId, String type);

    /**
     * Метод для поиска списка идентификаторов счетчиков по идентификатору человека.
     *
     * @param personId Идентификатор человека.
     * @return Список идентификаторов счетчиков, принадлежащих указанному человеку.
     */
    List<Long> findIdsByPersonId(long personId);
}
