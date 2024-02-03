package org.ylab.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.ylab.domain.models.Person;
import org.ylab.exceptions.BadCredentialsException;
import org.ylab.exceptions.PersonNotFoundException;
import org.ylab.repositories.implementations.PersonRepo;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PersonServiceTest {

    @InjectMocks
    PersonService personUseCase;

    @Mock
    PasswordService passwordUseCase;

    @Mock
    PersonRepo personRepo;

    @Mock
    OperationService operationUseCase;


    @Test
    @DisplayName("Регистрация: существующий email - выбрасывает исключение BadCredentialsException")
    void register_withUsedEmail_throwsException() {
        Person person = new Person("user@user.com", "password");
        Mockito.when(passwordUseCase.encrypt("password"))
                .thenReturn("xCfNDSIR3Xnfz5Sdq4d4jA==:/MUq+hTBBM2u1ZwLOlUBFS+JpkKQGUWqYngV8N5XybrwyrJI2No7kwy2aEQpfpPhbjdACL1BE0aisWUn1WPuAQ==");

        Mockito.when(personRepo.findByEmail("user@user.com"))
                .thenReturn(Optional.of(person));

        Optional<Long> id = Optional.of(1L);
        Mockito.when(personRepo.findIdByEmail("user@user.com"))
                .thenReturn(id);

        Assertions.assertThrows(BadCredentialsException.class, () -> personUseCase.register(person));
    }

    @Test
    @DisplayName("Регистрация: новый email - успешно сохраняет пользователя")
    void register_withNewEmail_savesPerson() {
        Person person = new Person("user@user.com", "password");
        Mockito.when(passwordUseCase.encrypt("password"))
                .thenReturn("xCfNDSIR3Xnfz5Sdq4d4jA==:/MUq+hTBBM2u1ZwLOlUBFS+JpkKQGUWqYngV8N5XybrwyrJI2No7kwy2aEQpfpPhbjdACL1BE0aisWUn1WPuAQ==");

        Optional<Long> id = Optional.of(1L);
        Mockito.when(personRepo.findIdByEmail("user@user.com"))
                .thenReturn(id);

        personUseCase.register(person);
        Mockito.verify(personRepo, Mockito.times(1)).save(person);
    }

    @Test
    @DisplayName("Аутентификация: новый email - выбрасывает исключение PersonNotFoundException")
    void authenticate_withNewEmail_throwsException() {
        Person person = new Person("user@user.com", "password");
        Assertions.assertThrows(PersonNotFoundException.class, () -> personUseCase.authenticate(person));
    }

    @Test
    @DisplayName("Аутентификация: неверный пароль - выбрасывает исключение BadCredentialsException")
    void authenticate_withWrongPassword_throwsException() {
        Person person = new Person("user@user.com", "password_wrong");
        Mockito.when(personRepo.findByEmail("user@user.com")).thenReturn(Optional.of(person));
        Assertions.assertThrows(BadCredentialsException.class, () -> personUseCase.authenticate(person));
    }

}