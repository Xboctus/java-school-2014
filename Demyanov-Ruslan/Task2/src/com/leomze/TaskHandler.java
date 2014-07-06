package com.leomze;


import java.awt.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class TaskHandler implements Serializable{


    private static final String WRONG_NAME = "Wrong name";
    private static final String USER_EXIST = "User exist";
    private static final String WRONG_TIMEZONE = "Wrong timezone";
    private static final String ACTIVE = "active";
    private static final String INACTIVE = "inactive";
    private static final String WRONG_STATUS = "Wrong status";
    private static final String USER_ADDED = "User Added!";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String CHANGED_SUCCESSFULLY = "Changed user's parameters successfully!";
    private static final String DIDNOT_FIND_TEXT = "Didn't find any text to add Event";
    private static final String EVENT_CREATED = "Event already created";
    private static final String WRONG_DATE = "Wrong Date";
    private static final String EVENT_NOT_FOUND = "event not found";
    private static final String EVENT_REMOVED = "Event removed";
    private static final String EVENT_ADDED = "Event added!";
    private static final String WRONG_DATE_RANGE = "Wrong date range :(";
    private static final String USERS_LIST = "Users list: \n";

    HashMap<String, User> users = new HashMap<>();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public String create(String name, String timeZone, String active) {
        if(name.length() == 0)
            return WRONG_NAME;

        if(users.containsKey(name))
            return USER_EXIST;

        Integer userTimeZone = parseTimeZone(timeZone);
        if (userTimeZone == null)
            return WRONG_TIMEZONE;

        Boolean userActive = parseStatus(active);
        if(userActive == null)
            return WRONG_STATUS;



        User user = new User(name, userTimeZone, userActive);
        users.put(name, user);
        return USER_ADDED;
    }

    public String modify(String name, String timezone, String active) {
        if(!users.containsKey(name))
            return USER_NOT_FOUND;
        User user = users.get(name);

        Integer UserTimeZone = parseTimeZone(timezone);
        if(UserTimeZone == null)
            return WRONG_TIMEZONE;

        Boolean UserAcive = parseStatus(active);
        if(UserAcive == null)
            return WRONG_STATUS;
        user.setActive(UserAcive);
        user.setTimeZone(UserTimeZone);

        return CHANGED_SUCCESSFULLY;

    }

    public String showInfo(String name){
        if (users.containsKey(name)){
            User user = users.get(name);

            StringBuffer sb = new StringBuffer("\nUser name: " + user.getName() + "\n");
            sb.append("User timezone: GMT");
            if (user.getTimeZone() >= 0)
                sb.append("+");
            sb.append(user.getTimeZone() + "\n");
            sb.append("User in active: " + (user.isActive() ? ACTIVE : INACTIVE) + "\n");
            sb.append("User's events: " + user.getUserEvents() + "\n");
            return sb.toString();

        } else {
            return USER_NOT_FOUND;
        }
    }

    public String[] showUserNamesArray(){
       return users.keySet().toArray(new String[0]);

    }

    public String showUsers(){
        String str = new String();
        for(String name: users.keySet()){
            str = "\n" + str + name;
        }
        return USERS_LIST + str;
    }

    public String addEvent(String name, String eventText, String datetime){
        if(!users.containsKey(name))
            return USER_NOT_FOUND;
        User user = users.get(name);

        if(eventText.length() == 0)
            return DIDNOT_FIND_TEXT;
        if(user.getEvent(eventText) != null)
            return EVENT_CREATED;

        Date date = parseDate(datetime);
        if(date == null)
            return WRONG_DATE;

        user.addEvent(date, eventText);
        return EVENT_ADDED;

    }

    public String addRandomEvent(String name, String text, String dateFrom, String dateTo){
        if(!users.containsKey(name))
            return USER_NOT_FOUND;
        User user = users.get(name);

        if(text.length() == 0)
            return DIDNOT_FIND_TEXT;
        if(user.getEvent(text) != null)
            return EVENT_CREATED;

        Date UserDateFrom = parseDate(dateFrom);
        if(UserDateFrom == null)
            return WRONG_DATE;
        Date UserDateTo = parseDate(dateTo);
        if(UserDateTo == null)
            return WRONG_DATE;

        long diff = (UserDateFrom.getTime() - UserDateTo.getTime()) / 1000;
        if(diff <= 0){
            long randomLong = Math.abs((new Random()).nextLong());
            randomLong = Math.abs(randomLong);
            double coef = (double)diff/Long.MAX_VALUE;
            long datetime  = (long) (UserDateFrom.getTime() + randomLong * coef * 1000);
            if(users.containsKey(name)){
                users.get(name).addEvent(new Date(datetime), text);
                return EVENT_ADDED;
            }else {
                return USER_NOT_FOUND;
            }
        } else {
            return WRONG_DATE_RANGE;
        }
    }

    public String removeEvent(String name, String text){
        if(users.containsKey(name)){
            if(users.get(name).getUserEvents() != null){
               users.get(name).removeEvent(text);
               return EVENT_REMOVED;
            } else {
                return EVENT_NOT_FOUND;
            }
        }else {
            return USER_NOT_FOUND;
        }
    }

    public String cloneEvent(String name, String text, String nameTo) {
        if(users.containsKey(name) & users.containsKey(nameTo)){
            User user = users.get(name);
            User userTo = users.get(nameTo);
            if(text.length() == 0)
                return DIDNOT_FIND_TEXT;

            if(user.getEvent(text) != null){
                Event event = user.getEvent(text);
                userTo.addEvent(event.clone());
                return EVENT_ADDED;
            }else{
                return EVENT_NOT_FOUND;
            }

        }else {
            return USER_NOT_FOUND;
        }

    }

    private Integer parseTimeZone(String timeZone) {
        try {
            if (!timeZone.startsWith("GMT+") && !timeZone.startsWith("GMT-"))
                return null;

            int parseTimeZone = Integer.parseInt(timeZone.substring(3));

            if (parseTimeZone < -12 || parseTimeZone > 14)
                return null;

            return parseTimeZone;
        }
        catch (NumberFormatException exeption) {
            return null;
        }
    }

    private Date parseDate(String string) {
        try {
            return simpleDateFormat.parse(string);
        } catch (ParseException e) {
            return null;
        }
    }

    private Boolean parseStatus(String string) {
        if (string.equals(ACTIVE))
            return true;
        else if (string.equals(INACTIVE))
            return false;

        return null;
    }



}
