package controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.Controller;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import model.command.CommentCommand;
import model.entity.TaskEntity;
import service.CommentService;
import service.TaskService;

import static constants.Constants.*;

@Log4j2
@Setter
@WebServlet(SLASH + API + SLASH + COMMENTS + SLASH + ASTERISK)
public class ApiCommentController extends HttpServlet implements Controller {

    private ServletContext ctx;
    private ObjectMapper mapper;
    private TaskService taskService;
    private CommentService commentService;

    @Override
    public void init(ServletConfig config) {
        ctx = config.getServletContext();
        mapper = (ObjectMapper) ctx.getAttribute(OBJECT_MAPPER);
        taskService = (TaskService) ctx.getAttribute(TASK_SERVICE);
        commentService = (CommentService) ctx.getAttribute(COMMENT_SERVICE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            CommentCommand commentCommand = getObjectFromBody(mapper, req, CommentCommand.class);
            TaskEntity task = taskService.getTaskByAttributes(commentCommand.getTitle(), Long.valueOf(commentCommand.getUserId()));
            commentService.save(commentCommand, task);
            resp.setStatus(201);
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatus(400);
        }
    }
}
