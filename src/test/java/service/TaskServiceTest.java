package service;

import com.ibatis.common.jdbc.ScriptRunner;
import config.PostgresqlSessionFactory;
import liquibase.Liquibase;
import liquibase.database.*;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.SneakyThrows;
import mapper.*;
import model.command.TaskCommand;
import model.dto.TaskDTO;
import model.entity.*;
import model.enums.State;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import repository.*;

import java.io.*;
import java.sql.*;
import java.util.*;

import static constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );
    String url;
    String username;
    String password;
    String driver;
    SessionFactory factory;
    TaskMapper taskMapper;
    UserMapper userMapper;
    TagMapper tagMapper;
    TaskRepository taskRepo;
    UserRepository userRepo;
    TagRepository tagRepo;
    TaskService taskService;
    UserService userService;
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
        taskMapper = TaskMapper.INSTANCE;
        userMapper = UserMapper.INSTANCE;
        tagMapper = TagMapper.INSTANCE;
        taskRepo = new TaskRepository(factory);
        userRepo = new UserRepository(factory);
        tagRepo = new TagRepository(factory);
        taskService = new TaskService(taskMapper, taskRepo, factory);
        userService = new UserService(userMapper, userRepo, factory);
        tagService = new TagService(tagMapper, tagRepo, factory);
    }

    @Test
    void shouldSaveTaskCorrectly() {
        TaskCommand taskCommand = new TaskCommand("Test title", "2", new String[]{"Работа", "Хобби"});
        UserEntity user = userService.getById(Long.valueOf("2"));
        Set<TagEntity> tags = new HashSet<>();
        tags.add(tagService.getById(91L));
        tags.add(tagService.getById(92L));

        taskService.save(taskCommand, user, tags);

        assertTrue(taskService.getAllElements().stream()
                .anyMatch(task -> task.getTitle().equals("Test title")
                        && task.getUser().getId().equals("2")
                        && task.getTags().length == 2));
    }

    @Test
    void shouldThrowExceptionCaseCreateTaskDataError() {
        TaskCommand taskCommand = new TaskCommand(null, "2", new String[]{"Работа", "Хобби"});
        UserEntity user = userService.getById(Long.valueOf("2"));
        Set<TagEntity> tags = new HashSet<>();
        tags.add(tagService.getById(91L));
        tags.add(tagService.getById(92L));

        Exception exception = assertThrows(Exception.class, () ->
                taskService.save(taskCommand, user, tags)
        );
        assertEquals(ENTITY_SAVE_ERROR, exception.getMessage());
    }

    @Test
    void shouldGetTaskByAttributes() {
        String commandTitle = "РЎРґР°С‚СЊ РєРІР°СЂС‚Р°Р»СЊРЅС‹Р№ РѕС‚С‡РµС‚";
        Long userId = 5L;

        TaskEntity task = taskService.getTaskByAttributes(commandTitle, userId);

        assertEquals("РЎРґР°С‚СЊ РєРІР°СЂС‚Р°Р»СЊРЅС‹Р№ РѕС‚С‡РµС‚", task.getTitle());
        assertEquals(5L, task.getUser().getId());
    }

    @Test
    void shouldGetNullTaskByAttributes() {
        String commandTitle = "РЎРґР°С‚СЊ РєРІР°СЂС‚Р°Р»СЊРЅС‹Р№ РѕС‚С‡РµС‚";
        Long userId = 6L;

        TaskEntity task = taskService.getTaskByAttributes(commandTitle, userId);

        assertNull(task);
    }

    @Test
    void shouldGetAllTasksForAdminRole() {
        List<TaskDTO> tasks = taskService.getTasks(userService.getById(1L), null);

        assertEquals(20, tasks.size());
    }

    @Test
    void shouldGetFilteredTasksForAdminRole() {
        List<TaskDTO> tasks = taskService.getTasks(userService.getById(1L), userService.getById(4L));

        assertEquals(4, tasks.size());
    }

    @Test
    void shouldGetFilteredTasksForUserRole() {
        List<TaskDTO> tasks = taskService.getTasks(userService.getById(4L), null);

        assertEquals(4, tasks.size());
    }

    @Test
    @SneakyThrows
    void shouldEditTaskCorrectly() {
        String requestUrl = "http://localhost:8080/tasks/100";
        String[] parameters = {"Test title", "3", "DONE"};
        Set<TagEntity> tags = new HashSet<>();
        tags.add(tagService.getById(91L));
        tags.add(tagService.getById(95L));
        UserEntity user = userService.getById(Long.valueOf(parameters[1]));

        taskService.editTask(requestUrl, parameters, tags, user);

        assertEquals("Test title", taskService.getById(100L).getTitle());
        assertEquals(userService.getById(3L), taskService.getById(100L).getUser());
        assertEquals(State.DONE, taskService.getById(100L).getState());
        assertEquals(2, taskService.getById(100L).getTags().size());
    }

    @Test
    void shouldThrowExceptionCaseIdError() {
        String requestUrl = "http://localhost:8080/tasks/qwe";
        String[] parameters = {"Test title", "3", "DONE"};
        Set<TagEntity> tags = new HashSet<>();
        tags.add(tagService.getById(91L));
        tags.add(tagService.getById(95L));
        UserEntity user = userService.getById(Long.valueOf(parameters[1]));

        Exception exception = assertThrows(Exception.class, () ->
                taskService.editTask(requestUrl, parameters, tags, user)
        );
        assertEquals(ID_EXTRACT_ERROR, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionCaseEditTaskDataError() {
        String requestUrl = "http://localhost:8080/tasks/6";
        String[] parameters = {"Test title", "3", "DONE"};
        Set<TagEntity> tags = new HashSet<>();
        tags.add(tagService.getById(91L));
        tags.add(tagService.getById(95L));
        UserEntity user = userService.getById(Long.valueOf(parameters[1]));

        Exception exception = assertThrows(Exception.class, () ->
                taskService.editTask(requestUrl, parameters, tags, user)
        );
        assertEquals(ENTITY_SAVE_ERROR, exception.getMessage());
    }

    @Test
    void shouldTagFromTasksCorrectly() {
        TagEntity tag = tagService.getById(95L);

        taskService.deleteTagFromTasks(tag);

        int tagCount = taskService.getAllElements().stream()
                .filter(task -> Arrays.stream(task.getTags())
                        .anyMatch(tagDTO -> tagDTO.getId().equals("95")))
                .toList().size();

        assertEquals(0, tagCount);
    }
}