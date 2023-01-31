package com.eng1.enums;

public enum Difficulty {
    EASY,
    MEDIUM,
    HARD;

    @Override
    public String toString() {
        switch (this) {
            case EASY:
                return "Easy";
            case MEDIUM:
                return "Medium";
            case HARD:
                return "Hard";
            default:
                return "Error getting string of Difficulty";
        }
    }
}
