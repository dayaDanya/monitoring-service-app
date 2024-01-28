package org.ylab.domain.dto;

/**
 * Data Transfer Object (DTO) - входные данные для пользователя,
 * класс для передачи данных о пользователе между слоями приложения
 */
public class PersonInDto {

    /**
     * Электронная почта пользователя
     */
    private String email;

    /**
     * Пароль пользователя
     */
    private String password;

    /**
     * Конструктор входных данных для пользователя на основе электронной почты и пароля
     * @param email Электронная почта пользователя
     * @param password Пароль пользователя
     */
    public PersonInDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Пустой конструктор
     */
    public PersonInDto() {
    }

    /**
     * Геттер для электронной почты пользователя
     * @return Электронная почта пользователя
     */
    public String getEmail() {
        return email;
    }

    /**
     * Сеттер для электронной почты пользователя
     * @param email Новая электронная почта пользователя
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Геттер для пароля пользователя
     * @return Пароль пользователя
     */
    public String getPassword() {
        return password;
    }

    /**
     * Сеттер для пароля пользователя
     * @param password Новый пароль пользователя
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
