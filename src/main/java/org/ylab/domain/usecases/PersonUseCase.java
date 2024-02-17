package org.ylab.domain.usecases;

import org.ylab.domain.models.Person;

public interface PersonUseCase {
    void register(Person person);

    String authorize(Person person);

    Person findById(long id);
}
