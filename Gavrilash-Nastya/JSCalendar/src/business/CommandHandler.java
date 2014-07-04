package business;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * This class handle all queries for User, Event and Scheduler independently of
 * source of this query. Ideally no one method should calls methods of this
 * classes except methods of this handler.
 * 
 * @author Gavrilash
 * 
 */
public class CommandHandler {
	/**
	 * Handles create-user query, throws IOException if this user exist
	 * 
	 * @param name
	 * @param timeZoneOffset
	 * @param activityStatus
	 * @throws IOException
	 */
	public static void createNewUser(String name, int timeZoneOffset,
			boolean activityStatus) throws IOException {
		if (Scheduler.users.containsKey(name)) {
			throw new IOException("This user is already exist");
		}
		Scheduler.users.put(name,
				new User(name, activityStatus, timeZoneOffset));
	}

	/**
	 * Handles modify-user query, throws IOException if this user does'n exist
	 * 
	 * @param name
	 * @param timeZoneOffset
	 * @param activityStatus
	 * @throws IOException
	 */
	public static void modifyUser(String name, int timeZoneOffset,
			boolean activityStatus) throws IOException {
		if (!Scheduler.users.containsKey(name)) {
			throw new IOException("This user doesn't exist");
		}
		Scheduler.users.get(name).setActiveStatus(activityStatus);
		Scheduler.users.get(name).setTimeZoneOffset(timeZoneOffset);
	}

	/**
	 * Handles add-event query, throws IOException if this user does'n exist
	 * 
	 * @param name
	 * @param text
	 * @param gc
	 * @throws IOException
	 */
	public static void addEvent(String name, String text, GregorianCalendar gc)
			throws IOException {
		if (!Scheduler.users.containsKey(name)) {
			throw new IOException("This user doesn't exist");
		}
		Scheduler.users.get(name).addEventToUser(
				new Event(Scheduler.users.get(name), gc, text));

	}

	/**
	 * Handles remove-event query, throws IOException if this user does'n exist
	 * 
	 * @param name
	 * @param text
	 * @throws IOException
	 */
	public static void removeEvent(String name, String text) throws IOException {
		if (!Scheduler.users.containsKey(name)) {
			throw new IOException("This user doesn't exist");
		}
		Scheduler.users.get(name).removeEventFromUser(text);
	}

	/**
	 * Handles random time event query, throws IOExeption if user doesn't exist
	 * or From-date is bigger than To-date
	 * 
	 * @param name
	 * @param text
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	public static void addRandomTimeEvent(String name, String text,
			GregorianCalendar from, GregorianCalendar to) throws IOException {
		if (!Scheduler.users.containsKey(name)) {
			throw new IOException("This user doesn't exist");
		}
		if (from.after(to)) {
			throw new IOException("From date is bigger than To date");
		}
		GregorianCalendar gc = randDate(from, to);

		Scheduler.users.get(name).addEventToUser(
				new Event(Scheduler.users.get(name), gc, text));

	}

	/**
	 * Handles clone-event query
	 * 
	 * @param name
	 * @param text
	 * @param nameTo
	 * @throws IOException
	 */
	public static void cloneEvent(String name, String text, String nameTo)
			throws IOException {
		if (!Scheduler.users.containsKey(name)) {
			throw new IOException("User " + name + " doesn't exist");
		}
		if (!Scheduler.users.containsKey(nameTo)) {
			throw new IOException("User " + nameTo + " doesn't exist");
		}
		Scheduler.users.get(name).cloneEventFromUser(text,
				Scheduler.users.get(nameTo));
	}

	/**
	 * Returns all info about user in List<Strings> TODO here must be Set
	 * somewhere
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static List<String> showInfo(String name) throws IOException {
		if (!Scheduler.users.containsKey(name)) {
			throw new IOException("This user doesn't exist");
		}
		List<String> output = new ArrayList<String>();
		User user = Scheduler.users.get(name);
		output.add(formattedUserInfo(user));
		SimpleDateFormat fm = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
		for (String k : user.getEvents().keySet()) {
			output.add("Event: "
					+ fm.format(user.getEvents().get(k).getDate().getTime())
					+ " " + user.getEvents().get(k).getText());
		}
		return output;
	}

	public static void printInfoToFile(String fileName) throws IOException {
		FileLoader.writeToFile(fileName);
	}

	/**
	 * Generates random time date between two dates
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	private static GregorianCalendar randDate(GregorianCalendar from,
			GregorianCalendar to) {
		long first = from.getTimeInMillis();
		long second = to.getTimeInMillis();
		long rand = first + (long) ((second - first) * Math.random());
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(rand);
		return calendar;
	}

	/**
	 * Returns info about condition of user's fields
	 * 
	 * @param user
	 * @return
	 */
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
