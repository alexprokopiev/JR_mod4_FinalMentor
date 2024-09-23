package service;

import com.ibatis.common.jdbc.ScriptRunner;
import config.PostgresqlSessionFactory;
import liquibase.Liquibase;
import liquibase.database.*;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.SneakyThrows;
import mapper.TagMapper;
import model.entity.TagEntity;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import repository.TagRepository;

import java.io.*;
import java.sql.*;
import java.util.Set;

import static constants.Constants.ID_EXTRACT_ERROR;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );
    String url;
    String username;
    String password;
    String driver;
    SessionFactory factory;
    TagMapper tagMapper;
    TagRepository tagRepo;
    TagService tagService;

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
        tagRepo = new TagRepository(factory);
        tagService = new TagService(tagMapper, tagRepo, factory);
    }

    @Test
    void shouldGetTagsByTitles() {
        String[] titles = {"Р Р°Р±РѕС‚Р°", "РҐРѕР±Р±Рё"};

        Set<TagEntity> tags = tagService.getTagsSetByTitles(titles);

        assertEquals(2, tags.size());
    }

    @Test
    @SneakyThrows
    void shouldEditTagCorrectly() {
        String requestUrl = "http://localhost:8080/tags/92";
        String[] parameters = {"Увлечения", "WHITE"};

        tagService.editTag(requestUrl, parameters);

        assertEquals("Увлечения", tagService.getById(92L).getTitle());
        assertEquals("WHITE", tagService.getById(92L).getColor().name());
    }

    @Test
    void shouldThrowExceptionCaseIdExtractError() {
        String requestUrl = "http://localhost:8080/tags/qwe";
        String[] parameters = {"Увлечения", "WHITE"};

        Exception exception = assertThrows(Exception.class, () ->
                tagService.editTag(requestUrl, parameters)
        );
        assertEquals(ID_EXTRACT_ERROR, exception.getMessage());
    }
}