package org.kouzma.schedule;

import java.util.Date;
import java.util.TreeSet;

/**
 * 
 * @author Anastasya Kouzma
 *
 */
public class User {	
	private String name;
	private int timeZone;
	private boolean status;
	private TreeSet<Event> lstEvents = new TreeSet<Event>();
	
	public User(String userName, int zone, boolean userStatus) {
		name = userName;
		timeZone = zone;
		status = userStatus;
	}
	
	public void modify(int zone, boolean userStatus) {
		timeZone = zone;
		status = userStatus;
	}

	public String getName() {
		return name;
	}
	
	public int getTimeZone() {
		return timeZone;
	}

	public boolean getStatus() {
		return status;
	}
	
	public TreeSet<Event> getEvents() {
		return lstEvents;
	}
	
	public void AddEvent(Event event) {
		lstEvents.add(event);
	}	
	
	public void AddEvent(String text, Date eventDate) {
		lstEvents.add(new Event(text, toGMT(eventDate)));
	} 	

	public boolean RemoveEvent(String text) {
		for (Event event : lstEvents) {
			if (event.getText().equals(text)) {
				lstEvents.remove(event);
				return true;
			}
		}
		return false;
	}
	
	public Event findEvent(String text) {
		for (Event event : lstEvents) {
			if (event.getText().equals(text))
				return event;
		}
		return null;
	}

	public Date toGMT(Date eventDate) {
		return new Date(eventDate.getTime() - timeZone * 60 * 60 * 1000);
	}
}
