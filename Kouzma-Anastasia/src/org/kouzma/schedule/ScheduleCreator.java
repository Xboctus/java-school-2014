package org.kouzma.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;

public class ScheduleCreator {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");

	private final String ACTIVE = "active";
	private final String PASSIVE = "passive";
	
		
	private final String ERROR_WRONG_USER_NAME = "Wrong user name";
	private final String ERROR_NAME_EXISTS = "Such user name already exists";
	private final String ERROR_USER_NOT_FOUND = "User is not found";
	
	private final String ERROR_WRONG_EVENT = "Wrong event description";
	private final String ERROR_EVENT_EXISTS = "Such user event already exists";
	private final String ERROR_EVENT_NOT_FOUND = "Event is not found";
	
	private final String ERROR_WRONG_TIMEZONE = "Wrong timezone";
	private final String ERROR_WRONG_STATUS = "Wrong status";
	private final String ERROR_WRONG_DATE = "Wrong date format";
	private final String ERROR_WRONG_DATE_RANGE = "Wrong date range";
	
	private final String SUCCESS = "Command complete";
	
	private HashMap<String, User> lstUsers = new HashMap<String, User>();

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
		return SUCCESS;
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
		return SUCCESS;
	}

	/**
	 *  добавление события для пользователя. Текст должен быть 
	 *  уникальным, формат даты – DD.MM.YYYY-HH24:Mi:SS
	 */
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
		
		user.AddEvent(eventText, date);
		return SUCCESS;
	} 

	public String RemoveEvent(String name, String eventText) {
		if (!lstUsers.containsKey(name))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(name);
		
		if (user.RemoveEvent(eventText))
			return SUCCESS;
		else
			return ERROR_EVENT_NOT_FOUND;
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
		
		user.AddEvent(eventText, randomDate);
		return SUCCESS;
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
		
		userTo.AddEvent(event.clone());
		return SUCCESS;
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

		Calendar cal = new GregorianCalendar();
		int offset = cal.get(Calendar.ZONE_OFFSET);
		
		for (Event event : user.getEvents()) {
			answer.append("\n" + dateFormat.format(fromGTM(event.getDate(), offset)) + " \"" + event.getText() + "\"");
		}	
		return answer.toString();
	}

	public String StartScheduling() {
		(new ShowMode()).show(lstUsers);
		
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
	
	private Date fromGTM(Date date, int offset) {
		return new Date(date.getTime() + offset);
	}
}
