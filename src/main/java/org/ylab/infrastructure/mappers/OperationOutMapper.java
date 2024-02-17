package org.ylab.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.ylab.domain.dto.OperationOutDto;
import org.ylab.domain.models.Operation;
@Mapper
public interface OperationOutMapper {
    OperationOutDto objToDto(Operation operation);
}
