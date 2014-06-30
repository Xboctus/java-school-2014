package org.kouzma.schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class ShowMode {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
	private TreeMap<Date, List<String>> treeEvent = new TreeMap<Date, List<String>>();
	
	public void show(HashMap<String, User> lstUsers) {
		Calendar cal = new GregorianCalendar();
		int offset = cal.get(Calendar.ZONE_OFFSET);
		
		for (User user : lstUsers.values()){
			if (!user.getStatus())
				continue;
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
		}
		
		Timer eventTimer = new Timer();
		for (final Date eventDate : treeEvent.keySet()) {
			TimerTask eventTask = new TimerTask() {

				@Override
				public void run() {
					List<String> lstMessages = treeEvent.get(eventDate);
					Collections.sort(lstMessages); // TODO
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
