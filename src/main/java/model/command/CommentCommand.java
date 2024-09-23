package model.command;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCommand implements Command {
    private String title;
    private String userId;
    private String comment;
}
