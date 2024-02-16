package org.ylab.infrastructure.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mapstruct.factory.Mappers;
import org.ylab.domain.dto.PersonInDto;
import org.ylab.domain.dto.Response;
import org.ylab.domain.models.Person;
import org.ylab.domain.usecases.PersonUseCase;
import org.ylab.exceptions.BadCredentialsException;
import org.ylab.exceptions.EmailFormatException;
import org.ylab.exceptions.PasswordLengthException;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.infrastructure.mappers.PersonInputMapper;
import org.ylab.infrastructure.utils.RequestDeserializer;
import org.ylab.infrastructure.utils.ValidationUtil;
import org.ylab.repositories.implementations.PersonRepo;
import org.ylab.services.PasswordService;
import org.ylab.services.PersonService;

import java.io.IOException;

/**
 * Сервлет регистрации
 */
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private final PersonUseCase personUseCase;
    private final PersonInputMapper personInputMapper;
    private final ObjectMapper objectMapper;

    private final RequestDeserializer deserializer;

    /**
     * Конструктор
     */
    public RegistrationServlet() {
        ConnectionAdapter connectionAdapter = new ConnectionAdapter();
        this.personUseCase = new PersonService(new PasswordService(),
                new PersonRepo(connectionAdapter));
        this.personInputMapper = Mappers.getMapper(PersonInputMapper.class);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        deserializer = new RequestDeserializer();
    }

    /**
     * Обработка post-запроса регистрации пользователя
     *
     * @param req  запрос пользователя
     * @param resp ответ сервера
     * @throws IOException ошибка ввода вывода
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = deserializer.deserialize(req.getReader());
        PersonInDto personDto = objectMapper.readValue(json,
                PersonInDto.class);
        try {
            ValidationUtil.checkIsEmail(personDto.getEmail());
            ValidationUtil.checkIsPasswordLongEnough(personDto.getPassword());
            Person person = personInputMapper.dtoToObj(personDto);
            personUseCase.register(person);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
        } catch (EmailFormatException | PasswordLengthException | BadCredentialsException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            Response response = new Response(e.getMessage());
            resp.getOutputStream().write(objectMapper.writeValueAsBytes(response));
        }

    }
}
