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

public class ModifyUser extends JDialog {


    public ModifyUser(JFrame tv, boolean modal) {
        super(tv, modal);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Modify user");
        modifyUser(getContentPane());
        setResizable(false);
        pack();

    }

    private void modifyUser(Container pane) {


        paneDialog = new JPanel();
        panelContent = new JPanel();
        userNameLb = new JLabel();
        nameTextField = new JComboBox<>(TaskerView.dbManager.getUsersname());
        statusLb = new JLabel();
        StatusRadioBtn = new JRadioButton();
        timeZoneLb = new JLabel();
        int value = 0;
        SpinnerNumberModel model = new SpinnerNumberModel(value, value - 12, value + 14, 1);
        GMTSpinner = new JSpinner(model);
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
                ((GridBagLayout)panelContent.getLayout()).columnWidths = new int[] {89, 115, 113, 0};
                ((GridBagLayout)panelContent.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                ((GridBagLayout)panelContent.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout)panelContent.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};


                userNameLb.setText("User name");
                panelContent.add(userNameLb, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 10), 0, 0));
                panelContent.add(nameTextField, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 0), 0, 0));


                statusLb.setText("User status");
                panelContent.add(statusLb, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 10), 0, 0));


                StatusRadioBtn.setText("");
                panelContent.add(StatusRadioBtn, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 10), 0, 0));


                timeZoneLb.setText("User timezone: GMT");
                panelContent.add(timeZoneLb, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 10), 0, 0));

                panelContent.add(GMTSpinner, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 10), 0, 0));
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
                        data[0] = nameTextField.getSelectedItem().toString();
                        if(StatusRadioBtn.isSelected() == true){data[1] = "1"; }else {data[1] = "0";}
                        data[2] = GMTSpinner.getValue().toString();
                        TaskerView.dbManager.modifyUser(data[0], data[2], data[1]);
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
    private JLabel userNameLb;
    private JComboBox<String> nameTextField;
    private JLabel timeZoneLb;
    private JRadioButton StatusRadioBtn;
    private JLabel statusLb;
    private JSpinner GMTSpinner;
    private JPanel barButton;
    private JButton btnOk;
}
