package org.kouzma.schedule;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.kouzma.schedule.gui.ScheduleListener;
import org.kouzma.schedule.util.DateUtil;
import org.kouzma.schedule.util.FileLoader;
import org.kouzma.schedule.util.DBLoader;
import org.kouzma.schedule.util.SocketLoader;
/**
 * @author Anastasya Kouzma
 */
public class ScheduleCreator {	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");

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
	
	private static final String ERROR_FILE = "File not found";
	private static final String ERROR_FILE_LOAD = "Can't load file ";
	private static final String ERROR_SYNC = "Can't syncronize";
	private static final String ERROR_DB_LOAD = "Can't load from database";
	private static final String ERROR_DB = "Can't save to database";
	private static final String ERROR_UNSAVED = " objects are not saved because of name/text duplication";
	
	private static final String USER_ADDED = "User \"%1$2s\" added";
	private static final String USER_MODIFIED = "User \"%1$2s\" modified";
	private static final String EVENT_ADDED = "Event \"%1$2s\" added";
	private static final String EVENT_REMOVED = "Event \"%1$2s\" removed";
	private static final String SAVED = "Current state is saved";
	private static final String LOADED = " objects are loaded";
	private static final String SAVED_TO_DB = "Data saved to database";

	private HashMap<String, User> lstUsers = new HashMap<String, User>();

	List<User> lstNewUsers = new LinkedList<User>();
	List<User> lstModifiedUsers = new LinkedList<User>();
	List<Event> lstNewEvents = new LinkedList<Event>();
	List<Event> lstRemovedEvents = new LinkedList<Event>();

	private ScheduleExecutor executor;

	public ScheduleCreator() {
		executor = new ScheduleExecutor();
	}

	public synchronized String createUser(String name, String zone, String status) {		
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
		lstNewUsers.add(newUser);
		
		return String.format(USER_ADDED, name);
	}


	public synchronized String ModifyUser(String name, String zone, String status) {
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
		
		if (isActive && user.getEvents().size() > 0) {
			executor.checkTimerTask(user.getEvents().first());
		}

		if (user.getId() != -1)
			lstModifiedUsers.add(user);

		return String.format(USER_MODIFIED, name);
	}

	public synchronized String AddEvent(String name, String eventText, String eventDate) {
		Date date = parseDate(eventDate);
		if (eventDate == null)
			return ERROR_WRONG_DATE;
		
		return addEventWithDate(name, eventText, date);
	} 

	private String addEventWithDate(String name, String eventText, Date date) {
		if (!lstUsers.containsKey(name))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(name);
		
		if (eventText.length() == 0)
			return ERROR_WRONG_EVENT;
		if (user.findEvent(eventText) != null)
			return ERROR_EVENT_EXISTS;
		
		Event newEvent = user.AddEvent(eventText, date);

		executor.addEvent(newEvent);
		
		lstNewEvents.add(newEvent);

		return String.format(EVENT_ADDED, eventText);
	}

	public synchronized String RemoveEvent(String name, String eventText) {
		if (!lstUsers.containsKey(name))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(name);
		
		Event event = user.findEvent(eventText);
		if (event == null)
			return ERROR_EVENT_NOT_FOUND;
		
		user.RemoveEvent(event);
		executor.removeEvent(event);
		
		if (event.getId() != -1)
			lstRemovedEvents.add(event);
		else
			lstNewEvents.remove(event);

		return String.format(EVENT_REMOVED, eventText);
	}

	public synchronized String AddRandomTimeEvent(String name, String eventText, String eventDateFrom, String eventDateTo) {
		Date fromDate = parseDate(eventDateFrom);
		if (fromDate == null)
			return ERROR_WRONG_DATE;
		Date toDate = parseDate(eventDateTo);
		if (toDate == null)
			return ERROR_WRONG_DATE;
		
		Date randomDate = DateUtil.computeRandomDate(fromDate, toDate);
		if (randomDate == null)
			return ERROR_WRONG_DATE_RANGE;

		return addEventWithDate(name, eventText, randomDate);
	}

	public synchronized String CloneEvent(String name, String eventText, String nameTo) {
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

		executor.addEvent(cloneEvent);
		
		lstNewEvents.add(cloneEvent);

		return String.format(EVENT_ADDED, eventText);
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
			answer.append("\n" + dateFormat.format(DateUtil.fromGTM(event.getDate())) + " \"" + event.getText() + "\"");
		}	
		return answer.toString();
	}

	
	public synchronized String StartScheduling() {
		if (!executor.StartScheduling())
			return ERROR_SHOWING;

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

	public String[] getUserInfo(String name) {
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
			res[3 + 2 * i] = dateFormat.format(DateUtil.fromGTM(event.getDate()));
			res[4 + 2 * i] = event.getText();
			i++;
		}
		return res;
	}

	public synchronized String SaveToFile(File file) {
		boolean res = FileLoader.SaveToFile(file, lstUsers);
		if (!res)
			return ERROR_FILE;
				
		return SAVED;
	}

	public synchronized String LoadFromFile(File file) {
		HashMap<String, User> lstTemp = new HashMap<String, User>();
		boolean res = FileLoader.loadFromFile(file, lstTemp);
		if (!res)
			return ERROR_FILE_LOAD + file.getName();
		
		lstUsers = lstTemp;
		return afterLoad();
	}
	
	public String LoadFromFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists())
			return ERROR_FILE_LOAD + fileName;
		
		return LoadFromFile(file);
	}

	private void clearAll() {
		executor.clear();
		
		lstNewUsers.clear();
		lstModifiedUsers.clear();
		lstNewEvents.clear();
		lstRemovedEvents.clear();		
	}

	public void addListener(ScheduleListener scheduleListener) {
		executor.addListener(scheduleListener);
	}

	public HashMap<String, User> getUsers() {
		return lstUsers;
	}

	public synchronized String sync(String adress) {
		HashMap<String, User> lstTemp = new HashMap<String, User>();
		boolean res = SocketLoader.sync(adress, lstTemp);
		if (!res)
			return ERROR_SYNC;

		lstUsers = lstTemp;
		
		return afterLoad();
	}

	public synchronized String saveToDb() {
		DBLoader dao;
		try {
			dao = DBLoader.getInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return ERROR_DB;
		}
		
		boolean res = dao.saveChanges(lstNewUsers, lstModifiedUsers, lstNewEvents, lstRemovedEvents);
		if (!res)
			return ERROR_DB;
		lstNewUsers = dao.getUnsavedUsers();
		lstNewEvents = dao.getUnsavedEvents();
		int unsavedAmount = lstNewUsers.size() + lstNewEvents.size();
		if (unsavedAmount > 0) {
			dao.clearUnsavedObjects();
			return unsavedAmount + ERROR_UNSAVED;
		}
		
		return SAVED_TO_DB;
	}

	public synchronized String loadFromDb() {
		try {
			DBLoader dao;
			try {
				dao = DBLoader.getInstance();
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				e.printStackTrace();
				return ERROR_DB_LOAD;
			}
			
			List<User> lstDbUsers = dao.selectUsersAndEvents();
			lstUsers.clear();
			for (User user : lstDbUsers) {
				lstUsers.put(user.getName(), user);
			}
			
			return afterLoad();
		} catch (SQLException e) {
			e.printStackTrace();
			return ERROR_DB_LOAD;
		}
	}

	public boolean hasChanges() {
		return (lstNewUsers.size() > 0 || lstModifiedUsers.size() > 0 ||
				lstNewEvents.size() > 0 || lstRemovedEvents.size() > 0);
	}

	private String afterLoad() {
		clearAll();
		executor.extractEvents(lstUsers);
		int objCount = lstUsers.size() + executor.getEventsAmount();
		return objCount + LOADED;
	}
}