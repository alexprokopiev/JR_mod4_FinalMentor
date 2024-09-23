package model.dto;

import lombok.Data;
import model.enums.Role;

@Data
public class UserDTO implements DTO {
    private String id;
    private Role role;
    private String name;
    private String lastName;
}
