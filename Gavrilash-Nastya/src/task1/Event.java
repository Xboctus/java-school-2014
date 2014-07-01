package task1;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimerTask;

public class Event extends TimerTask implements Cloneable, Comparable<Event> {
	private GregorianCalendar date;
	private String text;
	private GregorianCalendar innerSisdate;
	private int currentOfFsetInHours;
	private String userName;

	public Event(String userName, GregorianCalendar date, String text,
			int txOffset) {
		this.userName = userName;
		this.date = date;
		this.text = text;
		this.currentOfFsetInHours = txOffset;
		long sisOffset = new GregorianCalendar().getTimeZone().getRawOffset();
		long userOffsett = txOffset * 1000 * 60 * 60;
		this.innerSisdate = new GregorianCalendar();
		innerSisdate.setTimeInMillis(sisOffset - userOffsett
				+ date.getTimeInMillis());
	}

	public GregorianCalendar getDate() {
		return date;
	}

	public String getText() {
		return text;
	}

	public void correctInnerTime(int newOffsetinHours) {
		this.currentOfFsetInHours = newOffsetinHours;
		long sisOffset = new GregorianCalendar().getTimeZone().getRawOffset();
		long userOffsett = newOffsetinHours * 1000 * 60 * 60;
		this.innerSisdate = new GregorianCalendar();
		innerSisdate.setTimeInMillis(sisOffset - userOffsett
				+ date.getTimeInMillis());
	}

	@Override
	public Object clone() {
		return new Event(userName, (GregorianCalendar) date.clone(),
				new String(text), currentOfFsetInHours);
	}

	@Override
	public int compareTo(Event o) {
		return date.compareTo(o.date);
	}

	@Override
	public void run() {
		SimpleDateFormat fm = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
		System.out.println(userName + ": " + fm.format(innerSisdate.getTime())
				+ " " + text);
	}

	public GregorianCalendar getInnerSisdate() {
		return innerSisdate;
	}

	public void setInnerSisdate(GregorianCalendar innerSisdate) {
		this.innerSisdate = innerSisdate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
