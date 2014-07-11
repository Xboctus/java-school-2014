package org.kouzma.schedule;

import java.io.Serializable;
import java.util.Date;
import java.util.TreeSet;

import org.kouzma.schedule.util.StateType;


/**
 * 
 * @author Anastasya Kouzma
 *
 */
public class User implements Serializable {	
	private String name;
	private int timeZone;
	private boolean status;
	private TreeSet<Event> lstEvents = new TreeSet<Event>();
	private StateType state;
	private int idUser = -1;
	
	public User(String userName, int zone, boolean userStatus) {
		name = userName;
		timeZone = zone;
		status = userStatus;
		state = StateType.NEW;
	}
	
	public void modify(int zone, boolean userStatus) {
		timeZone = zone;
		status = userStatus;
		if (!state.equals(StateType.NEW))
			state = StateType.MODIFIED;
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
	
	public Event AddEvent(String text, Date eventDate) {
		Event newEvent = new Event(text, toGMT(eventDate), this);
		lstEvents.add(newEvent);
		return newEvent;
	} 	

	public void RemoveEvent(Event event) {
		lstEvents.remove(event);
		
		if (!event.getState().equals(StateType.NEW))
			Event.getLstRemove().add(event.getId());
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
	
	public StateType getState() {
		return state;
	}

	public void setState(StateType state) {
		this.state = state;
	}

	public void setId(int id) {
		idUser = id;
	}
	
	public int getId() {
		return idUser;
	}
}
