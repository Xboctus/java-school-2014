package com.javaschool2014.task1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GUICoordinator extends AbstractCoordinator {

    private String appName             = "Planfx v0.1";
    private JFrame newFrame            = new JFrame(appName);
    private final JTextArea textArea   = new JTextArea();

    @Override
    protected void printOutput(String string) {
        textArea.append(string + "\n");
    }

    @Override
    protected void connectDefaultDataFile() {

    }

    public void display() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Serif", Font.PLAIN, 14));


        JButton create             = new JButton("Create");
        JButton modify             = new JButton("Modify");
        JButton addEvent           = new JButton("AddEvent");
        JButton removeEvent        = new JButton("RemoveEvent");
        JButton addRandomTimeEvent = new JButton("AddRandomTimeEvent");
        JButton cloneEvent         = new JButton("CloneEvent");
        JButton showInfo           = new JButton("ShowInfo");

        final ActionListener createListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.append("asd! \n");
            }
        };

        create.addActionListener(createListener);

        leftPanel.add(new JScrollPane(textArea));

        rightPanel.add(create);
        rightPanel.add(modify);
        rightPanel.add(addEvent);
        rightPanel.add(removeEvent);
        rightPanel.add(addRandomTimeEvent);
        rightPanel.add(cloneEvent);
        rightPanel.add(showInfo);

        mainPanel.add(BorderLayout.WEST, leftPanel);
        mainPanel.add(rightPanel);

        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(460, 460);
        newFrame.setVisible(true);

    }

}