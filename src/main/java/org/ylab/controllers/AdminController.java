package org.ylab.controllers;

/**
 * Класс, представляющий контроллер для административных операций.
 */
public class AdminController {
//    private final MeasurementService measurementUseCase;
//    private final OperationService operationUseCase;
//    private final PersonUseCase personUseCase;
//    private final TokenService tokenService;
//    private final CounterTypeService counterTypeUseCase;
//    private final CounterService counterUseCase;
//
//    /**
//     * Конструктор класса AdminController, инициализирующий используемые компоненты.
//     */
//    public AdminController(MeasurementService measurementUseCase,
//                           OperationService operationUseCase,
//                           PersonService personUseCase, TokenService tokenService,
//                           CounterTypeService counterTypeUseCase,
//                           CounterService counterUseCase) {
//        this.measurementUseCase = measurementUseCase;
//        this.operationUseCase = operationUseCase;
//        this.personUseCase = personUseCase;
//        this.tokenService = tokenService;
//        this.counterTypeUseCase = counterTypeUseCase;
//        this.counterUseCase = counterUseCase;
//    }
//
//    /**
//     * Метод для получения списка всех операций (для администратора).
//     *
//     * @param token Токен администратора.
//     * @return Ответ с результатом операции и списком операций.
//     */
//    public OperationOutResponse allOperations(String token) {
//        try {
//            long personId = tokenService.getPersonId(token);
//            Person person = personUseCase.findById(personId);
//
//            if (person.getRole() == Role.ADMIN) {
//               Map<Long,OperationOutDto> dtoMap = operationUseCase.findAll()
//                        .entrySet()
//                        .stream()
//                        .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), new OperationOutDto(entry.getValue())),
//                        HashMap::putAll);
//                return new OperationOutResponse("200 OK.", dtoMap);
//            }
//            return new OperationOutResponse("400 bad request", Collections.emptyMap());
//        } catch (TokenNotActualException e) {
//            return new OperationOutResponse("400 bad request", Collections.emptyMap());
//        } catch (PersonNotFoundException e){
//            return new OperationOutResponse("400 bad request", Collections.emptyMap());
//        }
//    }
//
//    /**
//     * Метод для получения списка всех измерений (для администратора).
//     *
//     * @param token Токен администратора.
//     * @return Ответ с результатом операции и списком измерений.
//     */
//    public MeasurementOutResponse allMeasurements(String token) {
//        try {
//            long personId = tokenService.getPersonId(token);
//            Person person = personUseCase.findById(personId);
//
//            if (person.getRole() == Role.ADMIN) {
//                Map<Long, MeasurementOutDto> dtoMap = measurementUseCase.findAll()
//                        .entrySet()
//                        .stream()
//                        .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), new MeasurementOutDto(entry.getValue())),
//                        HashMap::putAll);
//                return new MeasurementOutResponse("200 OK.", dtoMap);
//            }
//            return new MeasurementOutResponse("400 bad request", Collections.emptyMap());
//        } catch (TokenNotActualException e) {
//            return new MeasurementOutResponse("400 bad request", Collections.emptyMap());
//        }
//    }
//
//    /**
//     * Метод для добавления нового типа счетчика (для администратора).
//     *
//     * @param dto   DTO с информацией о новом типе счетчика.
//     * @param token Токен администратора.
//     * @return Строка с результатом операции.
//     */
//    public String addCounterType(CounterTypeDto dto, String token) {
//        try {
//            long personId = tokenService.getPersonId(token);
//            Person person = personUseCase.findById(personId);
//
//            if (person.getRole() == Role.ADMIN) {
//                try {
//                    counterTypeUseCase.save(new CounterType(dto.getName()));
//                    return "201 created.";
//                } catch (CounterTypeAlreadyExistsException e){
//                    return "400 bad request: " + e.getMessage();
//                }
//            }
//            return "400 bad request";
//        } catch (TokenNotActualException e) {
//            return "400 bad request " + e.getMessage();
//        }
//    }
//
//    /**
//     * Метод для передачи счетчика другому пользователю (для администратора).
//     *
//     * @param dto         DTO с информацией о типе счетчика.
//     * @param receiverId  Идентификатор пользователя-получателя счетчика.
//     * @param token       Токен администратора.
//     * @return Строка с результатом операции.
//     */
//    public String giveCounter(CounterTypeDto dto, long receiverId, String token) {
//        try {
//            long personId = tokenService.getPersonId(token);
//            Person person = personUseCase.findById(personId);
//
//            if (person.getRole() == Role.ADMIN) {
//                counterUseCase.save(new Counter(receiverId, new CounterType(dto.getName())));
//                operationUseCase.save(new Operation(personId, Action.GIVE_COUNTER, LocalDateTime.now()));
//                return "201 created.";
//            }
//            return "400 bad request";
//        } catch (TokenNotActualException | PersonNotFoundException | CounterTypeNotFoundException e) {
//            return "400 bad request " + e.getMessage();
//        }
//    }
}
