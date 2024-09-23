package model.enums;

import lombok.Getter;

@Getter
public enum Color {
    AQUA("Морская волна:#00ffff"),
    BLACK("Чёрный:#000000"),
    BLUE("Синий:#0000ff"),
    FUCHSIA("Фуксия:#ff00ff"),
    GRAY("Серый:#808080"),
    GREEN("Зелёный:#008000"),
    LIME("Ярко-зелёный:#00ff00"),
    MAROON("Тёмно-бордовый:#800000"),
    NAVY("Тёмно-синий:#000080"),
    OLIVE("Оливковый:#808000"),
    PURPLE("Пурпурный:#800080"),
    RED("Красный:#ff0000"),
    SILVER("Серебряный:#c0c0c0"),
    TEAL("Сине-зелёный:#008080"),
    WHITE("Белый:#ffffff"),
    YELLOW("Жёлтый:#ffff00");

    public final String label;

    Color(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
