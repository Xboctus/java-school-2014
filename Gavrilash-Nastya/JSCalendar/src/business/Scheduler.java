package business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is responsible for timer tasks and notification of all Scheduler
 * listeners
 * 
 * @author Gavrilash
 * 
 */
public class Scheduler {
	public static Map<String, User> users = new HashMap<String, User>();
	public static Map<Integer, Event> events = new HashMap<Integer, Event>();
	private static Scheduler mainScheduler = new Scheduler();
	private List<SchedulerListener> listeners;
	private Timer t;

	/**
	 * May be this is not ideal solution TODO ask teacher
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
	public void parseEventList() {
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
	public void generateEventMessage(String eventMessage) {
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
		return mainScheduler;
	}

}
