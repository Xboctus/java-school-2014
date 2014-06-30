package org.kouzma.schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Anastasya Kouzma
 *
 */
public class ScheduleController {

	private boolean editShowMode = false; // false - edit; true - show

	private Pattern commandPattern = Pattern.compile("(\\w+)(\\((.*)\\))?");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");

	private final String ACTIVE = "active";
	private final String PASSIVE = "passive";
	
	private final String CREATE = "Create";
	private final String MODIFY = "Modify";
	private final String ADD_EVENT = "AddEvent";
	private final String REMOVE_EVENT = "RemoveEvent";
	private final String ADD_RANDOME_EVENT = "AddRandomTimeEvent";
	private final String CLONE_EVENT = "CloneEvent";
	private final String SHOW_INFO = "ShowInfo";
	private final String START_SCHEDULING = "StartScheduling";


	private final String ERROR_WRONG_COMMAND = "No such command";
	private final String ERROR = "Error";
	
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
	
	private TreeMap<Date, List<String>> treeEvent = new TreeMap<Date, List<String>>();
	
	public void main() {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(System.in)); 
		
		try {
			while (!editShowMode) {
				String command;
					command = br.readLine();
	
				System.out.println(parseCommand(command));
			}
		} catch (IOException e) {
			System.out.println(ERROR);
		}
	}
	
	public String parseCommand(String command) {
		String answer;
		
		Matcher matcher = commandPattern.matcher(command);
		if (matcher.matches()) {
			String commandName = matcher.group(1);
			String[] arrArgs = null;
			
			if (matcher.groupCount() == 3 &&  matcher.group(3) != null) {
				arrArgs = matcher.group(3).split(",");
				for (int i = 0; i < arrArgs.length; i++)
					arrArgs[i] = arrArgs[i].trim();
			}
			
			switch (commandName) {
			case CREATE:
				answer = createUser(arrArgs);
				break;
			case MODIFY:
				answer = ModifyUser(arrArgs);
				break;
			case ADD_EVENT:
				answer = AddEvent(arrArgs);
				break;
			case REMOVE_EVENT:
				answer = RemoveEvent(arrArgs);
				break;
			case ADD_RANDOME_EVENT:
				answer = AddRandomTimeEvent(arrArgs);
				break;
			case CLONE_EVENT:
				answer = CloneEvent(arrArgs);
				break;
			case SHOW_INFO:
				answer = ShowInfo(arrArgs);
				break;
			case START_SCHEDULING:
				answer = StartScheduling();
				break;
			default:
				answer = ERROR_WRONG_COMMAND;
			}
		}
		else {
			answer = ERROR_WRONG_COMMAND;
		}
		
		return answer;
	}


	private String createUser(String[] arrArgs) {
		if (arrArgs == null || arrArgs.length != 3)
			return ERROR_WRONG_COMMAND;
		
		String name = arrArgs[0];
		if (name.length() == 0)
			return ERROR_WRONG_USER_NAME;
		if (lstUsers.containsKey(arrArgs[0]))
			return ERROR_NAME_EXISTS;
		
		Integer timeZone = parseTimeZone(arrArgs[1]);
		if (timeZone == null)
			return ERROR_WRONG_TIMEZONE;

		boolean isActive;
		if (arrArgs[2].equals(ACTIVE))
			isActive = true;
		else if (arrArgs[2].equals(PASSIVE))
			isActive = false;
		else
			return ERROR_WRONG_STATUS;
		
		User newUser = new User(name, timeZone, isActive);
		lstUsers.put(name, newUser);
		return SUCCESS;
	}


	private String ModifyUser(String[] arrArgs) {
		if (arrArgs == null || arrArgs.length != 3)
			return ERROR_WRONG_COMMAND;
		
		if (!lstUsers.containsKey(arrArgs[0]))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(arrArgs[0]);
		
		Integer timeZone = parseTimeZone(arrArgs[1]);
		if (timeZone == null)
			return ERROR_WRONG_TIMEZONE;

		Boolean isActive = parseStatus(arrArgs[2]);
		if (isActive == null)
			return ERROR_WRONG_STATUS;
		
		user.modify(timeZone, isActive);
		return SUCCESS;
	}

	/**
	 *  добавление события для пользователя. Текст должен быть 
	 *  уникальным, формат даты – DD.MM.YYYY-HH24:Mi:SS
	 */
	private String AddEvent(String[] arrArgs) {
		if (arrArgs == null || arrArgs.length != 3)
			return ERROR_WRONG_COMMAND;
		
		if (!lstUsers.containsKey(arrArgs[0]))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(arrArgs[0]);

		String eventText = arrArgs[1];
		if (eventText.length() == 0)
			return ERROR_WRONG_EVENT;
		if (user.findEvent(eventText) != null)
			return ERROR_EVENT_EXISTS;
		
		Date eventDate = parseDate(arrArgs[2]);
		if (eventDate == null)
			return ERROR_WRONG_DATE;
		
		user.AddEvent(eventText, eventDate);
		return SUCCESS;
	} 


	private String RemoveEvent(String[] arrArgs) {
		if (arrArgs == null || arrArgs.length != 2)
			return ERROR_WRONG_COMMAND;

		if (!lstUsers.containsKey(arrArgs[0]))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(arrArgs[0]);
		
		if (user.RemoveEvent(arrArgs[1]))
			return SUCCESS;
		else
			return ERROR_EVENT_NOT_FOUND;
	}

	private String AddRandomTimeEvent(String[] arrArgs) {
		if (arrArgs == null || arrArgs.length != 4)
			return ERROR_WRONG_COMMAND;
		
		if (!lstUsers.containsKey(arrArgs[0]))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(arrArgs[0]);

		String eventText = arrArgs[1];
		if (eventText.length() == 0)
			return ERROR_WRONG_EVENT;
		if (user.findEvent(eventText) != null)
			return ERROR_EVENT_EXISTS;

		Date fromDate = parseDate(arrArgs[2]);
		if (fromDate == null)
			return ERROR_WRONG_DATE;
		Date toDate = parseDate(arrArgs[3]);
		if (toDate == null)
			return ERROR_WRONG_DATE;
		
		//TODO
		long diff = (toDate.getTime() - fromDate.getTime()) / 1000; // в секундах
		if (diff <= 0)
			return ERROR_WRONG_DATE_RANGE;
		long randomLong = Math.abs((new Random()).nextLong());
		randomLong = Math.abs(randomLong);
		
		double coefficient = (double)diff / Long.MAX_VALUE;
		long eventTime = (long) (fromDate.getTime() + randomLong * coefficient * 1000);
		
		user.AddEvent(eventText, new Date(eventTime));
		return SUCCESS;
	}

	private String CloneEvent(String[] arrArgs) {
		if (arrArgs == null || arrArgs.length != 3)
			return ERROR_WRONG_COMMAND;
		
		if (!lstUsers.containsKey(arrArgs[0]))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(arrArgs[0]);

		if (!lstUsers.containsKey(arrArgs[0]))
			return ERROR_USER_NOT_FOUND;
		User userTo = lstUsers.get(arrArgs[2]);

		String eventText = arrArgs[1];
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

	private String ShowInfo(String[] arrArgs) {
		if (arrArgs == null || arrArgs.length != 1)
			return ERROR_WRONG_COMMAND;

		if (!lstUsers.containsKey(arrArgs[0]))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(arrArgs[0]);

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
		//return user.toString();
	}

	private String StartScheduling() {
		editShowMode = true;

		Calendar cal = new GregorianCalendar();
		int offset = cal.get(Calendar.ZONE_OFFSET);
		
		for (User user : lstUsers.values())
			for (Event event : user.getEvents()) {
				Date eventDate = fromGTM(event.getDate(), offset);
				List<String> lstMessages;
				if (treeEvent.containsKey(eventDate))
					lstMessages = treeEvent.get(eventDate);
				else {
					lstMessages = new LinkedList<String>();
					treeEvent.put(eventDate, lstMessages);
				}
				lstMessages.add(user.getName() + " \"" + event.getText() + "\"");
			}
		
		for (final Date eventDate : treeEvent.keySet()) {
			Timer eventTimer = new Timer();
			TimerTask eventTask = new TimerTask() {

				@Override
				public void run() {
					List<String> lstMessages = treeEvent.get(eventDate);
					for (String msg : lstMessages)
						System.out.println(dateFormat.format(eventDate) + " : " + msg);
				}
				
			};
			eventTimer.schedule(eventTask, eventDate);
		}
		
		return SUCCESS;
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

	private Date fromGTM(Date date, int offset) {
		return new Date(date.getTime() + offset);
	}
}
