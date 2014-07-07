package centralStructure;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import sun.awt.windows.ThemeReader;

import business.FileLoader;
import business.SchedulerListener;
import business.SocketLoader;

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
		User.createNewUser(name, timeZoneOffset, activityStatus);
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
		User.modifyUser(name, timeZoneOffset, activityStatus);
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
		if (!Scheduler.getUsers().containsKey(name)) {
			throw new IOException("This user doesn't exist");
		}
		Scheduler
				.getUsers()
				.get(name)
				.addEventToUser(
						new Event(Scheduler.getUsers().get(name), gc, text));

	}

	/**
	 * Handles remove-event query, throws IOException if this user does'n exist
	 * 
	 * @param name
	 * @param text
	 * @throws IOException
	 */
	public static void removeEvent(String name, String text) throws IOException {
		if (!Scheduler.getUsers().containsKey(name)) {
			throw new IOException("This user doesn't exist");
		}
		Scheduler.getUsers().get(name).removeEventFromUser(text);
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
		if (!Scheduler.getUsers().containsKey(name)) {
			throw new IOException("This user doesn't exist");
		}
		if (from.after(to)) {
			throw new IOException("From date is bigger than To date");
		}
		GregorianCalendar gc = randDate(from, to);

		Scheduler
				.getUsers()
				.get(name)
				.addEventToUser(
						new Event(Scheduler.getUsers().get(name), gc, text));

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
		if (!Scheduler.getUsers().containsKey(name)) {
			throw new IOException("User " + name + " doesn't exist");
		}
		if (!Scheduler.getUsers().containsKey(nameTo)) {
			throw new IOException("User " + nameTo + " doesn't exist");
		}
		Scheduler.getUsers().get(name)
				.cloneEventFromUser(text, Scheduler.getUsers().get(nameTo));
	}

	/**
	 * Returns all info about user in List<Strings>
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static List<String> showInfo(String name) throws IOException {
		if (!Scheduler.getUsers().containsKey(name)) {
			throw new IOException("This user doesn't exist");
		}
		List<String> output = new ArrayList<String>();
		User user = Scheduler.getUsers().get(name);
		output.add(formattedUserInfo(user));
		SimpleDateFormat fm = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
		Set<Event> sortedEvents = sortedEventSet(user);
		for (Event e : sortedEvents) {
			output.add("Event: " + fm.format(e.getDate().getTime()) + " "
					+ e.getText());
		}
		return output;
	}

	/**
	 * returns all events for this user as sorted collection
	 * 
	 * @param user
	 * @return
	 */
	private static Set<Event> sortedEventSet(User user) {
		Set<Event> output = new TreeSet<Event>();
		for (String k : user.getEvents().keySet()) {
			output.add(user.getEvents().get(k));
		}
		return output;
	}

	/**
	 * This method invokes method of FileLoader, witch saves current state to
	 * file
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public static void printInfoToFile(String fileName) {
		new FileLoader().writeToSource(fileName);
	}

	public static void printInfoToSocket() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				new SocketLoader().writeToSource();
			}
		});
		t.setDaemon(true);
		t.start();
	}

	public static void downloadFromSocket() {
		Map<String, User> newUsers = new SocketLoader().readFromSource();
		resetSchedulerMaps();
		for (String userName : newUsers.keySet()) {
			User user = newUsers.get(userName);
			Scheduler.getUsers().put(user.getName(), user);
			for (String eventText : user.getEvents().keySet()) {
				user.addEventToSchedulerInner(user.getEvents().get(eventText));
			}
		}
	}

	/**
	 * This method downloads some state of users map and set it as current for
	 * FILE
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public static void downloadFromFile(String fileName) {
		Map<String, User> newUsers = new FileLoader().readFromSource(fileName);
		resetSchedulerMaps();
		for (String userName : newUsers.keySet()) {
			User user = newUsers.get(userName);
			Scheduler.getUsers().put(user.getName(), user);
			for (String eventText : user.getEvents().keySet()) {
				user.addEventToSchedulerInner(user.getEvents().get(eventText));
			}
		}
	}

	/**
	 * This method downloads some state of users map and set it as current for
	 * some source (it can be file or socket)
	 * 
	 * @param l
	 * @param fileName
	 * @throws IOException
	 */

	/**
	 * This method removes everything from scheduler inner memory
	 */
	private static void resetSchedulerMaps() {
		for (int id : Scheduler.getEvents().keySet()) {
			Scheduler.getEvents().get(id).setActive(false);
		}
		Scheduler.getEvents().clear();
		Scheduler.getUsers().clear();
		
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
