package com.example.customerservice.customerservice.model;

public enum ContactOption {

    SMS,
    EMAIL,
    SNS;

    public static boolean contains(String s) {
        try {
            ContactOption.valueOf(s);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
