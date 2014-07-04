package business;

import java.util.GregorianCalendar;

public class Event implements Cloneable, Comparable<Event> {
	private GregorianCalendar date;
	private String text;
	private GregorianCalendar innerSisdate;
	private User user;
	private static int commonId = 1;
	private int id;
	private boolean viewed;
	private boolean active;

	/**
	 * Constructor for creating new event. It sets fields user, date of event in
	 * user's time zone and text, count date of event in system time zone and
	 * set fields viewed as false and active as true
	 * 
	 * @param user
	 * @param date
	 * @param text
	 */
	public Event(User user, GregorianCalendar date, String text) {
		this.user = user;
		this.date = date;
		this.text = text;
		long sisOffset = new GregorianCalendar().getTimeZone().getRawOffset();
		long userOffsett = user.getTimeZoneOffset() * 1000 * 60 * 60;
		this.innerSisdate = new GregorianCalendar();
		innerSisdate.setTimeInMillis(sisOffset - userOffsett
				+ date.getTimeInMillis());
		viewed = false;
		active = true;
	}

	/**
	 * This method compares two events as their dates in system time zone. TODO
	 * this method is never used, when "show info" add sorted output
	 */
	@Override
	public int compareTo(Event o) {
		return innerSisdate.compareTo(o.getInnerSisdate());
	}

	/**
	 * Returns deep copy of event
	 */
	@Override
	public Object clone() {
		return new Event(user, (GregorianCalendar) date.clone(), new String(
				text));
	}

	/**
	 * returns date of event in user's time zone
	 * 
	 * @return
	 */
	public GregorianCalendar getDate() {
		return date;
	}

	/**
	 * returns text of event
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * Shifts time of advent of user's events in system time zone Example: user
	 * Vasya has offset +4 and event "Meeting" in 12-00. System has an offset
	 * +4, so "Meeting" will occurs in 12-00. Call of this method with parameter
	 * +6 on event lead to events "Meeting" will occur in 10-00
	 * 
	 * @param timeZoneOffset
	 */
	public void correctInnerTime(int newOffsetinHours) {
		long sisOffset = new GregorianCalendar().getTimeZone().getRawOffset();
		long userOffsett = newOffsetinHours * 1000 * 60 * 60;
		this.innerSisdate = new GregorianCalendar();
		innerSisdate.setTimeInMillis(sisOffset - userOffsett
				+ date.getTimeInMillis());
	}

	/**
	 * returns date of event in system time zone
	 * 
	 * @return
	 */
	public GregorianCalendar getInnerSisdate() {
		return innerSisdate;
	}

	/**
	 * sets date of event in system time zone
	 * 
	 * @param innerSisdate
	 */
	public void setInnerSisdate(GregorianCalendar innerSisdate) {
		this.innerSisdate = innerSisdate;
	}

	/**
	 * return user to which this event is belong
	 * 
	 * @return
	 */
	public User getUser() {
		return user;
	}

	/**
	 * set user to which this event is belong
	 * 
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * get common for all Event class counter of ids
	 * 
	 * @return
	 */
	public static int getCommonId() {
		return commonId;
	}

	/**
	 * increase common id
	 */
	public static void increaseCommonId() {
		commonId = commonId++;
	}

	/**
	 * return event id
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * set event id
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * If this event already were added to timer task, it marked as viewed. This
	 * method show meaning of field viewed
	 * 
	 * @return
	 */
	public boolean isViewed() {
		return viewed;
	}

	/**
	 * Set meaning of field viewed
	 * 
	 * @param viewed
	 */
	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}

	/**
	 * Even if this event were deleted from scheduler inner map, it can be in
	 * timer tasks. If event is inactive, timer task will not notify scheduler
	 * listeners about it.
	 * 
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Set meaning of field active
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

}
