package org.ylab.infrastructure.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private final PersonUseCase personUseCase;
    private final PersonInputMapper transactionMapper;
    private final ObjectMapper objectMapper;


    public RegistrationServlet() {
        ConnectionAdapter connectionAdapter = new ConnectionAdapter();
        this.personUseCase = new PersonService(new PasswordService(),
                new PersonRepo(connectionAdapter),
                new OperationService(new OperationRepo(connectionAdapter)));
        this.transactionMapper = Mappers.getMapper(PersonInputMapper.class);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PersonInDto personDto = new PersonInDto(req.getHeader("email"),
                req.getHeader("password"));
        try {
            personUseCase.register(new Person(personDto.getEmail(), personDto.getPassword()));
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
        } catch (BadCredentialsException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }
}
