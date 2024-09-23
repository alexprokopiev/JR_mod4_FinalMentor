package service;

import com.ibatis.common.jdbc.ScriptRunner;
import config.PostgresqlSessionFactory;
import liquibase.Liquibase;
import liquibase.database.*;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import mapper.*;
import model.command.TagCommand;
import model.dto.CommentDTO;
import model.enums.Color;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import repository.*;

import java.io.*;
import java.sql.*;
import java.util.Objects;

import static constants.Constants.ENTITY_SAVE_ERROR;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AbstractServiceTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );
    String url;
    String username;
    String password;
    String driver;
    SessionFactory factory;
    TagMapper tagMapper;
    CommentMapper commentMapper;
    TagRepository tagRepo;
    CommentRepository commentRepo;
    TagService tagService;
    CommentService commentService;

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
        tagMapper = TagMapper.INSTANCE;
        commentMapper = CommentMapper.INSTANCE;
        tagRepo = new TagRepository(factory);
        commentRepo = new CommentRepository(factory);
        tagService = new TagService(tagMapper, tagRepo, factory);
        commentService = new CommentService(commentMapper, commentRepo, factory);
    }

    @Test
    void shouldSaveEntityInDB() {
        TagCommand tagCommand = new TagCommand("Тестовый тэг", "GREEN");

        tagService.save(tagCommand);

        assertTrue(tagService.getAllElements().stream()
                .anyMatch(tag -> tag.getColor() == Color.GREEN && Objects.equals(tag.getTitle(), "Тестовый тэг")));
    }

    @Test
    void shouldThrowExceptionCaseEntityDataError() {
        TagCommand tagCommand = new TagCommand("Тестовый тэг", null);

        Exception exception = assertThrows(Exception.class, () ->
                tagService.save(tagCommand)
        );
        assertEquals(ENTITY_SAVE_ERROR, exception.getMessage());
    }

    @Test
    void shouldDeleteEntityFromDB() {
        commentService.deleteEntity(commentService.getById(51L));

        assertFalse(commentService.getAllElements().stream()
                .map(CommentDTO::getId).toList()
                .contains("51"));
    }
}