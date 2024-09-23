package model.dto;

import lombok.Data;
import model.enums.Color;

@Data
public class TagDTO implements DTO {
    private String id;
    private Color color;
    private String title;
}
