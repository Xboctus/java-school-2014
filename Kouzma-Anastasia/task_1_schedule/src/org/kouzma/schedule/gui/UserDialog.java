package org.kouzma.schedule.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTable;

import org.kouzma.schedule.gui.SheduleWindow.DialogCallBack;

public class UserDialog extends ScheduleDialog {
	
	public UserDialog(String title, String[] arrLabelNames,
			InputType[] arrControlTypes, DialogCallBack dialogCallBack, String[] userInfo) {
		super(title, arrLabelNames, arrControlTypes, dialogCallBack);

		String[] columnNames = {"Date", "Text"};
		int eventsAmount = (userInfo.length - 3) / 2;
		String[][] dataTable = new String[eventsAmount][2];
		
		
		for (int i = 0; i < eventsAmount; i++) {
			dataTable[i] = new String[] {userInfo[3 + 2*i], userInfo[4 + 2*i]};
		}
		
		//TODO
		JTable eventTable = new JTable(dataTable, columnNames);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 3;
		contentPanel.add(eventTable, gbc);

		arrControls.get(0).setText(userInfo[0]);
		arrControls.get(1).setText(userInfo[1]);
		arrControls.get(2).setText(userInfo[2]);
		
		arrControls.get(0).setEditable(false);
		arrControls.get(1).setEditable(false);
		arrControls.get(2).setEditable(false);
	}
}
