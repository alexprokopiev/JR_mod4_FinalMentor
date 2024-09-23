package model.enums;

import lombok.Getter;

@Getter
public enum State {
    NOT_STARTED("Не начатые"),
    IN_PROGRESS("В процессе"),
    DONE("Завершенные");

    public final String label;

    State(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
