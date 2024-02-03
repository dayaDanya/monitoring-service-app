package org.ylab.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.ylab.domain.models.Measurement;
import org.ylab.exceptions.BadMeasurementAmountException;
import org.ylab.exceptions.CounterNotFoundException;
import org.ylab.exceptions.WrongDateException;
import org.ylab.repositories.implementations.MeasurementRepo;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MeasurementServiceTest {
    @InjectMocks
    MeasurementService measurementUseCase;
    @Mock
    MeasurementRepo measurementRepo;

    @Mock
    CounterService counterUseCase;

    @Mock
    OperationService operationUseCase;

    @Test
    void save_ifCounterNotExists_throwsException() {
        LocalDateTime localDateTime = LocalDateTime.of(
                2023, Month.DECEMBER, 25, 10, 15, 1
        );
        Measurement measurement = new Measurement(1, 1000.1, localDateTime,
                "HOT", 2);
        Mockito.when(counterUseCase.findIdByPersonIdAndCounterType(3,
                        measurement.getCounterType())).thenReturn(Optional.empty());
        Assertions.assertThrows(CounterNotFoundException.class,
                () -> measurementUseCase.save(measurement, 3));
    }
    @Test
    void save_incorrectDate_throwsException() {
        LocalDateTime localDateTime = LocalDateTime.of(
                2023, Month.DECEMBER, 25, 10, 15, 1
        );
        Measurement measurement = new Measurement(1, 1000.1, localDateTime,
                "HOT", 2);
        Mockito.when(counterUseCase.findIdByPersonIdAndCounterType(3,
                measurement.getCounterType())).thenReturn(Optional.of(2L));
        Mockito.when(measurementRepo.findLastDate(2L))
                        .thenReturn(Optional.of(LocalDateTime.now()));
        Assertions.assertThrows(WrongDateException.class,
                () -> measurementUseCase.save(measurement, 3));
    }
    @Test
    void save_smallerAmount_throwsException() {
        LocalDateTime localDateTime = LocalDateTime.of(
                2023, Month.DECEMBER, 25, 10, 15, 1
        );
        Measurement measurement = new Measurement(1, 1000.1, LocalDateTime.now(),
                "HOT", 2);
        Mockito.when(counterUseCase.findIdByPersonIdAndCounterType(3,
                measurement.getCounterType())).thenReturn(Optional.of(2L));
        Mockito.when(measurementRepo.findLastDate(2L))
                .thenReturn(Optional.of(localDateTime));
        Mockito.when(measurementRepo.findLast(2L))
                        .thenReturn(Optional.of(new Measurement(1, 1000.2, LocalDateTime.now(),
                                "HOT", 2)));
        Assertions.assertThrows(BadMeasurementAmountException.class,
                () -> measurementUseCase.save(measurement, 3));
    }
    //todo other tests
    @Test
    void findAll() {
    }

    @Test
    void findAllById() {
    }

    @Test
    void findLast() {

    }

    @Test
    void findByMonth() {


    }
}