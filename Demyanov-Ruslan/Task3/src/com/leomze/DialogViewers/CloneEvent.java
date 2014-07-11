package com.leomze.DialogViewers;

import com.leomze.TaskerView;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;


public class CloneEvent extends JDialog{




    public CloneEvent(JFrame tv, boolean modal){
        super(tv,modal);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Clone Event");
        Container pane = getContentPane();
        cloneEvent(pane);
        setResizable(false);
        pack();
    }

    private void cloneEvent(Container pane){

        paneDialog = new JPanel();
        panelContent = new JPanel();
        userNameFromLb = new JLabel();
        userNameFromBox = new JComboBox<>(TaskerView.dbManager.getUsersname());
        statusLb = new JLabel();
        eventTextField = new JTextField();
        userNameToLb= new JLabel();
        userNameToBox= new JComboBox<>(TaskerView.dbManager.getUsersname());
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
                panelContent.setLayout(new GridBagLayout());
                ((GridBagLayout) panelContent.getLayout()).columnWidths = new int[] {89, 115, 113, 0};
                ((GridBagLayout) panelContent.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                ((GridBagLayout) panelContent.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout) panelContent.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};


                userNameFromLb.setText("User name from");
                panelContent.add(userNameFromLb, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 10), 0, 0));
                panelContent.add(userNameFromBox, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 0), 0, 0));


                statusLb.setText("Event message");
                panelContent.add(statusLb, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 10), 0, 0));
                panelContent.add(eventTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 0), 0, 0));


                userNameToLb.setText("User name to:");
                panelContent.add(userNameToLb, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 10), 0, 0));
                panelContent.add(userNameToBox, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 0), 0, 0));
            }
            paneDialog.add(panelContent, BorderLayout.CENTER);


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
                        String[] data = new String[3];
                        if(TaskerView.dbManager.getUsersname().length > 1){
                        data[0] = userNameFromBox.getSelectedItem().toString();
                        data[2] = userNameToBox.getSelectedItem().toString();
                        data[1] = eventTextField.getText();
                        TaskerView.dbManager.cloneEvent(data[0], data[1], data[2]);
                        }else {
                            TaskerView.textArea.append("\n Error! Choose correct name or create more,\n then one user for cloning :)");
                        }
                        dispose();
                    }
                });


            }
            paneDialog.add(barButton, BorderLayout.SOUTH);
        }
        contentPane.add(paneDialog, BorderLayout.CENTER);


    }



    private JPanel paneDialog;
    private JPanel panelContent;
    private JLabel userNameFromLb;
    private JComboBox<String> userNameFromBox;
    private JLabel userNameToLb;
    private JComboBox<String> userNameToBox;
    private JLabel statusLb;
    private JTextField eventTextField;
    private JPanel barButton;
    private JButton btnOk;
}
