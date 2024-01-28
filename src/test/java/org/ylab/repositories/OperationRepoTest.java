package org.ylab.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ylab.domain.models.Operation;
import org.ylab.domain.models.enums.Action;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

class OperationRepoTest {
    private OperationRepo repo;

    @BeforeEach
    void setUp() {
        repo  =  new OperationRepo();
        repo.deleteAll();
    }



    @Test
    void findAll() {
        Operation o1 = new Operation(1, Action.WATCH_ALL, LocalDateTime.now());
        Operation o2 = new Operation(1, Action.WATCH_ALL, LocalDateTime.now());
        repo.save(o1);
        repo.save(o2);
        List<Operation> expected = Arrays.asList(o1, o2);
        Assertions.assertEquals(expected, repo.findAll());
    }

    @Test
    void findAllById() {
        Operation ofUser1 = new Operation(1, Action.AUTHENTICATION, LocalDateTime.now());
        Operation ofUser2 = new Operation(1, Action.LOGOUT, LocalDateTime.now());
        Operation ofOtherUser1 = new Operation(2, Action.REGISTRATION, LocalDateTime.now());
        Operation ofOtherUser2 = new Operation(2, Action.AUTHENTICATION, LocalDateTime.now());
        repo.save(ofUser1);
        repo.save(ofUser2);
        repo.save(ofOtherUser1);
        repo.save(ofOtherUser2);
        List<Operation> expected = Arrays.asList(ofUser1, ofUser2);
        List<Operation> actual = repo.findAllById(1);
        expected.stream().forEach(o-> System.out.println(o.toString()));
        actual.stream().forEach(o-> System.out.println(o.toString()));
        Assertions.assertEquals(expected, actual);
    }
}