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
public class RemoveEventFrame extends TemplateFrame {

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

    okButton = new JButton("OK");
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String userName = nameInput.getText();
        String taskText = taskTextInput.getText();
        if (userName.isEmpty() || taskText.isEmpty()) {
          JOptionPane.showMessageDialog(contentPanel, "Fields cannot be empty", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        ResponseStatus status = taskCoordinator.removeEvent(userName, taskText);
        showDialogByResponseStatus(status);
      }
    });
    contentPanel.add(okButton);

    this.add(contentPanel);

    this.setBounds(400, 200, 400, 200);
    showFrame();
  }

}
