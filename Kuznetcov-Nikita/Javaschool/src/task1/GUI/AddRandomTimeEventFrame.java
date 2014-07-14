package task1.GUI;

import task1.Coordinator;
import task1.Util.ResponseStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Sunrise on 03.07.2014.
 */
public class AddRandomTimeEventFrame extends TemplateFrame {

  private JFormattedTextField userNameInput;
  private JFormattedTextField taskTextInput;
  private JFormattedTextField dateFromTextInput;
  private JFormattedTextField dateToTextInput;

  public AddRandomTimeEventFrame(final Coordinator taskCoordinator) {
    super("Add random time event");

    contentPanel = new JPanel(new GridLayout(5, 2));

    userNameInput = ComponentsFactory.getTextInputFieldInstance();
    taskTextInput = ComponentsFactory.getTextInputFieldInstance();
    dateFromTextInput = ComponentsFactory.getDateInputFieldInstance();
    dateToTextInput = ComponentsFactory.getDateInputFieldInstance();

    contentPanel.add(new JLabel("User name"));
    contentPanel.add(userNameInput);
    contentPanel.add(new JLabel("Task text"));
    contentPanel.add(taskTextInput);
    contentPanel.add(new JLabel("Event date from"));
    contentPanel.add(dateFromTextInput);
    contentPanel.add(new JLabel("Event date to"));
    contentPanel.add(dateToTextInput);

    okButton = new JButton("OK");
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String userName = userNameInput.getText();
        String taskText = taskTextInput.getText();
        String dateFrom = dateFromTextInput.getText();
        String dateTo = dateToTextInput.getText();
        if (userName.isEmpty() || taskText.isEmpty() || dateFrom.isEmpty() || dateTo.isEmpty()) {
          showDialogByResponseStatus(ResponseStatus.EMPTY_FIELDS);
          return;
        }
        ResponseStatus status = taskCoordinator.addRandomTimeEvent(userName, taskText, dateFrom, dateTo);
        showDialogByResponseStatus(status);
      }
    });
    contentPanel.add(okButton);

    this.add(contentPanel);

    this.setBounds(400, 200, 400, 300);
    showFrame();
  }
}
