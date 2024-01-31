package org.ylab.usecases;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ylab.domain.models.Person;
import org.ylab.repositories.PersonRepo;
import org.ylab.exceptions.BadCredentialsException;
import org.ylab.exceptions.PersonNotFoundException;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class PersonUseCaseTest {

    PersonUseCase personUseCase ;

    @BeforeEach
    void setUp() {
        personUseCase = new PersonUseCase(new PersonRepo(new ArrayList<>()));
    }

    @Test
    void register_withUsedEmail_throwsException() {
        Person person = new Person("user@user.com", "password");
        personUseCase.register(person);
        Assertions.assertThrows(BadCredentialsException.class, () -> personUseCase.register(person));

    }

    @Test
    void authenticate_withNewEmail_throwsException() {
        Person person = new Person("user@user.com", "password");
        Assertions.assertThrows(PersonNotFoundException.class, () -> personUseCase.authenticate(person));
    }
}