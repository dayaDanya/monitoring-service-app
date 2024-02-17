package org.ylab.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.ylab.domain.dto.PersonInDto;
import org.ylab.domain.dto.Response;
import org.ylab.domain.models.Person;
import org.ylab.domain.usecases.PersonUseCase;
import org.ylab.exceptions.BadCredentialsException;
import org.ylab.exceptions.EmailFormatException;
import org.ylab.exceptions.PasswordLengthException;
import org.ylab.exceptions.PersonNotFoundException;
import org.ylab.infrastructure.mappers.PersonInputMapper;
import org.ylab.infrastructure.utils.ValidationUtil;

@RestController
public class PersonController {
    private final PersonUseCase personUseCase;
    private final PersonInputMapper personInputMapper;

    public PersonController(PersonUseCase personUseCase, PersonInputMapper personInputMapper) {
        this.personUseCase = personUseCase;
        this.personInputMapper = personInputMapper;
    }
    @PostMapping(value= "/authorization", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authorize(@RequestBody PersonInDto personDto){
        Person person = personInputMapper.dtoToObj(personDto);
        try {
            String token = personUseCase.authorize(person);
            Response response = new Response(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BadCredentialsException | PersonNotFoundException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }
    }
    @PostMapping(value= "/registration", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody PersonInDto personDto){
        Person person = personInputMapper.dtoToObj(personDto);
        try {
            ValidationUtil.checkIsEmail(personDto.getEmail());
            ValidationUtil.checkIsPasswordLongEnough(personDto.getPassword());
            personUseCase.register(person);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (EmailFormatException | PasswordLengthException | BadCredentialsException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }
    }

}
