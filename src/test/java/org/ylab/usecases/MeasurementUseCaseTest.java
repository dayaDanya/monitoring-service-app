package org.ylab.usecases;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ylab.domain.dto.MeasurementInDto;
import org.ylab.domain.models.Counter;
import org.ylab.domain.models.CounterType;
import org.ylab.domain.models.Measurement;
import org.ylab.util.WrongDateException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MeasurementUseCaseTest {

    MeasurementUseCase measurementUseCase;

    @BeforeEach
    void setUp() {
        CounterTypeUseCase counterTypeUseCase = new CounterTypeUseCase();
        counterTypeUseCase.save(new CounterType("cold"));
        CounterUseCase counterUseCase = new CounterUseCase(counterTypeUseCase);
        counterUseCase.save(new Counter(1, new CounterType("cold")));
        measurementUseCase = new MeasurementUseCase(counterUseCase);


    }
    @Test
    void save(){
        MeasurementInDto dto = new MeasurementInDto(5345.5, "cold");
        Measurement measurement = new Measurement(dto, LocalDateTime.now());
        measurementUseCase.save(measurement, 1);
        Assertions.assertThrows(WrongDateException.class, () -> measurementUseCase.save(measurement, 1));
    }

    @Test
    void findAll() {
    }

    @Test
    void findAllById() {
    }

    @Test
    void findLast() {
        MeasurementInDto dto = new MeasurementInDto(5345.5, "cold");
        Measurement measurement1 = new Measurement(dto, LocalDateTime.of(
                2023, 1, 25, 13, 7, 23));
        Measurement measurement2 = new Measurement(dto, LocalDateTime.of(
                2023, 3, 25, 13, 7, 23));
        Measurement measurement3 = new Measurement(dto, LocalDateTime.of(
                2023, 4, 25, 13, 7, 23));
        measurementUseCase.save(measurement1, 1);
        measurementUseCase.save(measurement2, 1);
        measurementUseCase.save(measurement3, 1);
        Assertions.assertEquals(measurement3, measurementUseCase.findByMonth(1, 4, new CounterType("cold")));
    }

    @Test
    void findByMonth() {
        MeasurementInDto dto = new MeasurementInDto(5345.5, "cold");
        Measurement measurement = new Measurement(dto, LocalDateTime.of(
                2024, 7, 25, 13, 7, 23));
        measurementUseCase.save(measurement, 1);
        Assertions.assertEquals(measurement, measurementUseCase.findByMonth(1, 7, new CounterType("cold")));

    }
}