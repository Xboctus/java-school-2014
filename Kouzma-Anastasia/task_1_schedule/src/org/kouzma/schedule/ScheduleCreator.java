package org.kouzma.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;

import org.kouzma.schedule.gui.SheduleWindow.ScheduleCallBack;


public class ScheduleCreator {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");

	private int offset = (new GregorianCalendar()).get(Calendar.ZONE_OFFSET);
	
	private static final String ACTIVE = "active";
	private static final String PASSIVE = "passive";
		
	private static final String ERROR_WRONG_USER_NAME = "Wrong user name";
	private static final String ERROR_NAME_EXISTS = "Such user name already exists";
	private static final String ERROR_USER_NOT_FOUND = "User is not found";
	
	private static final String ERROR_WRONG_EVENT = "Wrong event description";
	private static final String ERROR_EVENT_EXISTS = "Such user event already exists";
	private static final String ERROR_EVENT_NOT_FOUND = "Event is not found";
	
	private static final String ERROR_WRONG_TIMEZONE = "Wrong timezone";
	private static final String ERROR_WRONG_STATUS = "Wrong status";
	private static final String ERROR_WRONG_DATE = "Wrong date format";
	private static final String ERROR_WRONG_DATE_RANGE = "Wrong date range";
	private static final String ERROR_SHOWING = "Events showing is already running";

	private static final String USER_ADDED = "User \"?\" added";
	private static final String USER_MODIFIED = "User \"?\" modified";
	private static final String EVENT_ADDED = "Event \"?\" added";
	private static final String EVENT_REMOVED = "Event \"?\" removed";
	
	private HashMap<String, User> lstUsers = new HashMap<String, User>();
	private TreeSet<Event> treeEvent = new TreeSet<Event>();

	private ScheduleCallBack callBack;

	private Timer scheduleTimer;
	private Event nextEvent;
	private class ScheduleTimerTask extends TimerTask {

		@Override
		public void run() {
			StringBuffer message = new StringBuffer();
			
			Iterator<Event> eventIterator = treeEvent.tailSet(nextEvent, true).iterator();
			while (eventIterator.hasNext()) {
				Date eventDate = nextEvent.getDate();
				nextEvent = eventIterator.next();
				if (eventDate.equals(nextEvent.getDate())) {
					User user = nextEvent.getUser();
					if (user.getStatus()) {
						message.append(dateFormat.format(eventDate) + " : " + 
								user.getName() + " \"" + nextEvent.getText() + "\"\n");
					} 
				}
				else {
					scheduleTimer.schedule(new ScheduleTimerTask(), fromGTM(nextEvent.getDate()));
					break;
				}
			}
								
			if (callBack != null)
				callBack.sendMessage(message.toString());
			else
				System.out.print(message);
		}
	};
	
	
	public ScheduleCreator(ScheduleCallBack scheduleCallBack) {
		callBack = scheduleCallBack;
	}

	public ScheduleCreator() {}

	public String createUser(String name, String zone, String status) {		
		if (name.length() == 0)
			return ERROR_WRONG_USER_NAME;
		if (lstUsers.containsKey(name))
			return ERROR_NAME_EXISTS;
		
		Integer timeZone = parseTimeZone(zone);
		if (timeZone == null)
			return ERROR_WRONG_TIMEZONE;

		Boolean isActive = parseStatus(status);
		if (isActive == null)
			return ERROR_WRONG_STATUS;
		
		User newUser = new User(name, timeZone, isActive);
		lstUsers.put(name, newUser);
		return USER_ADDED.replace("?", name);
	}


	public String ModifyUser(String name, String zone, String status) {
		if (!lstUsers.containsKey(name))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(name);
		
		Integer timeZone = parseTimeZone(zone);
		if (timeZone == null)
			return ERROR_WRONG_TIMEZONE;

		Boolean isActive = parseStatus(status);
		if (isActive == null)
			return ERROR_WRONG_STATUS;
		
		user.modify(timeZone, isActive);
		return USER_MODIFIED.replace("?", name);
	}

	public String AddEvent(String name, String eventText, String eventDate) {
		if (!lstUsers.containsKey(name))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(name);
		
		if (eventText.length() == 0)
			return ERROR_WRONG_EVENT;
		if (user.findEvent(eventText) != null)
			return ERROR_EVENT_EXISTS;
		
		Date date = parseDate(eventDate);
		if (eventDate == null)
			return ERROR_WRONG_DATE;
		
		Event newEvent = user.AddEvent(eventText, date);
		treeEvent.add(newEvent);
		if (nextEvent == null || newEvent.compareTo(nextEvent) < 0) {
			nextEvent = newEvent;
			scheduleTimer.schedule(new ScheduleTimerTask(), fromGTM(nextEvent.getDate()));
		}
		
		return EVENT_ADDED.replace("?", eventText);
	} 

	public String RemoveEvent(String name, String eventText) {
		if (!lstUsers.containsKey(name))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(name);
		
		Event event = user.findEvent(eventText);
		if (event == null)
			return ERROR_EVENT_NOT_FOUND;
		
		user.RemoveEvent(event);
		treeEvent.remove(event);
		
		return EVENT_REMOVED.replace("?", eventText);
	}

	public String AddRandomTimeEvent(String name, String eventText, String eventDateFrom, String eventDateTo) {
		if (!lstUsers.containsKey(name))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(name);

		if (eventText.length() == 0)
			return ERROR_WRONG_EVENT;
		if (user.findEvent(eventText) != null)
			return ERROR_EVENT_EXISTS;

		Date fromDate = parseDate(eventDateFrom);
		if (fromDate == null)
			return ERROR_WRONG_DATE;
		Date toDate = parseDate(eventDateTo);
		if (toDate == null)
			return ERROR_WRONG_DATE;
		
		Date randomDate = computeRandomDate(fromDate, toDate);
		if (randomDate == null)
			return ERROR_WRONG_DATE_RANGE;

		Event newEvent = user.AddEvent(eventText, randomDate);
		treeEvent.add(newEvent);

		if (nextEvent == null || newEvent.compareTo(nextEvent) < 0) {
			nextEvent = newEvent;
			scheduleTimer.schedule(new ScheduleTimerTask(), fromGTM(nextEvent.getDate()));
		}
		
		return EVENT_ADDED.replace("?", eventText);
	}

	public String CloneEvent(String name, String eventText, String nameTo) {
		if (!lstUsers.containsKey(name))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(name);

		if (!lstUsers.containsKey(nameTo))
			return ERROR_USER_NOT_FOUND;
		User userTo = lstUsers.get(nameTo);

		if (eventText.length() == 0)
			return ERROR_WRONG_EVENT;
		
		Event event = user.findEvent(eventText);
		if (event == null)
			return ERROR_EVENT_NOT_FOUND;

		if (userTo.findEvent(eventText) != null)
			return ERROR_EVENT_EXISTS;
		
		Event cloneEvent = event.clone(userTo);
		userTo.AddEvent(cloneEvent);
		
		return EVENT_ADDED.replace("?", eventText);
	}

	public String ShowInfo(String name) {
		if (!lstUsers.containsKey(name))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(name);

		StringBuffer answer = new StringBuffer(user.getName());
		answer.append("(");
		answer.append("GMT");
		if (user.getTimeZone() >=0)
			answer.append("+");
		answer.append(user.getTimeZone());
		answer.append(") - ");
		answer.append(user.getStatus() ? ACTIVE : PASSIVE);
		
		for (Event event : user.getEvents()) {
			answer.append("\n" + dateFormat.format(fromGTM(event.getDate())) + " \"" + event.getText() + "\"");
		}	
		return answer.toString();
	}

	public String StartScheduling() {
		if (scheduleTimer != null)
			return ERROR_SHOWING;
		
		scheduleTimer = new Timer();
		if (treeEvent.isEmpty())
			nextEvent = null;
		else {
			nextEvent = treeEvent.first();
			scheduleTimer.schedule(new ScheduleTimerTask(), fromGTM(nextEvent.getDate()));
		}
		
		return null;
	}
	
	private Integer parseTimeZone(String string) {
		try {
			if (!string.startsWith("GMT+") && !string.startsWith("GMT-"))
				return null;
			
			int zone = Integer.parseInt(string.substring(3));
			
			if (zone < -12 || zone > 14)
				return null;
			
			return zone;
		}
		catch (NumberFormatException exeption) {
			return null;
		}
	}

	private Date parseDate(String string) {
		try {
			return dateFormat.parse(string);
		} catch (ParseException e) {
			return null;
		}
	}	
	
	private Boolean parseStatus(String string) {
		if (string.equals(ACTIVE))
			return true;
		else if (string.equals(PASSIVE))
			return false;
		
		return null;
	}

	private Date computeRandomDate(Date fromDate, Date toDate) {
		long diff = (toDate.getTime() - fromDate.getTime()) / 1000; // в секундах
		if (diff <= 0)
			return null;
				
		double coefficient = new Random().nextDouble();
		long randomTime = (long) (fromDate.getTime() + diff * coefficient * 1000);
		
		return new Date(randomTime);
	}
	
	private Date fromGTM(Date date) {
		return new Date(date.getTime() + offset);
	}

	public String [] getUser(String name) {
		if (!lstUsers.containsKey(name))
			return null;

		User user = lstUsers.get(name);
		String[] res = new String[2*user.getEvents().size() + 3];
		
		res[0] = user.getName();
		
		StringBuffer strGmt = new StringBuffer();
		strGmt.append("GMT");
		if (user.getTimeZone() >=0)
			strGmt.append("+");
		strGmt.append(user.getTimeZone());
		
		res[1] = strGmt.toString();
		res[2] = user.getStatus() ? ACTIVE : PASSIVE;
		
		int i = 0;
		for (Event event : user.getEvents()) {
			res[3 + 2 * i] = dateFormat.format(fromGTM(event.getDate()));
			res[4 + 2 * i] = event.getText();
			i++;
		}
		return res;
	}
}