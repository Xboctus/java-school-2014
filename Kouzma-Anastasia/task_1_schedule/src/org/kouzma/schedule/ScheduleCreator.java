package org.kouzma.schedule;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import javax.swing.JButton;

import org.kouzma.schedule.gui.ScheduleListener;
import org.kouzma.schedule.util.ScheduleConnection;
import org.kouzma.schedule.util.StateType;

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
	
	private static final String ERROR_FILE = "File not found";
	private static final String ERROR_FILE_LOAD = "Can't load file";
	private static final String ERROR_SYNC = "Can't syncronize";
	private static final String ERROR_DB = "Can't save to database";
	
	private static final String USER_ADDED = "User \"?\" added";
	private static final String USER_MODIFIED = "User \"?\" modified";
	private static final String EVENT_ADDED = "Event \"?\" added";
	private static final String EVENT_REMOVED = "Event \"?\" removed";
	private static final String SAVED = "Current state is saved";
	private static final String LOADED = " objects are loaded";
	private static final String SAVED_TO_DB = "Data saved to database";

	
	private HashMap<String, User> lstUsers = new HashMap<String, User>();
	private TreeSet<Event> treeEvent = new TreeSet<Event>();

	private List<ScheduleListener> lstListeners = new LinkedList<ScheduleListener>();

	private Timer scheduleTimer;
	private ScheduleTimerTask stt;
	private Event nextEvent;

	private ScheduleConnection conn;
	private boolean isModified = false;
	private class ScheduleTimerTask extends TimerTask {

		@Override
		public void run() {
			StringBuffer message = new StringBuffer();
			
			Iterator<Event> eventIterator = treeEvent.tailSet(nextEvent, true).iterator();
			while (eventIterator.hasNext()) {
				Date eventDate = toGMT(new Date(this.scheduledExecutionTime()));
				nextEvent = eventIterator.next();
				if (eventDate.equals(nextEvent.getDate())) {
					User user = nextEvent.getUser();
					if (user.getStatus() && !nextEvent.isShown()) {
						message.append(dateFormat.format(eventDate) + " : " + 
								user.getName() + " \"" + nextEvent.getText() + "\"\n");
						nextEvent.setShown(true);
					} 
				}
				else {
					stt = new ScheduleTimerTask();
					scheduleTimer.schedule(stt, fromGTM(nextEvent.getDate()));
					break;
				}
			}
								
			for (ScheduleListener callBack : lstListeners) {
				callBack.sendMessage(message.toString());
			}
			System.out.print(message);
		}

	};

	public ScheduleCreator() {}

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
		
		isModified = true;
		
		return USER_ADDED.replace("?", name);
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
			checkTimerTask(user.getEvents().first());
		}

		isModified = true;
		
		return USER_MODIFIED.replace("?", name);
	}

	public synchronized String AddEvent(String name, String eventText, String eventDate) {
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
		checkTimerTask(newEvent);

		isModified = true;
		
		return EVENT_ADDED.replace("?", eventText);
	} 


	public synchronized String RemoveEvent(String name, String eventText) {
		if (!lstUsers.containsKey(name))
			return ERROR_USER_NOT_FOUND;
		User user = lstUsers.get(name);
		
		Event event = user.findEvent(eventText);
		if (event == null)
			return ERROR_EVENT_NOT_FOUND;
		
		user.RemoveEvent(event);
		treeEvent.remove(event);

		if (event.getState().equals(StateType.SAVED))
			isModified = true;
		
		return EVENT_REMOVED.replace("?", eventText);
	}

	public synchronized String AddRandomTimeEvent(String name, String eventText, String eventDateFrom, String eventDateTo) {
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
		
		synchronized (treeEvent) {
			treeEvent.add(newEvent);
			checkTimerTask(newEvent);
		}

		isModified = true;
		
		return EVENT_ADDED.replace("?", eventText);
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

		treeEvent.add(cloneEvent);

		isModified = true;
		
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

	private void checkTimerTask(Event newEvent) {
		if (nextEvent == null || newEvent.compareTo(nextEvent) < 0) {
			nextEvent = newEvent;
			if (stt != null)
				stt.cancel();
			scheduleTimer.schedule(new ScheduleTimerTask(), fromGTM(nextEvent.getDate()));
		}
	}
	
	public synchronized String StartScheduling() {
		if (scheduleTimer != null)
			return ERROR_SHOWING;
		
		scheduleTimer = new Timer();
		if (treeEvent.isEmpty())
			nextEvent = null;
		else {
			nextEvent = treeEvent.first();
			stt = new ScheduleTimerTask();
			scheduleTimer.schedule(stt, fromGTM(nextEvent.getDate()));
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

	private Date toGMT(Date date) {
		return new Date(date.getTime() - offset);
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
			res[3 + 2 * i] = dateFormat.format(fromGTM(event.getDate()));
			res[4 + 2 * i] = event.getText();
			i++;
		}
		return res;
	}

	public synchronized String SaveToFile(File file) {
			ObjectOutputStream out;
			try {
				out = new ObjectOutputStream(new FileOutputStream(file));
				out.writeObject(lstUsers);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				return ERROR_FILE;
			}
			return SAVED;

	}

	public synchronized String LoadFromFile(File file) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			try {
				lstUsers = (HashMap<String, User>) in.readObject();
				getEvents();
				int objCount = lstUsers.size() + treeEvent.size();
				return objCount + LOADED;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return ERROR_FILE_LOAD;
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR_FILE_LOAD;
		}
	}

	public String LoadFromFile(String fileName) { // TODO
		File file = new File(fileName);
		if (!file.exists())
			return ERROR_FILE_LOAD;
		
		return LoadFromFile(file);
	}
	
	private void getEvents() {
		for (User user : lstUsers.values()) {
			TreeSet<Event> events = user.getEvents();
			treeEvent.addAll(events);
		}
	}

	public void addListener(ScheduleListener scheduleListener) {
		lstListeners.add(scheduleListener);
	}

	public HashMap<String, User> getUsers() {
		return lstUsers;
	}

	public synchronized String sync(String adress) { //TODO
		String[] arrAdress = adress.split(":");
		if (arrAdress.length != 2)
			return ERROR_SYNC;
		String host = arrAdress[0];
		int port;
		try {
			port = Integer.parseInt(arrAdress[1]);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			return ERROR_SYNC;
		}
		
		Socket socket;
		try {
			socket = new Socket(host, port);
			InputStream is = socket.getInputStream();
			InputStream isb = new BufferedInputStream(is);
			ObjectInputStream ois = new ObjectInputStream(isb);
			
			try {
				lstUsers = (HashMap<String, User>) ois.readObject();
				socket.close();
				getEvents();
				int objCount = lstUsers.size() + treeEvent.size();
				return objCount + LOADED;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return ERROR_SYNC;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR_SYNC;
		}
	}

	public synchronized String saveToDb() {
		try {
			conn = ScheduleConnection.getInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return ERROR_DB;
		}
		
		try {
			for (User user : lstUsers.values()) {
				saveUser(user);
				for (Event event : user.getEvents()) {
					saveEvent(event);
				}
			}
			List<Integer> lstRemoveIds = Event.getLstRemove();
			if (lstRemoveIds.size() > 0) {
				conn.deleteEvents(lstRemoveIds);
				lstRemoveIds.clear();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			return ERROR_DB;
		}

		return SAVED_TO_DB;
	}

	private void saveUser(User user) throws SQLException {
		switch (user.getState()) {
		case NEW:
			int id = conn.insertUser(user);
			if (id == -1)
				break;
			
			user.setId(id);
			user.setState(StateType.SAVED);
			break;
		case MODIFIED:
			conn.updateUser(user);
			user.setState(StateType.SAVED);
			break;
		case SAVED:
		default:
			break;
		}
	}
	
	private void saveEvent(Event event) throws SQLException {
		switch (event.getState()) {
		case NEW:
			int id = conn.insertEvent(event);
			if (id == -1)
				break;
			
			event.setId(id);
			event.setState(StateType.SAVED);
			break;
		/*case MODIFIED:
			conn.updateEvent(event);
			event.setState(StateType.SAVED);
			break;*/
		case SAVED:
		default:
			break;
		}
	}

}