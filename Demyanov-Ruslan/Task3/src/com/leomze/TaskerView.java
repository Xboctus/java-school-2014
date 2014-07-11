package com.leomze;




import com.leomze.DialogViewers.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;



public class TaskerView extends JFrame {


    public static TaskHandler taskHandler = new TaskHandler();
    public static DBManager dbManager = new DBManager();
    public static JTextArea textArea = new JTextArea();
    private static JButton createBtn;
    private static JButton startSchedulingBtn;
    private static JButton showInfoBtn;
    private static JButton modifyBtn;
    private static JButton addEventBtn;
    private static JButton addRandEventBtn;
    private static JButton cloneBtn;
    private static JButton removeBtn;
    private static JButton syncBtn;
    private static JButton saveBtn;
    private static JLabel IPLb;

    public void addComponentsToPane(Container pane) {



        pane.setLayout(new GridBagLayout());
        GridBagConstraints bc = new GridBagConstraints();

        createBtn = new JButton("Create User");
        bc.weightx = 0.5;
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.insets = new Insets(10,10, 10, 10);
        bc.gridx = 2; //изменение по X с левого верхнего угла
        bc.gridy = 1; //изменение по Y с левого верхнего угла
        pane.add(createBtn, bc);
        textArea.setText("Welcome!!");

        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnCreateUser(e);

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
                btnModifyUser(e);

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
                btnAddEvent(e);
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
                btnAddRandomEvent(e);
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
                btnRemoveEent(e);
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
                btnCloneEvent(e);            }
        });

        IPLb = new JLabel(IP());
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.anchor = GridBagConstraints.CENTER;
        bc.insets = new Insets(10, 10, 10, 10);  // поставить заглушку
        bc.gridx = 3;       // выравнять компонент по Button 2
        bc.gridy = 6;       // и 3 столбец
        pane.add(IPLb, bc);

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
                new WebSync().start();
            }
        });

        saveBtn = new JButton("Save/Load");
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.anchor = GridBagConstraints.SOUTH;
        bc.insets = new Insets(10, 10, 10, 10);  // поставить заглушку
        bc.gridx = 2;       // выравнять компонент по Button 2
        bc.gridy = 5;       // и 3 столбец
        pane.add(saveBtn, bc);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SerData().start();
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
                        new StartSchedule().start(taskHandler.users);

            }
        });
        showInfoBtn = new JButton("Show Info");
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.anchor = GridBagConstraints.SOUTH;
        bc.insets = new Insets(10, 10, 10, 10);  // поставить заглушку
        bc.gridwidth = 2;
        bc.gridx = 2;       // выравнять компонент по Button 2
        bc.gridy = 6;       // и 3 столбец
        pane.add(showInfoBtn, bc);
        showInfoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    dbManager.showinfo();


            }
        });


        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(300, 300));
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.insets = new Insets(10,10, 10, 10);
        bc.weightx = 0.0;
        bc.gridwidth = 1;
        bc.gridheight = 7;
        bc.gridx = 0; //изменение по X с левого верхнего угла
        bc.gridy = 0; //изменение по Y с левого верхнего угла
        pane.add(scrollPane, bc);



    }

    private void btnModifyUser(ActionEvent e) {
      ModifyUser modifyUser = new ModifyUser(this, true);
        modifyUser.setVisible(true);
    }
    private void btnAddEvent(java.awt.event.ActionEvent evt) {
        AddEvent addEvent = new AddEvent(this, true);
        addEvent.setVisible(true);
    }
    private void btnCreateUser(java.awt.event.ActionEvent evt) {
        CreateUser createUser = new CreateUser(this, true);
        createUser.setVisible(true);
    }
    private void btnAddRandomEvent(java.awt.event.ActionEvent evt) {
        AddRandomEvent addRandomEvent = new AddRandomEvent(this, true);
        addRandomEvent.setVisible(true);
    }
    private void btnCloneEvent(java.awt.event.ActionEvent evt){
        CloneEvent cloneEvent = new CloneEvent(this,true);
        cloneEvent.setVisible(true);
    }

    private void btnRemoveEent(java.awt.event.ActionEvent evt){
       RemoveEvent removeEvent = new RemoveEvent(this, true);
        removeEvent.setVisible(true);
    }

    public TaskerView() {
        setTitle("TaskTraker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(getContentPane());
        setResizable(false);
        pack();

    }

    public void start() {
        final Thread serverThread = new Thread(){
            public void run(){
                try {
                    new WebSyncHandler().server(taskHandler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TaskerView().setVisible(true);
                serverThread.start();
            }
        });



    }

    public static String IP(){
        try {
            InetAddress local = InetAddress.getLocalHost();
            String IP = "Your IP: " + local.getHostAddress();
            return IP;
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }


}



