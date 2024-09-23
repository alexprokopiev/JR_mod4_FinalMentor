package constants;

public class Constants {

    //пути к файлу properties
    public static final String PROPS_PATH = "/config.properties";

    //свойства в файле properties
    public static final String URL = "url";
    public static final String DRIVER = "driver";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    //статус аутентификации
    public static final String USER = "User";
    public static final String AUTH = "User_authenticated";

    //маппер
    public static final String OBJECT_MAPPER = "Object mapper";

    //коллекции моделей для HTML-форм
    public static final String ROLES = "Roles";
    public static final String COLORS = "Colors";
    public static final String STATES = "States";
    public static final String TAGS_COLLECTION = "Tags";
    public static final String USERS_COLLECTION = "Users";

    //компоненты
    public static final String TASK_SECURITY = "Task security";
    public static final String USER_SECURITY = "User security";
    public static final String TASK_MAPPER = "Task mapper";
    public static final String TAG_SERVICE = "Tag service";
    public static final String TASK_SERVICE = "Task service";
    public static final String USER_SERVICE = "User service";
    public static final String COMMENT_SERVICE = "Comment service";

    //регулярные выражения и паттерны
    public static final String PASSWORD_REGEX = "^[\\d\\w]{8,16}$";

    //параметры запроса
    public static final String COLOR = "color";
    public static final String TITLE = "title";
    public static final String STATE = "state";
    public static final String TAG_ID = "tagId";
    public static final String USER_ID = "userId";

    //endpoints
    public static final String API = "api";
    public static final String SLASH = "/";
    public static final String TAGS = "tags";
    public static final String ASTERISK = "*";
    public static final String USERS = "users";
    public static final String LOGIN = "login";
    public static final String TASKS = "tasks";
    public static final String COMMENTS = "comments";

    //jsp страницы
    public static final String INDEX_JSP = "/index.jsp";
    public static final String TODOLIST_JSP = "/todoList.jsp";

    //сообщения для логов
    public static final String ADD_TAG_MSG = "Added a new tag";
    public static final String ADD_USER_MSG = "Added a new user";
    public static final String EDIT_TAG_MSG = "Tag data updated";
    public static final String ADD_TASK_MSG = "Added a new task";
    public static final String EDIT_TASK_MSG = "Task data updated";

    //сообщения об ошибках
    public static final String AUTHENTICATION_ERROR = "Access denied";
    public static final String ID_EXTRACT_ERROR = "There was an error receiving the ID";
    public static final String PROPERTIES_ERROR = "Configuration file could not be found";
    public static final String UNTAGGED_TASK_ERROR = "The task must have at least one tag";
    public static final String ENTITY_SAVE_ERROR = "An error occurred when saving an entity in the database";
    public static final String TAG_DELETE_ERROR = "Tag deletion error. The task must contain at least one tag";
    public static final String LOGIN_VALIDATE_ERROR = "The login you have chosen already exists, select another one";
    public static final String DATA_FORMAT_ERROR = "The format of the entered data does not comply with the system requirements";
    public static final String TASK_TITLE_VALIDATE_ERROR = "The task title is inadequate and should be between 3 and 32 characters in length";
    public static final String PASSWORD_VALIDATE_ERROR = "Password must contain only numbers and Latin letters, from 8 to 16 characters inclusive";

}
