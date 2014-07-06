package org.kouzma.schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;


public class ScheduleViewer {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
	private TreeMap<Date, TreeSet<String>> treeEvent = new TreeMap<Date, TreeSet<String>>();
	
	public void show(HashMap<String, User> lstUsers) {
		Calendar cal = new GregorianCalendar();
		int offset = cal.get(Calendar.ZONE_OFFSET);
		
		for (User user : lstUsers.values()){
			if (!user.getStatus())
				continue;
			for (Event event : user.getEvents()) {
				Date eventDate = fromGTM(event.getDate(), offset);
				TreeSet<String> lstMessages;
				if (treeEvent.containsKey(eventDate))
					lstMessages = treeEvent.get(eventDate);
				else {
					lstMessages = new TreeSet<String>();
					treeEvent.put(eventDate, lstMessages);
				}
				lstMessages.add(user.getName() + " \"" + event.getText() + "\"");
			}
		}
		
		Timer eventTimer = new Timer();
		for (final Date eventDate : treeEvent.keySet()) {
			TimerTask eventTask = new TimerTask() {

				@Override
				public void run() {
					TreeSet<String> lstMessages = treeEvent.get(eventDate);
					for (String msg : lstMessages)
						System.out.println(dateFormat.format(eventDate) + " : " + msg);
				}
				
			};
			eventTimer.schedule(eventTask, eventDate);
		}
	}
	
	private Date fromGTM(Date date, int offset) {
		return new Date(date.getTime() + offset);
	}
}
