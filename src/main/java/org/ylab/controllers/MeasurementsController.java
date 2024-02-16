package org.ylab.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
import org.ylab.infrastructure.mappers.CounterMapper;
import org.ylab.infrastructure.mappers.MeasurementInMapper;
import org.ylab.infrastructure.mappers.MeasurementOutMapper;
import org.ylab.infrastructure.utils.ValidationUtil;
import org.ylab.security.services.JwtService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {
    private final MeasurementUseCase measurementUseCase;
    private final CounterUseCase counterUseCase;
    private final MeasurementInMapper measurementInMapper;
    private final MeasurementOutMapper measurementOutMapper;
    private final CounterMapper counterMapper;

    @Autowired
    public MeasurementsController(MeasurementUseCase measurementUseCase,
                                  CounterUseCase counterUseCase,
                                  MeasurementInMapper measurementInMapper,
                                  MeasurementOutMapper measurementOutMapper,
                                  CounterMapper counterMapper) {
        this.measurementUseCase = measurementUseCase;
        this.counterUseCase = counterUseCase;
        this.measurementInMapper = measurementInMapper;
        this.measurementOutMapper = measurementOutMapper;
        this.counterMapper = counterMapper;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@RequestBody MeasurementInDto dto) {
        long principal = 0L;
        try {
            principal = JwtService.validateTokenAndGetSubject(req);
        } catch (TokenNotActualException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        //todo разобраться с jwt
        try {
            ValidationUtil.checkIsNumericValuePositive(dto.getAmount());
            Measurement measurement = measurementInMapper.dtoToObj(dto);
            measurement.setSubmissionDate(LocalDateTime.now());
            measurementUseCase.save(measurement, principal);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (NumberFormatException | WrongDateException | TokenNotActualException |
                 BadMeasurementAmountException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllMeasurements() {
        long principal = 0L;
        try {
            principal = JwtService.validateTokenAndGetSubject(req);
        } catch (TokenNotActualException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        //todo мапить с помощью mapstruct, а не stream
        Map<Long, MeasurementOutDto> map = measurementUseCase
                .findAllById(principal)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> measurementOutMapper.objToDto(entry.getValue())));
        if (map.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else {
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/counters", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCounters() {
        long principal = 0L;
        try {
            principal = JwtService.validateTokenAndGetSubject(req);
        } catch (TokenNotActualException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        Map<Long, CounterDto> counters =
                counterUseCase.findByPersonId(principal)
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey,
                                entry -> counterMapper.objToDto(entry.getValue())));
        if (counters.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else {
            return new ResponseEntity<>(counters, HttpStatus.OK);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLast(@RequestParam(value = "type") String type) {
        long principal = 0L;
        try {
            principal = JwtService.validateTokenAndGetSubject(req);
        } catch (TokenNotActualException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        try {
            Measurement last = measurementUseCase.findLast(type, principal);
            return new ResponseEntity<>(last, HttpStatus.OK);
        } catch (MeasurementNotFoundException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByMonth(@RequestParam(value = "type") String type,
                                        @RequestParam(value = "month") int month) {
        long principal = 0L;
        try {
            principal = JwtService.validateTokenAndGetSubject(req);
        } catch (TokenNotActualException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        try {
            ValidationUtil.checkIsNumericValuePositive(month);
            Measurement byMonth = measurementUseCase.findByMonth(principal,
                    month, type);
            return new ResponseEntity<>(byMonth, HttpStatus.OK);
        } catch (NumberFormatException | MeasurementNotFoundException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
    }
}
