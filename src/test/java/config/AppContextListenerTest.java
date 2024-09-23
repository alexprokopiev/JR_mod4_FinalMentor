package config;

import service.*;
import security.*;
import model.enums.*;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import mapper.TaskMapper;
import jakarta.servlet.*;
import lombok.SneakyThrows;
import liquibase.Liquibase;
import liquibase.database.*;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.*;
import java.util.*;
import java.io.InputStream;

import static constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class AppContextListenerTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );
    String url;
    String username;
    String password;
    String driver;
    AppContextListener contextListener;
    @Mock
    ServletContextEvent sce;
    @Mock
    ServletContext ctx;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        try(Connection conn = postgres.createConnection("")) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            Liquibase liquibase = new Liquibase("/db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update("test");
        } catch (SQLException | LiquibaseException e){
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
        contextListener = new AppContextListener(url, driver, username, password);
    }

    @Test
    @SneakyThrows
    void shouldCreateCorrectlyAppContextListener() {
        Properties properties = new Properties();
        InputStream inputStream = AppContextListenerTest.class.getResourceAsStream(PROPS_PATH);
        properties.load(inputStream);

        AppContextListener listener = new AppContextListener();

        assertEquals(listener.getUrl(), properties.getProperty(URL));
        assertEquals(listener.getDriver(), properties.getProperty(DRIVER));
        assertEquals(listener.getUsername(), properties.getProperty(USERNAME));
        assertEquals(listener.getPassword(), properties.getProperty(PASSWORD));
    }

    @Test
    void shouldReturnContextAttributesValues() {
        Mockito.when(sce.getServletContext()).thenReturn(ctx);

        contextListener.contextInitialized(sce);

        verify(ctx).setAttribute(eq(AUTH), any(String.class));
        verify(ctx).setAttribute(eq(ROLES), any(Role[].class));
        verify(ctx).setAttribute(eq(COLORS), any(Color[].class));
        verify(ctx).setAttribute(eq(STATES), any(State[].class));
        verify(ctx).setAttribute(eq(TASK_MAPPER), any(TaskMapper.class));
        verify(ctx).setAttribute(eq(OBJECT_MAPPER), any(ObjectMapper.class));
        verify(ctx).setAttribute(eq(TAG_SERVICE), any(TagService.class));
        verify(ctx).setAttribute(eq(TASK_SERVICE), any(TaskService.class));
        verify(ctx).setAttribute(eq(USER_SERVICE), any(UserService.class));
        verify(ctx).setAttribute(eq(COMMENT_SERVICE), any(CommentService.class));
        verify(ctx).setAttribute(eq(TASK_SECURITY), any(TaskSecurity.class));
        verify(ctx).setAttribute(eq(USER_SECURITY), any(UserSecurity.class));
        verify(ctx).setAttribute(eq(TAGS_COLLECTION), any(List.class));
        verify(ctx).setAttribute(eq(USERS_COLLECTION), any(List.class));
    }
}