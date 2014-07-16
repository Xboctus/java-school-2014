package graphicsWorks;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CloneEventFrame extends JFrame {
	public static final int DEFAULT_WIDTH = 400;
	public static final int DEFAULT_HEIGHT = 200;

	private JTextField userFrom;
	private JTextField userTo;
	private JTextField event;
	private JLabel error;
	private JButton button;

	public CloneEventFrame() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2
				- this.getSize().height / 2);
		setTitle("clone event");
		setResizable(false);
		initializeItems();
		addItems();

	}

	private void initializeItems() {
		userFrom = new JTextField(20);
		userTo = new JTextField(20);
		event = new JTextField(20);
		error = new JLabel();
		button = new JButton("clone");
		addFunctionsToButton();
	}

	private void addItems() {
		JPanel backGround = new JPanel();
		JPanel userFromPanel = new JPanel();
		JPanel userToPanel = new JPanel();
		JPanel eventPanel = new JPanel();
		JPanel errorPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		userFromPanel.add(new JLabel("from user: "));
		userFromPanel.add(userFrom);
		userToPanel.add(new JLabel("to user: "));
		userToPanel.add(userTo);
		eventPanel.add(new JLabel("event: "));
		eventPanel.add(event);
		errorPanel.add(error);
		buttonPanel.add(button);

		backGround.setLayout(new GridLayout(5, 1));
		backGround.add(userFromPanel);
		backGround.add(userToPanel);
		backGround.add(eventPanel);
		backGround.add(errorPanel);
		backGround.add(buttonPanel);
		add(backGround);
	}

	private void addFunctionsToButton() {
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String userFromName = userFrom.getText();
				String userToName = userTo.getText();
				String eventText = event.getText();
				try {
					CommandAnalisis.cloneQuery(userFromName, userToName,
							eventText);
					userFrom.setText("");
					event.setText("");
					userTo.setText("");
					error.setText("");
					MainClass.frame.addStringToLog("Event " + eventText
							+ " cloned from user " + userFromName + " to user "
							+ userToName);
				} catch (IOException e1) {
					error.setText(e1.getMessage());
				}

			}
		});
	}
}
