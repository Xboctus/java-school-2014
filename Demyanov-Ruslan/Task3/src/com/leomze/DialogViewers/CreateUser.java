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

public class CreateUser extends JDialog{

    public CreateUser(JFrame tv, boolean modal) {
        super(tv, modal);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Create User");
        Container pane = getContentPane();
        createUser(pane);
        setResizable(false);
        pack();

    }

    private void createUser(Container pane) {



        paneDialog = new JPanel();
        panelContetnt = new JPanel();
        userNameLb = new JLabel();
        nameTextField = new JTextField();
        statusLb = new JLabel();
        statusRadioBtn = new JRadioButton();
        timeZoneLb = new JLabel();
        int value = 0;
        SpinnerNumberModel model = new SpinnerNumberModel(value, value - 12, value + 14, 1);
        GMTSpinner = new JSpinner(model);
        barButton = new JPanel();
        btnOk = new JButton();
        arg = new String[3];


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
                panelContetnt.add(nameTextField, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 0), 0, 0));



                statusLb.setText("User status");
                panelContetnt.add(statusLb, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 10), 0, 0));
                panelContetnt.add(statusRadioBtn, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 10), 0, 0));


                timeZoneLb.setText("User timezone: GMT");
                panelContetnt.add(timeZoneLb, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 10), 0, 0));

                panelContetnt.add(GMTSpinner, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 10), 0, 0));
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

                            arg[0] = nameTextField.getText();
                            if(statusRadioBtn.isSelected() == true){arg[1] = "1"; }else {arg[1] = "0";}
                            arg[2] = GMTSpinner.getValue().toString();
                            TaskerView.dbManager.createUser(arg[0], arg[2], arg[1]);
                            dispose();

                    }
                });


            }
            paneDialog.add(barButton, BorderLayout.SOUTH);
        }
        contentPane.add(paneDialog, BorderLayout.CENTER);


    }



    private JPanel paneDialog;
    private JPanel panelContetnt;
    private JLabel userNameLb;
    public JTextField nameTextField;
    private JLabel timeZoneLb;
    public JRadioButton statusRadioBtn;
    private JLabel statusLb;
    public JSpinner GMTSpinner;
    private JPanel barButton;
    private JButton btnOk;
    private String[] arg;





}
