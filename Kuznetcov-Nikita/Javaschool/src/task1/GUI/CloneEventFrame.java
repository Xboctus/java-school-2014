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
public class CloneEventFrame extends TemplateFrame {

  private JFormattedTextField srcUserNameInput;
  private JFormattedTextField srcTaskTextInput;
  private JFormattedTextField targetUserNameInput;

  public CloneEventFrame(final Coordinator taskCoordinator) {
    super("Clone event");
    contentPanel = new JPanel(new GridLayout(4, 2));

    srcUserNameInput = ComponentsFactory.getTextInputFieldInstance();
    srcTaskTextInput = ComponentsFactory.getTextInputFieldInstance();
    targetUserNameInput = ComponentsFactory.getTextInputFieldInstance();

    contentPanel.add(new JLabel("Source user name"));
    contentPanel.add(srcUserNameInput);
    contentPanel.add(new JLabel("Source task text"));
    contentPanel.add(srcTaskTextInput);
    contentPanel.add(new JLabel("Target user name"));
    contentPanel.add(targetUserNameInput);

    okButton = new JButton("OK");
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String srcUserName = srcUserNameInput.getText();
        String srcTaskText = srcTaskTextInput.getText();
        String targetUserName = targetUserNameInput.getText();
        if (srcUserName.isEmpty() || srcTaskText.isEmpty() || targetUserName.isEmpty()) {
          showDialogByResponseStatus(ResponseStatus.EMPTY_FIELDS);
        }
        ResponseStatus status = taskCoordinator.cloneEvent(srcUserName, srcTaskText, targetUserName);
        showDialogByResponseStatus(status);
      }
    });
    contentPanel.add(okButton);

    this.add(contentPanel);

    this.setBounds(400, 200, 400, 300);
    showFrame();
  }
}
