package com.javaschool2014.task1;

import java.util.Calendar;

public class Event implements Cloneable {

    private String text;
    private Calendar calendar;

    public Event (String text, Calendar calendar) {

        this.text = text;
        this.calendar = calendar;

    }

    public Calendar getDate() {
        return calendar;
    }

    public String getText() {
        return text;
    }

}