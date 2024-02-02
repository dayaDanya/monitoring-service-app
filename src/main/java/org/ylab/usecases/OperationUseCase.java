package org.ylab.usecases;

import org.ylab.domain.models.Operation;
import org.ylab.repositories.OperationRepo;

import java.util.Map;

/**
 * Класс, представляющий использование сущности Operation в рамках бизнес-логики.
 * Реализует методы для сохранения операций и получения списка операций.
 */

public class OperationUseCase {
    private final OperationRepo operationRepo;

    /**
     * Конструктор класса OperationUseCase, инициализирующий репозиторий операций.
     */
    public OperationUseCase(OperationRepo operationRepo) {
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
    public Map<Long, Operation> findAll() {
        return operationRepo.findAll();
    }

}

