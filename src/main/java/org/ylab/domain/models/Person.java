package org.ylab.domain.models;

import org.ylab.domain.models.enums.Role;

/**
 * Сущность - пользователь,
 * класс описывающий атрибуты пользователя в системе
 */
public class Person {

    /**
     * Уникальный идентификатор пользователя
     */
    private long id;

    /**
     * Электронная почта пользователя
     */
    private String email;

    /**
     * Пароль пользователя
     */
    private String password;

    /**
     * Роль пользователя в системе
     */
    private Role role;


    /**
     * Конструктор пользователя с указанием электронной почты, пароля и роли
     * @param id идентификатор пользователя
     * @param email Электронная почта пользователя
     * @param password Пароль пользователя
     * @param role Роль пользователя
     */
    public Person(long id, String email, String password, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Конструктор пользователя с указанием электронной почты и пароля (роль по умолчанию - USER)
     * @param email Электронная почта пользователя
     * @param password Пароль пользователя
     */
    public Person(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = Role.USER;
    }

    /**
     * Геттер для уникального идентификатора пользователя
     * @return Уникальный идентификатор пользователя
     */
    public long getId() {
        return id;
    }

    /**
     * Сеттер для уникального идентификатора пользователя
     * @param id Уникальный идентификатор пользователя
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Геттер для электронной почты пользователя
     * @return Электронная почта пользователя
     */
    public String getEmail() {
        return email;
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
     * @param password Пароль пользователя
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Сеттер для роли пользователя
     * @param role роль пользователя
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Геттер для роли пользователя
     * @return Роль пользователя в системе
     */
    public Role getRole() {
        return role;
    }
}
