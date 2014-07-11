package org.kouzma.schedule;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.kouzma.schedule.util.StateType;

/**
 * 
 * @author Anastasya Kouzma
 *
 */
public class Event implements Comparable<Event>, Serializable {
	private static List<Integer> lstRemoveIds = new LinkedList<Integer>();
	
	private String text;
	private Date date;
	private User user;
	private boolean isShown = false;
	private StateType state;
	private int idUser = -1;
	
	public Event(String eventText, Date eventDate, User eventUser) {
		text = eventText;
		date = eventDate;
		user = eventUser;
		state = StateType.NEW;
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
	
	public StateType getState() {
		return state;
	}

	public void setState(StateType state) {
		this.state = state;
	}

	public int getId() {
		return idUser;
	}
	
	public void setId(int id) {
		idUser = id;
	}

	public static List<Integer> getLstRemove() {
		return lstRemoveIds;
	}
}
