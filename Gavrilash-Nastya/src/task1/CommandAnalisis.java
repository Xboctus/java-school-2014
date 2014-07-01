package task1;

import java.io.IOException;
import java.util.GregorianCalendar;

public class CommandAnalisis {
	public static void analizeQuery(String query) throws IOException {
		String delims = "[(),]+";
		query = query.trim();
		String[] tokens = query.split(delims);
		tokens[0] = tokens[0].replaceAll("\\s+", "");
		switch (tokens[0]) {
		case "Create":
			createUserQuery(tokens);
			break;
		case "Modify":
			modufyQuery(tokens);
			break;
		case "AddEvent":
			addEventQuery(tokens);
			break;
		case "RemoveEvent":
			removeEventQuery(tokens);
			break;
		case "AddRandomTimeEvent":
			addRandomTimeEventQuery(tokens);
			break;
		case "CloneEvent":
			cloneEventQuery(tokens);
			break;
		case "ShowInfo":
			showInfoQuery(tokens);
			break;
		case "StartScheduling":
			startSchedulingQuery(tokens);
			break;
		default:
			IOException myIO = new IOException("Wrong command, try again");
			throw myIO;
		}
	}

	public static void createUserQuery(String[] tokens) throws IOException {
		if (tokens.length != 4) {
			throw new IOException("Wrong command, try again");
		}
		CommandHandler.createNewUser(getName(tokens[1]),
				getTimezoneOffset(tokens[2]), getActiveStatus(tokens[3]));
	}

	public static void modufyQuery(String[] tokens) throws IOException {
		if (tokens.length != 4) {
			throw new IOException("Wrong command, try again");
		}
		CommandHandler.modifyUser(getName(tokens[1]),
				getTimezoneOffset(tokens[2]), getActiveStatus(tokens[3]));
	}

	public static void addEventQuery(String[] tokens) throws IOException {
		if (tokens.length != 4) {
			throw new IOException("Wrong command, try again");
		}
		CommandHandler.addEvent(getName(tokens[1]), getName(tokens[2]),
				getGregorianCalendar(tokens[3]));
	}

	public static void removeEventQuery(String[] tokens) throws IOException {
		if (tokens.length != 3) {
			throw new IOException("Wrong command, try again");
		}
		CommandHandler.removeEvent(getName(tokens[1]), getName(tokens[2]));
	}

	public static void addRandomTimeEventQuery(String[] tokens)
			throws IOException {
		if (tokens.length != 5) {
			throw new IOException("Wrong command, try again");
		}
		CommandHandler.addRandomTimeEvent(getName(tokens[1]),
				getName(tokens[2]), getGregorianCalendar(tokens[3]),
				getGregorianCalendar(tokens[4]));
	}

	public static void cloneEventQuery(String[] tokens) throws IOException {
		if (tokens.length != 4) {
			throw new IOException("Wrong command, try again");
		}
		CommandHandler.cloneEvent(getName(tokens[1]), getName(tokens[2]),
				getName(tokens[3]));
	}

	public static void showInfoQuery(String[] tokens) throws IOException {
		if (tokens.length != 2) {
			throw new IOException("Wrong command, try again");
		}
		CommandHandler.showInfo(getName(tokens[1]));
	}

	public static void startSchedulingQuery(String[] tokens) throws IOException {
		if (tokens.length != 1) {
			throw new IOException("Wrong command, try again");
		}
		CommandHandler.startScheduling();
	}

	public static int getTimezoneOffset(String text) throws IOException {
		text = text.replaceAll("\\s+", "");
		if (text.indexOf("GMT") == -1) {
			throw new IOException("Wrong time zone format. Try GMT+X");
		}
		text = text.replaceAll("GMT", "");
		int timeZoneOffset = 0;
		try {
			timeZoneOffset = Integer.valueOf(text);
		} catch (NumberFormatException e) {
			throw new IOException("Wrong time zone format. Try GMT+X");
		}
		return timeZoneOffset;
	}

	public static String getName(String text) throws IOException {
		return text.trim();
	}

	public static boolean getActiveStatus(String text) throws IOException {
		text = text.replaceAll("\\s+", "");
		switch (text) {
		case "active":
			return true;
		case "passive":
			return false;
		default:
			throw new IOException("Wrong activity status. Try active/passive");
		}
	}

	public static GregorianCalendar getGregorianCalendar(String text)
			throws IOException {
		text = text.replaceAll("\\s+", "");
		try {
			int day = Integer.valueOf(text.substring(0, 2));
			int month = Integer.valueOf(text.substring(3, 5)) - 1;
			int year = Integer.valueOf(text.substring(6, 10));
			int hour = Integer.valueOf(text.substring(11, 13));
			int minute = Integer.valueOf(text.substring(14, 16));
			int second = Integer.valueOf(text.substring(17, 19));
			return new GregorianCalendar(year, month, day, hour, minute, second);
		} catch (NumberFormatException e) {
			throw new IOException("Wrong date format. Try DD.MM.YYYY-HH:mm:ss");
		} catch (StringIndexOutOfBoundsException e) {
			throw new IOException("Wrong date format. Try DD.MM.YYYY-HH:mm:ss");
		}
	}
}
