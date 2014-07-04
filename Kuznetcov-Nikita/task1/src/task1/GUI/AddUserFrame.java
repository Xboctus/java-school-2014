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
public class AddUserFrame extends TemplateFrame {

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

    okButton = new JButton("OK");
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String userName = nameInput.getText();
        String timeZoneID = timeZoneInput.getText();
        boolean userStatus = isUserActiveCheckBox.isSelected();

        if (userName.isEmpty() || timeZoneID.isEmpty()) {
          Coordinator.logger.warning("Fields cannot be empty!");
          showDialogByResponseStatus(ResponseStatus.EMPTY_FIELDS);
          return;
        }

        ResponseStatus status = taskCoordinator.addNewUser(userName, timeZoneID, userStatus);
        showDialogByResponseStatus(status);
      }
    });
    contentPanel.add(okButton);

    this.add(contentPanel);

    this.setBounds(400, 200, 400, 200);
    showFrame();
  }

}
