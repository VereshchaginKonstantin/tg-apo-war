package ru.taksebe.telegram.writeRead.constants.bot;

/**
 * Названия кнопок основной клавиатуры
 */
public enum ButtonNameEnum {
    START_FIGHT_TASKS_BUTTON("Начать бой с кем-нибудь"),
    START_FIGHT_USER_TASKS_BUTTON("Начать бой с "),
    GET_TASKS_BUTTON("Атака!!!"),
    GET_DICTIONARY_BUTTON("Разведка"),
    UPLOAD_DICTIONARY_BUTTON("Защита!"),
    HELP_BUTTON("Помощь");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}