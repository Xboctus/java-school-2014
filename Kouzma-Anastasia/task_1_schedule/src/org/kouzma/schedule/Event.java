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
	
	public Event(String eventText, Date eventDate) {
		text = eventText;
		date = eventDate;
	}
	
	public String getText() {
		return text;
	}

	public Date getDate() {
		return date;
	}
	
	public Event clone() {
		return new Event(text, date);
	}

	@Override
	public int compareTo(Event e) {
		return date.compareTo(e.getDate());
	}
}
