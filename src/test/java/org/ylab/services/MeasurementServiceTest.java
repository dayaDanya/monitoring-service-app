package org.ylab.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.ylab.domain.dto.MeasurementInDto;
import org.ylab.domain.models.Measurement;
import org.ylab.exceptions.BadMeasurementAmountException;
import org.ylab.exceptions.CounterNotFoundException;
import org.ylab.exceptions.WrongDateException;
import org.ylab.repositories.implementations.MeasurementRepo;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;
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
    @DisplayName("Сохранение: счетчик не существует - " +
            "выбрасывает исключение CounterNotFoundException")
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
    @DisplayName("Сохранение: некорректная дата - выбрасывает исключение WrongDateException")
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
    @DisplayName("Сохранение: новое измерение меньше предыдущего - выбрасывает исключение BadMeasurementAmountException")
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
    @Test
    @DisplayName("Получение всех измерений")
    void findAll() {
        Measurement hot = new Measurement(4234.3, "HOT"
        );
        Map<Long,Measurement> map = Map.of(2L, hot);
        Mockito.when(measurementRepo.findAll())
                        .thenReturn(map);
        Assertions.assertEquals(1, measurementUseCase.findAll().size());
    }

}