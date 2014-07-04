package task1;

import task1.Util.ResponseStatus;
import java.util.*;
import java.util.logging.Logger;

public class Coordinator {

  private SortedMap<String, User> usersMap = new TreeMap<String, User>(); // list of users
  public static final Logger logger = Logger.getLogger(Coordinator.class.getName());

  public ResponseStatus addNewUser(String userName, String timeZoneID, boolean status) {
    if (usersMap.containsKey(userName)) {
      return ResponseStatus.USER_ALREADY_EXISTS;
    }
    if (!timeZoneID.matches("GMT[+-][0-2]?[0-9](:[0-5]?[0-9])?")) {
      return ResponseStatus.BAD_TIMEZONE_FORMAT;
    }
    usersMap.put(userName, new User(userName, TimeZone.getTimeZone(timeZoneID), status));
    return ResponseStatus.USER_ADDED;
  }

  public ResponseStatus modifyUser(String userName, String timeZoneID, boolean status) {
    User targetUser = usersMap.get(userName);
    if (targetUser == null) {
      return ResponseStatus.USER_NOT_FOUND;
    }
    if (!timeZoneID.matches("GMT[+-][0-2]?[0-9](:[0-5]?[0-9])?")) {
      // if timezoneID has bad format
      return ResponseStatus.BAD_TIMEZONE_FORMAT;
    }
    targetUser.setTimeZone(TimeZone.getTimeZone(timeZoneID));
    targetUser.setActive(status);
    return ResponseStatus.USER_MODIFIED;
  }

  public ResponseStatus addEvent(String userName, String taskText, String taskDateInput) {
    User targetUser = usersMap.get(userName);
    if (targetUser == null) {
      return ResponseStatus.USER_NOT_FOUND;
    }
    Date date = DateFormatter.parseDate(taskDateInput, targetUser.getTimeZone());
    if (date == null) {
      // bad date format
      return ResponseStatus.BAD_DATE_FORMAT;
    }
    if (!targetUser.addEvent(new Event(DateFormatter.parseDate(taskDateInput, targetUser.getTimeZone()), taskText))) {
      // event with specified text already exists
      return ResponseStatus.EVENT_ALREADY_EXISTS;
    }
    return ResponseStatus.EVENT_ADDED;
  }

  public ResponseStatus removeEvent(String userName, String targetText) {
    User targetUser = usersMap.get(userName);
    if (targetUser == null) {
      // if user with such username doesn't exists
      return ResponseStatus.USER_NOT_FOUND;
    }
    Event targetEvent = targetUser.getEventByText(targetText);
    if (targetEvent == null) {
      // event with specified text doesn't exists
      return ResponseStatus.EVENT_NOT_FOUND;
    }
    targetUser.removeEvent(targetUser.getEventByText(targetText));
    return ResponseStatus.EVENT_REMOVED;
  }

  public ResponseStatus addRandomTimeEvent(String userName, String taskText, String dateFromInput, String dateToInput) {

    User targetUser = usersMap.get(userName);
    if (targetUser == null) {
      return ResponseStatus.USER_NOT_FOUND;
    }

    Date dateTo = DateFormatter.parseDate(dateToInput, targetUser.getTimeZone());
    Date dateFrom = DateFormatter.parseDate(dateFromInput, targetUser.getTimeZone());
    long diff = dateTo.getTime() - dateFrom.getTime();
    if (diff <= 0) return ResponseStatus.WRONG_DATE_DIFFERENCE;

    Random generator = new Random();
    long addition;
    if (diff > Integer.MAX_VALUE) {
      addition = diff - generator.nextInt();
    } else {
      // else we choose any int from [0, diff)
      addition = generator.nextInt((int) diff);
    }

    if (targetUser.addEvent(new Event(new Date(dateFrom.getTime() + addition), taskText))) {
      return ResponseStatus.EVENT_ADDED;
    } else {
      return ResponseStatus.EVENT_NOT_FOUND;
    }
  }

  public ResponseStatus cloneEvent(String userName, String taskText, String targetUserName) {
    try {
      User srcUser = usersMap.get(userName);
      User targetUser = usersMap.get(targetUserName);
      if (srcUser == null) {
        // source user doesn't exists
        return ResponseStatus.USER_NOT_FOUND;
      }
      if (targetUser == null) {
        // target user doesn't exists
        return ResponseStatus.TARGET_USER_NOT_FOUND;
      }
      Event srcEvent = srcUser.getEventByText(taskText);
      if (srcEvent == null) {
        // srcEvent not found
        return ResponseStatus.EVENT_NOT_FOUND;
      }
      if (!usersMap.get(targetUserName).addEvent((Event)srcEvent.clone())) {
        // if Event with specified text already present is user's event list
        return ResponseStatus.EVENT_ALREADY_EXISTS;
      }
    } catch (CloneNotSupportedException ex) {
      // won't happen
    }
    // if all going well
    return ResponseStatus.EVENT_CLONED;
  }

  public String getUserInfo(String userName) {
    if (userName == null) return null;
    User user = usersMap.get(userName);
    return user != null ? user.toString() : "";
  }

  public void startScheduling() {
    TimerTask timerTask = new TimerTask() {
      @Override
      public void run() {

        long currentTime = new Date().getTime() / 1000;

        for (Map.Entry<String, User> userEntry : usersMap.entrySet()) {
          if (userEntry.getValue().isActive()) {
            for (Event event : userEntry.getValue().getUserTaskArray()) {
              if (event.getEventDate().getTime() / 1000 == currentTime) {
                System.out.println("---EVENT---\r\nUser: " + userEntry.getKey() + "\r\nEvent info: " + event);
                logger.info("---EVENT---\r\nUser: " + userEntry.getKey() + "\r\nEvent info: " + event);
              }
            }
          }
        }
      }
    };

    Timer scheduler = new Timer("scheduler", false);
    scheduler.scheduleAtFixedRate(timerTask, 0, 1000);
  }

  public SortedMap<String, User> getUsersMap() {
    return this.usersMap;
  }

}
