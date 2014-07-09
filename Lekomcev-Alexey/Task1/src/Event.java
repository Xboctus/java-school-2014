import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Event implements Cloneable, Serializable{
    Date date;
    String description;

    Event(Date p_date, String p_description){
        date = p_date;
        description = p_description;
    }

    Event(String p_description){
        description = p_description;
    }

    Event(String p_date, String p_description) throws ParseException{
        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yy:HH:mm:ss");
        date = ft.parse(p_date);
        description = p_description;
    }

    public Event clone() throws CloneNotSupportedException{
        Event cloned = (Event) super.clone();

        cloned.date = (Date) date.clone();
        return cloned;
    }

    public Date getDate(){
        return date;
    }

    public String getDescription(){
        return description;
    }

    public boolean equals(Object otherObject){
        if (this == otherObject) return true;

        if (otherObject == null) return false;

        if (getClass() != otherObject.getClass())
            return false;

        Event other = (Event) otherObject;

        return this.description.equals(other.description);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
