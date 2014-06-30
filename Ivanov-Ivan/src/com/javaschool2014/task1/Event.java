package com.javaschool2014.task1;

import java.util.Date;

public class Event implements Cloneable {

    private String text;
    private Date dateTime;

    public Event (String text, Date dateTime) {

        this.text = text;
        this.dateTime = dateTime;

    }

    public Date getDate() {
        return dateTime;
    }

    public String getText() {
        return text;
    }

}