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
import org.ylab.domain.dto.MeasurementOutDto;
import org.ylab.domain.dto.OperationOutDto;
import org.ylab.domain.models.Counter;
import org.ylab.domain.models.CounterType;
import org.ylab.domain.usecases.CounterTypeUseCase;
import org.ylab.domain.usecases.CounterUseCase;
import org.ylab.domain.usecases.MeasurementUseCase;
import org.ylab.domain.usecases.OperationUseCase;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.infrastructure.mappers.MeasurementOutMapper;
import org.ylab.infrastructure.mappers.OperationOutMapper;
import org.ylab.repositories.implementations.CounterRepo;
import org.ylab.repositories.implementations.CounterTypeRepo;
import org.ylab.repositories.implementations.MeasurementRepo;
import org.ylab.repositories.implementations.OperationRepo;
import org.ylab.services.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private final AdminService adminService;

    private final MeasurementOutMapper measurementOutMapper;

    private final OperationOutMapper operationOutMapper;

    private final ObjectMapper objectMapper;

    public AdminServlet() {
        ConnectionAdapter connectionAdapter = new ConnectionAdapter();
        CounterTypeUseCase counterTypeUseCase = new CounterTypeService(new CounterTypeRepo(connectionAdapter));
        CounterUseCase counterUseCase = new CounterService(new CounterRepo(connectionAdapter),
                new CounterTypeService(new CounterTypeRepo(connectionAdapter)));
        OperationUseCase operationUseCase = new OperationService(new OperationRepo(connectionAdapter));
        MeasurementUseCase measurementUseCase = new MeasurementService(new MeasurementRepo(connectionAdapter),
                new CounterService(new CounterRepo(connectionAdapter), new CounterTypeService(new CounterTypeRepo(connectionAdapter))));
        adminService = new AdminService(counterUseCase, counterTypeUseCase, operationUseCase, measurementUseCase);
        operationOutMapper = Mappers.getMapper(OperationOutMapper.class);
        measurementOutMapper = Mappers.getMapper(MeasurementOutMapper.class);
        this.objectMapper =
                new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        this.objectMapper.registerModule(javaTimeModule);
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/counters")) {
            String counterType = req.getParameter("counterType");
            long receiverId = Long.parseLong(req.getParameter("receiverId"));
            adminService.saveCounter(new Counter(receiverId, new CounterType(counterType)));
        } else if (pathInfo.equals("/counter-types")) {
            String counterType = req.getParameter("counterType");
            adminService.saveCounterType(new CounterType(counterType));
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //todo зарефакторить на try/else и добавить проверку токена
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/measurements")) {
            Map<Long, MeasurementOutDto> measurements = adminService
                    .findAllMeasurements()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> measurementOutMapper.objToDto(entry.getValue())));
            resp.getOutputStream().write(objectMapper.writeValueAsString(measurements).getBytes());
        } else if (pathInfo.equals("/audit")) {
            Map<Long, OperationOutDto> operations = adminService
                    .findAllOperations()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> operationOutMapper.objToDto(entry.getValue())));
            resp.getOutputStream().write(objectMapper.writeValueAsString(operations).getBytes());
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
