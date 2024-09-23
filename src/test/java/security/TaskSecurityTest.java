package security;

import com.ibatis.common.jdbc.ScriptRunner;
import config.PostgresqlSessionFactory;
import liquibase.Liquibase;
import liquibase.database.*;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import mapper.TaskMapper;
import model.command.TaskCommand;
import model.dto.TaskDTO;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import repository.TaskRepository;
import service.TaskService;

import java.io.*;
import java.sql.*;
import java.util.List;

import static constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskSecurityTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );
    String url;
    String username;
    String password;
    String driver;
    SessionFactory factory;
    TaskSecurity taskSecurity;
    TaskMapper taskMapper = TaskMapper.INSTANCE;
    TaskRepository taskRepo;
    TaskService taskService;

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
        factory = PostgresqlSessionFactory.createSessionFactory(url, driver, username, password);
        taskRepo = new TaskRepository(factory);
        taskSecurity = new TaskSecurity();
        taskService = new TaskService(taskMapper, taskRepo, factory);
    }

    @Test
    void shouldThrowExceptionCaseNullTags() {
        String[] tagTitles = {};
        TaskCommand taskCommand = new TaskCommand("Задача для тестирования", "1", tagTitles);

        Exception exception = assertThrows(Exception.class, () ->
                taskSecurity.validateTaskData(taskCommand)
        );
        assertEquals(UNTAGGED_TASK_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionCaseLongTitle() {
        String[] tagTitles = {"Первый тэг", "Второй тэг"};
        TaskCommand taskCommand = new TaskCommand("Задача для тестирования с очень длииииииииииииииииинным названием", "1", tagTitles);

        Exception exception = assertThrows(Exception.class, () ->
                taskSecurity.validateTaskData(taskCommand)
        );
        assertEquals(TASK_TITLE_VALIDATE_ERROR, exception.getMessage());
    }

    @Test
    void shouldNotThrowValidateTaskException() {
        String[] tagTitles = {"Первый тэг", "Второй тэг"};
        TaskCommand taskCommand = new TaskCommand("Задача для тестирования", "1", tagTitles);

        assertDoesNotThrow(() -> taskSecurity.validateTaskData(taskCommand));
    }

    @Test
    void shouldThrowExceptionCaseValidateTagsToDeleteError() {
        String[] tagsId = {"91", "92", "93"};
        List<TaskDTO> tasks = taskService.getAllElements();

        Exception exception = assertThrows(Exception.class, () ->
                taskSecurity.validateTagsDelete(tagsId, tasks)
        );
        assertEquals(TAG_DELETE_ERROR, exception.getMessage());
    }

    @Test
    void shouldNotThrowValidateTagsToDeleteException() {
        String[] tagsId = {"5"};
        List<TaskDTO> tasks = taskService.getAllElements();

        assertDoesNotThrow(() -> taskSecurity.validateTagsDelete(tagsId, tasks));
    }

}