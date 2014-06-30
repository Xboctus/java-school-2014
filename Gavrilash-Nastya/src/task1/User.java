package task1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class User {
	private String name;
	private boolean activeStatus;
	private int timeZoneOffset;
	private Map<String, Event> events;

	public User(String name, boolean actStatus, int timeZone) {
		this.name = name;
		this.activeStatus = actStatus;
		this.timeZoneOffset = timeZone;
		events = new HashMap<String, Event>();
	}

	public boolean isActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public int getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(int timeZoneOffset) {
		for (String k : events.keySet()) {
			events.get(k).correctInnerTime(timeZoneOffset);
		}
		this.timeZoneOffset = timeZoneOffset;
	}

	public String getName() {
		return name;
	}

	public Map<String, Event> getEvents() {
		return events;
	}

	public void addEventToUser(Event e) throws IOException {
		if (events.containsKey(e.getText())) {
			throw new IOException("This event is already exist");
		}
		events.put(e.getText(), e);
	}

	public void removeEventFromUser(String text) throws IOException {
		if (!events.containsKey(text)) {
			throw new IOException("This event isn't exist");
		}
		events.remove(text);
	}

	public void cloneEventFromUser(String eventText, String nameTo)
			throws IOException {
		if (!events.containsKey(eventText)) {
			throw new IOException("This event isn't exist");
		}
		if (Scheduler.users.get(nameTo).events.containsKey(eventText)) {
			throw new IOException("This event already exist");
		}
		Event cloneEvent = (Event) events.get(eventText).clone();
		cloneEvent.correctInnerTime(Scheduler.users.get(nameTo)
				.getTimeZoneOffset());
		cloneEvent.setUserName(nameTo);
		Scheduler.users.get(nameTo).events
				.put(cloneEvent.getText(), cloneEvent);
	}
}
