package ru.verekonn.telegram.appowar.model;

public enum BattleState {
    DRAW_BY_TIME(true),
    END(true),
    PROCESS(false),
    INIT(false);

    private boolean isFinal;

    BattleState(boolean isFinal) {
        this.isFinal = isFinal;
    }
    public boolean IsFinal() {
        return isFinal;
    }
}
