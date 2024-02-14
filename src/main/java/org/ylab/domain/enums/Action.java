package org.ylab.domain.enums;

import org.ylab.exceptions.ActionNotFoundException;

import java.util.Optional;

/**
 * Перечисление - действия,
 * определяет различные действия, которые могут быть выполнены в системе
 */
public enum Action {

    /**
     * Аутентификация пользователя
     */
    AUTHENTICATION,

    /**
     * Регистрация нового пользователя
     */
    REGISTRATION,

    /**
     * Добавление измерения или показания
     */
    ADD_MEASUREMENT,

    /**
     * Просмотр истории действий
     */
    WATCH_HISTORY,

    /**
     * Просмотр последнего действия
     */
    WATCH_LAST,

    /**
     * Просмотр действий за месяц
     */
    WATCH_BY_MONTH,

    /**
     * Просмотр всех действий
     */
    WATCH_ALL,
    /**
     * Добавление нового типа счетчика
     */
    ADD_NEW_COUNTER_TYPE,
    /**
     * Выдача нового счетчика
     */
    GIVE_COUNTER,
    /**
     * Просмотр аудита
     */
    WATCH_AUDIT,
    /**
     * Просмотр списка счетчиков
     */
    WATCH_COUNTERS;

    /**
     * Метод ищущий Action соответствующий определенному
     * методу бизнес-логики. Используется в аудите.
     * @param name название метода бизнес-логики
     * @return Action
     * @exception ActionNotFoundException выбрасывается в случае несоответствия
     * ни одного action переданному названию метода
     */
    public static Action find(String name) {
        switch (name) {
            case "authenticate" -> {
                return Action.AUTHENTICATION;
            }
            case "register" -> {
                return Action.REGISTRATION;
            }
            case "save" -> {
                return Action.ADD_MEASUREMENT;
            }
            case "findLast" -> {
                return Action.WATCH_LAST;
            }
            case "findByMonth" -> {
                return Action.WATCH_BY_MONTH;
            }
            case "findAllById" -> {
                return Action.WATCH_HISTORY;
            }
            case "findByPersonId" -> {
                return Action.WATCH_COUNTERS;
            }
            case "saveCounter" -> {
                return Action.GIVE_COUNTER;
            }
            case "saveCounterType" -> {
                return Action.ADD_NEW_COUNTER_TYPE;
            }
            case "findAllOperations" -> {
                return Action.WATCH_ALL;
            }
            case "findAllMeasurements" ->{
                return Action.WATCH_AUDIT;
            }
        }

        throw new ActionNotFoundException();
    }
}
