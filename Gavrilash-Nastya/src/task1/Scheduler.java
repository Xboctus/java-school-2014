package task1;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class Scheduler implements Runnable {
	public static Map<String, User> users;
	public List<Event> futureEvent;

	@Override
	public void run() {
		futureEvent = new ArrayList<Event>();
		for (String name : users.keySet()) {
			if (users.get(name).isActiveStatus()) {
				for (String text : users.get(name).getEvents().keySet()) {
					Event event = users.get(name).getEvents().get(text);
					if (event.getInnerSisdate().after(new GregorianCalendar())) {
						futureEvent.add(event);
					}
				}
			}
		}
		Timer t = new Timer();
		for (Event ev : futureEvent) {
			t.schedule(ev, ev.getInnerSisdate().getTime());
		}

	}
}
