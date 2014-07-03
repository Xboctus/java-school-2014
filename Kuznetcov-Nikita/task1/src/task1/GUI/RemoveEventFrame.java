package task1.GUI;

import task1.Coordinator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Sunrise on 02.07.2014.
 */
public class RemoveEventFrame extends JFrame {

  private JPanel contentPanel;
  private JFormattedTextField nameInput;
  private JFormattedTextField taskTextInput;

  public RemoveEventFrame(final Coordinator taskCoordinator) {
    super("Remove event");
    contentPanel = new JPanel(new GridLayout(3, 2));

    JLabel userNameLabel = new JLabel("User name");
    JLabel taskTextLabel = new JLabel("Task text");

    nameInput = ComponentsFactory.getTextInputFieldInstance();
    taskTextInput = ComponentsFactory.getTextInputFieldInstance();

    contentPanel.add(userNameLabel);
    contentPanel.add(nameInput);
    contentPanel.add(taskTextLabel);
    contentPanel.add(taskTextInput);

    JButton okButton = new JButton("OK");
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String userName = nameInput.getText();
        String taskText = taskTextInput.getText();
        if (userName.isEmpty() || taskText.isEmpty()) {
          JOptionPane.showMessageDialog(contentPanel, "Fields cannot be empty", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        int result = taskCoordinator.removeEvent(userName, taskText);
        switch (result) {
          case 0: {
            JOptionPane.showMessageDialog(contentPanel, "Event successfully removed!", "Information", JOptionPane.INFORMATION_MESSAGE);
            break;
          }
          case 1: {
            JOptionPane.showMessageDialog(contentPanel, "User with such username not found!", "Warning", JOptionPane.WARNING_MESSAGE);
            break;
          }
          case 2: {
            JOptionPane.showMessageDialog(contentPanel, "Event with such text not found!", "Warning", JOptionPane.WARNING_MESSAGE);
            break;
          }
        }
      }
    });
    contentPanel.add(okButton);

    this.add(contentPanel);

    pack();
    this.setBounds(400, 200, 400, 200);
    setDefaultCloseOperation(HIDE_ON_CLOSE);
    setVisible(true);
  }

}
