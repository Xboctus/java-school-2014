package task1;

import java.util.*;

public class Coordinator {

  private SortedMap<String, User> usersMap = new TreeMap<String, User>(); // list of users

  public int addNewUser(String userName, String timeZoneID, boolean status) {
    if (usersMap.containsKey(userName)) {
      // if user with such username already exists
      return 1;
    }
    if (!timeZoneID.matches("GMT[+-][0-2]?[0-9](:[0-5]?[0-9])?")) {
      // if timezoneID has bad format
      return 2;
    }
    usersMap.put(userName, new User(userName, TimeZone.getTimeZone(timeZoneID), status));
    return 0;
  }

  public int modifyUser(String userName, String timeZoneID, boolean status) {
    User targetUser = usersMap.get(userName);
    if (targetUser == null) {
      // if user with such username doesn't exists
      return 1;
    }
    if (!timeZoneID.matches("GMT[+-][0-2]?[0-9](:[0-5]?[0-9])?")) {
      // if timezoneID has bad format
      return 2;
    }
    targetUser.setTimeZone(TimeZone.getTimeZone(timeZoneID));
    targetUser.setActive(status);
    return 0;
  }

  public int addEvent(String userName, String taskText, String taskDateInput) {
    User targetUser = usersMap.get(userName);
    if (targetUser == null) {
      // if user with such username doesn't exists
      return 1;
    }
    Date date = DateFormatter.parseDate(taskDateInput, targetUser.getTimeZone());
    if (date == null) {
      // bad date format
      return 2;
    }
    if (!targetUser.getUserTaskSet().add(new Event(DateFormatter.parseDate(taskDateInput, targetUser.getTimeZone()), taskText))) {
      // event with specified text already exists
      return 3;
    }
    return 0;
  }

  public int removeEvent(String userName, String targetText) {
    User targetUser = usersMap.get(userName);
    if (targetUser == null) {
      // if user with such username doesn't exists
      return 1;
    }
    Event targetEvent = targetUser.getEventByText(targetText);
    if (targetEvent == null) {
      // event with specified text doesn't exists
      return 2;
    }
    targetUser.getUserTaskSet().remove(targetUser.getEventByText(targetText));
    return 0;
  }

  public int addRandomTimeEvent(String userName, String taskText, String dateFromInput, String dateToInput) {

    User targetUser = usersMap.get(userName);
    if (targetUser == null) {
      // if user with such username doesn't exists
      return 1;
    }

    Date dateTo = DateFormatter.parseDate(dateToInput, targetUser.getTimeZone());
    Date dateFrom = DateFormatter.parseDate(dateFromInput, targetUser.getTimeZone());
    long diff = dateTo.getTime() - dateFrom.getTime();
    if (diff <= 0) return 2; // dateFrom is early than dateTo

    Random generator = new Random();
    long addition;
    if (diff > Integer.MAX_VALUE) {
      addition = diff - generator.nextInt();
    } else {
      // else we choose any int from [0, diff)
      addition = generator.nextInt((int) diff);
    }

    if (targetUser.getUserTaskSet().add(new Event(new Date(dateFrom.getTime() + addition), taskText))) {
      return 0;  // if all going well
    } else {
      return 3; // if Event with specified text already present is user's event list
    }
  }

  public int cloneEvent(String userName, String taskText, String targetUserName) {
    try {
      User srcUser = usersMap.get(userName);
      User targetUser = usersMap.get(targetUserName);
      if (srcUser == null) {
        // source user doesn't exists
        return 1;
      }
      if (targetUser == null) {
        // target user doesn't exists
        return 2;
      }
      Event srcEvent = srcUser.getEventByText(taskText);
      if (srcEvent == null) {
        // srcEvent not found
        return 3;
      }
      if (!usersMap.get(targetUserName).getUserTaskSet().add((Event)srcEvent.clone())) {
        // if Event with specified text already present is user's event list
        return 4;
      }
    } catch (CloneNotSupportedException ex) {
      // won't happen
    }
    // if all going well
    return 0;
  }

  public void StartScheduling() {
    TimerTask timerTask = new TimerTask() {
      @Override
      public void run() {

        long currentTime = new Date().getTime() / 1000;

        for (Map.Entry<String, User> userEntry : usersMap.entrySet()) {
          if (userEntry.getValue().isActive()) {
            for (Event event : userEntry.getValue().getUserTaskSet()) {
              if (event.getEventDate().getTime() / 1000 == currentTime) {
                System.out.println(event);
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
