package org.ylab.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.ylab.domain.dto.CounterDto;
import org.ylab.domain.models.Counter;

@Mapper
public interface CounterMapper {
    CounterDto objToDto(Counter counter);
    Counter dtoToObj(CounterDto dto);
}
