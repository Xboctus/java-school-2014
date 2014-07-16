package org.kouzma.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import org.kouzma.schedule.gui.ScheduleListener;
import org.kouzma.schedule.util.DateUtil;
/**
 * @author Anastasya Kouzma
 */
public class ScheduleExecutor {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
	
	private TreeSet<Event> treeEvents = new TreeSet<Event>();

	private List<ScheduleListener> lstListeners = new LinkedList<ScheduleListener>();

	private Timer scheduleTimer;
	private Event nextEvent;
	
	private class ScheduleTimerTask extends TimerTask {

		@Override
		public void run() {
			StringBuffer message = new StringBuffer();
			
			Iterator<Event> eventIterator = treeEvents.tailSet(nextEvent, true).iterator();
			
			while (eventIterator.hasNext()) {
				Date eventDate = DateUtil.toGMT(new Date(this.scheduledExecutionTime()));
				
				nextEvent = eventIterator.next();
				
				if (eventDate.equals(nextEvent.getDate())) {
					User user = nextEvent.getUser();
					if (user.getStatus() && !nextEvent.isShown()) {
						message.append(dateFormat.format(eventDate) + " : " + user.getName() + " \"" + nextEvent.getText() + "\"\n");
						nextEvent.setShown(true);
					} 
				}
				else {
					scheduleTimer.schedule(new ScheduleTimerTask(), DateUtil.fromGTM(nextEvent.getDate()));
					break;
				}
			}
								
			for (ScheduleListener callBack : lstListeners) {
				callBack.sendMessage(message.toString());
			}
			System.out.print(message);
		}

	};

	public synchronized boolean StartScheduling() {
		if (scheduleTimer != null)
			return false;

		runScheduling();
		return true;
	}
	
	private void runScheduling() {
		scheduleTimer = new Timer();
		if (treeEvents.isEmpty())
			nextEvent = null;
		else {
			nextEvent = treeEvents.first();
			scheduleTimer.schedule(new ScheduleTimerTask(), DateUtil.fromGTM(nextEvent.getDate()));
		}
	}
	
	public void checkTimerTask(Event newEvent) {
		if (nextEvent == null || newEvent.compareTo(nextEvent) < 0) {
			nextEvent = newEvent;
			scheduleTimer.cancel();
			scheduleTimer = new Timer();
			scheduleTimer.schedule(new ScheduleTimerTask(), DateUtil.fromGTM(nextEvent.getDate()));
		}
	}
	
	public void extractEvents(HashMap<String, User> lstUsers) {
		for (User user : lstUsers.values()) {
			TreeSet<Event> events = user.getEvents();
			treeEvents.addAll(events);
		}
		runScheduling();
	}

	public void addEvent(Event newEvent) {
		treeEvents.add(newEvent);
		checkTimerTask(newEvent);
	}

	public void removeEvent(Event event) {
		treeEvents.remove(event);
	}

	public int getEventsAmount() {
		return treeEvents.size();
	}

	public void clear() {
		treeEvents.clear();
		nextEvent = null;
		scheduleTimer.cancel();
	}

	public void addListener(ScheduleListener scheduleListener) {
		lstListeners.add(scheduleListener);
	}
}
