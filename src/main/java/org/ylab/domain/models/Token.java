package org.ylab.domain.models;

/**
 * Модель токена, представляющая собой сущность с уникальным идентификатором, идентификатором пользователя
 * и значением токена.
 */
public class Token {

    private long id;
    private long personId;
    private String value;

    /**
     * Конструктор для создания объекта Token с заданными значениями.
     *
     * @param id       Уникальный идентификатор токена.
     * @param personId Идентификатор пользователя, связанного с токеном.
     * @param value    Значение токена.
     */
    public Token(long id, long personId, String value) {
        this.id = id;
        this.personId = personId;
        this.value = value;
    }

    /**
     * Возвращает уникальный идентификатор токена.
     *
     * @return Уникальный идентификатор токена.
     */
    public long getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор токена.
     *
     * @param id Уникальный идентификатор токена.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Возвращает идентификатор пользователя, связанного с токеном.
     *
     * @return Идентификатор пользователя.
     */
    public long getPersonId() {
        return personId;
    }

    /**
     * Устанавливает идентификатор пользователя, связанного с токеном.
     *
     * @param personId Идентификатор пользователя.
     */
    public void setPersonId(long personId) {
        this.personId = personId;
    }

    /**
     * Возвращает значение токена.
     *
     * @return Значение токена.
     */
    public String getValue() {
        return value;
    }

    /**
     * Устанавливает значение токена.
     *
     * @param value Значение токена.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
