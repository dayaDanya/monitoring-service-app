package org.ylab.repositories;

import org.ylab.domain.models.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Репозиторий для сущности Operation.
 * На текущий момент реализован с использованием коллекции.
 */
public class OperationRepo {

    /**
     * Список операций
     */
    private static List<Operation> operations;

    /**
     * Счетчик операций
     */
    private static int counter;

    /**
     * Конструктор, инициализирующий список (вызывается один раз)
     */
    public OperationRepo() {
        if (operations == null) {
            counter = 0;
            operations = new ArrayList<>();
        }
    }

    /**
     * Сохранение операции
     * @param operation Операция для сохранения
     */
    public void save(Operation operation) {
        operation.setId(++counter);
        operations.add(operation);
    }

    /**
     * Получение списка всех операций
     * @return Список всех операций
     */
    public List<Operation> findAll() {
        return operations;
    }

    /**
     * Получение списка всех операций для заданного идентификатора пользователя
     * @param id Идентификатор пользователя
     * @return Список всех операций для заданного пользователя
     */
    public List<Operation> findAllById(long id) {
        return operations.stream()
                .filter(o -> o.getPersonId() == id)
                .collect(Collectors.toList());
    }

    /**
     * Удаление всех операций из репозитория
     */
    public void deleteAll() {
        operations.clear();
    }
}
