package graphics;

import graphics.CommandAnalisis.Command;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class InitialWindow extends JFrame {
	public static final int DEFAULT_WIDTH = 400;
	public static final int DEFAULT_HEIGHT = 500;

	private JButton newUser;
	private JButton modifyUser;
	private JButton addEvent;
	private JButton removeEvent;
	private JButton addRandTimeEvent;
	private JButton cloneEvent;
	private JButton showInfo;
	private JButton startScheduling;
	private JButton printToFile;
	private JButton downloadFromFile;
	private JButton downloadFromSocket;
	private boolean scheduling;

	private JTextArea log;

	public InitialWindow() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2
				- this.getSize().height / 2);
		setResizable(false);

		setTitle("Smart calendar");
		addCells();
		addFunctionsToButtons();
		addStringToLog("Hello Human! I'm calendar");
		scheduling = false;
	}

	public void addStringToLog(String smth) {
		SimpleDateFormat form = new SimpleDateFormat("dd.MM-HH:mm");
		Date date = new Date();
		log.append(form.format(date) + "\n");
		log.append(smth + "\n");
	}

	public void addUserInfoToLog(String smth) {
		log.append(smth + "\n");
	}

	private void addFunctionsToButtons() {
		newUserFunction();
		modifyUserFunction();
		addEventFunction();
		removeEventFunction();
		addRandTimeFunction();
		cloneEventFunction();
		showInfoFunctions();
		startSchedulingFunction();
		printToFileFunction();
		downloadFromFileFunction();
		downloadFromSocketFunctions();
	}

	private void downloadFromSocketFunctions() {
		downloadFromSocket.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CommandAnalisis.downloadFromSocketQuery();
				addStringToLog("Current state downloaded from socket");
			}
		});
	}

	private void downloadFromFileFunction() {
		downloadFromFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new FileReadWriteFrame(Command.READ_FROM_FILE);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	private void printToFileFunction() {
		printToFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new FileReadWriteFrame(Command.WRITE_TO_FILE);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	private void startSchedulingFunction() {
		CommandAnalisis.startSchedulingQuery();
		startScheduling.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!scheduling) {
					scheduling = true;
					startScheduling.setText("stop scheduling");
					addStringToLog("Start scheduling");
				} else {
					scheduling = false;
					startScheduling.setText("start scheduling");
					addStringToLog("Stop scheduling");
				}
			}
		});
	}

	private void showInfoFunctions() {
		showInfo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new ShowInfoFrame();
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	private void addRandTimeFunction() {
		addRandTimeEvent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new AddRandTimeFrame();
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	private void cloneEventFunction() {
		cloneEvent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new CloneEventFrame();
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	private void removeEventFunction() {
		removeEvent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new RemoveEventFrame();
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	private void addEventFunction() {
		addEvent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new AddEventFrame();
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	private void newUserFunction() {
		newUser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new CreateModifyUserWindow(Command.CREATE);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	private void modifyUserFunction() {
		modifyUser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new CreateModifyUserWindow(Command.MODIFY);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	private void addCells() {
		JPanel backgroundPanel = new JPanel();
		backgroundPanel.setLayout(new GridLayout(1, 2));

		log = new JTextArea(15, 10);
		log.setEditable(false);
		JPanel buttonBackground = new JPanel();
		JScrollPane scrollingArea = new JScrollPane(log);

		newUser = new JButton("create user");
		modifyUser = new JButton("modify user");
		addEvent = new JButton("add event");
		removeEvent = new JButton("remove event");
		addRandTimeEvent = new JButton("add random time event");
		cloneEvent = new JButton("clone event");
		showInfo = new JButton("show info");
		startScheduling = new JButton("start scheduling");
		printToFile = new JButton("print to file");
		downloadFromFile = new JButton("download from file");
		downloadFromSocket = new JButton("download from socket");

		buttonBackground.add(newUser);
		buttonBackground.add(modifyUser);
		buttonBackground.add(addEvent);
		buttonBackground.add(removeEvent);
		buttonBackground.add(addRandTimeEvent);
		buttonBackground.add(cloneEvent);
		buttonBackground.add(showInfo);
		buttonBackground.add(startScheduling);
		buttonBackground.add(printToFile);
		buttonBackground.add(downloadFromFile);
		buttonBackground.add(downloadFromSocket);

		backgroundPanel.add(scrollingArea);
		backgroundPanel.add(buttonBackground);
		add(backgroundPanel);
	}

	public boolean isScheduling() {
		return scheduling;
	}
}
