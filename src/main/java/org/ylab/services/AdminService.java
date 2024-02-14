package org.ylab.services;

import org.ylab.aop.annotations.Recordable;
import org.ylab.domain.enums.Role;
import org.ylab.domain.models.Counter;
import org.ylab.domain.models.CounterType;
import org.ylab.domain.models.Measurement;
import org.ylab.domain.models.Operation;
import org.ylab.domain.usecases.*;
import org.ylab.exceptions.CounterTypeAlreadyExistsException;
import org.ylab.exceptions.CounterTypeNotFoundException;
import org.ylab.repositories.IPersonRepo;
import org.ylab.repositories.implementations.PersonRepo;

import java.util.Map;
/**
 * Интерфейс, представляющий действия админа в рамках бизнес логики.
 */
@Recordable
public class AdminService implements AdminUseCase {
    private final CounterUseCase counterUseCase;
    private final CounterTypeUseCase counterTypeUseCase;

    private final OperationUseCase operationUseCase;

    private final MeasurementUseCase measurementUseCase;

    private final IPersonRepo personRepo;

    /**
     * Конструктор
     * @param counterUseCase
     * @param counterTypeUseCase
     * @param operationUseCase
     * @param measurementUseCase
     * @param personRepo
     */
    public AdminService(CounterUseCase counterUseCase, CounterTypeUseCase counterTypeUseCase, OperationUseCase operationUseCase, MeasurementUseCase measurementUseCase, PersonRepo personRepo) {
        this.counterUseCase = counterUseCase;
        this.counterTypeUseCase = counterTypeUseCase;
        this.operationUseCase = operationUseCase;
        this.measurementUseCase = measurementUseCase;
        this.personRepo = personRepo;
    }
    /**
     * Метод для сохранения счетчика с проверкой существования связанного типа счетчика.
     *
     * @param counter Счетчик, который требуется сохранить.
     * @throws CounterTypeNotFoundException Возникает, если связанный тип счетчика не найден.
     */
    @Recordable
    public void saveCounter(Counter counter) {
        try {
            counterUseCase.saveCounter(counter);
        } catch (CounterTypeNotFoundException e) {
            throw e;
        }
    }
    /**
     * Метод для сохранения типа счетчика.
     * Сохранение производится только в случае, если тип счетчика с таким именем еще не существует.
     *
     * @param counterType Тип счетчика, который требуется сохранить.
     */
    @Recordable
    public void saveCounterType(CounterType counterType) {
        try {
            counterTypeUseCase.saveCounterType(counterType);
        } catch (CounterTypeAlreadyExistsException e) {
            throw e;
        }
    }
    /**
     * Метод для получения списка всех операций.
     *
     * @return Список всех операций.
     */
    @Recordable
    public Map<Long, Operation> findAllOperations() {
        return operationUseCase.findAllOperations();
    }
    /**
     * Метод для получения списка всех измерений.
     *
     * @return Список всех измерений.
     */
    @Recordable
    public Map<Long, Measurement> findAllMeasurements() {
        return measurementUseCase.findAllMeasurements();
    }
    /**
     * Метод для проверки является ли полученный id пользователя
     * - id админа
     * @param principal id
     * @return true if admin, false if not
     */
    public boolean isAdmin(long principal) {
        return personRepo.findById(principal).get().getRole() == Role.ADMIN;
    }
}
