package centralStructure;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class encapsulates all information about user, his name, time zone
 * offset and activity status. It also contains all events, belonging to this
 * user.
 * 
 * @author Gavrilash
 * 
 */
public class User implements Serializable {
	private String name;
	private boolean activeStatus;
	private int timeZoneOffset;
	private Map<String, Event> events;

	/**
	 * Constructor of User class
	 * 
	 * @param name
	 * @param actStatus
	 * @param timeZone
	 */
	public User(String name, boolean actStatus, int timeZone) {
		this.name = name;
		this.activeStatus = actStatus;
		this.timeZoneOffset = timeZone;
		events = new HashMap<String, Event>();
		Scheduler.getUsers().put(this.name, this);
	}

	/**
	 * creates new user and puts it to Scheduler map
	 * 
	 * @param name
	 * @param timeZoneOffset
	 * @param activityStatus
	 * @throws IOException
	 */
	public static synchronized void createNewUser(String name,
			int timeZoneOffset, boolean activityStatus) throws IOException {
		if (Scheduler.getUsers().containsKey(name)) {
			throw new IOException("This user is already exist");
		}

		new User(name, activityStatus, timeZoneOffset);
	}

	public static synchronized void modifyUser(String name, int timeZoneOffset,
			boolean activityStatus) throws IOException {
		if (!Scheduler.getUsers().containsKey(name)) {
			throw new IOException("This user doesn't exist");
		}
		Scheduler.getUsers().get(name).setActiveStatus(activityStatus);
		Scheduler.getUsers().get(name).setTimeZoneOffset(timeZoneOffset);
	}

	/**
	 * Checks the activity status of the user
	 * 
	 * @return activity status
	 */
	public boolean isActiveStatus() {
		return activeStatus;
	}

	/**
	 * Sets the activity status of the user
	 * 
	 * @param activeStatus
	 */
	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	/**
	 * 
	 * @return time zone offset
	 */
	public int getTimeZoneOffset() {
		return timeZoneOffset;
	}

	/**
	 * Sets time zone for the user and shifts time of advent of user's events
	 * Example: user Vasya has offset +4 and event "Meeting" in 12-00. System
	 * has an offset +4, so "Meeting" will occurs in 12-00. Call of this method
	 * with parameter +6 on user Vasya lead to Vasya's time zone offset changes
	 * to +6 and "Meeting" will occur in 10-00
	 * 
	 * @param timeZoneOffset
	 */
	public synchronized void setTimeZoneOffset(int timeZoneOffset) {
		for (String k : events.keySet()) {
			events.get(k).correctInnerTime(timeZoneOffset);
		}
		this.timeZoneOffset = timeZoneOffset;
	}

	/**
	 * returns the name of the user
	 * 
	 * @return name of user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns all events of this user. For user it can be only one event with
	 * this text, so in map - text of event is a key and event is an object
	 * 
	 * @return map of events of this user
	 */
	public Map<String, Event> getEvents() {
		return events;
	}

	/**
	 * This method adds event to user. If event with this text is already exist
	 * in user's map, method throws IOException. Event is added to inner user's
	 * map of his own events and to common map of all events of all users, witch
	 * belongs to Scheduler. In this second map it can be more than one event
	 * with same text, that's why it necessary to assign to all events unique
	 * id.
	 * 
	 * @param e
	 * @throws IOException
	 */
	public synchronized void addEventToUser(Event e) throws IOException {
		if (events.containsKey(e.getText())) {
			throw new IOException("This event is already exist");
		}
		events.put(e.getText(), e);
		addEventToSchedulerInner(e);
	}

	/**
	 * Adds events to scheduler inner event map, where all events stored
	 * 
	 * @param e
	 */
	public void addEventToSchedulerInner(Event e) {
		e.setViewed(false);
		Scheduler.getEvents().put(e.getId(), e);
		Scheduler.getMainScheduler().parseEventList();
	}

	/**
	 * This method removes events from user. If this event doesn't exist or
	 * marked as inactive, method throws IOException. If this event exist and
	 * marked as active, method sets it's active status as inactive(otherwise it
	 * will be called by timer independently of availability it in maps), delete
	 * it from user's inner event map and from scheduler's map
	 * 
	 * @param text
	 * @throws IOException
	 */
	public synchronized void removeEventFromUser(String text) {
		events.get(text).setActive(false);
		Scheduler.getEvents().remove(events.get(text).getId());
		events.remove(text);

	}

	/**
	 * This method clones the event from one user to another. When event doesn't
	 * exist in user's From map or already exist in user's To map, method throw
	 * IOException. Otherwise it creates the copy of old event, sets new user to
	 * this event and put it to new user's map
	 * 
	 * @param eventText
	 * @param userTo
	 * @throws IOException
	 */
	public synchronized void cloneEventFromUser(String eventText, User userTo)
			throws IOException {
		if (!events.containsKey(eventText)) {
			throw new IOException("This event doesn't exist");
		}
		if (Scheduler.getUsers().get(userTo.getName()).events
				.containsKey(eventText)) {
			throw new IOException("This event already exist");
		}
		Event cloneEvent = (Event) events.get(eventText).clone();
		cloneEvent.correctInnerTime(Scheduler.getUsers().get(userTo.getName())
				.getTimeZoneOffset());
		cloneEvent.setUser(userTo);
		Scheduler.getUsers().get(userTo.getName()).addEventToUser(cloneEvent);
	}
}
