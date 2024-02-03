package org.ylab.repositories;

import org.ylab.domain.models.Counter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * репозиторий счетчиков, то есть слой взаимодействия с бд счетчиков
 * на данный момент реализован с помощью коллекции
 */
public interface ICounterRepo {


    /**
     * сохранение счетчика
     *
     * @param counter
     */
    void save(Counter counter) ;

    /**
     * находит ид счетчика при условии что ид пользователя и тип счетчика соответствует переданным
     *
     * @param personId
     * @param type
     * @return айди счетчика
     */
    Optional<Long> findIdByPersonIdAndCounterType(long personId, String type) ;


    /**
     * возвращает список айди счетчиков пользователя
     *
     * @param personId
     * @return список айди счетчиков
     */
   List<Long> findIdsByPersonId(long personId);

    /**
     * возвращает список счетчиков пользователя
     *
     * @param personId
     * @return список счетчиков
     */
    Map<Long, Counter> findByPersonId(long personId);


}
