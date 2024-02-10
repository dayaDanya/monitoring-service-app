package org.ylab.services;

import org.ylab.aop.annotations.Recordable;
import org.ylab.domain.enums.Role;
import org.ylab.domain.models.Counter;
import org.ylab.domain.models.CounterType;
import org.ylab.domain.models.Measurement;
import org.ylab.domain.models.Operation;
import org.ylab.domain.usecases.CounterTypeUseCase;
import org.ylab.domain.usecases.CounterUseCase;
import org.ylab.domain.usecases.MeasurementUseCase;
import org.ylab.domain.usecases.OperationUseCase;
import org.ylab.exceptions.CounterTypeAlreadyExistsException;
import org.ylab.exceptions.CounterTypeNotFoundException;
import org.ylab.repositories.IPersonRepo;
import org.ylab.repositories.implementations.PersonRepo;

import java.util.Map;
@Recordable
public class AdminService {
    private final CounterUseCase counterUseCase;
    private final CounterTypeUseCase counterTypeUseCase;

    private final OperationUseCase operationUseCase;

    private final MeasurementUseCase measurementUseCase;

    private final IPersonRepo personRepo;
    public AdminService(CounterUseCase counterUseCase, CounterTypeUseCase counterTypeUseCase, OperationUseCase operationUseCase, MeasurementUseCase measurementUseCase, PersonRepo personRepo) {
        this.counterUseCase = counterUseCase;
        this.counterTypeUseCase = counterTypeUseCase;
        this.operationUseCase = operationUseCase;
        this.measurementUseCase = measurementUseCase;
        this.personRepo = personRepo;
    }
    @Recordable
    public void saveCounter(Counter counter) {
        try {
            counterUseCase.saveCounter(counter);
        } catch (CounterTypeNotFoundException e) {
            throw e;
        }
    }
    @Recordable
    public void saveCounterType(CounterType counterType){
        try {
            counterTypeUseCase.saveCounterType(counterType);
        } catch (CounterTypeAlreadyExistsException e) {
            throw e;
        }
    }
    //todo добавить интерфейс
    @Recordable
    public Map<Long, Operation> findAllOperations(){
        return operationUseCase.findAllOperations();
    }
    @Recordable
    public Map<Long, Measurement> findAllMeasurements(){
        return measurementUseCase.findAllMeasurements();
    }

    public boolean isAdmin(long principal) {
        return personRepo.findById(principal).get().getRole() == Role.ADMIN;
    }
}
