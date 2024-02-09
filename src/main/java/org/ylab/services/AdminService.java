package org.ylab.services;

import org.ylab.domain.models.Counter;
import org.ylab.domain.models.CounterType;
import org.ylab.domain.models.Measurement;
import org.ylab.domain.models.Operation;
import org.ylab.domain.usecases.CounterTypeUseCase;
import org.ylab.domain.usecases.CounterUseCase;
import org.ylab.domain.usecases.MeasurementUseCase;
import org.ylab.domain.usecases.OperationUseCase;
import org.ylab.exceptions.CounterTypeNotFoundException;

import java.util.Map;

public class AdminService {
    private final CounterUseCase counterUseCase;
    private final CounterTypeUseCase counterTypeUseCase;

    private final OperationUseCase operationUseCase;

    private final MeasurementUseCase measurementUseCase;
    public AdminService(CounterUseCase counterUseCase, CounterTypeUseCase counterTypeUseCase, OperationUseCase operationUseCase, MeasurementUseCase measurementUseCase) {
        this.counterUseCase = counterUseCase;
        this.counterTypeUseCase = counterTypeUseCase;
        this.operationUseCase = operationUseCase;
        this.measurementUseCase = measurementUseCase;
    }

    public void saveCounter(Counter counter) {
        try {
            counterUseCase.save(counter);
        } catch (CounterTypeNotFoundException e) {
            throw e;
        }
    }
    public void saveCounterType(CounterType counterType){
        try {
            counterTypeUseCase.save(counterType);
        } catch (CounterTypeNotFoundException e) {
            throw e;
        }
    }
    public Map<Long, Operation> findAllOperations(){
        return operationUseCase.findAll();
    }
    public Map<Long, Measurement> findAllMeasurements(){
        return measurementUseCase.findAll();
    }
}
