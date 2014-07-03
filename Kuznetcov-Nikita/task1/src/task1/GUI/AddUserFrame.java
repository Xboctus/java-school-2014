package task1.GUI;

import task1.Coordinator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Sunrise on 02.07.2014.
 */
public class AddUserFrame extends JFrame {

  private JPanel contentPanel;
  private JFormattedTextField nameInput;
  private JFormattedTextField timeZoneInput;
  private JCheckBox isUserActiveCheckBox;

  public AddUserFrame(final Coordinator taskCoordinator) {
    super("Add new user");

    contentPanel = new JPanel(new GridLayout(4, 2));

    JLabel userNameLabel = new JLabel("UserName");
    JLabel timeZoneLabel = new JLabel("Timezone");
    JLabel statusLabel = new JLabel("User status");
    nameInput = ComponentsFactory.getTextInputFieldInstance();
    timeZoneInput = ComponentsFactory.getTimeZoneInputFieldInstance();
    isUserActiveCheckBox = new JCheckBox();

    contentPanel.add(userNameLabel);
    contentPanel.add(nameInput);
    contentPanel.add(timeZoneLabel);
    contentPanel.add(timeZoneInput);
    contentPanel.add(statusLabel);
    contentPanel.add(isUserActiveCheckBox);

    final JButton okButton = new JButton("OK");
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String userName = nameInput.getText();
        String timeZoneID = timeZoneInput.getText();
        boolean userStatus = isUserActiveCheckBox.isSelected();

        if (userName.isEmpty() || timeZoneID.isEmpty()) {
          JOptionPane.showMessageDialog(contentPanel, "Fields cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
          return;
        }

        int result = taskCoordinator.addNewUser(userName, timeZoneID, userStatus);
        switch (result) {
          case 0: {
            JOptionPane.showMessageDialog(contentPanel, "User successfully added!", "Information", JOptionPane.INFORMATION_MESSAGE);
            break;
          }
          case 1: {
            JOptionPane.showMessageDialog(contentPanel, "User with such username already exists!", "Warning", JOptionPane.WARNING_MESSAGE);
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
