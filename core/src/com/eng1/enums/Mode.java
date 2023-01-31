package com.eng1.enums;

public enum Mode {
    ASSESSMENT_1,
    ASSESSMENT_2;

    @Override
    public String toString() {
        switch (this) {
            case ASSESSMENT_1:
                return "Assessment 1";
            case ASSESSMENT_2:
                return "Assessment 2";
            default:
                return "Error getting string of Mode";
        }
    }
}
