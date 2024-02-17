package org.ylab.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylab.domain.models.CounterType;
import org.ylab.exceptions.CounterTypeAlreadyExistsException;
import org.ylab.repositories.ICounterTypeRepo;
import org.ylab.repositories.implementations.CounterTypeRepo;

import java.util.Optional;

/**
 * Класс, представляющий использование сущности CounterType в рамках бизнес-логики.
 * Реализует методы для поиска и сохранения данных о типах счетчиков.
 */
@Service
public class CounterTypeService implements org.ylab.domain.usecases.CounterTypeUseCase {

    /**
     * репозиторий
     */
    private final ICounterTypeRepo repo;

    /**
     * Конструктор класса CounterTypeUseCase.
     * Инициализирует репозиторий для работы с типами счетчиков.
     */
    @Autowired
    public CounterTypeService(CounterTypeRepo repo) {
        this.repo = repo;
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
    public void saveCounterType(CounterType counterType) {
        if (findOne(counterType.getName()).isEmpty())
            repo.save(counterType);
        else throw new CounterTypeAlreadyExistsException(counterType.getName());
    }
}
