package service;

import com.ibatis.common.jdbc.ScriptRunner;
import config.PostgresqlSessionFactory;
import liquibase.Liquibase;
import liquibase.database.*;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import mapper.*;
import model.command.CommentCommand;
import model.entity.*;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import repository.*;

import java.io.*;
import java.sql.*;

import static constants.Constants.ENTITY_SAVE_ERROR;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );
    String url;
    String username;
    String password;
    String driver;
    SessionFactory factory;
    CommentMapper commentMapper;
    TaskMapper taskMapper;
    CommentRepository commentRepo;
    TaskRepository taskRepo;
    CommentService commentService;
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
        commentMapper = CommentMapper.INSTANCE;
        taskMapper = TaskMapper.INSTANCE;
        commentRepo = new CommentRepository(factory);
        taskRepo = new TaskRepository(factory);
        commentService = new CommentService(commentMapper, commentRepo, factory);
        taskService = new TaskService(taskMapper, taskRepo, factory);
    }

    @Test
    void shouldSaveComment() {
        CommentCommand commentCommand = new CommentCommand("Позвонить поставщику", "1", "Тестовый комментарий");
        TaskEntity task = taskService.getById(81L);

        commentService.save(commentCommand, task);

        assertEquals(1, taskService.getById(81L).getComments().size());
        assertEquals("Тестовый комментарий", ((CommentEntity) taskService.getById(81L).getComments().toArray()[0]).getComment());
    }

    @Test
    void shouldThrowExceptionCaseCommentsDataError() {
        CommentCommand commentCommand = new CommentCommand("Позвонить поставщику", "1", null);
        TaskEntity task = taskService.getById(0L);

        Exception exception = assertThrows(Exception.class, () ->
                commentService.save(commentCommand, task)
        );
        assertEquals(ENTITY_SAVE_ERROR, exception.getMessage());
    }
}