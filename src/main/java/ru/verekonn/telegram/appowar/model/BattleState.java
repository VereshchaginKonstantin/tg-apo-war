package ru.verekonn.telegram.appowar.model;

public enum BattleState {
    DRAW(true),
    END(true),
    START(false);

    private boolean isFinal;

    BattleState(boolean isFinal) {
        this.isFinal = isFinal;
    }
    public boolean IsFinal() {
        return isFinal;
    }
}
