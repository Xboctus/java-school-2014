package com.leomze.DialogViewers;


import com.leomze.Serializer;
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

public class SerData {

    private void createAndShowGUI() throws ParseException {
        dialog = new JDialog();
        dialog.setTitle("Data save");
        dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        serDate(dialog.getContentPane());
        dialog.setResizable(false);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void serDate(Container pane) throws ParseException {



        paneDialog = new JPanel();
        panelContetnt = new JPanel();
        timeZoneLb = new JLabel();
        fileChooser = new JFileChooser();
        barButton = new JPanel();
        btnSave = new JButton();
        btnLoad = new JButton();

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


//                timeZoneLb.setText("Choose File for save/load state: ");
//                panelContetnt.add(timeZoneLb, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
//                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
//                        new Insets(0, 0, 0, 10), 0, 0));
//                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                panelContetnt.add(fileChooser, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
//                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
//                        new Insets(0, 0, 10, 0), 0, 0));
            }
            paneDialog.add(panelContetnt, BorderLayout.CENTER);


            {
                barButton.setBorder(new EmptyBorder(12, 0, 0, 0));
                barButton.setLayout(new GridBagLayout());
                ((GridBagLayout) barButton.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout) barButton.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};


                btnSave.setText("Save");
                barButton.add(btnSave, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                        new Insets(10, 10, 10, 10), 0, 0));
                btnSave.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new Serializer().serialize(TaskerView.taskHandler);
                        dialog.dispose();
                    }
                });

                btnLoad.setText("Load");
                barButton.add(btnLoad, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                        new Insets(10, 10, 10, 10), 0, 0));
                btnLoad.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                         new Serializer().deserialize();
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
    private JFileChooser fileChooser;
    private JLabel timeZoneLb;
    private JPanel barButton;
    private JButton btnSave;
    private JButton btnLoad;

}
