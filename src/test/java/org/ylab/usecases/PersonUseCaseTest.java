package org.ylab.usecases;

import org.junit.jupiter.api.Assertions;
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
import org.ylab.repositories.PersonRepo;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PersonUseCaseTest {

    @InjectMocks
    PersonUseCase personUseCase;

    @Mock
    PasswordUseCase passwordUseCase;

    @Mock
    PersonRepo personRepo;

    @Mock
    OperationUseCase operationUseCase;


    @Test
    void register_withUsedEmail_throwsException() {
        Person person = new Person("user@user.com", "password");
        //подмена логики шифрования пароля
        Mockito.when(passwordUseCase.encrypt("password"))
                .thenReturn("xCfNDSIR3Xnfz5Sdq4d4jA==:/MUq+hTBBM2u1ZwLOlUBFS+JpkKQGUWqYngV8N5XybrwyrJI2No7kwy2aEQpfpPhbjdACL1BE0aisWUn1WPuAQ==");
        //подмена логики предварительной регистрации пользователя с таким email
        Mockito.when(personRepo.findByEmail("user@user.com"))
                .thenReturn(Optional.of(person));
        //подмена логики поиска пользователя по email
        Optional<Long> id = Optional.of(1L);
        Mockito.when(personRepo.findIdByEmail("user@user.com"))
                .thenReturn(id);
        Assertions.assertThrows(BadCredentialsException.class, () -> personUseCase.register(person));

    }
    @Test
    void register_withNewEmail_savesPerson(){
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
    void authenticate_withNewEmail_throwsException() {
        Person person = new Person("user@user.com", "password");
        Assertions.assertThrows(PersonNotFoundException.class, () -> personUseCase.authenticate(person));
    }
    @Test
    void authenticate_withWrongPassword_throwsException(){
        Person person = new Person("user@user.com", "password_wrong");
        Mockito.when(personRepo.findByEmail("user@user.com")).thenReturn(Optional.of(person));
        Assertions.assertThrows(BadCredentialsException.class, () -> personUseCase.authenticate(person));
    }

}