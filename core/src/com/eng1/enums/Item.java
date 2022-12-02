package com.eng1.enums;

public enum Item {
    TEST_0,
    TEST_1,
    TEST_2,
    TEST_3,
    TEST_4,
    TEST_5;


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
            case TEST_4:
                return "Test 4";
            case TEST_5:
                return "Test 5";
            default:
                return null;
        }
    }
}
