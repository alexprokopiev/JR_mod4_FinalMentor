package controller.ui;

import service.*;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.Controller;
import security.TaskSecurity;
import model.entity.TagEntity;
import model.command.TagCommand;

import java.util.Arrays;
import java.util.List;

import static constants.Constants.*;

@Log4j2
@Setter
@WebServlet(SLASH + TAGS + SLASH + ASTERISK)
public class TagController extends HttpServlet implements Controller {

    private ServletContext ctx;
    private ObjectMapper mapper;
    private TagService tagService;
    private TaskService taskService;
    private TaskSecurity taskSecurity;

    @Override
    public void init(ServletConfig config) {
        ctx = config.getServletContext();
        mapper = (ObjectMapper) ctx.getAttribute(OBJECT_MAPPER);
        tagService = (TagService) ctx.getAttribute(TAG_SERVICE);
        taskService = (TaskService) ctx.getAttribute(TASK_SERVICE);
        taskSecurity = (TaskSecurity) ctx.getAttribute(TASK_SECURITY);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            TagCommand tagCommand = getObjectFromBody(mapper, req, TagCommand.class);
            tagService.save(tagCommand);
            ctx.setAttribute(TAGS_COLLECTION, tagService.getAllElements());
            resp.setStatus(201);
            log.info(ADD_TAG_MSG);
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
                    req.getParameter(COLOR)
            };
            tagService.editTag(requestUrl, parameters);
            ctx.setAttribute(TAGS_COLLECTION, tagService.getAllElements());
            log.info(EDIT_TAG_MSG);
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String[] tagsId = req.getParameterValues(TAG_ID);
            taskSecurity.validateTagsDelete(tagsId, taskService.getAllElements());
            List<TagEntity> tags = Arrays.stream(tagsId)
                    .map(tagId -> tagService.getById(Long.valueOf(tagId)))
                    .toList();
            tags.forEach(taskService::deleteTagFromTasks);
            tags.forEach(tagService::deleteEntity);
            ctx.setAttribute(TAGS_COLLECTION, tagService.getAllElements());
            resp.setStatus(204);
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatus(400);
        }
    }
}
