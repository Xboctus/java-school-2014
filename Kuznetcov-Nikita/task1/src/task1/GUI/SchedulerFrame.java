package task1.GUI;

import task1.Coordinator;
import task1.Util.FileManager;
import task1.Util.SchedulerLogRecordFormatter;
import task1.Util.TextAreaStreamHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

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
  private final Coordinator taskCoordinator;
  private static int instanceNum = 1;

  public SchedulerFrame(String frameName, final Coordinator coordinator) {
    super(frameName);
    taskCoordinator = coordinator;
    final Logger logger = coordinator.logger;
    logger.setUseParentHandlers(false);
    instanceNum++;

    mainPanel = new JPanel(new BorderLayout());
    buttonsPanel = new JPanel(new GridLayout(7, 2, 5, 0));

    logArea = new JTextArea("Log will be here\r\n", 30, 20);
    logArea.setLineWrap(true);
    logArea.setWrapStyleWord(true);
    logger.addHandler(new TextAreaStreamHandler(logArea, new SchedulerLogRecordFormatter()));
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
    JButton cloneEventButton = new JButton("Clone event");
    cloneEventButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new CloneEventFrame(taskCoordinator);
      }
    });
    JButton showUserInfoButton = new JButton("Show user info");
    showUserInfoButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String userInfo = coordinator.getUserInfo(JOptionPane.showInputDialog("Enter user name"));
        if (!userInfo.isEmpty())
          logArea.append(userInfo);
        else
          JOptionPane.showMessageDialog(null, "User with such username not found!", "Information", JOptionPane.INFORMATION_MESSAGE);
      }
    });
    JButton saveStateButton = new JButton("Save current state");
    saveStateButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String path = JOptionPane.showInputDialog("Enter path to file");
        try {
          if (FileManager.writeStringToFile(taskCoordinator.getCurrentState().toJSONString(), path))
            JOptionPane.showMessageDialog(null, "File successfully created!", "Information", JOptionPane.INFORMATION_MESSAGE);
          else
            JOptionPane.showMessageDialog(null, "File with such path already exists!", "Warning", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ioex) {
          JOptionPane.showMessageDialog(null, "Error when saving current scheduler state!", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    JButton loadStateButton = new JButton("load new state from file");
    loadStateButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        int status = chooser.showDialog(null, "Open file!");
        if (status == JFileChooser.APPROVE_OPTION) {
          File inputFile = chooser.getSelectedFile();
          try {
            if (taskCoordinator.parseStateJSON(FileManager.readTextDataFromRile(inputFile)))
              logger.info("State recovered successfully");
            else
              logger.warning("Bad input file format!");
          } catch (FileNotFoundException fnfex) {
            logger.warning("File not found!");
          } catch (IOException ioex) {
            logger.severe("Error when reading a file");
          }
        }
      }
    });
    JButton syncButton = new JButton("Sync");
    syncButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String host = JOptionPane.showInputDialog(null, "Enter host for sync");
        int port = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter port for sync"));
        taskCoordinator.sync(host, port);
      }
    });
    JButton socketInfo = new JButton("Socket");
    socketInfo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "Local port is " + taskCoordinator.getSocketLocalPort(), "Info", JOptionPane.INFORMATION_MESSAGE);
      }
    });
    JButton newInstanceButton = new JButton("New instance");
    newInstanceButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            new SchedulerFrame("An " + SchedulerFrame.instanceNum + " instance", new Coordinator());
          }
        }).start();
      }
    });
    JButton saveStateToDBButton = new JButton("save to DB");
    saveStateToDBButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (taskCoordinator.saveStateToDB())
          JOptionPane.showMessageDialog(null, "State saved to DB", "Information", JOptionPane.INFORMATION_MESSAGE);
        else
          JOptionPane.showMessageDialog(null, "Error when saving state to DB", "Error", JOptionPane.ERROR_MESSAGE);
      }
    });
    JButton loadFromDBButton = new JButton("load from DB");
    loadFromDBButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (taskCoordinator.loadStateFromDB())
          JOptionPane.showMessageDialog(null, "State loaded from DB", "Information", JOptionPane.INFORMATION_MESSAGE);
        else
          JOptionPane.showMessageDialog(null, "Error when loading state to DB", "Error", JOptionPane.ERROR_MESSAGE);
      }
    });

    ArrayList<JButton> buttons = new ArrayList<JButton>();
    buttons.add(addUserButton);
    buttons.add(modifyUserButton);
    buttons.add(addEventButton);
    buttons.add(removeEventButton);
    buttons.add(addRandomTimeEventButton);
    buttons.add(cloneEventButton);
    buttons.add(showUserInfoButton);
    buttons.add(saveStateButton);
    buttons.add(loadStateButton);
    buttons.add(syncButton);
    buttons.add(socketInfo);
    buttons.add(newInstanceButton);
    buttons.add(saveStateToDBButton);
    buttons.add(loadFromDBButton);

    for (JButton controlButton : buttons) buttonsPanel.add(controlButton);
    mainPanel.add(buttonsPanel, BorderLayout.EAST);

    this.add(mainPanel, BorderLayout.CENTER);

    pack();
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setBounds(200, 200, 700, 600);
    setResizable(false);
    setVisible(true);
    taskCoordinator.startScheduling();
  }
}
