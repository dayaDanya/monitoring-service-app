package org.ylab.domain.rules;

import org.ylab.domain.models.Person;

public interface PersonInputBoundary {
    void register(Person person);

    String authenticate(Person person);
}
