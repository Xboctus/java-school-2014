package task1.GUI;

import task1.Coordinator;
import task1.Util.ResponseStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Sunrise on 02.07.2014.
 */
public class AddEventFrame extends TemplateFrame {

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

    okButton = new JButton("OK");
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

        ResponseStatus status = taskCoordinator.addEvent(userName, eventText, eventDate);
        showDialogByResponseStatus(status);
      }
    });
    contentPanel.add(okButton);

    this.add(contentPanel);

    this.setBounds(400, 200, 400, 250);
    showFrame();
  }
}
