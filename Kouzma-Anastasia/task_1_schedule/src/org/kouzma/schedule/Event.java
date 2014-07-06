package org.kouzma.schedule;

import java.util.Date;

/**
 * 
 * @author Anastasya Kouzma
 *
 */
public class Event implements Comparable<Event> {
	private String text;
	private Date date;
	private User user;
	
	public Event(String eventText, Date eventDate, User eventUser) {
		text = eventText;
		date = eventDate;
		user = eventUser;
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
}
