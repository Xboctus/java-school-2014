package task1.GUI;

import task1.Coordinator;

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

  private JPanel mainPanel;
  private JPanel buttonsPanel;
  private JTextArea logArea;

  private static final Coordinator taskCoordinator = new Coordinator();
  private static final SimpleDateFormat formatter = new SimpleDateFormat("hh:MM:ss");

  public SchedulerFrame(String frameName) {
    super(frameName);

    mainPanel = new JPanel(new BorderLayout());
    buttonsPanel = new JPanel(new GridLayout(8, 1, 5, 0));

    logArea = new JTextArea("Log will be here\r\n", 40, 15);
    logArea.setLineWrap(true);
    logArea.setWrapStyleWord(true);
    mainPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);

    JButton addUserButton = new JButton("Add new user");
    addUserButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new AddUserFrame(taskCoordinator);
      }
    });
    JButton modifyUserButton = new JButton("Modify exists user");
    modifyUserButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new ModifyUserFrame(taskCoordinator);
      }
    });
    JButton addEventButton = new JButton("Add event");
    addEventButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new AddEventFrame(taskCoordinator);
      }
    });
    JButton removeEventButton = new JButton("Remove event");
    removeEventButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new RemoveEventFrame(taskCoordinator);
      }
    });

    JButton addRandomTimeEventButton = new JButton("Add random time event");
    addRandomTimeEventButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new AddRandomTimeEventFrame(taskCoordinator);
      }
    });
    // TODO JButton cloneEventButton = new JButton("Clone event");

    JButton showUserInfoButton = new JButton("Show user info");
    showUserInfoButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String userInfo = taskCoordinator.getUserInfo(JOptionPane.showInputDialog("Enter user name"));
        if (userInfo != null) {
          logArea.append(userInfo);
        } else {
          JOptionPane.showMessageDialog(mainPanel, "User with such username not found!", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    });
    JButton startSchedulingButton = new JButton("Start scheduling");
    startSchedulingButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        taskCoordinator.startScheduling();
      }
    });

    ArrayList<JButton> buttons = new ArrayList<JButton>();
    buttons.add(addUserButton);
    buttons.add(modifyUserButton);
    buttons.add(addEventButton);
    buttons.add(removeEventButton);
    buttons.add(addRandomTimeEventButton);
//    buttons.add(cloneEventButton);
    buttons.add(showUserInfoButton);
    buttons.add(startSchedulingButton);

    for (JButton controlButton : buttons) {
      controlButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          logArea.append(((JButton) e.getSource()).getText() + "\r\n");
        }
      });
      buttonsPanel.add(controlButton);
    }
    mainPanel.add(buttonsPanel, BorderLayout.EAST);

    this.add(mainPanel);

    pack();
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLocation(300, 200);
    setResizable(false);
    setVisible(true);
  }

}
