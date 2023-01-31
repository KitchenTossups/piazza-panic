package com.eng1.enums;

public enum Item {
    TOP_BUN,
    BOTTOM_BUN,
    PATTY,
    CHEESE,
    LETTUCE,
    TOMATO,
    ONION;


    @Override
    public String toString() {
        switch (this) {
            case TOP_BUN:
                return "Top Bun";
            case BOTTOM_BUN:
                return "Bottom Bun";
            case PATTY:
                return "Patty";
            case CHEESE:
                return "Cheese";
            case LETTUCE:
                return "Lettuce";
            case TOMATO:
                return "Tomato";
            case ONION:
                return "Onion";
            default:
                return null;
        }
    }
}
