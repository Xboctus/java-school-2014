package org.kouzma.schedule.gui;

import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import javax.swing.JButton;

import org.kouzma.schedule.ScheduleCreator;
import org.kouzma.schedule.User;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SheduleWindow {

	public abstract class DialogCallBack {
		public void sendParams(List<String> arrParams) {
			run(arrParams);
		}
		
		protected abstract void run(List<String> arrParams);
	}
	
	public class ScheduleCallBack {
		public void sendMessage(String message) {
			logArea.append(message);
		}
	}

	private ScheduleCreator creator;

	private JFrame frame;

	private JTextArea logArea;

	private final Map<ActionType, ActionParams> actionMap = new HashMap<ActionType, ActionParams>();
	
	/**
	 * Create the application.
	 */
	public SheduleWindow() {
		creator = new ScheduleCreator(new ScheduleCallBack());
		actionMap.put(ActionType.CREATE, new ActionParams("Create user",
				new String[] {"Name", "TimeZone", "Status"},
				new InputType[] {InputType.TEXT, InputType.TIMEZONE, InputType.STATUS},
				"Create"));
		actionMap.put(ActionType.MODIFY, new ActionParams("Modify user",
				new String[] {"Name", "TimeZone", "Status"},
				new InputType[] {InputType.TEXT, InputType.TIMEZONE, InputType.STATUS},
				"Modify"));
		actionMap.put(ActionType.ADD, new ActionParams("Add event",
				new String[] {"User name", "Text", "Date"},
				new InputType[] {InputType.TEXT, InputType.TEXT, InputType.DATE},
				"AddEvent"));
		actionMap.put(ActionType.REMOVE, new ActionParams("Remove event",
				new String[] {"User name", "Text"},
				new InputType[] {InputType.TEXT, InputType.TEXT},
				"RemoveEvent"));
		actionMap.put(ActionType.RANDOM, new ActionParams("Add random time event",
				new String[] {"User name", "Text", "Date from", "Date to"},
				new InputType[] {InputType.TEXT, InputType.TEXT, InputType.DATE, InputType.DATE},
				"AddRandomTimeEvent"));
		actionMap.put(ActionType.CLONE, new ActionParams("Clone event",
				new String[] {"User name", "Text", "User name to"},
				new InputType[] {InputType.TEXT, InputType.TEXT, InputType.TEXT},
				"CloneEvent"));
		actionMap.put(ActionType.SHOW, new ActionParams("Show user information",
				new String[] {"User name"},
				new InputType[] {InputType.TEXT},
				"ShowUserInfo"));
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Лог
		{
			JPanel logPanel = new JPanel();
			frame.getContentPane().add(logPanel, BorderLayout.CENTER);
	
			logPanel.setLayout(new BorderLayout(0, 0));
	
			JScrollPane scrollPanel = new JScrollPane();
			logPanel.add(scrollPanel);
	
			logArea = new JTextArea();
			logArea.setEditable(false);
			logArea.setLineWrap(true);
			scrollPanel.setViewportView(logArea);
		}
		
		// Кнопки
		{
			GridBagLayout gbl_buttonPanel = new GridBagLayout();
			gbl_buttonPanel.rowHeights = new int[] {40, 40, 40, 40, 40, 40, 40, 120, 0};
			gbl_buttonPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			
			JPanel buttonPanel = new JPanel(gbl_buttonPanel);
			frame.getContentPane().add(buttonPanel, BorderLayout.EAST);
			
			JButton btnCreateUser = createButton(ActionType.CREATE);
			
			JButton btnModifyUser = createButton(ActionType.MODIFY);
			JButton btnAddEvent = createButton(ActionType.ADD);
			JButton btnRemoveEvent = createButton(ActionType.REMOVE);
			JButton btnAddRandomTimeEvent = createButton(ActionType.RANDOM);
			JButton btnCloneEvent = createButton(ActionType.CLONE);
			JButton btnShowInfo = createButton(ActionType.SHOW);
			
			JButton btnStartScheduling = new JButton("Start scheduling");
			btnStartScheduling.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent ev) {
					logArea.append("> StartScheduling()\n");
					String answer = creator.StartScheduling();
					if (answer != null)
						logArea.append(answer);
				}
			});
			
			JButton[] arrButtons =new JButton[] {btnCreateUser, btnModifyUser, btnAddEvent, btnRemoveEvent, 
												btnAddRandomTimeEvent, btnCloneEvent, btnShowInfo, btnStartScheduling};
			
			for (int i = 0; i < arrButtons.length; i++) {
				GridBagConstraints constraints = new GridBagConstraints();
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.gridx = 0;
				constraints.gridy = i;
	
				constraints.insets = new Insets(5, 5, 0, 5);
				buttonPanel.add(arrButtons[i], constraints);
			}
		}
		creator.StartScheduling();
	}

	private JButton createButton(final ActionType type) {
		final ActionParams actionParams = actionMap.get(type);
		JButton button = new JButton(actionParams.title);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				ScheduleDialog dialog = new ScheduleDialog(actionParams.title, actionParams.arrParamNames, actionParams.arrInputTypes,
						new DialogCallBack() {

							@Override
							protected void run(List<String> arrParams) {
								String answer = "";
								switch (type) {
								case CREATE:
									 answer = creator.createUser(arrParams.get(0), arrParams.get(1), arrParams.get(2));
									 break;
								case MODIFY:
									 answer = creator.ModifyUser(arrParams.get(0), arrParams.get(1), arrParams.get(2));
									 break;
								case ADD:
									 answer = creator.AddEvent(arrParams.get(0), arrParams.get(1), arrParams.get(2));
									 break;
								case REMOVE:
									 answer = creator.RemoveEvent(arrParams.get(0), arrParams.get(1));
									 break;
								case RANDOM:
									 answer = creator.AddRandomTimeEvent(arrParams.get(0), arrParams.get(1), arrParams.get(2), arrParams.get(3));
									 break;
								case CLONE:
									 answer = creator.CloneEvent(arrParams.get(0), arrParams.get(1), arrParams.get(2));
									 break;
								case SHOW: // TODO сделать по-человечески
									 answer = creator.ShowInfo(arrParams.get(0));
									 String[] userInfo = creator.getUser(arrParams.get(0));
									 if (userInfo != null) {
										 final ActionParams userActionParams = actionMap.get(ActionType.MODIFY);
										 UserDialog userDialog = new UserDialog(
												 userActionParams.title, userActionParams.arrParamNames, userActionParams.arrInputTypes,
												 new DialogCallBack() {
											
											@Override
											protected void run(List<String> arrParams) {
												// TODO Auto-generated method stub
												
											}
										}, userInfo);
										 userDialog.setVisible(true);
									 }
									 break;
								}
								
								StringBuffer sb = new StringBuffer();
								sb.append("> ");
								sb.append(actionParams.command);
								sb.append("(");
								
								for (int i = 0; i < arrParams.size(); i++) {
									sb.append(arrParams.get(i));
									if (i < arrParams.size() - 1)
										sb.append(", ");
								}
								
								sb.append(")\n");
								
								logArea.append(sb.toString());
								logArea.append(answer + "\n");
							}
					
				});
				
				dialog.setVisible(true);
			}
		});
		return button;
	}

	public Window getFrame() {
		return frame;
	}

}
