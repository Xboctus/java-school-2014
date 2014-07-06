package com.leomze.DialogViewers;


import com.leomze.TaskerView;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

public class RemoveEvent {




    private void createAndShowGUI() throws ParseException {
        dialog = new JDialog();
        dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        dialog.setTitle("Remove Event");
        createUser(dialog.getContentPane());
        dialog.setResizable(false);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void createUser(Container pane) throws ParseException {



        paneDialog = new JPanel();
        panelContetnt = new JPanel();
        userNameLb = new JLabel();
        eventTextBox = new JTextField();
        statusLb = new JLabel();
        nameTextBox = new JComboBox<>(TaskerView.taskHandler.showUserNamesArray());
        barButton = new JPanel();
        btnOk = new JButton();

        Container contentPane = pane;
        contentPane.setLayout(new BorderLayout());

        {
            paneDialog.setBorder(new EmptyBorder(12, 12, 12, 12));


            paneDialog.setBorder(new CompoundBorder(
                    new TitledBorder(new EmptyBorder(0, 0, 0, 0),
                            null, TitledBorder.CENTER,
                            TitledBorder.BOTTOM, new Font("Dialog", Font.BOLD, 12),
                            Color.red), paneDialog.getBorder())); paneDialog.addPropertyChangeListener(new PropertyChangeListener(){public void propertyChange(PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

            paneDialog.setLayout(new BorderLayout());


            {
                panelContetnt.setLayout(new GridBagLayout());
                ((GridBagLayout)panelContetnt.getLayout()).columnWidths = new int[] {89, 115, 113, 0};
                ((GridBagLayout)panelContetnt.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                ((GridBagLayout)panelContetnt.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout)panelContetnt.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};


                userNameLb.setText("User name");
                panelContetnt.add(userNameLb, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 10), 0, 0));
                panelContetnt.add(nameTextBox, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 0), 0, 0));


                statusLb.setText("Event message");
                panelContetnt.add(statusLb, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 10), 0, 0));
                panelContetnt.add(eventTextBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 0), 0, 0));

            }
            paneDialog.add(panelContetnt, BorderLayout.CENTER);


            {
                barButton.setBorder(new EmptyBorder(12, 0, 0, 0));
                barButton.setLayout(new GridBagLayout());
                ((GridBagLayout) barButton.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout) barButton.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};


                btnOk.setText("OK");
                barButton.add(btnOk, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                btnOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String[] data = new String[2];
                        data[0] = nameTextBox.getSelectedItem().toString();
                        data[1] = eventTextBox.getText();
                        TaskerView.textArea.append("\n" + TaskerView.taskHandler.removeEvent(data[0], data[1]));
                        dialog.dispose();
                    }
                });


            }
            paneDialog.add(barButton, BorderLayout.SOUTH);
        }
        contentPane.add(paneDialog, BorderLayout.CENTER);


    }



    public void start() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private JDialog dialog;
    private JPanel paneDialog;
    private JPanel panelContetnt;
    private JLabel userNameLb;
    private JTextField eventTextBox;
    private JLabel statusLb;
    private JComboBox nameTextBox;
    private JPanel barButton;
    private JButton btnOk;
}
