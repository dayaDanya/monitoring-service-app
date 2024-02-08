package org.ylab.infrastructure.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mapstruct.factory.Mappers;
import org.ylab.domain.dto.PersonInDto;
import org.ylab.domain.models.Person;
import org.ylab.domain.usecases.PersonUseCase;
import org.ylab.exceptions.BadCredentialsException;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.infrastructure.mappers.PersonInputMapper;
import org.ylab.repositories.implementations.OperationRepo;
import org.ylab.repositories.implementations.PersonRepo;
import org.ylab.services.OperationService;
import org.ylab.services.PasswordService;
import org.ylab.services.PersonService;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private final PersonUseCase personUseCase;
    private final PersonInputMapper personInputMapper;
    private final ObjectMapper objectMapper;


    public RegistrationServlet() {
        ConnectionAdapter connectionAdapter = new ConnectionAdapter();
        this.personUseCase = new PersonService(new PasswordService(),
                new PersonRepo(connectionAdapter),
                new OperationService(new OperationRepo(connectionAdapter)));
        this.personInputMapper = Mappers.getMapper(PersonInputMapper.class);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        PersonInDto personDto = objectMapper.readValue(jsonBuilder.toString(),
                PersonInDto.class);
        try {
            Person person = personInputMapper.dtoToObj(personDto);
            personUseCase.register(person);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
        } catch (BadCredentialsException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }
}
