package org.ylab.repositories;

import org.ylab.domain.models.Counter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * репозиторий счетчиков, то есть слой взаимодействия с бд счетчиков
 * на данный момент реализован с помощью коллекции
 */
public class CounterRepo {

    /**
     * количество счетчиков
     */
    private static long counterAmount;
    /**
     * список счетчиков
     */
    private static List<Counter> counters;

    /**
     * конструктор, инициализирует список единожды
     */
    public CounterRepo() {
        if(counters == null) {
            counterAmount = 0;
            counters = new ArrayList<>();
        }
    }

    /**
     * сохранение счетчика
     * @param counter
     */
    public void save(Counter counter){
        counter.setId(++counterAmount);
        counters.add(counter);
    }

    /**
     * сохранение списка счетчиков
     * @param newCounters
     */
    public void saveAll(List<Counter> newCounters) {
        newCounters.forEach(nc -> nc.setId(++counterAmount));
        counters.addAll(newCounters);
    }

    /**
     * находит ид счетчика при условии что ид пользователя и тип счетчика соответствует переданным
     * @param personId
     * @param type
     * @return айди счетчика
     */
    public Optional<Long> findIdByPersonIdAndCounterType(long personId, String type){
        return counters.stream()
                .filter(c -> c.getPersonId() == personId)
                .filter(c -> c.getCounterType().getName().equals(type))
                .map(Counter::getId)
                .findFirst();
    }

    /**
     * возвращает список айди счетчиков пользователя
     * @param personId
     * @return список айди счетчиков
     */
    public List<Long> findIdsByPersonId(long personId) {
        return counters.stream()
                .filter(c -> c.getPersonId() == personId)
                .map(Counter::getId)
                .collect(Collectors.toList());
    }

    /**
     * возвращает список счетчиков пользователя
     * @param personId
     * @return список счетчиков
     */
    public List<Counter> findByPersonId(long personId) {
        return counters.stream()
                .filter(c -> c.getPersonId() == personId)
                .collect(Collectors.toList());
    }


}
