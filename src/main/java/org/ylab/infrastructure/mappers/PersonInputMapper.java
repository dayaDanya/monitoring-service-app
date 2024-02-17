package org.ylab.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.ylab.domain.dto.PersonInDto;
import org.ylab.domain.models.Person;

@Mapper
public interface PersonInputMapper {
    Person dtoToObj(PersonInDto personInDto);
}
