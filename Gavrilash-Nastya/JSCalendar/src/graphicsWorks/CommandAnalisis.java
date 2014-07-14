package graphicsWorks;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import centralStructure.CommandHandler;
import centralStructure.Scheduler;

import business.SchedulerListener;

public class CommandAnalisis {

	public enum Command {
		CREATE, MODIFY, START_SCHEDULING, WRITE_TO_FILE, READ_FROM_FILE
	}

	private static void textValidator(String text, String textForWhiteSpace)
			throws IOException {
		if (text.equals("")) {
			throw new IOException("please,write " + textForWhiteSpace);
		}
		if (text.length() < 3 || text.length() > 255) {
			throw new IOException("Lenght of " + textForWhiteSpace
					+ " must be between 3 and 255 digits");
		}
	}

	private static void dateValidator(String dateText) throws IOException {
		if (dateText.equals("  .  .    ")) {
			throw new IOException("please, enter date like this DD.MM.YYYY");
		}
		int year = Integer.valueOf(dateText.substring(6));
		int month = Integer.valueOf(dateText.substring(3, 5)) - 1;
		int day = Integer.valueOf(dateText.substring(0, 2));
		if (month > 11) {
			throw new IOException("month should be from 1 to 12");
		}
		Calendar cal = new GregorianCalendar(year, month, 1);
		int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (day > daysInMonth || day == 0) {
			throw new IOException("wrong day of month");
		}
	}

	private static void timeValidator(String timeText) throws IOException {
		if (timeText.equals("  :  :  ")) {
			throw new IOException("please, enter time like this HH:MM:SS");
		}
		int hour = Integer.valueOf(timeText.substring(0, 2));
		int minute = Integer.valueOf(timeText.substring(3, 5));
		int second = Integer.valueOf(timeText.substring(6));
		if (hour > 23) {
			throw new IOException(
					"wrong hours,please, enter time like this HH:MM:SS");
		}
		if (minute > 59) {
			throw new IOException(
					"wrong minutes,please, enter time like this HH:MM:SS");
		}
		if (second > 59) {
			throw new IOException(
					"wrong seconds,please, enter time like this HH:MM:SS");
		}
	}

	public static void createUserQuery(String name, boolean activeStatus,
			int timeZoneOffset) throws IOException {
		textValidator(name, "name");
		CommandHandler.createNewUser(name, timeZoneOffset, activeStatus);
	}

	public static void modifyUserQuery(String name, boolean activeStatus,
			int timeZoneOffset) throws IOException {
		textValidator(name, "name");
		CommandHandler.modifyUser(name, timeZoneOffset, activeStatus);
	}

	public static void addEventQuery(String name, String event, String date,
			String time) throws IOException {
		textValidator(name, "name");
		textValidator(event, "event text");
		dateValidator(date);
		timeValidator(time);
		int hour = Integer.valueOf(time.substring(0, 2));
		int minute = Integer.valueOf(time.substring(3, 5));
		int second = Integer.valueOf(time.substring(6));
		int year = Integer.valueOf(date.substring(6));
		int month = Integer.valueOf(date.substring(3, 5)) - 1;
		int day = Integer.valueOf(date.substring(0, 2));

		CommandHandler.addEvent(name, event, new GregorianCalendar(year, month,
				day, hour, minute, second));
	}

	public static void removeEventQuery(String name, String text)
			throws IOException {
		textValidator(name, "name");
		textValidator(text, "text of event");
		CommandHandler.removeEvent(name, text);
	}

	public static void addRandomQuery(String name, String event,
			String dateFrom, String dateTo) throws IOException {
		textValidator(name, "name");
		textValidator(event, "text of event");
		dateValidator(dateFrom);
		dateValidator(dateTo);
		int yearFrom = Integer.valueOf(dateFrom.substring(6));
		int monthFrom = Integer.valueOf(dateFrom.substring(3, 5)) - 1;
		int dayFrom = Integer.valueOf(dateFrom.substring(0, 2));
		int yearTo = Integer.valueOf(dateTo.substring(6));
		int monthTo = Integer.valueOf(dateTo.substring(3, 5)) - 1;
		int dayTo = Integer.valueOf(dateTo.substring(0, 2));
		CommandHandler.addRandomTimeEvent(name, event, new GregorianCalendar(
				yearFrom, monthFrom, dayFrom), new GregorianCalendar(yearTo,
				monthTo, dayTo));
	}

	public static void cloneQuery(String userFrom, String userTo, String event)
			throws IOException {
		textValidator(userFrom, "user From");
		textValidator(userTo, "user To");
		textValidator(event, "text of event");
		CommandHandler.cloneEvent(userFrom, event, userTo);
	}

	public static List<String> showInfoQuery(String name) throws IOException {
		textValidator(name, "name");
		return CommandHandler.showInfo(name);

	}

	public static void startSchedulingQuery() {
		/**
		 * TODO add query through CommandHandler
		 */
		Scheduler sh = Scheduler.getMainScheduler();
		sh.addListener(new SchedulerListener() {

			@Override
			public void eventMessageReceived(String eventMessage) {
				if (MainClass.frame.isScheduling()) {
					MainClass.frame.addStringToLog(eventMessage);
				}
			}
		});
	}

	public static void downloadFromBaseQuery() {
		CommandHandler.downloadFromBase();
	}

	public static void printQuery(String fileName) throws IOException {
		CommandHandler.printInfoToFile(fileName);
	}

	public static void downloadFromFileQuery(String fileName)
			throws IOException {
		CommandHandler.downloadFromFile(fileName);

	}

	public static void downloadFromSocketQuery() {
		CommandHandler.printInfoToSocket();
		CommandHandler.downloadFromSocket();
	}

	public static void saveToBaseQuery() {
		CommandHandler.saveToBase();
	}
}
