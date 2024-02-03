package org.ylab.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ylab.domain.models.CounterType;
import org.ylab.exceptions.CounterTypeAlreadyExistsException;
import org.ylab.repositories.implementations.CounterTypeRepo;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CounterTypeServiceTest {
    @InjectMocks
    CounterTypeService counterTypeUseCase;
    @Mock
    CounterTypeRepo counterTypeRepo;
    @DisplayName("Проверяет нахождение типа счетчика с переданным названием")
    @Test
    void findOne_correctCounterType_finds() {
        CounterType counterType = new CounterType("hot");

        Mockito.when(counterTypeRepo.findOne("hot"))
                        .thenReturn(Optional.of(counterType));
        Assertions.assertEquals(counterType, counterTypeUseCase.findOne(counterType.getName()).get());
    }
    @DisplayName("Проверяет то, что при добавлении нового типа счетчика, репозиторий отработает")
    @Test
    void save_newCounterType_saves(){
        CounterType counterType = new CounterType("hot");
        Mockito.when(counterTypeRepo.findOne("hot"))
                .thenReturn(Optional.empty());
        counterTypeUseCase.save(counterType);
        Mockito.verify(counterTypeRepo, Mockito
                .times(1))
                .save(counterType);
    }
    @DisplayName("Проверяет то, что при добавлении существующего типа счетчика, будет выброшено исключение")
    @Test
    void save_existingCounterType_throwsException(){
        CounterType counterType = new CounterType("hot");
        Mockito.when(counterTypeRepo.findOne("hot"))
                .thenReturn(Optional.of(counterType));
        Assertions.assertThrows(CounterTypeAlreadyExistsException.class,
                () -> counterTypeUseCase.save(counterType));
    }

}