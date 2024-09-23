package security;

import com.ibatis.common.jdbc.ScriptRunner;
import config.PostgresqlSessionFactory;
import liquibase.Liquibase;
import liquibase.database.*;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import mapper.UserMapper;
import model.command.*;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import repository.UserRepository;
import service.UserService;

import java.io.*;
import java.sql.*;

import static constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserSecurityTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );
    String url;
    String username;
    String password;
    String driver;
    SessionFactory factory;
    UserMapper userMapper;
    UserRepository userRepo;
    UserSecurity userSecurity;
    UserService userService;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        try(Connection conn = postgres.createConnection("")) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            Liquibase liquibase = new Liquibase("/db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update("test");
            ScriptRunner runner = new ScriptRunner(conn, false, false);
            Reader reader = new BufferedReader(new FileReader("src/main/resources/initData.sql"));
            runner.runScript(reader);
        } catch (IOException | SQLException | LiquibaseException e){
            fail(e.getMessage());
        }
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        url = postgres.getJdbcUrl();
        username = postgres.getUsername();
        password = postgres.getPassword();
        driver = "org.postgresql.Driver";
        userMapper = UserMapper.INSTANCE;
        factory = PostgresqlSessionFactory.createSessionFactory(url, driver, username, password);
        userRepo = new UserRepository(factory);
        userService = new UserService(userMapper, userRepo, factory);
        userSecurity = new UserSecurity(userService);
    }

    @Test
    void shouldThrowExceptionCasePasswordError() {
        UserCommand userCommand = new UserCommand("Тест", "USER", "qwerty123", "Тест", "123");

        Exception exception = assertThrows(Exception.class, () ->
                userSecurity.validateUserData(userCommand)
        );
        assertEquals(PASSWORD_VALIDATE_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionCaseLoginError() {
        UserCommand userCommand = new UserCommand("Тест", "USER", "popov.p", "Тест", "qwerty123");

        Exception exception = assertThrows(Exception.class, () ->
                userSecurity.validateUserData(userCommand)
        );
        assertEquals(LOGIN_VALIDATE_ERROR, exception.getMessage());
    }

    @Test
    void shouldNotThrowValidateException() {
        UserCommand userCommand = new UserCommand("Тест", "USER", "qwerty123", "Тест", "qwerty123");

        assertDoesNotThrow(() -> userSecurity.validateUserData(userCommand));
    }

    @Test
    void shouldThrowExceptionCaseAuthError() {
        LoginCommand loginCommand = new LoginCommand("popov.p", "qwerty123");

        Exception exception = assertThrows(Exception.class, () ->
                userSecurity.authenticateUser(loginCommand)
        );
        assertEquals(AUTHENTICATION_ERROR, exception.getMessage());
    }

    @Test
    void shouldNotThrowAuthException() {
        LoginCommand loginCommand = new LoginCommand("popov.p", "274hxx2l");

        assertDoesNotThrow(() -> userSecurity.authenticateUser(loginCommand));
    }
}