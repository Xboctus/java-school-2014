package com.leomze;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StartSchedule {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");


    public void start(ConcurrentHashMap<String, User> users) {

        Calendar calendar = new GregorianCalendar();
        int offset = calendar.get(Calendar.ZONE_OFFSET);
        Timer timerEvent = new Timer();

        for(final User user : users.values()){
            if(!user.isActive())
                continue;

            for(final Event event : user.getEvents()){
                final Date date = dateFromGMT(event.getDate(), offset);
                TimerTask taskEvent = new TimerTask() {
                    @Override
                    public void run() {
                        TaskerView.textArea.append("\n=====================\n"
                                + dateFormat.format(date) + "\n" + user.getName() + "\n" + event.getText() +
                                                   "\n=====================\n");
                        System.out.println(dateFormat.format(date) + "\n" + user.getName() + "\n" + event.getText());
                        user.removeEvent(event.getText());
                    }
                };
                timerEvent.schedule(taskEvent, date);
            }
        }



    }


    private Date dateFromGMT(Date date, int offset) {
        return new Date(date.getTime() + offset);
    }
}








