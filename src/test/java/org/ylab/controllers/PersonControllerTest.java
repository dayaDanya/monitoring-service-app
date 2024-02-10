package org.ylab.controllers;

class PersonControllerTest {
//
//    PersonController personController;
//    @Container
//    private static PostgreSQLContainer postgres =
//            new PostgreSQLContainer<>("postgres:13.3");
//
//    @BeforeAll
//    static void beforeAll() {
//        postgres.start();
//        MigrationUtil migrationConfig = new MigrationUtil(new ConnectionAdapter(postgres.getJdbcUrl(),
//                postgres.getUsername(),
//                postgres.getPassword())
//        );
//        migrationConfig.migrate();
//    }
//
//    @BeforeEach
//    void setUp() {
//        PersonRepo personRepo = new PersonRepo(new ConnectionAdapter(postgres.getJdbcUrl(),
//                postgres.getUsername(),
//                postgres.getPassword()));
//        OperationRepo operationRepo = new OperationRepo(new ConnectionAdapter(postgres.getJdbcUrl(),
//                postgres.getUsername(),
//                postgres.getPassword()));
//        CounterTypeService counterTypeUseCase = new CounterTypeService(new CounterTypeRepo(new ConnectionAdapter()));
//        OperationService operationUseCase = new OperationService(operationRepo);
//        PersonService personUseCase = new PersonService(new PasswordService(), personRepo);
//        personController = new PersonController(personUseCase);
//    }
//
//    @AfterEach
//    void tearDown() {
//        try (Connection connection = DriverManager
//                .getConnection(postgres.getJdbcUrl(),
//                        postgres.getUsername(),
//                        postgres.getPassword())) {
//            connection.setAutoCommit(false);
//            String insertDataSQL = "delete from entities.person";
//            PreparedStatement deleteDataStatement = connection.prepareStatement(insertDataSQL);
//            deleteDataStatement.executeUpdate();
//            connection.commit();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @AfterAll
//    static void afterAll() {
//
//        postgres.stop();
//    }
//
//    @Test
//    void register_200() {
//        PersonInDto person = new PersonInDto("email", "password");
//        Assertions.assertEquals("201 created", personController.register(person));
//    }
//
//    @Test
//    void register_400() {
//        PersonInDto person = new PersonInDto("email", "password");
//        personController.register(person);
//        Assertions.assertEquals("400 bad request",
//                personController.register(person));
//    }
//
//    @Test
//    void authenticate_200() {
//        PersonInDto person = new PersonInDto("email", "password");
//        personController.register(person);
//        String result = personController.authenticate(person);
//        Assertions.assertEquals("200 OK. Your authorization token is:",
//                result.substring(0, result.indexOf(":") + 1));
//    }
//
//    @Test
//    void authenticate_400() {
//        PersonInDto person = new PersonInDto("email", "password");
//        String result = personController.authenticate(person);
//        Assertions.assertEquals("400 bad request:",
//                result.substring(0, result.indexOf(":") + 1));
//    }
}