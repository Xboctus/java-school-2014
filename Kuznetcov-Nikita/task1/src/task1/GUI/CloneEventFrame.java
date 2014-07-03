package task1.GUI;

import task1.Coordinator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Sunrise on 03.07.2014.
 */
public class CloneEventFrame extends JFrame {

  private JPanel contentPanel;
  private JFormattedTextField srcUserNameInput;
  private JFormattedTextField srcTaskTextInput;
  private JFormattedTextField targetUserNameInput;

  public CloneEventFrame(final Coordinator taskCoordinator) {
    super("Clone event");

    srcUserNameInput = ComponentsFactory.getTextInputFieldInstance();
    srcTaskTextInput = ComponentsFactory.getTextInputFieldInstance();
    targetUserNameInput = ComponentsFactory.getTextInputFieldInstance();

    contentPanel.add(new JLabel("Source user name"));
    contentPanel.add(srcUserNameInput);
    contentPanel.add(new JLabel("Source task text"));
    contentPanel.add(srcTaskTextInput);
    contentPanel.add(new JLabel("Target user name"));
    contentPanel.add(targetUserNameInput);

    JButton okButton = new JButton("OK");
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String srcUserName = srcUserNameInput.getText();
        String srcTaskText = srcTaskTextInput.getText();
        String targetUserName = targetUserNameInput.getText();
        if (srcUserName.isEmpty() || srcTaskText.isEmpty() || targetUserName.isEmpty()) {
          JOptionPane.showMessageDialog(contentPanel, "Fields cannot be empty", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        int result = taskCoordinator.cloneEvent(srcUserName, srcTaskText, targetUserName);
        if (result == 0) {
          JOptionPane.showMessageDialog(contentPanel, "OK", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    });

    pack();
    setLocation(400, 200);
    setDefaultCloseOperation(HIDE_ON_CLOSE);
    setVisible(true);
  }
}
