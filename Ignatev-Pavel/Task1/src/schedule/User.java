package schedule;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

final class User implements Iterable<Event>,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final HashMap<String,Event> events = new HashMap();
	private final String name;
	private TimeZone timeZone = TimeZone.getDefault();	
	private boolean actFlag = false;
	
	User(String name){
		this.name =name;
	}
	
	
	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public String getName() {
		return name;
	}

	
	public Event getEvent(String text){
		return events.get(text);
	}
	
	public boolean addEvent(Date date,String text){
		if (events.containsKey(text)) {return false;}
		EventElement element = new EventElement(date,text);
		events.put(text, new Event(element));
		return true;
		
	}
	
	public boolean addEvent(Event event){
		if (events.containsKey(event.getElement().getText())) {return false;}
		events.put(event.getElement().getText(), event);
		return true;
		
	}
	
	public boolean removeEvent(String text){
		if (!events.containsKey(text)){return false;}
		events.remove(text);
		return true;
		
	}
	public boolean isActive(){
		return actFlag;
	}
	public void activate(){
		actFlag = true;
	}
	
	public void deactivate(){
		actFlag = false;
	}
	
	
	@Override
	public Iterator<Event> iterator() {
       Iterator<Event> it = new Iterator() {

    	   private Iterator it = events.entrySet().iterator();
    	   
           @Override
           public boolean hasNext() {
				
               return it.hasNext();
           }

           @Override
           public Event next() {
        	   Map.Entry pairs = (Map.Entry)it.next();
        	   Event event = (Event)pairs.getValue();
        	   return event;
				
           }

           @Override
           public void remove() {
               // TODO Auto-generated method stub
           }
       };
       return it;
   }
	
	
	
	@Override
	public String toString(){
		Iterator<Event> it = this.iterator();
		String out =  name + " "
				+ timeZone.getID() + " "
				+ ((actFlag) ? MessagesAndRegularExpressions.activeName : MessagesAndRegularExpressions.passiveName) + (it.hasNext() ? "\n":"");
		
		while (it.hasNext()) {
			
			Event event = it.next();
			out = out + event.getElement().getText() + " " + event.getElement().getDate().toString() + (it.hasNext() ? "\n":"");
		}
		
		return out;
		
	}
	
	
}
