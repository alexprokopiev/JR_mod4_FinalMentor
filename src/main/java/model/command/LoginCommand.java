package model.command;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCommand implements Command {
    private String login;
    private String password;
}
