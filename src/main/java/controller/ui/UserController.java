package controller.ui;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import service.UserService;
import security.UserSecurity;
import controller.Controller;
import model.command.UserCommand;
import com.fasterxml.jackson.databind.ObjectMapper;

import static constants.Constants.*;

@Log4j2
@Setter
@WebServlet(SLASH + USERS + SLASH + ASTERISK)
class UserController extends HttpServlet implements Controller {

    private ServletContext ctx;
    private ObjectMapper mapper;
    private UserService userService;
    private UserSecurity userSecurity;

    @Override
    public void init(ServletConfig config) {
        ctx = config.getServletContext();
        mapper = (ObjectMapper) ctx.getAttribute(OBJECT_MAPPER);
        userService = (UserService) ctx.getAttribute(USER_SERVICE);
        userSecurity = (UserSecurity) ctx.getAttribute(USER_SECURITY);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            UserCommand userCommand = getObjectFromBody(mapper, req, UserCommand.class);
            userSecurity.validateUserData(userCommand);
            userService.save(userCommand);
            log.info(ADD_USER_MSG);
            ctx.setAttribute(USERS_COLLECTION, userService.getAllElements());
            resp.setStatus(201);
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatus(400);
        }
    }
}
