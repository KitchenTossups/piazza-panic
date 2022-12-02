package com.eng1.enums;

public enum Product {
    TEST_0,
    TEST_1,
    TEST_2,
    TEST_3;

    @Override
    public String toString() {
        switch (this) {
            case TEST_0:
                return "Test 0";
            case TEST_1:
                return "Test 1";
            case TEST_2:
                return "Test 2";
            case TEST_3:
                return "Test 3";
            default:
                return null;
        }
    }
}
