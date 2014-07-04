package graphics;

import graphics.CommandAnalisis.Command;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class CreateModifyUserWindow extends JFrame {
	public static final int DEFAULT_WIDTH = 400;
	public static final int DEFAULT_HEIGHT = 150;

	private JCheckBox activeStatus;
	private JButton button;
	private JTextField name;
	private JComboBox timeZones;
	private JLabel errorMessage;
	private Command command;

	public CreateModifyUserWindow(Command command) {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2
				- this.getSize().height / 2);
		setTitle(command.toString().toLowerCase() + " user");
		setResizable(false);

		this.command = command;
		initializeItems();
		addItems();
	}

	private void initializeItems() {
		errorMessage = new JLabel();
		activeStatus = new JCheckBox();
		activeStatus.setSelected(true);
		name = new JTextField(15);
		timeZones = new JComboBox();
		String selectedItem = "+4";
		timeZones.addItem("+12");
		timeZones.addItem("+11");
		timeZones.addItem("+10");
		timeZones.addItem("+09");
		timeZones.addItem("+08");
		timeZones.addItem("+07");
		timeZones.addItem("+06");
		timeZones.addItem("+05");
		timeZones.addItem(selectedItem);
		timeZones.addItem("+03");
		timeZones.addItem("+02");
		timeZones.addItem("+01");
		timeZones.addItem("+00");
		timeZones.addItem("-01");
		timeZones.addItem("-02");
		timeZones.addItem("-03");
		timeZones.addItem("-04");
		timeZones.addItem("-05");
		timeZones.addItem("-06");
		timeZones.addItem("-07");
		timeZones.addItem("-08");
		timeZones.addItem("-09");
		timeZones.addItem("-10");
		timeZones.addItem("-11");
		timeZones.setSelectedItem(selectedItem);

		if (command.equals(Command.CREATE)) {
			button = new JButton("create user");
		} else {
			button = new JButton("modify user");
		}
		addFunctionsToButton();
	}

	private void addItems() {
		JPanel backgroundPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel fieldPanel = new JPanel();
		JPanel namePanel = new JPanel();

		backgroundPanel.setLayout(new BorderLayout());

		buttonPanel.add(button);
		fieldPanel.add(new JLabel("active"));
		fieldPanel.add(activeStatus);
		fieldPanel.add(new JLabel(" GMT"));
		fieldPanel.add(timeZones);
		namePanel.add(new JLabel("name: "));
		namePanel.add(name);
		namePanel.add(errorMessage);

		backgroundPanel.add(namePanel, BorderLayout.CENTER);
		backgroundPanel.add(buttonPanel, BorderLayout.EAST);
		backgroundPanel.add(fieldPanel, BorderLayout.SOUTH);

		add(backgroundPanel);
	}

	private void addFunctionsToButton() {
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = name.getText().trim();
				boolean activityStatus = activeStatus.isSelected();
				int tzOffset = Integer.valueOf(timeZones.getSelectedItem()
						.toString());
				try {
					if (command.equals(Command.CREATE)) {
						CommandAnalisis.createUserQuery(userName,
								activityStatus, tzOffset);
						MainClass.frame.addStringToLog("User " + userName
								+ " created");
					} else {
						CommandAnalisis.modifyUserQuery(userName,
								activityStatus, tzOffset);
						MainClass.frame.addStringToLog("User " + userName
								+ " modified");
					}
					name.setText("");
					errorMessage.setText("");
				} catch (IOException e1) {
					errorMessage.setText(e1.getMessage());
				}
			}
		});
	}
}
