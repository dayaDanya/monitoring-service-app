package org.ylab.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ylab.domain.models.Person;

class PersonRepoTest {

    PersonRepo personRepo;

    @BeforeEach
    void setUp() {

    }

    @Test
    void findByEmail_equal() {
//        personRepo = mock(PersonRepo.class);
        Person expected = new Person("email", "password");
        personRepo.save(expected);
        Assertions.assertEquals(expected, personRepo.findByEmail("email").get());
    }

    @Test
    void findIdByEmail_equal() {
        Person expected = new Person("email", "password");
        personRepo.save(expected);
        Assertions.assertEquals(1, personRepo.findIdByEmail("email").get());
    }

    @Test
    void findById_equal() {

        Person expected = new Person("email", "password");
        personRepo.save(expected);
        long id = personRepo.findIdByEmail("email").get();
        expected.setId(id);
        Assertions.assertEquals(expected, personRepo.findById(id).get());
    }
}