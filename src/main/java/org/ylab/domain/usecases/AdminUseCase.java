package org.ylab.domain.usecases;

import org.ylab.domain.models.Counter;
import org.ylab.domain.models.CounterType;
import org.ylab.domain.models.Measurement;
import org.ylab.domain.models.Operation;

import java.util.Map;

public interface AdminUseCase {


    void saveCounter(Counter counter);


    void saveCounterType(CounterType counterType);


    Map<Long, Operation> findAllOperations();


    Map<Long, Measurement> findAllMeasurements();

    boolean isAdmin(long principal);

}
