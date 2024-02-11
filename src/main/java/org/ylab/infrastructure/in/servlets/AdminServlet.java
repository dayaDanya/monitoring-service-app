package org.ylab.infrastructure.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mapstruct.factory.Mappers;
import org.ylab.domain.dto.*;
import org.ylab.domain.usecases.CounterTypeUseCase;
import org.ylab.domain.usecases.CounterUseCase;
import org.ylab.domain.usecases.MeasurementUseCase;
import org.ylab.domain.usecases.OperationUseCase;
import org.ylab.exceptions.CounterTypeAlreadyExistsException;
import org.ylab.exceptions.CounterTypeNotFoundException;
import org.ylab.exceptions.TokenNotActualException;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.infrastructure.mappers.CounterMapper;
import org.ylab.infrastructure.mappers.CounterTypeMapper;
import org.ylab.infrastructure.mappers.MeasurementOutMapper;
import org.ylab.infrastructure.mappers.OperationOutMapper;
import org.ylab.infrastructure.utils.RequestDeserializer;
import org.ylab.repositories.implementations.*;
import org.ylab.security.services.JwtService;
import org.ylab.services.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервлет для админской панели
 */
@WebServlet("/admin-panel/*")
public class AdminServlet extends HttpServlet {
    private final AdminService adminService;

    private final MeasurementOutMapper measurementOutMapper;

    private final OperationOutMapper operationOutMapper;

    private final CounterTypeMapper counterTypeMapper;

    private final CounterMapper counterMapper;

    private final ObjectMapper objectMapper;

    private final RequestDeserializer deserializer;

    /**
     *  конструктор
     */
    public AdminServlet() {
        ConnectionAdapter connectionAdapter = new ConnectionAdapter();
        CounterTypeUseCase counterTypeUseCase = new CounterTypeService(new CounterTypeRepo(connectionAdapter));
        CounterUseCase counterUseCase = new CounterService(new CounterRepo(connectionAdapter),
                new CounterTypeService(new CounterTypeRepo(connectionAdapter)));
        OperationUseCase operationUseCase = new OperationService(new OperationRepo(connectionAdapter));
        MeasurementUseCase measurementUseCase = new MeasurementService(new MeasurementRepo(connectionAdapter),
                new CounterService(new CounterRepo(connectionAdapter), new CounterTypeService(new CounterTypeRepo(connectionAdapter))));
        adminService = new AdminService(counterUseCase, counterTypeUseCase, operationUseCase, measurementUseCase, new PersonRepo(connectionAdapter));

        operationOutMapper = Mappers.getMapper(OperationOutMapper.class);
        measurementOutMapper = Mappers.getMapper(MeasurementOutMapper.class);
        counterTypeMapper = Mappers.getMapper(CounterTypeMapper.class);
        counterMapper = Mappers.getMapper(CounterMapper.class);


        this.objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        this.objectMapper.registerModule(javaTimeModule);
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.deserializer = new RequestDeserializer();
    }

    /**
     * Реализация обработки post-запроса
     * /counters - выдача нового счетчика
     * /counter-types - создание нового типа счетчика
     * @param req
     * @param resp
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        long principal = 0L;
        try {
            principal = JwtService.validateTokenAndGetSubject(req);

        } catch (TokenNotActualException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            Response response = new Response(e.getMessage());
            resp.getOutputStream().write(objectMapper.writeValueAsString(response).getBytes());
        }
        if (adminService.isAdmin(principal)) {
            String pathInfo = req.getPathInfo();
            if (pathInfo.equals("/counters")) {
                String json = deserializer.deserialize(req.getReader());
                CounterDto dto = objectMapper.readValue(json, CounterDto.class);
                try {
                    adminService.saveCounter(counterMapper.dtoToObj(dto));
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                } catch (CounterTypeNotFoundException e) {
                    Response response = new Response(e.getMessage());
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getOutputStream().write(objectMapper.writeValueAsString(response).getBytes());
                }
            } else if (pathInfo.equals("/counter-types")) {
                String json = deserializer.deserialize(req.getReader());
                CounterTypeDto dto = objectMapper.readValue(json, CounterTypeDto.class);
                try {
                    adminService.saveCounterType(counterTypeMapper.dtoToObj(dto));
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                } catch (CounterTypeAlreadyExistsException e) {
                    Response response = new Response(e.getMessage());
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getOutputStream().write(objectMapper.writeValueAsString(response).getBytes());
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);

    }

    /**
     * реализация обработки GET-запросов
     * /measurements возвращает все измерения
     * /operations просмотр аудита
     * @param req
     * @param resp
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        long principal = 0L;
        try {
            principal = JwtService.validateTokenAndGetSubject(req);
        } catch (TokenNotActualException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            Response response = new Response(e.getMessage());
            resp.getOutputStream().write(objectMapper.writeValueAsString(response).getBytes());
        }
        if (adminService.isAdmin(principal)) {
            String pathInfo = req.getPathInfo();
            if (pathInfo.equals("/measurements")) {
                Map<Long, MeasurementOutDto> measurements;
                measurements = adminService
                        .findAllMeasurements()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> measurementOutMapper.objToDto(entry.getValue())));
                if (measurements.isEmpty())
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                else {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getOutputStream().write(
                            objectMapper.writeValueAsString(measurements).getBytes());
                }
            } else if (pathInfo.equals("/operations")) {
                Map<Long, OperationOutDto> operations = adminService
                        .findAllOperations()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> operationOutMapper.objToDto(entry.getValue())));
                if (operations.isEmpty())
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                else {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getOutputStream().write(
                            objectMapper.writeValueAsString(operations).getBytes());
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }


}
