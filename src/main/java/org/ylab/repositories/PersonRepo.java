package org.ylab.repositories;

import org.ylab.domain.models.Person;
import org.ylab.domain.models.enums.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для сущности Person.
 */
public class PersonRepo {

    /**
     * Счетчик людей
     */
    private static long personCounter;

    /**
     * Список людей
     */
    private static List<Person> people;

    /**
     * Конструктор, инициализирующий список (вызывается один раз).
     * Создается администратор по умолчанию.
     */
    public PersonRepo() {
        if (people == null) {
            personCounter = 0;
            people = new ArrayList<>();

            save(new Person("admin",
                    "agSlkyvn99dVB6pKeD6MVA==:EvJ6J8w/LwveTJ+WA5AgTu9OzI7+FNakNFuZO8JqDCM2OUWL/82UXOAwVjPEpkxH5Apw65Pdp8iogVfDwIXbDQ==", Role.ADMIN));
        }
    }

    /**
     * Конструктор, принимающий пустой список для тестирования.
     * @param empty Пустой список
     */
    public PersonRepo(List<Person> empty) {
        people = empty;
        personCounter = 0;
    }

    /**
     * Поиск пользователя по электронной почте.
     * @param email Электронная почта пользователя
     * @return Пользователь (Optional)
     */
    public Optional<Person> findByEmail(String email) {
        return people
                .stream()
                .filter(p -> p.getEmail().equals(email))
                .findFirst();
    }

    /**
     * Поиск идентификатора пользователя по электронной почте.
     * @param email Электронная почта пользователя
     * @return Идентификатор пользователя (Optional)
     */
    public Optional<Long> findIdByEmail(String email) {
        return people
                .stream()
                .filter(p -> p.getEmail().equals(email))
                .findFirst()
                .map(Person::getId);
    }

    /**
     * Сохранение пользователя.
     * @param person Пользователь для сохранения
     */
    public void save(Person person) {
        person.setId(++personCounter);
        people.add(person);
    }

    /**
     * Поиск пользователя по идентификатору.
     * @param id Идентификатор пользователя
     * @return Пользователь (Optional)
     */
    public Optional<Person> findById(long id) {
        return people.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }
}
