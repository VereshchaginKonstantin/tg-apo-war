package ru.verekonn.telegram.appowar.model;

public enum UserAction {
    KILLED("Убит", true),
    CONTR_ATTACK("Контратака", true),
    DEFENSE_SUCCESS("Успешная защита", true),
    DEFENSE_FAILED("Пробили оборону", true),
    ATTACK_SUCCESS("Успешная Атака", true),
    ATTACK_FAILED("Атака не удалась", true),
    ATTACK("Атака", true),
    PREPARE_ATTACK("", false),
    DEFENSE("Оборона", false),
    PREPARE_DEFENSE("", false),
    EXPLORE("Разведка", false),
    START("", true);

    private final String buttonName;

    private final boolean broadcast;

    UserAction(String buttonName, boolean broadcast) {
        this.buttonName = buttonName;
        this.broadcast = broadcast;
    }

    public String getButtonName() {
        return buttonName;
    }

    public boolean isBroadcast() {
        return broadcast;
    }
}
