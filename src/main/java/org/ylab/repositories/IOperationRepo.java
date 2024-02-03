package org.ylab.repositories;

import org.ylab.domain.models.Operation;

import java.util.Map;

/**
 * Репозиторий для сущности Operation.
 */
public interface IOperationRepo {


    /**
     * Сохранение операции
     * @param operation Операция для сохранения
     */
   void save(Operation operation);

    /**
     * Получение списка всех операций
     * @return Список всех операций
     */
    Map<Long, Operation> findAll();


}
