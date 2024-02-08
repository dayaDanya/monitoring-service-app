package org.ylab.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.ylab.domain.dto.MeasurementInDto;
import org.ylab.domain.models.Measurement;

@Mapper
public interface MeasurementInMapper {
    Measurement dtoToObj(MeasurementInDto dto);

}
