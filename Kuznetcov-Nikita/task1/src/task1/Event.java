package task1;

import java.util.Date;
import java.util.TimeZone;

public class Event implements Comparable, Cloneable {

  private Date eventDate; // дата вывова события
  private String eventText; // пояснительный текст события

  public Event (Date targetDate, String text) {
    this.eventDate = targetDate;
    this.eventText = text;
  }

  @Override
  public int compareTo(Object o) {
    Event target = (Event)o;
    if (!this.eventDate.equals(target.eventDate)) {
      return this.eventDate.compareTo(target.eventDate);
    } else {
      return this.eventText.compareTo(target.eventText);
    }
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Дата: ");
    sb.append(DateFormatter.formatDate(eventDate, TimeZone.getDefault()));
    sb.append(", пояснение: '").append(eventText).append('\'');
    sb.append('\r').append('\n');
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Event)) return false;
    if (((Event) obj).getEventText().equals(this.eventText)) return true;
    return false;
  }

  public Object clone() throws CloneNotSupportedException {
    Event clone = (Event)super.clone();
    clone.setEventDate(eventDate);
    clone.setEventText(eventText);
    return clone;
  }

  public Date getEventDate() {
    return eventDate;
  }

  public void setEventDate(Date eventDate) {
    this.eventDate = eventDate;
  }

  public String getEventText() {
    return eventText;
  }

  public void setEventText(String eventText) {
    this.eventText = eventText;
  }
}
