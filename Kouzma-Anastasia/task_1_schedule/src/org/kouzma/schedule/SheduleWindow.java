package org.kouzma.schedule;

import java.awt.EventQueue;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SheduleWindow {

	public abstract class DialogCallBack {
		public void sendParams(List<String> arrParams) {
			run(arrParams);
		}
		
		protected abstract void run(List<String> arrParams);
	}

	private static ScheduleCreator creator;

	private JFrame frame;

	private JTextArea logArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		creator = new ScheduleCreator();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SheduleWindow window = new SheduleWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SheduleWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//TODO
		JPanel logPanel = new JPanel();
		frame.getContentPane().add(logPanel, BorderLayout.CENTER);

		logPanel.setLayout(new BorderLayout(0, 0));

		GridBagLayout gbl_buttonPanel = new GridBagLayout();
		gbl_buttonPanel.rowHeights = new int[] {40, 40, 40, 40, 40, 40, 40, 40, 0};
		gbl_buttonPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		JPanel buttonPanel = new JPanel(gbl_buttonPanel);
		frame.getContentPane().add(buttonPanel, BorderLayout.EAST);
		
		JButton btnCreateUser = new JButton("Create user");
		btnCreateUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				ScheduleDialog dialog = new ScheduleDialog("Create user",
						new String[] {"Name", "TimeZone", "Status", "Date"},
						new ControlType[] {ControlType.TEXT, ControlType.TIMEZONE, ControlType.STATUS, ControlType.DATE},
						new DialogCallBack() {

							@Override
							protected void run(List<String> arrParams) {
								String answer = creator.createUser(arrParams.get(0), arrParams.get(1), arrParams.get(2));
								logArea.append(">" + "create(" + arrParams.get(0) + ", " + arrParams.get(1) + ", " + arrParams.get(1) + ")\n");
								logArea.append(answer + "\n");
							}
					
				});
				
				dialog.setVisible(true);
			}
		});
		JButton btnModifyUser = new JButton("Modify user");
		JButton btnAddEvent = new JButton("Add event");
		JButton btnRemoveEvent = new JButton("Remove event");
		JButton btnAddRandomTimeEvent = new JButton("Add random time event");
		JButton btnCloneEvent = new JButton("Clone event");
		JButton btnShowInfo = new JButton("Show user information");
		JButton btnStartScheduling = new JButton("Start scheduling");
		
		JButton[] arrButtons =new JButton[] {btnCreateUser, btnModifyUser, btnAddEvent, btnRemoveEvent, 
											btnAddRandomTimeEvent, btnCloneEvent, btnShowInfo, btnStartScheduling};
		
		for (int i = 0; i < arrButtons.length; i++) {
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridx = 0;
			constraints.gridy = i;

			constraints.insets = new Insets(0, 0, 5, 5);
			buttonPanel.add(arrButtons[i], constraints);
		}
		
		JScrollPane scrollPanel = new JScrollPane();
		logPanel.add(scrollPanel);

		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setLineWrap(true);
		scrollPanel.setViewportView(logArea);
		
		/*buttonPanel.add(btnCreateUser);
		buttonPanel.add(btnModifyUser);
		buttonPanel.add(btnAddEvent);
		buttonPanel.add(btnRemoveEvent);
		buttonPanel.add(btnAddRandomTimeEvent);
		buttonPanel.add(btnCloneEvent);
		buttonPanel.add(btnShowInfo);
		buttonPanel.add(btnStartScheduling);*/
	}

}
