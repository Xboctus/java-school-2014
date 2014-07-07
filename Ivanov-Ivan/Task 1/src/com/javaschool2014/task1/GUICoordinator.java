package com.javaschool2014.task1;

import javax.swing.*;
import javax.swing.plaf.FileChooserUI;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;

public class GUICoordinator extends AbstractCoordinator {

    private String appName             = "Planfx v0.1";
    private JFrame newFrame            = new JFrame(appName);
    private final JTextArea textArea   = new JTextArea();

    @Override
    protected void printOutput(String string) {
        textArea.append(string + "\n");
    }

    @Override
    public void start() {

        getTimer().scheduleAtFixedRate(this, 0, 1000);
        createServer();

    }

    private MaskFormatter getMask(String mask, String input){

        MaskFormatter maskFormatter = null;

        try {

            maskFormatter = new MaskFormatter(mask);
            maskFormatter.setValidCharacters(input);
            maskFormatter.setPlaceholderCharacter('*');
            maskFormatter.setAllowsInvalid(false);

        } catch (ParseException e) {

            printOutput(e.getMessage());

        }

        return maskFormatter;

    }

    public void display() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(1, 1, 0, 0));
        leftPanel.setPreferredSize(new Dimension(400, 460));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(0, 1, 0, 15));

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Serif", Font.PLAIN, 14));

        final ActionListener createListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createForm();
            }
        };

        final ActionListener modifyListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyForm();
            }
        };

        final ActionListener addEventListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEventForm();
            }
        };

        final ActionListener addRandomTimeEventListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRandomTimeEventForm();
            }
        };

        final ActionListener removeEventListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeEventForm();
            }
        };

        final ActionListener cloneEventListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cloneEventForm();
            }
        };

        final ActionListener showInfoListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInfoForm();
            }
        };

        final ActionListener saveDataListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDataForm();
            }
        };

        final ActionListener loadDataListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDataForm();
            }
        };

        final ActionListener syncDataListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                syncDataForm();
            }
        };

        final ActionListener showPortListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getServerPort();
            }
        };

        JButton create             = new JButton("Create");
        JButton modify             = new JButton("Modify");
        JButton addEvent           = new JButton("AddEvent");
        JButton addRandomTimeEvent = new JButton("AddRandomTimeEvent");
        JButton removeEvent        = new JButton("RemoveEvent");
        JButton cloneEvent         = new JButton("CloneEvent");
        JButton showInfo           = new JButton("ShowInfo");
        JButton saveData           = new JButton("SaveData");
        JButton loadData           = new JButton("LoadData");
        JButton syncData           = new JButton("SyncData");
        JButton showPort           = new JButton("ShowPort");

        create.addActionListener(createListener);
        modify.addActionListener(modifyListener);
        addEvent.addActionListener(addEventListener);
        addRandomTimeEvent.addActionListener(addRandomTimeEventListener);
        removeEvent.addActionListener(removeEventListener);
        cloneEvent.addActionListener(cloneEventListener);
        showInfo.addActionListener(showInfoListener);
        saveData.addActionListener(saveDataListener);
        loadData.addActionListener(loadDataListener);
        syncData.addActionListener(syncDataListener);
        showPort.addActionListener(showPortListener);

        rightPanel.add(create);
        rightPanel.add(modify);
        rightPanel.add(addEvent);
        rightPanel.add(addRandomTimeEvent);
        rightPanel.add(removeEvent);
        rightPanel.add(cloneEvent);
        rightPanel.add(showInfo);
        rightPanel.add(saveData);
        rightPanel.add(loadData);
        rightPanel.add(syncData);
        rightPanel.add(showPort);

        leftPanel.add(new JScrollPane(textArea));

        mainPanel.add(BorderLayout.WEST, leftPanel);
        mainPanel.add(rightPanel);

        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.setSize(540, 460);
        newFrame.setResizable(false);
        newFrame.setVisible(true);

    }

    public void createForm() {

        String[] items = {"active", "idle"};
        JComboBox combo = new JComboBox(items);
        JTextField field1 = new JTextField("");
        JTextField field2 = new JFormattedTextField(getMask("GMT**","1234567890-+"));
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(field1);
        panel.add(new JLabel("GMT Timezone:"));
        panel.add(field2);
        panel.add(new JLabel("Status:"));
        panel.add(combo);

        int result = JOptionPane.showConfirmDialog(null, panel, "Create new user",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (createUser(field1.getText(), field2.getText(), combo.getSelectedItem().toString())) {
                printOutput(USER_CREATED);
            } else {
                printOutput(ERROR);
            }
        } else {
            printOutput(CANCELLED);
        }

    }

    public void modifyForm() {

        String[] items = {"active", "idle"};
        JComboBox combo = new JComboBox(items);
        JTextField field1 = new JTextField("");
        JTextField field2 = new JFormattedTextField(getMask("GMT**","1234567890-+"));
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(field1);
        panel.add(new JLabel("GMT Timezone:"));
        panel.add(field2);
        panel.add(new JLabel("Status:"));
        panel.add(combo);

        int result = JOptionPane.showConfirmDialog(null, panel, "Modify existing user",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (modifyUser(field1.getText(), field2.getText(), combo.getSelectedItem().toString())) {
                printOutput(USER_MODIFIED);
            } else {
                printOutput(ERROR);
            }
        } else {
            printOutput(CANCELLED);
        }

    }

    public void addEventForm() {

        JTextField field1 = new JTextField("");
        JTextField field2 = new JTextField("");
        JTextField field3 = new JFormattedTextField(getMask("##.##.####-##:##:##","1234567890"));
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(field1);
        panel.add(new JLabel("Event text:"));
        panel.add(field2);
        panel.add(new JLabel("Event time:"));
        panel.add(field3);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add new event for user",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (addUserEvent(field1.getText(), field2.getText(), field3.getText())) {
                printOutput(EVENT_ADDED);
            } else {
                printOutput(ERROR);
            }
        } else {
            printOutput(CANCELLED);
        }

    }

    public void addRandomTimeEventForm() {

        JTextField field1 = new JTextField("");
        JTextField field2 = new JTextField("");
        JTextField field3 = new JFormattedTextField(getMask("##.##.####-##:##:##","1234567890"));
        JTextField field4 = new JFormattedTextField(getMask("##.##.####-##:##:##","1234567890"));
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(field1);
        panel.add(new JLabel("Event text:"));
        panel.add(field2);
        panel.add(new JLabel("Event 'from' time:"));
        panel.add(field3);
        panel.add(new JLabel("Event 'to' time:"));
        panel.add(field4);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add new event for user",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (addRandomTimeUserEvent(field1.getText(), field2.getText(), field3.getText(), field4.getText())) {
                printOutput(RANDOM_EVENT_ADDED);
            } else {
                printOutput(ERROR);
            }
        } else {
            printOutput(CANCELLED);
        }

    }

    public void removeEventForm() {

        JTextField field1 = new JTextField("");
        JTextField field2 = new JTextField("");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(field1);
        panel.add(new JLabel("Event text:"));
        panel.add(field2);

        int result = JOptionPane.showConfirmDialog(null, panel, "Remove event",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (removeUserEvent(field1.getText(), field2.getText())) {
                printOutput(EVENT_REMOVED);
            } else {
                printOutput(ERROR);
            }
        } else {
            printOutput(CANCELLED);
        }

    }

    public void cloneEventForm() {

        JTextField field1 = new JTextField("");
        JTextField field2 = new JTextField("");
        JTextField field3 = new JTextField("");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("From name:"));
        panel.add(field1);
        panel.add(new JLabel("Event text:"));
        panel.add(field2);
        panel.add(new JLabel("To name:"));
        panel.add(field3);

        int result = JOptionPane.showConfirmDialog(null, panel, "Clone event",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (cloneUserEvent(field1.getText(), field2.getText(), field3.getText())) {
                printOutput(EVENT_CLONED);
            } else {
                printOutput(ERROR);
            }
        } else {
            printOutput(CANCELLED);
        }

    }

    public void showInfoForm() {

        JTextField field1 = new JTextField("");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(field1);

        int result = JOptionPane.showConfirmDialog(null, panel, "Show user info",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (!showUserInfo(field1.getText())) {
                printOutput(ERROR);
            }
        } else {
            printOutput(CANCELLED);
        }

    }

    public void saveDataForm() {

        JFileChooser fileChosen = new JFileChooser();

        int ret = fileChosen.showDialog(null, "Save file");

        if (ret == JFileChooser.APPROVE_OPTION) {

            File file = fileChosen.getSelectedFile();

            if (!saveUserData(file.getName())) {
                printOutput(ERROR);
            }

        } else {

            printOutput(CANCELLED);

        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(fileChosen);

    }

    public void loadDataForm() {

        JFileChooser fileChosen = new JFileChooser();

        int ret = fileChosen.showDialog(null, "Load file");

        if (ret == JFileChooser.APPROVE_OPTION) {

            File file = fileChosen.getSelectedFile();

            if (!loadUserData(file.getName())) {
                printOutput(ERROR);
            }

        } else {

            printOutput(CANCELLED);

        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(fileChosen);

    }

    public void syncDataForm() {

        JTextField field1 = new JTextField("");
        JTextField field2 = new JTextField("");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("IP:"));
        panel.add(field1);
        panel.add(new JLabel("Port:"));
        panel.add(field2);

        int result = JOptionPane.showConfirmDialog(null, panel, "Synchronize user data",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (!synchronizeData(field1.getText(), field2.getText())) {
                printOutput(ERROR);
            }
        } else {
            printOutput(CANCELLED);
        }

    }

}