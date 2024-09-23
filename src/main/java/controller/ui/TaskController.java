package controller.ui;

import service.*;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import utils.Utilities;
import model.entity.*;
import model.dto.TaskDTO;
import model.command.TaskCommand;
import controller.Controller;
import security.TaskSecurity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

import static constants.Constants.*;

@Log4j2
@Setter
@WebServlet(SLASH + TASKS + SLASH + ASTERISK)
public class TaskController extends HttpServlet implements Controller {

    private ServletContext ctx;
    private ObjectMapper mapper;
    private TaskSecurity taskSecurity;
    private TagService tagService;
    private TaskService taskService;
    private UserService userService;


    @Override
    public void init(ServletConfig config) {
        ctx = config.getServletContext();
        mapper = (ObjectMapper) ctx.getAttribute(OBJECT_MAPPER);
        taskSecurity = (TaskSecurity) ctx.getAttribute(TASK_SECURITY);
        tagService = (TagService) ctx.getAttribute(TAG_SERVICE);
        taskService = (TaskService) ctx.getAttribute(TASK_SERVICE);
        userService = (UserService) ctx.getAttribute(USER_SERVICE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            TaskCommand taskCommand = getObjectFromBody(mapper, req, TaskCommand.class);
            taskSecurity.validateTaskData(taskCommand);
            UserEntity user = userService.getById(Long.valueOf(taskCommand.getUserId()));
            Set<TagEntity> tags = tagService.getTagsSetByTitles(taskCommand.getTagTitles());
            taskService.save(taskCommand, user, tags);
            resp.setStatus(201);
            log.info(ADD_TASK_MSG);
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatus(400);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String userId = req.getParameter(USER_ID);
            UserEntity filteredUser = null;
            if (userId != null) filteredUser = userService.getById(Long.valueOf(userId));
            List<TaskDTO> tasks = taskService.getTasks((UserEntity) ctx.getAttribute(USER), filteredUser);
            tasks.forEach(TaskDTO::initCommentsCount);
            req.setAttribute(TASKS, tasks);
            req.setAttribute(USER_ID, userId);
            req.getRequestDispatcher(TODOLIST_JSP).forward(req, resp);
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatus(400);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String requestUrl = req.getRequestURL().toString();
            String[] parameters = {
                    req.getParameter(TITLE),
                    req.getParameter(USER_ID),
                    req.getParameter(STATE)
            };
            String[] tagsId = req.getParameterValues(TAG_ID);
            taskSecurity.validateTaskData(new TaskCommand(parameters[0], parameters[1], tagsId));
            UserEntity user = userService.getById(Long.valueOf(parameters[1]));
            Set<TagEntity> tags = Arrays.stream(tagsId)
                    .map(s -> tagService.getById(Long.valueOf(s)))
                    .collect(Collectors.toSet());
            taskService.editTask(requestUrl, parameters, tags, user);
            log.info(EDIT_TASK_MSG);
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String requestUrl = req.getRequestURL().toString();
            int taskId = Utilities.getId(requestUrl, TASKS);
            taskService.deleteEntity(taskService.getById((long) taskId));
            resp.setStatus(204);
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatus(400);
        }
    }
}
