package com.leomze;




import com.leomze.DialogViewers.DialogView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class TaskerView {


    final static boolean RIGHT_TO_LEFT = false;
    private static final int CREATE_USER = 1;
    private static final int MODIFY_USER = 2;
    private static final int ADD_EVENT = 3;
    private static final int ADD_RANDOM_EVENT = 4;
    private static final int REMOVE_EVENT = 5;
    private static final int CLONE_EVENT = 6;
    private static final int SYNC = 7;
    private static final int SAVE = 8;

    TaskHandler taskHandler = new TaskHandler();

    public ArrayList<String> getUser(){
       ArrayList<String> names =  new ArrayList<String>();
        for(String name: taskHandler.users.keySet()){
            names.add(name);
        }
        return names;

    }


    public static void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }


        JButton createBtn;
        JButton startSchedulingBtn;
        JButton modifyBtn;
        JButton addEventBtn;
        JButton addRandEventBtn;
        JButton cloneBtn;
        JButton removeBtn;
        JButton syncBtn;
        JButton saveBtn;
        JTextArea textArea = new JTextArea();
        final DialogView dv = new DialogView();

        pane.setLayout(new GridBagLayout());
        GridBagConstraints bc = new GridBagConstraints();

        createBtn = new JButton("Create User");
        bc.weightx = 0.5;
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.insets = new Insets(10,10, 10, 10);
        bc.gridx = 2; //изменение по X с левого верхнего угла
        bc.gridy = 1; //изменение по Y с левого верхнего угла
        pane.add(createBtn, bc);
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dv.dialogChooser(CREATE_USER);
            }
        });

        modifyBtn = new JButton("Modify user");
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.insets = new Insets(10,10, 10, 10);
        bc.gridx = 3;
        bc.gridy = 1;
        pane.add(modifyBtn, bc);
        modifyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dv.dialogChooser(MODIFY_USER);
            }
        });

        addEventBtn = new JButton("Add event");
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.insets = new Insets(10,10, 10, 10);
        bc.gridx = 2;
        bc.gridy = 2;
        pane.add(addEventBtn, bc);
        addEventBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dv.dialogChooser(ADD_EVENT);
            }
        });

        addRandEventBtn = new JButton("Add Random Event");
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.insets = new Insets(10,10, 10, 10);
        bc.gridx = 3;
        bc.gridy = 2;
        pane.add(addRandEventBtn, bc);
        addRandEventBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dv.dialogChooser(ADD_RANDOM_EVENT);
            }
        });

        removeBtn = new JButton("Remove Event");
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.insets = new Insets(10, 10, 10, 10);  // поставить заглушку
        bc.gridx = 3;       // выравнять компонент по Button 2
        bc.gridy = 3;       // и 3 столбец
        pane.add(removeBtn, bc);
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dv.dialogChooser(REMOVE_EVENT);
            }
        });

        cloneBtn = new JButton("Clone Event");
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.insets = new Insets(10, 10, 10, 10);  // поставить заглушку
        bc.gridx = 2;       // выравнять компонент по Button 2
        bc.gridy = 3;       // и 3 столбец
        pane.add(cloneBtn, bc);
        cloneBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dv.dialogChooser(CLONE_EVENT);
            }
        });

        syncBtn = new JButton("Sync");
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.anchor = GridBagConstraints.SOUTH;
        bc.insets = new Insets(10, 10, 10, 10);  // поставить заглушку
        bc.gridx = 3;       // выравнять компонент по Button 2
        bc.gridy = 5;       // и 3 столбец
        pane.add(syncBtn, bc);
        syncBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dv.dialogChooser(SYNC);
            }
        });

        saveBtn = new JButton("Save");
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.anchor = GridBagConstraints.SOUTH;
        bc.insets = new Insets(10, 10, 10, 10);  // поставить заглушку
        bc.gridx = 2;       // выравнять компонент по Button 2
        bc.gridy = 5;       // и 3 столбец
        pane.add(saveBtn, bc);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dv.dialogChooser(SAVE);
            }
        });

        startSchedulingBtn = new JButton("Start Scheduling");
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.anchor = GridBagConstraints.SOUTH;
        bc.insets = new Insets(10, 10, 10, 10);  // поставить заглушку
        bc.gridwidth = 2;
        bc.gridx = 2;       // выравнять компонент по Button 2
        bc.gridy = 4;       // и 3 столбец
        pane.add(startSchedulingBtn, bc);
        startSchedulingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(300, 300));
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.insets = new Insets(10,10, 10, 10);
        bc.weightx = 0.0;
        bc.gridwidth = 1;
        bc.gridheight = 6;
        bc.gridx = 0; //изменение по X с левого верхнего угла
        bc.gridy = 0; //изменение по Y с левого верхнего угла
        pane.add(scrollPane, bc);



    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("TaskTraker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(frame.getContentPane());
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public void start() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}



