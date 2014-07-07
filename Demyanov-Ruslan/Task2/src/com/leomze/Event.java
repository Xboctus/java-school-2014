package com.leomze;


import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable{
       private Date date;
       private String text;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Event(Date date, String text) {

        this.date = date;
        this.text = text;
    }

    public Event clone(){
        return new Event(date, text);
    }


}


