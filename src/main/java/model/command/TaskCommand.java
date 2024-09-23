package model.command;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCommand {
    private String title;
    private String userId;
    private String[] tagTitles;
}
