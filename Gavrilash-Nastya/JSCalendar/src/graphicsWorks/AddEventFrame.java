package graphicsWorks;

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

public class AddEventFrame extends JFrame {
	public static final int DEFAULT_WIDTH = 400;
	public static final int DEFAULT_HEIGHT = 200;

	private JFormattedTextField date;
	private JFormattedTextField time;
	private JTextField name;
	private JTextField event;
	private JButton button;
	private JLabel errorMassage;

	public AddEventFrame() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2
				- this.getSize().height / 2);
		setTitle("add event");
		setResizable(false);
		initializeItems();
		addItems();
	}

	private void initializeItems() {
		try {
			MaskFormatter dateMask = new MaskFormatter("##.##.####");
			MaskFormatter timeMask = new MaskFormatter("##:##:##");

			dateMask.setPlaceholderCharacter(' ');
			timeMask.setPlaceholderCharacter(' ');

			date = new JFormattedTextField(dateMask);
			time = new JFormattedTextField(timeMask);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		name = new JTextField(15);
		event = new JTextField(15);
		errorMassage = new JLabel();
		button = new JButton("add new event");
		addFunctionsToButton();
	}

	private void addItems() {
		JPanel backGround = new JPanel();
		JPanel namePanel = new JPanel();
		JPanel eventPanel = new JPanel();
		JPanel datePanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel errorPanel = new JPanel();

		namePanel.add(new JLabel("name: "));
		namePanel.add(name);
		eventPanel.add(new JLabel("event: "));
		eventPanel.add(event);
		datePanel.add(new JLabel("date (DD.MM.YYYY): "));
		datePanel.add(date);
		datePanel.add(new JLabel(" time (HH:MM:SS): "));
		datePanel.add(time);
		buttonPanel.add(button);
		errorPanel.add(errorMassage);

		backGround.setLayout(new GridLayout(5, 1));
		backGround.add(namePanel);
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
				String userName = name.getText().trim();
				String eventText = event.getText().trim();
				String dateText = date.getText();
				String timeText = time.getText();
				try {
					CommandAnalisis.addEventQuery(userName, eventText,
							dateText, timeText);
					MainClass.frame.addStringToLog("Event " + eventText
							+ " added to user " + userName);
					errorMassage.setText("");
					name.setText("");
					event.setText("");
					date.setText("");
					time.setText("");
				} catch (IOException e1) {
					errorMassage.setText(e1.getMessage());
				}
			}
		});
	}
}
