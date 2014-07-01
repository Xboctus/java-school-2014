import java.util.Date;


public class Event implements Cloneable{
    Date date;
    String description;

    Event(Date p_date, String p_description){
        date = p_date;
        description = p_description;
    }

    Event(String p_description){
        description = p_description;
    }

    public Event clone() throws CloneNotSupportedException{
        Event cloned = (Event) super.clone();

        cloned.date = (Date) date.clone();
        return cloned;
    }
}
