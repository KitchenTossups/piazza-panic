package com.eng1.enums;

public enum Product {
    BURGER,
    SALAD;

    @Override
    public String toString() {
        switch (this) {
            case BURGER:
                return "Burger";
            case SALAD:
                return "Salad";
            default:
                return null;
        }
    }
}
