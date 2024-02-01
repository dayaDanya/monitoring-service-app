package org.ylab.usecases;

import org.ylab.domain.models.Operation;
import org.ylab.domain.models.Person;
import org.ylab.domain.models.enums.Action;
import org.ylab.domain.models.enums.Role;
import org.ylab.domain.rules.PersonInputBoundary;
import org.ylab.exceptions.BadCredentialsException;
import org.ylab.exceptions.PersonNotFoundException;
import org.ylab.repositories.PersonRepo;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Класс, представляющий использование сущности Person в рамках бизнес-логики.
 * Реализует методы для регистрации, аутентификации, выхода из системы и поиска информации о пользователе.
 */
public class PersonUseCase implements PersonInputBoundary {

    private final PasswordUseCase passwordUseCase;
    private final PersonRepo personRepo;
    private final OperationUseCase operationUseCase;
    private final TokenService tokenService;
    private final CounterUseCase counterUseCase;
    private final CounterTypeUseCase counterTypeUseCase;

    /**
     * Конструктор класса PersonUseCase, инициализирующий репозиторий пользователей, использование паролей, операций,
     * токен-сервиса, использование счетчиков и типов счетчиков.
     */
    public PersonUseCase(PasswordUseCase passwordUseCase, PersonRepo personRepo,
                         OperationUseCase operationUseCase, TokenService tokenService,
                         CounterUseCase counterUseCase,
                         org.ylab.usecases.CounterTypeUseCase counterTypeUseCase) {
        this.passwordUseCase = passwordUseCase;
        this.personRepo = personRepo;
        this.operationUseCase = operationUseCase;
        this.tokenService = tokenService;
        this.counterUseCase = counterUseCase;
        this.counterTypeUseCase = counterTypeUseCase;
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
            long id = personRepo.findIdByEmail(person.getEmail()).get();

            //todo заменить этот код на триггер в бд

            // Добавляем пользователю три счетчика: для холодной и горячей воды и отопления
//            counterUseCase.saveAll(List.of(
//                    new Counter(id, counterTypeUseCase.findOne("HOT").get()),
//                    new Counter(id, counterTypeUseCase.findOne("COLD").get()),
//                    new Counter(id, counterTypeUseCase.findOne("HEAT").get())));

            operationUseCase.save(new Operation(person.getId(), Action.REGISTRATION, LocalDateTime.now()));
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
            operationUseCase.save(new Operation(found.get().getId(), Action.AUTHENTICATION, LocalDateTime.now()));
            return tokenService.getToken(found.get().getId());
        }
    }

    /**
     * Метод для выхода пользователя из системы (удаление токена).
     *
     * @param token Токен пользователя, который требуется удалить.
     * @return {@code true}, если токен успешно удален, иначе {@code false}.
     */
    public boolean logout(String token) {
        operationUseCase.save(new Operation(tokenService.getPersonId(token), Action.LOGOUT, LocalDateTime.now()));
        return tokenService.deleteToken(token) != null;
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
