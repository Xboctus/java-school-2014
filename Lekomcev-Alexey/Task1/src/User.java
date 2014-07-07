import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class User implements Serializable{
    String name;
    Boolean status;
    TimeZone tz;
    ArrayList<Event> events = new ArrayList<Event>();

    User(){
    }

    User(String p_name, String p_tz, String p_status){
        name = p_name;
        tz = TimeZone.getTimeZone(p_tz);
        status = new Boolean(p_status);
    }

    User(String params) throws InputFormatException{
        try {
            String nameValue = params.substring(params.indexOf('(') + 1, params.indexOf(','));
            name = nameValue;
            String tzID = params.substring(params.indexOf(',') + 1, params.lastIndexOf(','));
            tz = TimeZone.getTimeZone(tzID);
            String statusValue = params.substring(params.lastIndexOf(',') + 1, params.indexOf(')'));
            status = new Boolean(statusValue);
        }
        catch (Exception e){
            throw new InputFormatException();
        }
    }

    public static User getUserInstance(String p_name){
        User newUser = new User();
        newUser.name = p_name;
        return newUser;
    }

    public static Event getUserEvent(String p_text, User p_user){
        if (p_user.events.isEmpty()){return null;}
        Comparator<Event> comp = new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.description.compareTo(o2.description);
            }
        };
        Collections.sort(p_user.events, comp);
        int pos = Collections.binarySearch(p_user.events, new Event(p_text), comp);
        if (pos < 0){
            return null;
        }
        return p_user.events.get(pos);
    }

    public void addEvent(String params, Coordinator p_coordinator){
        try {
            String nameValue = params.substring(params.indexOf('(') + 1, params.indexOf(','));
            User user = p_coordinator.getUser(nameValue);
            if (user == null){
                System.out.println("User not found");
                throw new InputFormatException();
            }
            String textValue = params.substring(params.indexOf(',') + 1, params.lastIndexOf(','));
            Event event = getUserEvent(textValue, user);
            if (event != null){
                System.out.println("Event already exists");
                throw new InputFormatException();
            }
            String input = params.substring(params.lastIndexOf(',') + 1, params.indexOf(')'));
            SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yy:HH:mm:ss");
            Date date = ft.parse(input);
            events.add(new Event(date, textValue));
            System.out.println("Done");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public boolean addEvent(String p_description, String p_date){
        Event event = getUserEvent(p_description, this);
        if (event != null){
            return false;
        }
        try {
            event = new Event(p_description, p_date);
            events.add(event);
            Sheduler.getSortingEvents().add(this, event.date, event.description);
            return true;
        }
        catch (ParseException pe){
            return false;
        }
    }

    public void addRandomTimeEvent(String params, Coordinator p_coordinator){
        try {
            String nameValue = params.substring(params.indexOf('(') + 1, params.indexOf(','));
            User user = p_coordinator.getUser(nameValue);
            if (user == null){
                System.out.println("User not found");
                throw new InputFormatException();
            }
            String textValue = params.substring(params.indexOf(',') + 1, params.indexOf(',', params.indexOf(',') + 1));
            Event event = getUserEvent(textValue, user);
            if (event != null){
                System.out.println("Event already exists");
                throw new InputFormatException();
            }
            String substr = params.substring(params.indexOf(',') + 1);
            String inputFrom = substr.substring(substr.indexOf(',') + 1, substr.lastIndexOf(','));
            String inputTo = substr.substring(substr.lastIndexOf(',') + 1, substr.indexOf(')'));
            SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yy:HH:mm:ss");

            Date dateFrom = ft.parse(inputFrom);
            Date dateTo = ft.parse(inputTo);

            GregorianCalendar calendarFrom = new GregorianCalendar();
            GregorianCalendar calendarTo = new GregorianCalendar();
            GregorianCalendar randomCalendar = new GregorianCalendar();
            calendarFrom.setTime(dateFrom);
            calendarTo.setTime(dateTo);
            int year = randBetween(calendarFrom.get(calendarFrom.YEAR), calendarTo.get(calendarTo.YEAR));
            int dayOfYear = randBetween(1, calendarFrom.getActualMaximum(calendarFrom.DAY_OF_YEAR));
            randomCalendar.set(Calendar.YEAR, year);
            randomCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
            Date randomDate = randomCalendar.getTime();

            events.add(new Event(randomDate, textValue));
            System.out.println("Done");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void changeEventDate(String date, String description){
        if (events.contains(new Event(description))){
            Event event = events.get(events.indexOf(new Event(description)));
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy:HH:mm:ss");
            try{
                event.date = sdf.parse(date);
            }
            catch (ParseException pe){}
        }
    }

    public boolean equals(Object otherObject){
        if (this == otherObject) return true;

        if (otherObject == null) return false;

        if (getClass() != otherObject.getClass())
            return false;

        User other = (User) otherObject;

        return name.equals(other.name);
    }

    public static int randBetween(int start, int end){
        return start + (int)Math.round(Math.random() * (end - start));
    }

    public void removeEvent(String params, Coordinator p_coordinator){
        try{
            String nameValue = params.substring(params.indexOf('(') + 1, params.indexOf(','));
            User user = p_coordinator.getUser(nameValue);
            if (user == null){
                System.out.println("User not found");
                throw new InputFormatException();
            }
            String textValue = params.substring(params.indexOf(',') + 1, params.indexOf(')'));
            Event event = getUserEvent(textValue, user);
            if (event == null){
                System.out.println("Event not found");
                throw new InputFormatException();
            }
            events.remove(event);
            System.out.println("Done");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void showInfo(String params, Coordinator p_coordinator){
        try{
            String nameValue = params.substring(params.indexOf('(') + 1, params.indexOf(')'));
            User user = p_coordinator.getUser(nameValue);
            if (user == null){
                System.out.println("User not found");
                throw new InputFormatException();
            }
            System.out.println(nameValue + " " + user.tz.getID() + " " + user.status);
            Comparator<Event> comp = new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    return o1.date.compareTo(o2.date);
                }
            };
            if (events == null){return;}
            Collections.sort(user.events, comp);
            for(Event e : events){
                System.out.println(e.description + " " + e.date);
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

}

class InputFormatException extends Exception{
    public InputFormatException(){}
    public InputFormatException(String err){
        super(err);
    }
}