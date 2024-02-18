package org.ylab.controllers;

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
@RequestMapping(value = "/measurements", produces = MediaType.APPLICATION_JSON_VALUE)
public class MeasurementController {
    private final MeasurementUseCase measurementUseCase;
    private final CounterUseCase counterUseCase;
    private final MeasurementInMapper measurementInMapper;
    private final MeasurementOutMapper measurementOutMapper;
    private final CounterMapper counterMapper;

    @Autowired
    public MeasurementController(MeasurementUseCase measurementUseCase,
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

    @PostMapping()
    public ResponseEntity<?> add(@RequestHeader(name = "Authorization") String token,
                                 @RequestBody MeasurementInDto dto) {
        long principal = 0L;
        try {
            principal = JwtService.validateTokenAndGetSubject(token);
        } catch (TokenNotActualException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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

    @GetMapping()
    public ResponseEntity<?> getAllMeasurements(@RequestHeader(name = "Authorization") String token) {
        long principal = 0L;
        try {
            principal = JwtService.validateTokenAndGetSubject(token);
        } catch (TokenNotActualException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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

    @GetMapping(value = "/counters")
    public ResponseEntity<?> getCounters(@RequestHeader(name = "Authorization") String token) {
        long principal = 0L;
        try {
            principal = JwtService.validateTokenAndGetSubject(token);
        } catch (TokenNotActualException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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

    @GetMapping(value = "/{type}")
    public ResponseEntity<?> getLast(@RequestHeader(name = "Authorization") String token,
            @PathVariable(value = "type") String type) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long principal = 0L;
        try {
            principal = JwtService.validateTokenAndGetSubject(token);
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
    @GetMapping("/{type}/{month}")
    public ResponseEntity<?> getByMonth(@RequestHeader(name = "Authorization") String token,
                                        @PathVariable(value = "type") String type,
                                        @PathVariable(value = "month") int month) {
        long principal = 0L;
        try {
            principal = JwtService.validateTokenAndGetSubject(token);
        } catch (TokenNotActualException e) {
            Response response = new Response(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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
