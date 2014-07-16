package org.kouzma.schedule;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Anastasya Kouzma
 */
public class Event implements Comparable<Event>, Serializable {	
	private String text;
	private Date date;
	private User user;
	transient private boolean isShown = false;
	private int idEvent = -1;
	
	public Event(String eventText, Date eventDate, User eventUser) {
		text = eventText;
		date = eventDate;
		user = eventUser;
	}
	
	public Event(int id, String eventText, Date eventDate, User currentUser) {
		idEvent = id;
		text = eventText;
		date = eventDate;
		user = currentUser;
	}

	public String getText() {
		return text;
	}

	public Date getDate() {
		return date;
	}
	public User getUser() {
		return user;
	}
	
	public boolean isShown() {
		return isShown;
	}

	public void setShown(boolean isShown) {
		this.isShown = isShown;
	}
	
	public Event clone(User userTo) {
		return new Event(text, date, userTo);
	}

	@Override
	public int compareTo(Event e) {
		int res = date.compareTo(e.getDate());
		if (res == 0) {
			res = text.compareTo(e.getText());
			if (res == 0) {
				res = user.getName().compareTo(e.getUser().getName());
			}
		}
		return res;
	}	

	public int getId() {
		return idEvent;
	}
	
	public void setId(int id) {
		idEvent = id;
	}
}
