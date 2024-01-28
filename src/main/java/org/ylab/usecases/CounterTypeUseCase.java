package org.ylab.usecases;

import org.ylab.domain.models.CounterType;
import org.ylab.repositories.CounterTypeRepo;

import java.util.Optional;

/**
 * Класс, представляющий использование сущности CounterType в рамках бизнес-логики.
 * Реализует методы для поиска и сохранения данных о типах счетчиков.
 */
public class CounterTypeUseCase {

    /**
     * репозиторий
     */
    private CounterTypeRepo repo;

    /**
     * Конструктор класса CounterTypeUseCase.
     * Инициализирует репозиторий для работы с типами счетчиков.
     */
    public CounterTypeUseCase() {
        repo = new CounterTypeRepo();
    }

    /**
     * Метод для поиска типа счетчика по его имени.
     *
     * @param type Имя типа счетчика, по которому осуществляется поиск.
     * @return {@code Optional<CounterType>}, содержащий найденный тип счетчика, если он существует.
     */
    public Optional<CounterType> findOne(String type) {
        return repo.findOne(type);
    }

    /**
     * Метод для сохранения типа счетчика.
     * Сохранение производится только в случае, если тип счетчика с таким именем еще не существует.
     *
     * @param counterType Тип счетчика, который требуется сохранить.
     */
    public void save(CounterType counterType) {
        if (findOne(counterType.getName()).isEmpty())
            repo.save(counterType);
    }
}
