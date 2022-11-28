package com.eng1;

public enum StationType {
    FOOD_CHEST,
    BIN,
    CHOPPING,
    PREP,
    COUNTER,
    SERVING;

    @Override
    public String toString() {
        switch (this) {
            case FOOD_CHEST:
                return "Food Chest";
            case BIN:
                return "Bin";
            case CHOPPING:
                return "Chopping Station";
            case PREP:
                return "Preparation Station";
            case COUNTER:
                return "Counter Top";
            case SERVING:
                return "Serving Station";
            default:
                return null;
        }
    }
}
