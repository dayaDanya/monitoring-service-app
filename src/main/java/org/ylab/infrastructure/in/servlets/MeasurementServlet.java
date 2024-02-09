package org.ylab.infrastructure.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mapstruct.factory.Mappers;
import org.ylab.domain.dto.MeasurementInDto;
import org.ylab.domain.dto.MeasurementOutDto;
import org.ylab.domain.models.Measurement;
import org.ylab.domain.usecases.MeasurementUseCase;
import org.ylab.exceptions.BadMeasurementAmountException;
import org.ylab.exceptions.TokenNotActualException;
import org.ylab.exceptions.WrongDateException;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.infrastructure.mappers.MeasurementInMapper;
import org.ylab.infrastructure.mappers.MeasurementOutMapper;
import org.ylab.repositories.implementations.CounterRepo;
import org.ylab.repositories.implementations.CounterTypeRepo;
import org.ylab.repositories.implementations.MeasurementRepo;
import org.ylab.repositories.implementations.OperationRepo;
import org.ylab.security.services.JwtService;
import org.ylab.services.CounterService;
import org.ylab.services.CounterTypeService;
import org.ylab.services.MeasurementService;
import org.ylab.services.OperationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/measurements")
public class MeasurementServlet extends HttpServlet {
    private final MeasurementUseCase measurementUseCase;
    private final MeasurementInMapper measurementInMapper;

    private final MeasurementOutMapper measurementOutMapper;

    private final ObjectMapper objectMapper;

    public MeasurementServlet() {
        ConnectionAdapter connectionAdapter = new ConnectionAdapter();
        this.measurementUseCase = new MeasurementService(new MeasurementRepo(connectionAdapter),
                new CounterService(new CounterRepo(connectionAdapter),
                        new CounterTypeService(new CounterTypeRepo(connectionAdapter))));
        this.objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // Hack time module to allow 'Z' at the end of string (i.e. javascript json's)
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        this.objectMapper.registerModule(javaTimeModule);
        this.measurementInMapper = Mappers.getMapper(MeasurementInMapper.class);
        this.measurementOutMapper = Mappers.getMapper(MeasurementOutMapper.class);
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Long> principal = JwtService.validateTokenAndGetSubject(req);
        if (principal.isEmpty()) {
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            BufferedReader reader = req.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            MeasurementInDto dto = objectMapper.readValue(jsonBuilder.toString(),
                    MeasurementInDto.class);
            Measurement measurement = measurementInMapper.dtoToObj(dto);
            measurement.setSubmissionDate(LocalDateTime.now());
            try {
                measurementUseCase.save(measurement, principal.get());
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } catch (WrongDateException | TokenNotActualException | BadMeasurementAmountException e) {
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getOutputStream().write(e.getMessage().getBytes());
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Optional<Long> principal = JwtService.validateTokenAndGetSubject(req);
        if (principal.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            String action = req.getParameter("action");
            switch (action == null ? "badRequest" : action) {
                case "all":
                    Map<Long, Measurement> map = measurementUseCase.findAllById(principal.get());

                    //todo try catch, зарефакторить на if-else и выделить переменные
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getOutputStream().write(
                            objectMapper.writeValueAsString(map).getBytes());
                    break;
                case "last":
                    String type = req.getParameter("type");
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getOutputStream().write(
                            objectMapper.writeValueAsString(measurementOutMapper
                                    .objToDto(measurementUseCase
                                            .findLast(type, principal.get()))).getBytes());
                    break;
                case "month":
                    int month = Integer.parseInt(req.getParameter("number"));
                    String type1 = req.getParameter("type");
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getOutputStream().write(
                            objectMapper.writeValueAsString(measurementOutMapper.objToDto(
                                    measurementUseCase.findByMonth(principal.get(),
                                    month, type1))).getBytes());
                    break;
                case "badRequest":
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    break;
            }
        }
    }
}
