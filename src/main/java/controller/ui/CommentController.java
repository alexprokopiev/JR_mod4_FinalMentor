package controller.ui;

import service.*;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import controller.Controller;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import model.entity.TaskEntity;
import model.command.CommentCommand;
import com.fasterxml.jackson.databind.ObjectMapper;

import static constants.Constants.*;

@Log4j2
@Setter
@WebServlet(SLASH + COMMENTS + SLASH + ASTERISK)
public class CommentController extends HttpServlet implements Controller {

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
