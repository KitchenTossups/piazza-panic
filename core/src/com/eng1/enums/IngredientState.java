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
    OVERCOOKED
}
