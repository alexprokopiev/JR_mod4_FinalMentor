package model.command;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagCommand implements Command {
    private String title;
    private String color;
}
