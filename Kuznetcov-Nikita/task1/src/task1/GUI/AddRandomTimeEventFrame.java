package task1.GUI;

import task1.Coordinator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Sunrise on 03.07.2014.
 */
public class AddRandomTimeEventFrame extends JFrame {

  private JPanel contentPanel;
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

    JButton okButton = new JButton("OK");
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String userName = userNameInput.getText();
        String taskText = taskTextInput.getText();
        String dateFrom = dateFromTextInput.getText();
        String dateTo = dateToTextInput.getText();
        if (userName.isEmpty() || taskText.isEmpty() || dateFrom.isEmpty() || dateTo.isEmpty()) {
          JOptionPane.showMessageDialog(contentPanel, "Fields cannot be empty", "Warning", JOptionPane.WARNING_MESSAGE);
          return;
        }
        int result = taskCoordinator.addRandomTimeEvent(userName, taskText, dateFrom, dateTo);
        if (result == 0) {
          JOptionPane.showMessageDialog(contentPanel, "OK", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    });
    contentPanel.add(okButton);

    this.add(contentPanel);

    pack();
    setLocation(400, 200);
    setDefaultCloseOperation(HIDE_ON_CLOSE);
    setVisible(true);
  }
}
