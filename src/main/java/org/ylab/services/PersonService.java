package org.ylab.services;

import org.ylab.aop.annotations.Recordable;
import org.ylab.domain.models.Operation;
import org.ylab.domain.models.Person;
import org.ylab.domain.models.enums.Action;
import org.ylab.domain.models.enums.Role;
import org.ylab.domain.usecases.OperationUseCase;
import org.ylab.domain.usecases.PasswordEncoder;
import org.ylab.exceptions.BadCredentialsException;
import org.ylab.exceptions.PersonNotFoundException;
import org.ylab.repositories.IPersonRepo;
import org.ylab.repositories.implementations.PersonRepo;
import org.ylab.security.services.JwtService;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Класс, представляющий использование сущности Person в рамках бизнес-логики.
 * Реализует методы для регистрации, аутентификации, выхода из системы и поиска информации о пользователе.
 */
@Recordable
public class PersonService implements org.ylab.domain.usecases.PersonUseCase {

    private final PasswordEncoder passwordUseCase;
    private final IPersonRepo personRepo;
    private final OperationUseCase operationUseCase;



    /**
     * Конструктор класса PersonUseCase, инициализирующий репозиторий пользователей, использование паролей, операций,
     * сервис счетчиков и типов счетчиков.
     */
    public PersonService(PasswordService passwordUseCase, PersonRepo personRepo,
                         OperationService operationUseCase) {
        this.passwordUseCase = passwordUseCase;
        this.personRepo = personRepo;
        this.operationUseCase = operationUseCase;

    }


    /**
     * Метод для проверки уникальности адреса электронной почты при регистрации пользователя.
     *
     * @param email Адрес электронной почты для проверки уникальности.
     * @return {@code true}, если адрес электронной почты уникален, иначе {@code false}.
     */
    private boolean isUnique(String email) {
        return personRepo.findByEmail(email).isEmpty();
    }

    /**
     * Метод для регистрации нового пользователя.
     *
     * @param person Объект Person, содержащий информацию о пользователе.
     * @throws BadCredentialsException Выбрасывается в случае неверных учетных данных при регистрации.
     */
    @Override
    public void register(Person person) throws BadCredentialsException {
        if (isUnique(person.getEmail())) {
            person.setPassword(passwordUseCase.encrypt(person.getPassword()));
            person.setRole(Role.USER);
            personRepo.save(person);


            operationUseCase.save(new Operation(personRepo.findIdByEmail(person.getEmail()).get(), Action.REGISTRATION, LocalDateTime.now()));
        } else {
            throw new BadCredentialsException();
        }
    }


    /**
     * Метод для аутентификации пользователя и выдачи токена.
     *
     * @param person Объект Person, содержащий информацию для аутентификации.
     * @return Строковый токен в случае успешной аутентификации.
     * @throws PersonNotFoundException Выбрасывается, если пользователь с указанным адресом электронной почты не найден.
     * @throws BadCredentialsException Выбрасывается в случае неверных учетных данных при аутентификации.
     */
    @Override
    public String authenticate(Person person) throws PersonNotFoundException, BadCredentialsException {
        Optional<Person> found = personRepo.findByEmail(person.getEmail());
        if (found.isEmpty()) {
            throw new PersonNotFoundException();
        } else if (!passwordUseCase.isPswCorrect(person.getPassword(), found.get().getPassword())) {
            throw new BadCredentialsException();
        } else {
           // operationUseCase.save(new Operation(found.get().getId(), Action.AUTHENTICATION, LocalDateTime.now()));
            return JwtService.generateToken(String.valueOf(found.get().getId()));
        }
    }

    /**
     * Метод для поиска пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Объект Person, представляющий пользователя с указанным идентификатором.
     * @throws PersonNotFoundException Выбрасывается, если пользователь с указанным идентификатором не найден.
     */
    public Person findById(long id) throws PersonNotFoundException {
        return personRepo.findById(id).orElseThrow(PersonNotFoundException::new);
    }
}
