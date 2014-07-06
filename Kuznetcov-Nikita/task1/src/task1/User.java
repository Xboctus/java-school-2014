package task1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

public class User implements Comparable {

  private String userName; // имя пользователя
  private TimeZone timeZone; // таймзона пользователя
  private boolean isActive; // статус пользователя
  private Set<Event> userTaskSet; // набор пользовательских задач

  public User(String name, TimeZone timeZone, boolean status) {
    // конструктор для создания пользователя с указанными именем, статусом и тайм-зоной
    this.userName = name;
    this.timeZone = timeZone;
    this.isActive = status;
    this.userTaskSet = new TreeSet<Event>();
  }

  @Override
  public int compareTo(Object o) {
    User target = (User) o;
    return this.userName.compareTo(target.userName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("User: ");
    sb.append(userName);
    sb.append(", Time-ZoneID: ").append(timeZone.getID());
    sb.append(", status=").append(isActive ? "active" : "inactive").append("\r\n");
    sb.append("Event list:\r\n");
    if (!userTaskSet.isEmpty()) {
      for (Event event : userTaskSet) {
        sb.append(event.toString());
      }
    } else {
      sb.append("Event list is empty!\r\n");
    }
    return sb.toString();
  }

  public JSONObject getUserInfoAsJSON() {
    JSONObject result = new JSONObject();
    result.put("userName", userName);
    result.put("timeZoneID", timeZone.getID());
    result.put("isActive", isActive);

    JSONArray eventArray = new JSONArray();
    for (Event event : userTaskSet) {
      eventArray.add(event.getEventAsJSON());
    }
    result.put("userTaskSet", eventArray);
    return result;
  }

  public String getUserName() {
    return userName;
  }

  public Event getEventByText(String targetText) {
    for (Event event : userTaskSet) {
      if (event.getEventText().equals(targetText)) return event;
    }
    return null;
  }

  public boolean addEvent(Event event) {
    return this.userTaskSet.add(event);
  }

  public boolean removeEvent(Event event) {
    return this.userTaskSet.remove(event);
  }

  public TimeZone getTimeZone() {
    return timeZone;
  }

  public void setTimeZone(TimeZone timeZone) {
    this.timeZone = timeZone;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public Event[] getUserTaskArray() {
    Event[] result = new Event[this.userTaskSet.size()];
    return userTaskSet.toArray(result);
  }

}
