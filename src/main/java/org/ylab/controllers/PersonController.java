package org.ylab.controllers;

import org.ylab.domain.dto.PersonInDto;
import org.ylab.domain.models.Person;
import org.ylab.usecases.PersonUseCase;
import org.ylab.exceptions.BadCredentialsException;
import org.ylab.exceptions.PersonNotFoundException;

/**
 * Класс, представляющий контроллер для операций с пользователями.
 */
public class PersonController {

    private final PersonUseCase personUseCase;

    /**
     * Конструктор класса PersonController, инициализирующий используемые компоненты.
     */
    public PersonController(PersonUseCase personUseCase) {
        this.personUseCase = personUseCase;
    }

    /**
     * Метод для регистрации нового пользователя.
     *
     * @param personDto DTO с информацией о новом пользователе.
     * @return Строка с результатом операции.
     */
    public String register(PersonInDto personDto) {
        try {
            personUseCase.register(new Person(personDto.getEmail(), personDto.getPassword()));
            return "201 created";
        } catch (BadCredentialsException e) {
            return "400 bad request";
        }
    }

    /**
     * Метод для аутентификации пользователя и получения токена.
     *
     * @param personDto DTO с информацией для аутентификации.
     * @return Строка с результатом операции и, в случае успеха, токен авторизации.
     */
    public String authenticate(PersonInDto personDto) {
        try {
            String token = personUseCase.authenticate(new Person(personDto.getEmail(), personDto.getPassword()));
            return "200 OK. Your authorization token is: " + token;
        } catch (PersonNotFoundException | BadCredentialsException e) {
            return "400 bad request: " + e.getMessage();
        }
    }

    /**
     * Метод для выхода пользователя из системы (отзыв токена).
     *
     * @param token Токен пользователя, который требуется отозвать.
     * @return Строка с результатом операции.
     */
    public String logout(String token) {
        if (personUseCase.logout(token))
            return "200 OK. You were logged out. Authorize again to get a new token.";
        else
            return "400 bad request";
    }
}