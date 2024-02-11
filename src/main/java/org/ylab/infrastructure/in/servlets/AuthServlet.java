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
import org.ylab.exceptions.PersonNotFoundException;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.infrastructure.mappers.PersonInputMapper;
import org.ylab.infrastructure.utils.RequestDeserializer;
import org.ylab.repositories.implementations.PersonRepo;
import org.ylab.services.PasswordService;
import org.ylab.services.PersonService;

import java.io.IOException;

@WebServlet("/authentication")
public class AuthServlet extends HttpServlet {
    private final PersonUseCase personUseCase;
    private final PersonInputMapper personInputMapper;
    private final ObjectMapper objectMapper;


    public AuthServlet() {
        ConnectionAdapter connectionAdapter = new ConnectionAdapter();
        this.personUseCase = new PersonService(new PasswordService(),
                new PersonRepo(connectionAdapter));
        this.personInputMapper = Mappers.getMapper(PersonInputMapper.class);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String json = RequestDeserializer.deserialize(req.getReader());
        PersonInDto personDto = objectMapper.readValue(json,
                PersonInDto.class);
        Person person = personInputMapper.dtoToObj(personDto);
        try {
            String token = personUseCase.authenticate(person);
            resp.getOutputStream().write(token.getBytes());
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (BadCredentialsException | PersonNotFoundException e) {
            Response response = new Response(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            //todo проверить разницу asString и asBytes
            resp.getOutputStream().write(objectMapper.writeValueAsString(response).getBytes());
        }
    }
}
