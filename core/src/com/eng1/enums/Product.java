package com.eng1.enums;

import java.util.Random;

public enum Product {
    BURGER,
    CHEESEBURGER,
    SALAD;

    private static final Random RANDOM = new Random();

    public static Product getRandomProduct()  {
        Product[] directions = values();
        return directions[RANDOM.nextInt(directions.length)];
    }

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
