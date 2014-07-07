package org.kouzma.schedule.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class UserDialog extends JDialog {
	
	public UserDialog(String[] userInfo) {
		setBounds(100, 100, 350, 200);
		getContentPane().setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel(new GridLayout(3, 2));
		
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);

		// Поля
		{
			contentPanel.add(new JLabel("Name:"));
			JTextField txtName = new JTextField(userInfo[0]);
			txtName.setEditable(false);
			contentPanel.add(txtName);
			contentPanel.add(new JLabel("TimeZone:"));
			JTextField txtTimeZone = new JTextField(userInfo[1]);
			contentPanel.add(txtTimeZone);
			txtTimeZone.setEditable(false);
			contentPanel.add(new JLabel("Status:"));
			JTextField txtStatus = new JTextField(userInfo[2]);
			txtStatus.setEditable(false);
			contentPanel.add(txtStatus);
		}
		
		// Таблица событий
		{
			String[] columnNames = {"Date", "Text"};
			int eventsAmount = (userInfo.length - 3) / 2;
			String[][] dataTable = new String[eventsAmount][2];
			for (int i = 0; i < eventsAmount; i++) {
				dataTable[i] = new String[] {userInfo[3 + 2*i], userInfo[4 + 2*i]};
			}
			
			JTable eventTable = new JTable(dataTable, columnNames);
			eventTable.setEnabled(false);
			
			JScrollPane scrollPanel = new JScrollPane();
	
			getContentPane().add(scrollPanel, BorderLayout.CENTER);
			scrollPanel.setViewportView(eventTable);
		}
		
		// Кнопки
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			JButton okButton = new JButton("OK");
			okButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent ev) {
					UserDialog.this.setVisible(false);
					
				}
			});
			buttonPane.add(okButton);
		}
		
		setModal(true);
	}
}
