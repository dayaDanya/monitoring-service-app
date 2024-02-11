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
import org.ylab.domain.dto.CounterDto;
import org.ylab.domain.dto.MeasurementInDto;
import org.ylab.domain.dto.MeasurementOutDto;
import org.ylab.domain.dto.Response;
import org.ylab.domain.models.Measurement;
import org.ylab.domain.usecases.CounterUseCase;
import org.ylab.domain.usecases.MeasurementUseCase;
import org.ylab.exceptions.BadMeasurementAmountException;
import org.ylab.exceptions.MeasurementNotFoundException;
import org.ylab.exceptions.TokenNotActualException;
import org.ylab.exceptions.WrongDateException;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.infrastructure.mappers.CounterMapper;
import org.ylab.infrastructure.mappers.MeasurementInMapper;
import org.ylab.infrastructure.mappers.MeasurementOutMapper;
import org.ylab.infrastructure.utils.RequestDeserializer;
import org.ylab.infrastructure.utils.ValidationUtil;
import org.ylab.repositories.implementations.CounterRepo;
import org.ylab.repositories.implementations.CounterTypeRepo;
import org.ylab.repositories.implementations.MeasurementRepo;
import org.ylab.security.services.JwtService;
import org.ylab.services.CounterService;
import org.ylab.services.CounterTypeService;
import org.ylab.services.MeasurementService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервлет для взаимодействия с записями и счетчиками пользователя
 */
@WebServlet("/measurements")
public class MeasurementServlet extends HttpServlet {
    private final MeasurementUseCase measurementUseCase;
    private final CounterUseCase counterUseCase;
    private final MeasurementInMapper measurementInMapper;
    private final MeasurementOutMapper measurementOutMapper;

    private final CounterMapper counterMapper;

    private final ObjectMapper objectMapper;

    private final RequestDeserializer deserializer;


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
        deserializer= new RequestDeserializer();
    }

    /**
     * Обработка post-запроса: добавление измерения
     * @param req запрос пользователя
     * @param resp ответ сервера
     * @throws IOException ошибка ввода вывода
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
        String json = deserializer.deserialize(req.getReader());
        MeasurementInDto dto = objectMapper.readValue(json,
                MeasurementInDto.class);
        try {
            ValidationUtil.checkIsNumericValuePositive(dto.getAmount());
            Measurement measurement = measurementInMapper.dtoToObj(dto);
            measurement.setSubmissionDate(LocalDateTime.now());
            measurementUseCase.save(measurement, principal);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (NumberFormatException | WrongDateException | TokenNotActualException |
                 BadMeasurementAmountException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Response response = new Response(e.getMessage());
            resp.getOutputStream().write(objectMapper.writeValueAsBytes(response));
        }

    }

    /**
     * Обработка get-запросов
     * /action=all - просмотр всех добавленных измерений
     * /action=last?type=(HOT/COLD/HEAT) - просмотр последнего измерения для выбранного счетчика
     * /action=month?number=(1..12)&type=(HOT/COLD/HEAT) - просмотр измерения для выбранного счетчика
     * за конкретный месяц
     * /action=counters - просмотр закрепленных за пользователем счетчиков
     * @param req запрос пользователя
     * @param resp ответ сервера
     * @throws IOException ошибка ввода вывода
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println(req.getPathInfo());
        resp.setContentType("application/json");
        long principal = 0L;
        try {
            principal = JwtService.validateTokenAndGetSubject(req);
        } catch (TokenNotActualException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            Response response = new Response(e.getMessage());
            resp.getOutputStream().write(objectMapper.writeValueAsString(response).getBytes());
        }
        String action = req.getParameter("action");
        switch (action == null ? "notFound" : action) {
            case "all":
                Map<Long, MeasurementOutDto> map = measurementUseCase
                        .findAllById(principal)
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey,
                                entry -> measurementOutMapper.objToDto(entry.getValue())));
                if (map.isEmpty())
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                else {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getOutputStream().write(
                            objectMapper.writeValueAsString(map).getBytes());
                }
                break;
            case "last":
                String type = req.getParameter("type");
                try {
                    Measurement last = measurementUseCase
                            .findLast(type, principal);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getOutputStream().write(
                            objectMapper.writeValueAsString(measurementOutMapper
                                    .objToDto(last)).getBytes());
                } catch (MeasurementNotFoundException e) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    Response response = new Response(e.getMessage());
                    resp.getOutputStream().write(objectMapper.writeValueAsString(response).getBytes());
                }
                break;
            case "month":
                int month = Integer.parseInt(req.getParameter("number"));
                String type1 = req.getParameter("type");
                try {
                    ValidationUtil.checkIsNumericValuePositive(month);
                    Measurement byMonth = measurementUseCase.findByMonth(principal,
                            month, type1);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getOutputStream().write(
                            objectMapper.writeValueAsString(measurementOutMapper
                                    .objToDto(byMonth)).getBytes());
                } catch (NumberFormatException | MeasurementNotFoundException e) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    Response response = new Response(e.getMessage());
                    resp.getOutputStream().write(objectMapper.writeValueAsString(response).getBytes());
                }

                break;
            case "counters":
                Map<Long, CounterDto> counters =
                        counterUseCase.findByPersonId(principal)
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(Map.Entry::getKey,
                                        entry -> counterMapper.objToDto(entry.getValue())));
                if (counters.isEmpty())
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                else {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getOutputStream().write(
                            objectMapper.writeValueAsString(counters).getBytes());
                }
                break;
            case "notFound":
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }


}
