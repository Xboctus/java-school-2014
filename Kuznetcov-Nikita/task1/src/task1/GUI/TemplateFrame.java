package task1.GUI;

import task1.Util.ResponseStatus;

import javax.swing.*;

/**
 * Created by Sunrise on 04.07.2014.
 */
public class TemplateFrame extends JFrame {

  protected JPanel contentPanel;
  protected JButton okButton;

  public TemplateFrame(String title) {
    super(title);
  }

  protected void showFrame() {
//    this.pack();
    this.setDefaultCloseOperation(HIDE_ON_CLOSE);
    this.setVisible(true);
  }

  protected void showDialogByResponseStatus(ResponseStatus status) {
    switch (status) {
      case USER_ADDED: {
        JOptionPane.showMessageDialog(contentPanel, "User successfully added!", "Information", JOptionPane.INFORMATION_MESSAGE);
        break;
      }
      case USER_MODIFIED: {
        JOptionPane.showMessageDialog(contentPanel, "User info successfully updated", "Information", JOptionPane.INFORMATION_MESSAGE);
        break;
      }
      case EVENT_ADDED: {
        JOptionPane.showMessageDialog(contentPanel, "Event successfully added!", "Information", JOptionPane.INFORMATION_MESSAGE);
        break;
      }
      case EVENT_REMOVED: {
        JOptionPane.showMessageDialog(contentPanel, "Event successfully removed!", "Information", JOptionPane.INFORMATION_MESSAGE);
        break;
      }
      case EVENT_CLONED: {
        JOptionPane.showMessageDialog(contentPanel, "Event successfully cloned!", "Information", JOptionPane.INFORMATION_MESSAGE);
        break;
      }
      case USER_ALREADY_EXISTS: {
        JOptionPane.showMessageDialog(contentPanel, "User with such username already exists!", "Warning", JOptionPane.WARNING_MESSAGE);
        break;
      }
      case USER_NOT_FOUND: {
        JOptionPane.showMessageDialog(contentPanel, "User not found!", "Warning", JOptionPane.WARNING_MESSAGE);
        break;
      }
      case TARGET_USER_NOT_FOUND: {
        JOptionPane.showMessageDialog(contentPanel, "Target user not found!", "Warning", JOptionPane.WARNING_MESSAGE);
        break;
      }
      case EVENT_NOT_FOUND: {
        JOptionPane.showMessageDialog(contentPanel, "Event with such text not found!", "Warning", JOptionPane.WARNING_MESSAGE);
        break;
      }
      case EVENT_ALREADY_EXISTS: {
        JOptionPane.showMessageDialog(contentPanel, "Event with specified text already exists!", "Warning", JOptionPane.WARNING_MESSAGE);
        break;
      }
      case BAD_DATE_FORMAT: {
        JOptionPane.showMessageDialog(contentPanel, "Bad date format! Acceptable format: \"dd.MM.yyyy-HH:mm:ss\"", "Warning", JOptionPane.WARNING_MESSAGE);
        break;
      }
      case EMPTY_FIELDS: {
        JOptionPane.showMessageDialog(contentPanel, "Fields cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        break;
      }
      case BAD_TIMEZONE_FORMAT: {
        // not possible in gui version
      }
      case WRONG_DATE_DIFFERENCE: {
        // not possible in gui version
      }
    }
  }
}
