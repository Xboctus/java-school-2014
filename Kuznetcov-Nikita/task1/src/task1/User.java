package task1;

import java.util.Collections;
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
    final StringBuilder sb = new StringBuilder("Пользователь: ");
    sb.append(userName);
    sb.append(", тайм-зона: ").append(timeZone.getID());
    sb.append(", статус=").append(isActive ? "активен" : "неактивен").append("\r\n");
    sb.append("Список задач:\r\n");
    if (!userTaskSet.isEmpty()) {
      for (Event event : userTaskSet) {
        sb.append(event.toString());
      }
    } else {
      sb.append("Список задач пуст!\r\n");
    }
    return sb.toString();
  }

  public Event getEventByText(String targetText) {
    for (Event event : userTaskSet) {
      if (event.getEventText().equals(targetText)) return event;
    }
    return null;
  }

  public boolean addEvent(Event event) { return this.userTaskSet.add(event); }

  public boolean removeEvent(Event event) { return this.userTaskSet.remove(event); }

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
