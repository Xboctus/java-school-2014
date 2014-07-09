package com.leomze.DialogViewers;

import com.leomze.TaskerView;
import com.leomze.WebSyncHandler;

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
import java.util.regex.Matcher;


public class WebSync {

    private void createAndShowGUI() throws ParseException {
        dialog = new JDialog();
        dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        dialog.setTitle("Sync");
        createUser(dialog.getContentPane());
        dialog.setResizable(false);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void createUser(Container pane) throws ParseException {



        paneDialog = new JPanel();
        panelContent = new JPanel();
        ipLb = new JLabel();
        ipTextField = new JTextField();
        portLb = new JLabel();
        portTextField = new JTextField();
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


                ipLb.setText("Write server IP");
                panelContent.add(ipLb, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 10), 0, 0));
                panelContent.add(ipTextField, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 0), 0, 0));

                portLb.setText("Write server port");
                panelContent.add(portLb, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 10), 0, 0));
                panelContent.add(portTextField, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 0), 0, 0));


            }
            paneDialog.add(panelContent, BorderLayout.CENTER);


            {
                barButton.setBorder(new EmptyBorder(12, 0, 0, 0));
                barButton.setLayout(new GridBagLayout());
                ((GridBagLayout) barButton.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout) barButton.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};


                btnOk.setText("Sync");
                barButton.add(btnOk, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                btnOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String ipRegexp = "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b";
                        String portRegexp = "[0-9]{1,5}";

                        if(ipTextField.getText().matches(ipRegexp) & portTextField.getText().matches(portRegexp)){
                             Thread sync  = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int port  = Integer.parseInt(portTextField.getText());
                                        new WebSyncHandler().client(ipTextField.getText(), port);
                                    }
                                });
                            sync.start();
                        }else{
                            TaskerView.textArea.append("\n Incorrect data input (ip/port) :(");
                        }

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
    private JPanel panelContent;
    private JLabel ipLb;
    private JTextField ipTextField;
    private JLabel portLb;
    private JTextField portTextField;
    private JPanel barButton;
    private JButton btnOk;



}
