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
    nameInput = ComponentsFactory.getUserNameInputFieldInstance();
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
          JOptionPane.showMessageDialog(contentPanel, "Empty fields tard", "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }
        taskCoordinator.addNewUser(userName, timeZoneID, userStatus);
        System.out.println(taskCoordinator.getUsersMap());
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
