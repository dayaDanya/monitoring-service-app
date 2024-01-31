package org.ylab.usecases;

import org.ylab.domain.models.Operation;
import org.ylab.repositories.OperationRepo;

import java.util.List;

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
    public List<Operation> findAll() {
        return operationRepo.findAll();
    }

    /**
     * Метод для получения списка всех операций по идентификатору человека.
     *
     * @param id Идентификатор человека, для которого осуществляется поиск операций.
     * @return Список операций, принадлежащих указанному человеку.
     */
    public List<Operation> findAllById(long id) {
        return operationRepo.findAllById(id);
    }
}

