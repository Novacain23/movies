package com.example.scheduleservice.scheduleservice.model;

import javax.persistence.Entity;
import javax.persistence.Enumerated;


public enum Genre {

    ACTION,
    ADVENTURE,
    ANIMATION,
    BIOGRAPHY,
    COMEDY,
    CRIME,
    DOCUMENTARY,
    DRAMA,
    FAMILY,
    FANTASY,
    FILM_NOIR,
    HISTORY,
    HORROR,
    MUSIC,
    MUSICAL,
    MYSTERY,
    ROMANCE,
    SCI_FI,
    SHORT_FILM,
    SPORT,
    SUPERHERO,
    THRILLER,
    WAR,
    WESTERN;


    public static boolean contains(String s) {
        try {
            Genre.valueOf(s);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
