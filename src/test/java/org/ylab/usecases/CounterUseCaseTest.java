package org.ylab.usecases;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.ylab.domain.models.Counter;
import org.ylab.domain.models.CounterType;

import java.util.List;

class CounterUseCaseTest {
    @Mock
    CounterTypeUseCase counterTypeUseCase;
    @Mock
    PasswordUseCase passwordUseCase;
    @InjectMocks
    CounterUseCase counterUseCase;

    @BeforeEach
    void setUp() {

    }


    @Test
    void findByPersonId() {
        Counter counter = new Counter(1, new CounterType("cold"));
        counterUseCase.save(counter);
        Assertions.assertEquals(List.of(counter), counterUseCase.findByPersonId(1));
    }
}