package org.ylab.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.ylab.domain.dto.MeasurementOutDto;
import org.ylab.domain.models.Measurement;

@Mapper
public interface MeasurementOutMapper {
    MeasurementOutDto objToDto(Measurement measurement);

}
