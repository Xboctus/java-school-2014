package task1.GUI;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sunrise on 02.07.2014.
 */
public class ComponentsFactory {

  public static JFormattedTextField getUserNameInputFieldInstance() {
    JFormattedTextField userNameTextField = new JFormattedTextField(new RegExFormatter("^[^0-9][a-z|A-Z|А-Я|а-я|[0-9]]+"));
    userNameTextField.setToolTipText("User name, maximum 255 characters");
    return userNameTextField;
  }

  public static JFormattedTextField getTimeZoneInputFieldInstance() {
    JFormattedTextField timeZoneTextField = new JFormattedTextField(new RegExFormatter("GMT[+-][0-2]?[0-9](:[0-5]?[0-9])?"));
    timeZoneTextField.setToolTipText("TimeZone, format \"GMT Sign 5:30\" or \"GMT Sign 5\"");
    return timeZoneTextField;
  }

}

class RegExFormatter extends DefaultFormatter {

  private Matcher matcher;

  public RegExFormatter(String regEx) {
    Pattern pattern = Pattern.compile(regEx);
    this.matcher = pattern.matcher("");
    setOverwriteMode(false);
  }

  @Override
  public Object stringToValue(String string) throws ParseException {

    if (string == null) return null;

    matcher.reset(string);

    if (!matcher.matches()) {
      throw new ParseException("Input does not match template!", 0);
    }

    return super.stringToValue(string);
  }
}
