package com.example.notificationservice.notificationservice.model;

public enum ContactOption {

    SMS,
    EMAIL;

    public static boolean contains(String s) {
        try {
            ContactOption.valueOf(s);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
