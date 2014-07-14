package task1;

import org.json.simple.JSONObject;
import task1.Util.DateFormatter;

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
    if (this.getEventText().equals(target.getEventText())) return 0;

    if (!this.eventDate.equals(target.eventDate)) {
      return this.eventDate.compareTo(target.eventDate);
    } else {
      return this.eventText.compareTo(target.eventText);
    }
  }

  @Override
  public String toString() {
    return "Date: " + DateFormatter.formatDate(eventDate, TimeZone.getDefault()) + ", event text: '" + eventText + '\'' + "\r\n";
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Event && ((Event) obj).getEventText().equals(this.eventText);
  }

  public JSONObject getEventAsJSON() {
    JSONObject object = new JSONObject();
    object.put("eventDate", eventDate.getTime());
    object.put("eventText", eventText);
    return object;
  }

  public Object clone() throws CloneNotSupportedException {
    return new Event(this.eventDate, this.eventText);
  }

  public Date getEventDate() {
    return eventDate;
  }

  public String getEventText() {
    return eventText;
  }

}
