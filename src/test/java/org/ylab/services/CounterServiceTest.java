package org.ylab.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ylab.domain.models.Counter;
import org.ylab.domain.models.CounterType;
import org.ylab.exceptions.CounterTypeNotFoundException;
import org.ylab.repositories.implementations.CounterRepo;

import java.util.Optional;
@ExtendWith(MockitoExtension.class)
class CounterServiceTest {
    @Mock
    CounterTypeService counterTypeUseCase;
    @Mock
    CounterRepo counterRepo;
    @InjectMocks
    CounterService counterUseCase;
    @DisplayName("Проверяет, что при случае передачи неправильного типа счетчика, " +
            "будет выброшено исключение")
    @Test
    void save_withWrongCounterType_throwsException(){
        Counter counter = new Counter(1, new CounterType("BAD_TYPE"));
        Mockito.when(counterTypeUseCase.findOne(counter.getCounterType().getName()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(CounterTypeNotFoundException.class, () -> counterUseCase.saveCounter(counter));
    }
    @DisplayName("Проверяет, что при передаче правильных параметров, " +
            "сущность будет сохранена")
    @Test
    void save_withCorrectCounterType_saves() {
        Counter counter = new Counter(1, new CounterType("COLD"));
        Mockito.when(counterTypeUseCase.findOne(counter.getCounterType().getName()))
                .thenReturn(Optional.of(new CounterType("COLD")));
        counterUseCase.saveCounter(counter);
        Mockito.verify(counterRepo, Mockito.times(1))
                .save(counter);
    }

}