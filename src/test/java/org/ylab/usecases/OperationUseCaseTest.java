package org.ylab.usecases;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ylab.domain.models.Operation;
import org.ylab.domain.models.enums.Action;
import org.ylab.repositories.OperationRepo;

import java.time.LocalDateTime;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class OperationUseCaseTest {

    @Mock
    OperationRepo operationRepo;

    @InjectMocks
    OperationUseCase operationUseCase;

    @Test
    void save() {
        Operation operation = new Operation(1, Action.REGISTRATION, LocalDateTime.now());
        operationUseCase.save(operation);
        Mockito.verify(operationRepo , Mockito.times(1)).save(operation);
    }

    @Test
    void findAll() {
        Operation operation1 = new Operation(1, Action.REGISTRATION, LocalDateTime.now());
        Operation operation2 = new Operation(1, Action.AUTHENTICATION, LocalDateTime.now());
        Operation operation3 = new Operation(2, Action.REGISTRATION, LocalDateTime.now());
        operationUseCase.save(operation1);
        operationUseCase.save(operation2);
        operationUseCase.save(operation3);
        Map<Long, Operation> ops = Map.of(1L, operation1, 2L, operation2, 3L, operation3);
        Mockito.when(operationRepo.findAll())
                        .thenReturn(ops);
        Assertions.assertEquals(ops,
                operationUseCase.findAll());

    }


}