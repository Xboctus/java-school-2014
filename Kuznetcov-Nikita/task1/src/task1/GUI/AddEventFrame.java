package task1.GUI;

import task1.Coordinator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Sunrise on 02.07.2014.
 */
public class AddEventFrame extends JFrame {

  private JPanel contentPanel;
  private JFormattedTextField nameInput;
  private JFormattedTextField eventDateInput;
  private JFormattedTextField eventTextInput;

  public AddEventFrame(final Coordinator taskCoordinator) {
    super("Add event");

    contentPanel = new JPanel(new GridLayout(4, 2));

    JLabel userNameLabel = new JLabel("UserName");
    JLabel eventDateLabel = new JLabel("Event date");
    JLabel eventTextLabel = new JLabel("Event text");

    nameInput = ComponentsFactory.getTextInputFieldInstance();
    eventDateInput = ComponentsFactory.getDateInputFieldInstance();
    eventTextInput = ComponentsFactory.getTextInputFieldInstance();

    contentPanel.add(userNameLabel);
    contentPanel.add(nameInput);
    contentPanel.add(eventDateLabel);
    contentPanel.add(eventDateInput);
    contentPanel.add(eventTextLabel);
    contentPanel.add(eventTextInput);

    final JButton okButton = new JButton("OK");
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String userName = nameInput.getText();
        String eventDate = eventDateInput.getText();
        String eventText = eventTextInput.getText();

        if (userName.isEmpty() || eventDate.isEmpty() || eventText.isEmpty()) {
          JOptionPane.showMessageDialog(contentPanel, "Fields cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
          return;
        }

        int result = taskCoordinator.addEvent(userName, eventText, eventDate);
        switch (result) {
          case 0 : {
            JOptionPane.showMessageDialog(contentPanel, "Event successfully added to user " + userName + "'s event list!", "Information", JOptionPane.INFORMATION_MESSAGE); break;
          }
          case 1 : {
            JOptionPane.showMessageDialog(contentPanel, "User with such username doesn't exists!", "Warning", JOptionPane.WARNING_MESSAGE); break;
          }
          case 2 : {
            JOptionPane.showMessageDialog(contentPanel, "Bad date format! Acceptable format: \"dd.MM.yyyy-HH:mm:ss\"", "Warning", JOptionPane.WARNING_MESSAGE); break;
          }
          case 3 : {
            JOptionPane.showMessageDialog(contentPanel, "Event with specified text already exists!", "Warning", JOptionPane.WARNING_MESSAGE); break;
          }
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
