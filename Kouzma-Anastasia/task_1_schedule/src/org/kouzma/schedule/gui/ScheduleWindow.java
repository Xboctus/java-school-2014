package org.kouzma.schedule.gui;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import org.kouzma.schedule.ScheduleCreator;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
/**
 * @author Anastasya Kouzma
 */
public class ScheduleWindow implements ScheduleListener {
	String defaultFileName;
	public abstract class DialogCallBack {
		public void sendParams(List<String> arrParams) {
			run(arrParams);
		}
		
		protected abstract void run(List<String> arrParams);
	}
	
	private ScheduleCreator creator;

	private JFrame frame;

	private JTextArea logArea;

	private static final Map<ActionType, ActionParams> actionMap = new HashMap<ActionType, ActionParams>();
	static {
		actionMap.put(ActionType.CREATE, new ActionParams("Create user",
				new String[] {"Name:", "TimeZone:", "Status:"},
				new InputType[] {InputType.TEXT, InputType.TIMEZONE, InputType.STATUS},
				"Create"));
		actionMap.put(ActionType.MODIFY, new ActionParams("Modify user",
				new String[] {"Name:", "TimeZone:", "Status:"},
				new InputType[] {InputType.TEXT, InputType.TIMEZONE, InputType.STATUS},
				"Modify"));
		actionMap.put(ActionType.ADD, new ActionParams("Add event",
				new String[] {"User name:", "Text:", "Date:"},
				new InputType[] {InputType.TEXT, InputType.TEXT, InputType.DATE},
				"AddEvent"));
		actionMap.put(ActionType.REMOVE, new ActionParams("Remove event",
				new String[] {"User name:", "Text:"},
				new InputType[] {InputType.TEXT, InputType.TEXT},
				"RemoveEvent"));
		actionMap.put(ActionType.RANDOM, new ActionParams("Add random time event",
				new String[] {"User name:", "Text:", "Date from:", "Date to:"},
				new InputType[] {InputType.TEXT, InputType.TEXT, InputType.DATE, InputType.DATE},
				"AddRandomTimeEvent"));
		actionMap.put(ActionType.CLONE, new ActionParams("Clone event",
				new String[] {"User name:", "Text:", "User name to:"},
				new InputType[] {InputType.TEXT, InputType.TEXT, InputType.TEXT},
				"CloneEvent"));
		actionMap.put(ActionType.SHOW, new ActionParams("Show user information",
				new String[] {"User name:"},
				new InputType[] {InputType.TEXT},
				"ShowUserInfo"));
	}
		
	private int socketPort;
	
	/**
	 * Create the application.
	 * @param scheduleCreator 
	 * @param answer 
	 * @param port  
	 */
	public ScheduleWindow(ScheduleCreator scheduleCreator, String answer, int port) {
		creator = scheduleCreator;
		creator.addListener(this);
		socketPort = port;

		initialize();
		
		if (answer != null)
			logArea.append(answer + "\n");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 600);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	    frame.addWindowListener(new WindowAdapter() {
	    	 
	        @Override
	        public void windowClosing(WindowEvent e) {
	        	checkChanges();
	        }
	    });
	    
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
			gbl_buttonPanel.rowHeights = new int[] {30, 30, 30, 30, 30, 30, 50, 50, 30, 50, 30, 50, 30, 50, 30, 0};
			gbl_buttonPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			
			JPanel buttonPanel = new JPanel(gbl_buttonPanel);
			frame.getContentPane().add(buttonPanel, BorderLayout.EAST);
			
			JButton btnCreateUser = createActionButton(ActionType.CREATE);
			JButton btnModifyUser = createActionButton(ActionType.MODIFY);
			JButton btnAddEvent = createActionButton(ActionType.ADD);
			JButton btnRemoveEvent = createActionButton(ActionType.REMOVE);
			JButton btnAddRandomTimeEvent = createActionButton(ActionType.RANDOM);
			JButton btnCloneEvent = createActionButton(ActionType.CLONE);
			JButton btnShowInfo = createActionButton(ActionType.SHOW);
			
			JButton btnStartScheduling = createStartSchedulingButton();
			JButton btnSocket = createSocketButton();
			JButton btnSync = createSyncButton();	
			JButton btnSaveToFile = createSaveToFileButton();
			JButton btnLoadFromFile = createLoadFromFileButton();
			JButton btnSaveToDB = createSaveToDbButton();
			JButton btnLoadFromDB = createLoadFromDbButton();
			JButton btnExit = createExitButton();
			
			JButton[] arrButtons =new JButton[] {btnCreateUser, btnModifyUser,
												btnAddEvent, btnRemoveEvent, btnAddRandomTimeEvent, btnCloneEvent, 
												btnShowInfo, btnStartScheduling,
												btnLoadFromFile, btnSaveToFile,  
												btnSocket, btnSync, 
												btnLoadFromDB, btnSaveToDB,
												btnExit};
			
			for (int i = 0; i < arrButtons.length; i++) {
				GridBagConstraints constraints = new GridBagConstraints();
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.anchor = GridBagConstraints.NORTH;
				constraints.gridx = 0;
				constraints.gridy = i;
	
				constraints.insets = new Insets(5, 5, 0, 5);
				buttonPanel.add(arrButtons[i], constraints);
			}
		}
	}

	public void sendMessage(String message) {
		logArea.append(message);
	}
	
	private JButton createActionButton(final ActionType type) {
		final ActionParams actionParams = actionMap.get(type);
		JButton button = new JButton(actionParams.title);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				ActionDialog dialog = new ActionDialog(actionParams.title, actionParams.arrParamNames, actionParams.arrInputTypes,
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
								case SHOW:
									 answer = creator.ShowInfo(arrParams.get(0));
									 String[] userInfo = creator.getUserInfo(arrParams.get(0));
									 if (userInfo != null) {
										 new UserDialog(userInfo).setVisible(true);
									 }
									 break;
								default:
									return;
								}
								
								// Записываем в лог команду и ответ
								StringBuffer sb = new StringBuffer();
								sb.append(actionParams.command);
								sb.append("(");
								
								for (int i = 0; i < arrParams.size(); i++) {
									sb.append(arrParams.get(i));
									if (i < arrParams.size() - 1)
										sb.append(", ");
								}
								
								sb.append(")");
								
								appendLog(sb.toString(), answer);
							}
					
				});
				dialog.setModal(true);
				dialog.setVisible(true);
			}
		});
		return button;
	}
	
	private JButton createStartSchedulingButton() {
		JButton btnStartScheduling = new JButton("Start scheduling");
		btnStartScheduling.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				appendLog("StartScheduling()", creator.StartScheduling());
			}
		});	
		return btnStartScheduling;
	}
	
	private JButton createSaveToFileButton() {
		JButton btnSave = new JButton("Save to file");
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				JFileChooser fileopen = new JFileChooser();
				int ret = fileopen.showDialog(null, "Open file");
				if (ret == JFileChooser.APPROVE_OPTION) {
				    File file = fileopen.getSelectedFile();
					appendLog("SaveToFile(" + file.getName() + ")", creator.SaveToFile(file));
				}
			}
		});		
		return btnSave;
	}	
	private JButton createLoadFromFileButton() {
		JButton btnLoad = new JButton("Load from file");
		btnLoad.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				JFileChooser fileopen = new JFileChooser();
				int ret = fileopen.showDialog(null, "Open file");
				if (ret == JFileChooser.APPROVE_OPTION) {
				    File file = fileopen.getSelectedFile();
					appendLog("LoadFromFile(" + file.getName() + ")", creator.LoadFromFile(file));
				}
			}
		});	
		return btnLoad;
	}	
	
	private JButton createSocketButton() {
		final JButton btnSocket = new JButton("Socket");
		btnSocket.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				JOptionPane.showMessageDialog(btnSocket, "Port: " + socketPort, "Socket port",
			              JOptionPane.INFORMATION_MESSAGE);
				appendLog("GetPort()", "Port: " + socketPort);
			}
		});	
		return btnSocket;
	}
	
	private JButton createSyncButton() {
		JButton btnSync = new JButton("Sync");
		btnSync.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				String adress = JOptionPane.showInputDialog("adress");
				
				if (adress != null) {
					appendLog("Sync(" + adress + ")", creator.sync(adress));
				}
			}
		});
		return btnSync;
	}

	private JButton createSaveToDbButton() {
		JButton btnSaveToDb = new JButton("Save to database");
		btnSaveToDb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				appendLog("SaveToDb()", creator.saveToDb());
			}
		});
		return btnSaveToDb;		 
	}

	private JButton createLoadFromDbButton() {
		JButton btnLoadFromDb = new JButton("Load from database");
		btnLoadFromDb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				appendLog("LoadFromDb()", creator.loadFromDb());
			}
		});
		return btnLoadFromDb;	
	}

	private JButton createExitButton() {
		JButton btnExit = new JButton("Exit");
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				checkChanges();
			}
		});
		return btnExit;
	}

	private void appendLog(String command, String answer) {
		logArea.append("> " + command + "\n");
		if (answer != null)
			logArea.append(answer + "\n");
	}
	
	private void checkChanges() {
		if (creator.hasChanges())
			 new BeforeExitDialog().setVisible(true);
		else
			System.exit(0);
	}

	public Window getFrame() {
		return frame;
	}

	private class BeforeExitDialog extends JDialog {
		public BeforeExitDialog() {
			setBounds(100, 100, 350, 70);
			getContentPane().setLayout(new BorderLayout());
			JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(contentPanel, BorderLayout.CENTER);
	
			JButton btnSave = new JButton("Save");
			JButton btnExitWithoutSaving = new JButton("ExitWithoutSaving");
			JButton btnCancel = new JButton("Cancel");
			
			btnSave.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent ev) {
					BeforeExitDialog.this.setVisible(false);
					appendLog("SaveToDb()", creator.saveToDb());					
					if (!creator.hasChanges())
						System.exit(0);
				}
			});		
			
			btnExitWithoutSaving.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent ev) {
					System.exit(0);
				}
			});	
			
			btnCancel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent ev) {
					BeforeExitDialog.this.setVisible(false);
				}
			});	
			
			contentPanel.add(btnSave);
			contentPanel.add(btnExitWithoutSaving);
			contentPanel.add(btnCancel);
		}
	}
}
