package org.ylab.domain.models;
//todo doc
public class Token {
    private long id;

    private long personId;
    private String value;

    public Token(long id, long personId, String value) {
        this.id = id;
        this.personId = personId;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
