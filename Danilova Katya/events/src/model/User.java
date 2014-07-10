package model;

import java.util.*;

import static model.User.UserInfo.Status;

public class User {

    private final Map<String, Event> text2event = new HashMap<>();
    private final UserInfo userInfo;

    public User(String name, TimeZone timeZone, Status status) {
        userInfo = new UserInfo(name, timeZone, status);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void addEvent(Event event) {
        text2event.put(event.getText(), event);
    }

    public void removeEvent(String text) {
        text2event.remove(text);
    }

    public Collection<Event> getAllEvent() {
        return text2event.values();
    }

    public Event getEvent(String text) {
        return text2event.get(text);
    }

    public void addRandomTimeEvent(String text, Date from, Date to, String name) {
        long t1 = from.getTime();
        long t2 = to.getTime();
        long t = (new Random().nextLong()) % (t2 - t1) + t1;

        text2event.put(text, new Event(new Date(t), text, name));
    }

    public void modify(TimeZone timeZone, Status status) {
        userInfo.setTimeZone(timeZone);
        userInfo.setStatus(status);
    }

    @Override
    public String toString() {
        return "User{" +
                "text2event=" + text2event +
                ", userInfo=" + userInfo +
                '}';
    }

    public static class UserInfo {

        private final String name;
        private TimeZone timeZone;
        private Status status;

        private UserInfo(String name, TimeZone timeZone, Status status) {
            this.name = name;
            this.timeZone = timeZone;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public TimeZone getTimeZone() {
            return timeZone;
        }

        public Status getStatus() {
            return status;
        }

        public void setTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public static enum Status {
            ACTIVE, PASSIVE
        }

    }

}
