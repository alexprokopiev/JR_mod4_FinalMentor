package security;


import model.command.*;
import model.entity.UserEntity;
import service.UserService;
import lombok.AllArgsConstructor;

import java.util.regex.*;
import java.security.InvalidParameterException;
import javax.security.auth.login.AccountException;

import static constants.Constants.*;

@AllArgsConstructor
public class UserSecurity {

    private final UserService userService;

    public void validateUserData(UserCommand userCommand) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(userCommand.getPassword());
        String commandLogin = userCommand.getLogin();
        if (!matcher.matches()) throw new InvalidParameterException(PASSWORD_VALIDATE_ERROR);
        for (String login : userService.getLoginList()) {
            if (commandLogin.equals(login)) throw new InvalidParameterException(LOGIN_VALIDATE_ERROR);
        }
    }

    public UserEntity authenticateUser(LoginCommand loginCommand) throws AccountException {
        UserEntity user = userService.getUserByLogin(loginCommand.getLogin());
        if (user != null) {
            if (!loginCommand.getPassword().equals(user.getPassword())) throw new AccountException(AUTHENTICATION_ERROR);
            return user;
        }
        return null;
    }
}
