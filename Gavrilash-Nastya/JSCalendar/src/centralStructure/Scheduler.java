package centralStructure;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import business.SchedulerListener;

/**
 * This class is responsible for timer tasks and notification of all Scheduler
 * listeners
 * 
 * @author Gavrilash
 * 
 */
public class Scheduler {
	private static Map<String, User> users = new HashMap<String, User>();
	private static Map<Integer, Event> events = new HashMap<Integer, Event>();
	private static Scheduler mainScheduler;
	private List<SchedulerListener> listeners;
	private Timer t;

	/**
	 * Constructor is called only once, when method getMainScheduller called at
	 * the first time
	 */
	private Scheduler() {
		listeners = new ArrayList<SchedulerListener>();
		t = new Timer();
		parseEventList();
	}

	/**
	 * This method parses all events in event map of main scheduler and if user
	 * is active and event is in future and active, than this event added to
	 * timer in timer task
	 */
	void parseEventList() {
		for (int eventsId : events.keySet()) {
			final Event event = events.get(eventsId);
			if (users.get(event.getUser().getName()).isActiveStatus()
					&& !event.isViewed()
					&& event.getInnerSisdate().compareTo(
							new GregorianCalendar()) >= 0) {
				event.setViewed(true);
				final SimpleDateFormat fm = new SimpleDateFormat(
						"dd.MM.yyyy-HH:mm:ss");
				t.schedule(new TimerTask() {
					@Override
					public void run() {
						if (event.isActive()) {
							generateEventMessage("User "
									+ event.getUser().getName()
									+ " "
									+ fm.format(event.getInnerSisdate()
											.getTime()) + " " + event.getText());
						}
					}
				}, event.getInnerSisdate().getTime());

			}
		}

	}

	/**
	 * Adds listeners to main scheduler
	 * 
	 * @param toAdd
	 */
	public void addListener(SchedulerListener toAdd) {
		listeners.add(toAdd);
	}

	/**
	 * notify listeners
	 * 
	 * @param eventMessage
	 */
	void generateEventMessage(String eventMessage) {
		for (SchedulerListener l : listeners) {
			l.eventMessageReceived(eventMessage);
		}
	}

	/**
	 * Returns main scheduler
	 * 
	 * @return
	 */
	public static Scheduler getMainScheduler() {
		if (mainScheduler == null) {
			mainScheduler = new Scheduler();
		}
		return mainScheduler;
	}

	/**
	 * return static map of all users
	 * 
	 * @return
	 */
	public static Map<String, User> getUsers() {
		return users;
	}

	/**
	 * return static map of all events
	 * 
	 * @return
	 */
	static Map<Integer, Event> getEvents() {
		return events;
	}
}
