package org.ylab.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.ylab.domain.dto.CounterDto;
import org.ylab.domain.models.Counter;

/**
 * Маппер преобразующий dto в сущность и обратно
 */
@Mapper
public interface CounterMapper {
    /**
     * Преобразование сущности в dto
     * @param counter сущность
     * @return dto
     */
    CounterDto objToDto(Counter counter);

    /**
     * Преобразование dto в сущность
     * @param dto
     * @return сущность
     */
    Counter dtoToObj(CounterDto dto);
}
