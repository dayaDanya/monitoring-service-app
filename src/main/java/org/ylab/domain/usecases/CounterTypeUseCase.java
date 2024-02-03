package org.ylab.domain.usecases;

import org.ylab.domain.models.CounterType;

import java.util.Optional;

/**
 * Класс, представляющий использование сущности CounterType в рамках бизнес-логики.
 * Реализует методы для поиска и сохранения данных о типах счетчиков.
 */
public interface CounterTypeUseCase {



    /**
     * Метод для поиска типа счетчика по его имени.
     *
     * @param type Имя типа счетчика, по которому осуществляется поиск.
     * @return {@code Optional<CounterType>}, содержащий найденный тип счетчика, если он существует.
     */
    Optional<CounterType> findOne(String type);

    /**
     * Метод для сохранения типа счетчика.
     * Сохранение производится только в случае, если тип счетчика с таким именем еще не существует.
     *
     * @param counterType Тип счетчика, который требуется сохранить.
     */
    void save(CounterType counterType);
}
