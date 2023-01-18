package com.eng1.enums;

public enum Item {
    TOPBUN,
    BOTTOMBUN,
    PATTY,
    CHEESE,
    LETTUCE,
    TOMATO,
    ONION;


    @Override
    public String toString() {
        switch (this) {
            case TOPBUN:
                return "Top Bun";
            case BOTTOMBUN:
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
