package com.eng1.enums;

public enum Product {
    BURGER,
    CHEESEBURGER,
    SALAD;

    @Override
    public String toString() {
        switch (this) {
            case BURGER:
                return "Burger";
            case CHEESEBURGER:
                return "Cheeseburger";
            case SALAD:
                return "Salad";
            default:
                return null;
        }
    }
}
