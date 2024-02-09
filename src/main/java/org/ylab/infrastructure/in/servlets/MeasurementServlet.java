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
import org.ylab.domain.dto.CounterDto;
import org.ylab.domain.dto.MeasurementInDto;
import org.ylab.domain.dto.MeasurementOutDto;
import org.ylab.domain.models.Measurement;
import org.ylab.domain.usecases.CounterUseCase;
import org.ylab.domain.usecases.MeasurementUseCase;
import org.ylab.exceptions.BadMeasurementAmountException;
import org.ylab.exceptions.TokenNotActualException;
import org.ylab.exceptions.WrongDateException;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.infrastructure.mappers.CounterMapper;
import org.ylab.infrastructure.mappers.MeasurementInMapper;
import org.ylab.infrastructure.mappers.MeasurementOutMapper;
import org.ylab.repositories.implementations.CounterRepo;
import org.ylab.repositories.implementations.CounterTypeRepo;
import org.ylab.repositories.implementations.MeasurementRepo;
import org.ylab.security.services.JwtService;
import org.ylab.services.CounterService;
import org.ylab.services.CounterTypeService;
import org.ylab.services.MeasurementService;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/measurements")
public class MeasurementServlet extends HttpServlet {
    private final MeasurementUseCase measurementUseCase;
    private final CounterUseCase counterUseCase;
    private final MeasurementInMapper measurementInMapper;
    private final MeasurementOutMapper measurementOutMapper;

    private final CounterMapper counterMapper;

    private final ObjectMapper objectMapper;


    public MeasurementServlet() {
        ConnectionAdapter connectionAdapter = new ConnectionAdapter();
        CounterRepo counterRepo = new CounterRepo(connectionAdapter);
        CounterTypeService counterTypeUseCase = new CounterTypeService(new CounterTypeRepo(connectionAdapter));
        this.measurementUseCase = new MeasurementService(new MeasurementRepo(connectionAdapter),
                new CounterService(counterRepo,
                        counterTypeUseCase));
        this.objectMapper =
                new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        this.objectMapper.registerModule(javaTimeModule);
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.measurementInMapper = Mappers.getMapper(MeasurementInMapper.class);
        this.measurementOutMapper = Mappers.getMapper(MeasurementOutMapper.class);
        counterUseCase = new CounterService(counterRepo, counterTypeUseCase);
        counterMapper = Mappers.getMapper(CounterMapper.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
                    Map<Long, MeasurementOutDto> map = measurementUseCase
                            .findAllById(principal.get())
                            .entrySet()
                            .stream()
                            .collect(Collectors.toMap(Map.Entry::getKey,
                                    entry -> measurementOutMapper.objToDto(entry.getValue())));

                    //todo try catch, зарефакторить на if-else и выделить переменные
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getOutputStream().write(
                            objectMapper.writeValueAsString(map).getBytes());
                    break;
                case "last":
                    String type = req.getParameter("type");
                    Measurement last = measurementUseCase
                            .findLast(type, principal.get());
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getOutputStream().write(
                            objectMapper.writeValueAsString(measurementOutMapper
                                    .objToDto(last)).getBytes());
                    break;
                case "month":
                    int month = Integer.parseInt(req.getParameter("number"));
                    String type1 = req.getParameter("type");
                    Measurement byMonth = measurementUseCase.findByMonth(principal.get(),
                            month, type1);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getOutputStream().write(
                            objectMapper.writeValueAsString(measurementOutMapper
                                    .objToDto(byMonth)).getBytes());
                    break;
                case "counters":
                    resp.setStatus(HttpServletResponse.SC_OK);
                    Map<Long, CounterDto> counters =
                            counterUseCase.findByPersonId(principal.get())
                                    .entrySet()
                                    .stream()
                                    .collect(Collectors.toMap(Map.Entry::getKey,
                                            entry -> counterMapper.objToDto(entry.getValue())));
                    resp.getOutputStream().write(
                            objectMapper.writeValueAsString(counters).getBytes());
                    break;
                case "badRequest":
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    break;
            }
        }
    }
}
