package task1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: student7
 * Date: 30.06.14
 * Time: 18:54
 */
public class SchedulerFrame extends JFrame {

  private ArrayList<JButton> buttons = new ArrayList<JButton>();
  private JPanel mainPanel;
  private JPanel buttonsPanel;
  private JTextArea logArea;
  private static final SimpleDateFormat formatter = new SimpleDateFormat("hh:MM:ss");

  public SchedulerFrame(String frameName) {
    super(frameName);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setResizable(false);
    setLocation(300, 200);

    mainPanel = new JPanel(new BorderLayout());
    buttonsPanel = new JPanel(new GridLayout(8, 1, 5, 10));

    logArea = new JTextArea("Log will be here", 40, 15);
    logArea.setLineWrap(true);
    logArea.setWrapStyleWord(true);
    mainPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);

    JButton addUserButton = new JButton("Add new user");
    JButton modifyUserButton = new JButton("Modify exists user");
    JButton addEventButton = new JButton("Add event");
    JButton removeEventButton = new JButton("Remove event");
    JButton addRandomTimeEventButton = new JButton("Add random time event");
    JButton cloneEventButton = new JButton("Clone event");
    JButton showUserInfoButton = new JButton("Show user info");
    JButton startSchedulingButton = new JButton("Start scheduling");

    buttons.add(addUserButton);
    buttons.add(modifyUserButton);
    buttons.add(addEventButton);
    buttons.add(removeEventButton);
    buttons.add(addRandomTimeEventButton);
    buttons.add(cloneEventButton);
    buttons.add(showUserInfoButton);
    buttons.add(startSchedulingButton);

    for (JButton controlButton : buttons) {
      controlButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          logArea.append("\r\n" + ((JButton) e.getSource()).getText());
        }
      });
      buttonsPanel.add(controlButton);
    }
    mainPanel.add(buttonsPanel, BorderLayout.EAST);

    this.add(mainPanel);

    pack();
    setVisible(true);
  }

}
