package model.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("Администратор"),
    USER("Пользователь");

    public final String label;

    Role(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
