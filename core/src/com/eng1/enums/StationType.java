package com.eng1.enums;

public enum StationType {
    BIN,
    CHOPPING,
    COUNTER,
    FOOD_CHEST,
    GRILL,
    PREP,
    SERVING;

    @Override
    public String toString() {
        switch (this) {
            case BIN:
                return "Bin";
            case CHOPPING:
                return "Chopping Station";
            case COUNTER:
                return "Counter Top";
            case FOOD_CHEST:
                return "Food Chest";
            case GRILL:
                return "Grill";
            case PREP:
                return "Preparation Station";
            case SERVING:
                return "Serving Station";
            default:
                return null;
        }
    }
}
