package org.ylab.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.ylab.domain.dto.CounterTypeDto;
import org.ylab.domain.models.CounterType;

@Mapper
public interface CounterTypeMapper {
    CounterType dtoToObj(CounterTypeDto dto);
}
