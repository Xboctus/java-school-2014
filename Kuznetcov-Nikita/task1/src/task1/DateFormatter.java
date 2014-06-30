package task1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Sunrise on 28.06.2014.
 */
public class DateFormatter {

  public static String formatDate(Date date, TimeZone timeZone) {
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
    formatter.setTimeZone(timeZone);
    return formatter.format(date);
  }

  public static Date parseDate(String input, TimeZone timeZone) {
    Date result = null;
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
    format.setTimeZone(timeZone);
    try {
      result = format.parse(input);
    } catch (ParseException ex) {
      // Unparseable date, null will be returned
    }
    return result;
  }

}
