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
import model.command.LoginCommand;
import security.UserSecurity;

import static constants.Constants.*;

@Log4j2
@Setter
@WebServlet(SLASH + API + SLASH + LOGIN + SLASH + ASTERISK)
public class ApiLoginController extends HttpServlet implements Controller {

    private ServletContext ctx;
    private ObjectMapper mapper;
    private UserSecurity userSecurity;

    @Override
    public void init(ServletConfig config) {
        ctx = config.getServletContext();
        mapper = (ObjectMapper) ctx.getAttribute(OBJECT_MAPPER);
        userSecurity = (UserSecurity) ctx.getAttribute(USER_SECURITY);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            LoginCommand loginCommand = getObjectFromBody(mapper, req, LoginCommand.class);
            ctx.setAttribute(USER, userSecurity.authenticateUser(loginCommand));
            ctx.setAttribute(AUTH, "true");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatus(401);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ctx.setAttribute(AUTH, "false");
            ctx.removeAttribute(USER);
            req.getRequestDispatcher(INDEX_JSP).forward(req, resp);
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setStatus(400);
        }
    }
}
