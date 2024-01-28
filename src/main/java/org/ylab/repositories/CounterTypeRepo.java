package org.ylab.repositories;

import org.ylab.domain.models.CounterType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Репозиторий типов счетчиков, представляющий слой взаимодействия с базой данных типов счетчиков.
 * На текущий момент реализован с использованием коллекции.
 */
public class CounterTypeRepo {

    /**
     * Количество типов счетчиков
     */
    private static long counterTypeAmount;

    /**
     * Список типов счетчиков
     */
    private static List<CounterType> counterTypes;

    /**
     * конструктор, инициализирующий список и добавляющий несколько типов счетчиков
     */
    public CounterTypeRepo() {
        if(counterTypes == null) {
            counterTypeAmount = 0;
            counterTypes = new ArrayList<>();
            save(new CounterType("HOT"));
            save(new CounterType("COLD"));
            save(new CounterType("HEAT"));
        }
    }

    /**
     * Сохранение типа счетчика
     *
     * @param type Тип счетчика для сохранения
     */
    public void save(CounterType type) {
        type.setId(++counterTypeAmount);
        counterTypes.add(type);
    }

    /**
     * Поиск типа счетчика по его имени
     *
     * @param type Имя типа счетчика
     * @return Тип счетчика (Optional)
     */
    public Optional<CounterType> findOne(String type) {
        return counterTypes.stream()
                .filter(t -> t.getName().equals(type))
                .findFirst();
    }
}