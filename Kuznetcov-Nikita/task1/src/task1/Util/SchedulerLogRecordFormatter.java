package task1.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by Sunrise on 03.07.2014.
 */
public class SchedulerLogRecordFormatter extends Formatter {

  private static final String LINE_SEPARATOR =System.getProperty("line.separator");
  private static final SimpleDateFormat formatter = new SimpleDateFormat("hh:MM:ss");

  @Override
  public String format(LogRecord record) {
    StringBuffer sb = new StringBuffer();
    if (record.getLevel().intValue() == Level.INFO.intValue()) {
      sb.append("<span style=\"color:blue;\">");
    }
    if (record.getLevel().intValue() == Level.WARNING.intValue()) {
      sb.append("<span style=\"color:orange;\">");
    }
    if (record.getLevel().intValue() == Level.SEVERE.intValue()) {
      sb.append("<span style=\"color:red;\">");
    }
    sb.append(formatter.format(new Date(record.getMillis()))).append(": ");
    sb.append(record.getMessage());
    sb.append("</span>").append(LINE_SEPARATOR);
    return sb.toString();
  }
}
