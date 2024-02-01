package org.ylab.usecases;

import org.ylab.domain.models.Counter;
import org.ylab.domain.models.CounterType;
import org.ylab.repositories.CounterRepo;
import org.ylab.exceptions.CounterTypeNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Класс, представляющий использование сущности Counter в рамках бизнес-логики.
 * Реализует методы для сохранения счетчиков, поиска по различным критериям и связанных операций.
 */
public class CounterUseCase {

    private final CounterRepo counterRepo;
    private final CounterTypeUseCase counterTypeUseCase;

    /**
     * Конструктор класса CounterUseCase, инициализирующий репозиторий счетчиков и использование типов счетчиков.
     */
    public CounterUseCase(CounterRepo counterRepo,
                          CounterTypeUseCase counterTypeUseCase) {
        this.counterRepo = counterRepo;
        this.counterTypeUseCase = counterTypeUseCase;
    }



    /**
     * Метод для сохранения счетчика с проверкой существования связанного типа счетчика.
     *
     * @param counter Счетчик, который требуется сохранить.
     * @throws CounterTypeNotFoundException Возникает, если связанный тип счетчика не найден.
     */
    public void save(Counter counter) throws CounterTypeNotFoundException {
        Optional<CounterType> found = counterTypeUseCase.findOne(counter.getCounterType().getName());
        counter.setCounterType(found.orElseThrow(CounterTypeNotFoundException::new));
        counter.setCounterTypeId(found.get().getId());
        counterRepo.save(counter);
    }

    /**
     * Метод для поиска списка счетчиков по идентификатору человека.
     *
     * @param personId Идентификатор человека, для которого осуществляется поиск счетчиков.
     * @return Список счетчиков, принадлежащих указанному человеку.
     */
    public List<Counter> findByPersonId(long personId) {
        return counterRepo.findByPersonId(personId);
    }

    /**
     * Метод для поиска идентификатора счетчика по идентификатору человека и типу счетчика.
     *
     * @param personId Идентификатор человека.
     * @param type     Тип счетчика.
     * @return {@code Optional<Long>} содержащий идентификатор счетчика, если счетчик существует.
     */
    public Optional<Long> findIdByPersonIdAndCounterType(long personId, String type) {
        return counterRepo.findIdByPersonIdAndCounterType(personId, type);
    }

    /**
     * Метод для поиска списка идентификаторов счетчиков по идентификатору человека.
     *
     * @param personId Идентификатор человека.
     * @return Список идентификаторов счетчиков, принадлежащих указанному человеку.
     */
    public List<Long> findIdsByPersonId(long personId) {
        return counterRepo.findIdsByPersonId(personId);
    }
}
