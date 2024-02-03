package org.ylab.repositories;

import org.ylab.domain.models.CounterType;

import java.util.Optional;

/**
 * Репозиторий типов счетчиков, представляющий слой взаимодействия с базой данных типов счетчиков.
 * На текущий момент реализован с использованием коллекции.
 */
public interface ICounterTypeRepo {

    /**
     * Сохранение типа счетчика
     *
     * @param type Тип счетчика для сохранения
     */
    void save(CounterType type);

    /**
     * Поиск типа счетчика по его имени
     *
     * @param name Имя типа счетчика
     * @return Тип счетчика (Optional)
     */
    Optional<CounterType> findOne(String name);

}