package task1.GUI;

import task1.Coordinator;
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

  public SchedulerFrame(String frameName, Coordinator coordinator) {
    super(frameName);
    taskCoordinator = coordinator;
    Logger logger = Coordinator.logger;
    logger.setUseParentHandlers(false);

    mainPanel = new JPanel(new BorderLayout());
    buttonsPanel = new JPanel(new GridLayout(11, 1, 5, 0));

    logArea = new JTextArea("Log will be here\r\n", 30, 15);
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
        String userInfo = taskCoordinator.getUserInfo(JOptionPane.showInputDialog("Enter user name"));
        if (userInfo != null) {
          if (!userInfo.isEmpty()) {
            logArea.append(userInfo);
          } else {
            JOptionPane.showMessageDialog(mainPanel, "User with such username not found!", "Information", JOptionPane.INFORMATION_MESSAGE);
          }
        }
      }
    });
    JButton saveStateButton = new JButton("Save current state");
    saveStateButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String path = JOptionPane.showInputDialog("Enter path to file");
        if (path.isEmpty()) {
          JOptionPane.showMessageDialog(mainPanel, "Path cannot be null!", "Warning", JOptionPane.WARNING_MESSAGE);
          return;
        }
        try {
          File output = new File(path);
          if (output.createNewFile()) {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output));
            bos.write(taskCoordinator.getCurrentState().toJSONString().getBytes());
            bos.flush();
          }
        } catch (IOException ioex) {
          JOptionPane.showMessageDialog(mainPanel, "TERRIBLE ERROR", "ERROR", JOptionPane.ERROR_MESSAGE);
          ioex.printStackTrace();
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
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            StringBuilder sb = new StringBuilder();
            while (reader.ready()) {
              sb.append(reader.readLine());
            }
            reader.close();
            taskCoordinator.parseStateJSON(sb.toString());
          } catch (FileNotFoundException fnfex) {
            System.out.println("File not found!");
          } catch (IOException ioex) {
            System.out.println("Input exception!");
          }
        } else {
          JOptionPane.showMessageDialog(null, "File not selected", "Warning", JOptionPane.WARNING_MESSAGE);
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

    this.add(mainPanel, BorderLayout.CENTER);

    pack();
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setBounds(300, 300, 500, 600);
    setResizable(false);
    setVisible(true);
    taskCoordinator.startScheduling();
  }

}
