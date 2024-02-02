package org.ylab.usecases;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ylab.domain.models.CounterType;

class CounterTypeUseCaseTest {

    CounterTypeUseCase counterTypeUseCase;

    @BeforeEach
    void setUp() {

    }

    @Test
    void findOne_correctCounterType_finds() {
        CounterType counterType = new CounterType("hot");
        counterTypeUseCase.save(counterType);
        Assertions.assertEquals(counterType, counterTypeUseCase.findOne(counterType.getName()).get());
    }

}