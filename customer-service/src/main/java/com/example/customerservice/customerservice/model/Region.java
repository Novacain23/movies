package com.example.customerservice.customerservice.model;

public enum Region {

//    BR("Brazil"),
//    EUNE("Europe Nordic & East"),
//    EUW("Europe West"),
//    LAN("Latin America North"),
//    LAS("Latin America South"),
//    NA("North America"),
//    OCE("Oceania"),
//    RU("Russia"),
//    TR("Turkey"),
//    JP("Japan"),
//    SEA("South East Asia");

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
