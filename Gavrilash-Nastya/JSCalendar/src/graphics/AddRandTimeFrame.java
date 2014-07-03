package graphics;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

public class AddRandTimeFrame extends JFrame {
	public static final int DEFAULT_WIDTH = 400;
	public static final int DEFAULT_HEIGHT = 200;

	private JTextField user;
	private JTextField event;
	private JFormattedTextField dateFrom;
	private JFormattedTextField dateTo;
	private JLabel error;
	private JButton button;

	public AddRandTimeFrame() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2
				- this.getSize().height / 2);
		setTitle("add random time event");
		setResizable(false);
		initializeItems();
		addItems();
	}

	private void initializeItems() {
		user = new JTextField(20);
		event = new JTextField(20);
		error = new JLabel();
		try {
			MaskFormatter dateMask = new MaskFormatter("##.##.####");
			dateMask.setPlaceholderCharacter(' ');

			dateFrom = new JFormattedTextField(dateMask);
			dateTo = new JFormattedTextField(dateMask);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		button = new JButton("add event");
		addFunctionsToButton();
	}

	private void addItems() {
		JPanel backGround = new JPanel();
		JPanel userPanel = new JPanel();
		JPanel eventPanel = new JPanel();
		JPanel datePanel = new JPanel();
		JPanel errorPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		userPanel.add(new JLabel("user: "));
		userPanel.add(user);
		eventPanel.add(new JLabel("event: "));
		eventPanel.add(event);
		datePanel.add(new JLabel("from (DD.MM.YYYY): "));
		datePanel.add(dateFrom);
		datePanel.add(new JLabel(" to (DD.MM.YYYY): "));
		datePanel.add(dateTo);
		errorPanel.add(error);
		buttonPanel.add(button);

		backGround.setLayout(new GridLayout(5, 1));
		backGround.add(userPanel);
		backGround.add(eventPanel);
		backGround.add(datePanel);
		backGround.add(errorPanel);
		backGround.add(buttonPanel);
		add(backGround);
	}

	private void addFunctionsToButton() {
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = user.getText();
				String eventText = event.getText();
				String dateFromText = dateFrom.getText();
				String dateToText = dateTo.getText();
				try {
					CommandAnalisis.addRandomQuery(userName, eventText,
							dateFromText, dateToText);
					user.setText("");
					event.setText("");
					dateFrom.setText("");
					dateTo.setText("");
					error.setText("");
					MainClass.frame.addStringToLog("Random time event "
							+ eventText + " added to user " + userName);
				} catch (IOException e1) {
					error.setText(e1.getMessage());
				}

			}
		});
	}
}
