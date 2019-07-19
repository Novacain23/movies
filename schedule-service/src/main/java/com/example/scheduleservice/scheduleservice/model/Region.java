package com.example.scheduleservice.scheduleservice.model;

public enum Region {

    BR,
    EUNE,
    EUW,
    LAN,
    LAS,
    NA,
    OCE,
    RU,
    TR,
    JP,
    SEA;

//    public final String label;
//
//    private Region(String label) {
//        this.label = label;
//    }

    public static boolean contains(String r) {
        try {
            Region.valueOf(r);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
