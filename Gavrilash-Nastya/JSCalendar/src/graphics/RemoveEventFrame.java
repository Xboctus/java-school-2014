package graphics;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RemoveEventFrame extends JFrame {
	public static final int DEFAULT_WIDTH = 400;
	public static final int DEFAULT_HEIGHT = 200;
	JTextField name;
	JTextField event;
	JButton button;
	JLabel error;

	public RemoveEventFrame() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2
				- this.getSize().height / 2);
		setTitle("remove event");
		setResizable(false);
		initializeItems();
		addItems();
	}

	private void initializeItems() {
		name = new JTextField(20);
		event = new JTextField(20);
		button = new JButton("remove");
		error = new JLabel();
		addFunctionsToButton();
	}

	private void addItems() {
		JPanel backGroundPanel = new JPanel();
		JPanel namePanel = new JPanel();
		JPanel eventPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel errorPanel = new JPanel();

		namePanel.add(new JLabel("name: "));
		namePanel.add(name);
		eventPanel.add(new JLabel("event: "));
		eventPanel.add(event);
		errorPanel.add(error);
		buttonPanel.add(button);

		backGroundPanel.setLayout(new GridLayout(4, 1));
		backGroundPanel.add(namePanel);
		backGroundPanel.add(eventPanel);
		backGroundPanel.add(errorPanel);
		backGroundPanel.add(buttonPanel);

		add(backGroundPanel);
	}

	public void addFunctionsToButton() {
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = name.getText().trim();
				String eventText = event.getText().trim();
				try {
					CommandAnalisis.removeEventQuery(userName, eventText);
					name.setText("");
					event.setText("");
					error.setText("");
					MainClass.frame.addStringToLog("Event " + eventText
							+ " removed from user " + userName);
				} catch (IOException e1) {
					error.setText(e1.getMessage());

				}
			}
		});
	}
}
