package model.command;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCommand implements Command {
    private String name;
    private String role;
    private String login;
    private String lastName;
    private String password;
}
