package graphics;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sun.security.util.Length;

public class ShowInfoFrame extends JFrame {
	public static final int DEFAULT_WIDTH = 400;
	public static final int DEFAULT_HEIGHT = 120;

	private JTextField user;
	private JLabel error;
	private JButton button;

	public ShowInfoFrame() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2
				- this.getSize().height / 2);
		setTitle("show info");
		setResizable(false);
		initializeItems();
		addItems();
	}

	private void initializeItems() {
		user = new JTextField(20);
		error = new JLabel();
		button = new JButton("show info");
		addFunctionsToButton();
	}

	private void addItems() {
		JPanel backGround = new JPanel();
		JPanel userPanel = new JPanel();
		JPanel errorPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		userPanel.add(user);
		errorPanel.add(error);
		buttonPanel.add(button);

		backGround.setLayout(new GridLayout(3, 1));
		backGround.add(userPanel);
		backGround.add(errorPanel);
		backGround.add(buttonPanel);
		add(backGround);
	}

	private void addFunctionsToButton() {
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = user.getText();
				try {
					List<String> info = CommandAnalisis.showInfoQuery(userName);
					for (String s : info) {
						MainClass.frame.addUserInfoToLog(s);
					}
					user.setText("");
					error.setText("");
				} catch (IOException e1) {
					error.setText(e1.getMessage());
				}
			}
		});
	}
}
