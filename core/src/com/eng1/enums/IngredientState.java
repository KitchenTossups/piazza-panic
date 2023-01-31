package com.eng1.enums;

@SuppressWarnings("unused")
public enum IngredientState {
    UNPREPARED,
    PREPARED,
    NOT_APPLICABLE, // This is for ingredients that are not in need of preparation
    UNCUT,
    CUT,
    UNCOOKED,
    HALF_COOKED,
    COOKED,
    OVERCOOKED;

    @Override
    public String toString() {
        switch (this) {
            case UNCOOKED:
                return "Uncooked";
            case UNPREPARED:
                return "Unprepared";
            case UNCUT:
                return "Uncut";
            case CUT:
                return "Cut";
            case COOKED:
                return "Cooked";
            case PREPARED:
                return "Prepared";
            case OVERCOOKED:
                return "Overcooked";
            case HALF_COOKED:
                return "Half Cooked";
            case NOT_APPLICABLE:
                return "Not Applicable";
            default:
                return "Unable to return string of " + this;
        }
    }
}
