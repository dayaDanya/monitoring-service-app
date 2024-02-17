package org.ylab.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ylab.domain.dto.CounterDto;
import org.ylab.domain.dto.Response;
import org.ylab.domain.usecases.AdminUseCase;
import org.ylab.exceptions.CounterTypeNotFoundException;
import org.ylab.exceptions.TokenNotActualException;
import org.ylab.infrastructure.mappers.CounterMapper;
import org.ylab.infrastructure.mappers.CounterTypeMapper;
import org.ylab.infrastructure.mappers.MeasurementOutMapper;
import org.ylab.infrastructure.mappers.OperationOutMapper;
import org.ylab.infrastructure.utils.ValidationUtil;
import org.ylab.security.services.JwtService;

@RestController
@RequestMapping(value = "/admin-panel", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    private final AdminUseCase adminUseCase;

    private final MeasurementOutMapper measurementOutMapper;

    private final OperationOutMapper operationOutMapper;

    private final CounterTypeMapper counterTypeMapper;

    private final CounterMapper counterMapper;
    @Autowired
    public AdminController(AdminUseCase adminUseCase,
                           MeasurementOutMapper measurementOutMapper,
                           OperationOutMapper operationOutMapper,
                           CounterTypeMapper counterTypeMapper,
                           CounterMapper counterMapper) {
        this.adminUseCase = adminUseCase;
        this.measurementOutMapper = measurementOutMapper;
        this.operationOutMapper = operationOutMapper;
        this.counterTypeMapper = counterTypeMapper;
        this.counterMapper = counterMapper;
    }
    @PostMapping("/counters")
    public ResponseEntity<?> giveCounter(@RequestHeader(name = "Authorization") String token,
                                         @RequestBody CounterDto dto){
        long principal = 0L;
        try {
            principal = JwtService.validateTokenAndGetSubject(token);
        } catch (TokenNotActualException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        try {
            ValidationUtil.checkIsNotEmpty(dto.getCounterType().getName());
        } catch (RuntimeException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            adminUseCase.saveCounter(counterMapper.dtoToObj(dto));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CounterTypeNotFoundException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
