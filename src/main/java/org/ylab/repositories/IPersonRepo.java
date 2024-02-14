package org.ylab.repositories;

import org.ylab.domain.enums.Role;
import org.ylab.domain.models.Person;

import java.util.Optional;

/**
 * Репозиторий для сущности Person.
 */
public interface IPersonRepo {


    /**
     * Поиск пользователя по электронной почте.
     *
     * @param email Электронная почта пользователя
     * @return Пользователь (Optional)
     */
    Optional<Person> findByEmail(String email);

    /**
     * Поиск идентификатора пользователя по электронной почте.
     *
     * @param email Электронная почта пользователя
     * @return Идентификатор пользователя (Optional)
     */
   Optional<Long> findIdByEmail(String email);
    /**
     * Сохранение пользователя.
     *
     * @param person Пользователь для сохранения
     */
    void save(Person person);

    /**
     * Поиск пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя
     * @return Пользователь (Optional)
     */
    Optional<Person> findById(long id);
    /**
     * Поиск id пользователя по роли.
     *
     * @param role роль пользователя
     * @return id Пользователя (Optional)
     */
    Optional<Long> findIdByRole(Role role);


}
