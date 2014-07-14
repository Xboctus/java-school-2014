package task1.Util;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * StreamHandler which prints log to specified text area
 */
public class TextAreaStreamHandler extends StreamHandler {

  public TextAreaStreamHandler(final JTextArea textArea, final java.util.logging.Formatter formatter) {
    this.setOutputStream(new OutputStream() {
      @Override
      public void write(int b) throws IOException { /* never user */ }
      @Override
      public void write(byte[] b, int off, int len) {
        textArea.append(new String(b, off, len));
      }
    });
    this.setFormatter(formatter);
  }

  @Override
  public synchronized void publish(LogRecord record) {
    super.publish(record);
    flush();
  }
}
