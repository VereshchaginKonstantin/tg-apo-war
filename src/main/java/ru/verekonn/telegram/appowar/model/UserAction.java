package ru.verekonn.telegram.appowar.model;

public enum UserAction {
    ATTACK("Атака"),
    PREPARE_ATTACK(""),
    DEFENSE("Оборона"),
    PREPARE_DEFENSE(""),
    EXPLORE("Разведка"),
    START("");

    private final String buttonName;

    UserAction(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
