package graphicsWorks;

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

/**
 * This frame displayed when user wants to print current state of system to file
 * 
 * @author Gavrilash
 * 
 */
public class PrintFrame extends JFrame {
	public static final int DEFAULT_WIDTH = 400;
	public static final int DEFAULT_HEIGHT = 130;

	private JButton button;
	private JTextField fileName;
	private JLabel error;

	/**
	 * Construct frame with default width and height, centering it and invokes
	 * methods for initialization and location frame elements
	 */
	public PrintFrame() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2
				- this.getSize().height / 2);
		setTitle("print current state to file");
		setResizable(false);
		initializeItems();
		addItems();
	}

	private void initializeItems() {
		fileName = new JTextField(20);
		error = new JLabel();
		button = new JButton("print");
		addFunctionToButton();
	}

	private void addItems() {
		JPanel backGround = new JPanel();
		JPanel filePanel = new JPanel();
		JPanel errorPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		filePanel.add(new JLabel("Path to file: "));
		filePanel.add(fileName);
		errorPanel.add(error);
		buttonPanel.add(button);

		backGround.setLayout(new GridLayout(3, 1));
		backGround.add(filePanel);
		backGround.add(errorPanel);
		backGround.add(buttonPanel);
		add(backGround);
	}

	private void addFunctionToButton() {
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					CommandAnalisis.printQuery(fileName.getText());
					error.setText("");
					fileName.setText("");
					MainClass.frame
							.addStringToLog("Current state printed to file "
									+ fileName);
				} catch (IOException e1) {
					error.setText(e1.getMessage());
				}
			}
		});
	}

}
