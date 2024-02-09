package org.ylab.services;

import org.ylab.domain.models.Operation;
import org.ylab.domain.usecases.OperationUseCase;
import org.ylab.repositories.IOperationRepo;
import org.ylab.repositories.implementations.OperationRepo;

import java.util.Map;

/**
 * Класс, представляющий использование сущности Operation в рамках бизнес-логики.
 * Реализует методы для сохранения операций и получения списка операций.
 */

public class OperationService implements OperationUseCase {
    private final IOperationRepo operationRepo;

    /**
     * Конструктор класса OperationUseCase, инициализирующий репозиторий операций.
     */
    public OperationService(OperationRepo operationRepo) {
        this.operationRepo = operationRepo;
    }

    /**
     * Метод для сохранения операции.
     *
     * @param operation Операция, которую требуется сохранить.
     */
    public void save(Operation operation) {
        operationRepo.save(operation);
    }

    /**
     * Метод для получения списка всех операций.
     *
     * @return Список всех операций.
     */
    public Map<Long, Operation> findAllOperations() {
        return operationRepo.findAll();
    }

}

