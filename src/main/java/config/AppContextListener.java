package config;

import lombok.Getter;
import mapper.*;
import service.*;
import repository.*;
import security.*;
import model.enums.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;
import org.hibernate.SessionFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Properties;
import java.security.InvalidParameterException;

import static constants.Constants.*;

@Getter
@WebListener
public class AppContextListener implements ServletContextListener {

    private final String url;
    private final String driver;
    private final String username;
    private final String password;

    public AppContextListener() {
        url = readPropertyValue(URL);
        driver = readPropertyValue(DRIVER);
        username = readPropertyValue(USERNAME);
        password = readPropertyValue(PASSWORD);
    }

    public AppContextListener(String url, String driver, String username, String password) {
        this.url = url;
        this.driver = driver;
        this.username = username;
        this.password = password;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext ctx = sce.getServletContext();
        SessionFactory factory = PostgresqlSessionFactory.createSessionFactory(url, driver, username, password);

        ObjectMapper objectMapper = new ObjectMapper();

        TagMapper tagMapper = TagMapper.INSTANCE;
        TaskMapper taskMapper = TaskMapper.INSTANCE;
        UserMapper userMapper = UserMapper.INSTANCE;
        CommentMapper commentMapper = CommentMapper.INSTANCE;

        TagRepository tagRepo = new TagRepository(factory);
        TaskRepository taskRepo = new TaskRepository(factory);
        UserRepository userRepo = new UserRepository(factory);
        CommentRepository commentRepo = new CommentRepository(factory);

        TagService tagService = new TagService(tagMapper, tagRepo, factory);
        TaskService taskService = new TaskService(taskMapper, taskRepo, factory);
        UserService userService = new UserService(userMapper, userRepo, factory);
        CommentService commentService = new CommentService(commentMapper, commentRepo, factory);

        TaskSecurity taskSecurity = new TaskSecurity();
        UserSecurity userSecurity = new UserSecurity(userService);

        ctx.setAttribute(AUTH, "false");
        ctx.setAttribute(ROLES, Role.values());
        ctx.setAttribute(COLORS, Color.values());
        ctx.setAttribute(STATES, State.values());
        ctx.setAttribute(TASK_MAPPER, taskMapper);
        ctx.setAttribute(OBJECT_MAPPER, objectMapper);
        ctx.setAttribute(TAG_SERVICE, tagService);
        ctx.setAttribute(TASK_SERVICE, taskService);
        ctx.setAttribute(USER_SERVICE, userService);
        ctx.setAttribute(COMMENT_SERVICE, commentService);
        ctx.setAttribute(TASK_SECURITY, taskSecurity);
        ctx.setAttribute(USER_SECURITY, userSecurity);
        ctx.setAttribute(TAGS_COLLECTION, tagService.getAllElements());
        ctx.setAttribute(USERS_COLLECTION, userService.getAllElements());

        ServletContextListener.super.contextInitialized(sce);

    }

    private String readPropertyValue(String key) {
        Properties props = new Properties();
        try (InputStream inputStream = AppContextListener.class.getResourceAsStream(PROPS_PATH)) {
            props.load(inputStream);
        } catch (Exception e) {
            throw new InvalidParameterException(PROPERTIES_ERROR);
        }
        return props.getProperty(key);
    }
}
