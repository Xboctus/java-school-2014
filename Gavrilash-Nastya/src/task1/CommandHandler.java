package task1;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CommandHandler {
	public static void createNewUser(String name, int timeZoneOffset,
			boolean activityStatus) throws IOException {
		if (Scheduler.users.containsKey(name)) {
			throw new IOException("This user is already exist");
		}
		Scheduler.users.put(name,
				new User(name, activityStatus, timeZoneOffset));
		System.out.println("New user " + name + " created");
	}

	public static void modifyUser(String name, int timeZoneOffset,
			boolean activityStatus) throws IOException {
		if (!Scheduler.users.containsKey(name)) {
			throw new IOException("This user isn't exist");
		}
		Scheduler.users.get(name).setActiveStatus(activityStatus);
		Scheduler.users.get(name).setTimeZoneOffset(timeZoneOffset);
		System.out.println("User " + name + " modified");
	}

	public static void addEvent(String name, String text, GregorianCalendar gc)
			throws IOException {
		if (!Scheduler.users.containsKey(name)) {
			throw new IOException("This user isn't exist");
		}
		Scheduler.users.get(name).addEventToUser(
				new Event(name, gc, text, Scheduler.users.get(name)
						.getTimeZoneOffset()));
		System.out.println("Event " + text + " added to user " + name);
	}

	public static void removeEvent(String name, String text) throws IOException {
		if (!Scheduler.users.containsKey(name)) {
			throw new IOException("This user isn't exist");
		}
		Scheduler.users.get(name).removeEventFromUser(text);
		System.out.println("Event " + text + " removed from user " + name);
	}

	public static void addRandomTimeEvent(String name, String text,
			GregorianCalendar from, GregorianCalendar to) throws IOException {
		if (!Scheduler.users.containsKey(name)) {
			throw new IOException("This user isn't exist");
		}
		if (from.after(to)) {
			throw new IOException("From date is bigger than to date");
		}
		GregorianCalendar gc = randDate(from, to);

		Scheduler.users.get(name).addEventToUser(
				new Event(name, gc, text, Scheduler.users.get(name)
						.getTimeZoneOffset()));
		System.out.println("RantTime event " + text + " removed from user "
				+ name);
	}

	public static void cloneEvent(String name, String text, String nameTo)
			throws IOException {
		if (!Scheduler.users.containsKey(name)) {
			throw new IOException("User From isn't exist");
		}
		if (!Scheduler.users.containsKey(nameTo)) {
			throw new IOException("User To isn't exist");
		}
		Scheduler.users.get(name).cloneEventFromUser(text, nameTo);
		System.out.println("Event " + text + " cloned from user " + name
				+ " to user " + nameTo);
	}

	public static void showInfo(String name) throws IOException {
		if (!Scheduler.users.containsKey(name)) {
			throw new IOException("User From isn't exist");
		}
		User user = Scheduler.users.get(name);
		System.out.println(formattedUserInfo(user));
		SimpleDateFormat fm = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
		for (String k : user.getEvents().keySet()) {
			System.out.println(fm.format(user.getEvents().get(k).getDate()
					.getTime())
					+ " " + user.getEvents().get(k).getText());
		}
	}

	public static void startScheduling() {
		System.out.println("Start scheduling");
		Thread thread = new Thread(new Scheduler());
		thread.start();
	}

	private static GregorianCalendar randDate(GregorianCalendar from,
			GregorianCalendar to) {
		long first = from.getTimeInMillis();
		long second = to.getTimeInMillis();
		long rand = first + (long) ((second - first) * Math.random());
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(rand);
		return calendar;
	}

	private static String formattedUserInfo(User user) {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: " + user.getName() + " TimeZone: GMT");
		if (user.getTimeZoneOffset() < 0) {
			sb.append(user.getTimeZoneOffset());
		} else {
			sb.append("+" + user.getTimeZoneOffset());
		}
		if (user.isActiveStatus()) {
			sb.append(" Status: active");
		} else {
			sb.append(" Status: passive");
		}
		return sb.toString();
	}

}
