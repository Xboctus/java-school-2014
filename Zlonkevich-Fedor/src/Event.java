
import java.util.Date;

class Event {
    
    String text;
    Date date;
    
    Event(String text, Date date){
        this.text = text;
        this.date = date;
    }
    
    String getText(){
        return text;
    }
    
    Date getDate(){
        return date;
    }
    
    void show(){
        System.out.println(text + " " + date);
    }
    
}
