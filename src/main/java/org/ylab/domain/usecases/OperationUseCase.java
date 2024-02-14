package org.ylab.domain.usecases;

import org.ylab.domain.models.Operation;

import java.util.Map;

/**
 * Класс, представляющий использование сущности Operation в рамках бизнес-логики.
 * Реализует методы для сохранения операций и получения списка операций.
 */

public interface OperationUseCase {


    /**
     * Метод для сохранения операции.
     *
     * @param operation Операция, которую требуется сохранить.
     */
    void save(Operation operation);

    /**
     * Метод для получения списка всех операций.
     *
     * @return Список всех операций.
     */
    Map<Long, Operation> findAllOperations();

}

