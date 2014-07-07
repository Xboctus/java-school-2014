package org.kouzma.schedule;

import java.awt.EventQueue;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import org.kouzma.schedule.gui.SheduleWindow;
import org.kouzma.schedule.util.ScheduleConnection;

public class Schedule {

	private static ServerSocket serverSocket;

	public static void main(String[] args) {
		final ScheduleCreator creator = new ScheduleCreator();
		
		// get file by defaultFileName
		final String answer;
		if (args.length > 1 && args[0].equals("-defaultFileName")) {
			answer = creator.LoadFromFile(args[1]);
		}
		else {
			answer = null;
		}

		creator.StartScheduling();
		
		// port
		try {
			serverSocket = new ServerSocket(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final int port = (serverSocket != null) ? serverSocket.getLocalPort() : null;
		
		//Start server
		Thread serverThread = new Thread() {
			public void run() {
				try {
					while (true) {
						Socket socket = serverSocket.accept();
						
						OutputStream os = socket.getOutputStream();
						OutputStream bos = new BufferedOutputStream(os);
                        ObjectOutput output = new ObjectOutputStream(bos);
						
						output.writeObject(creator.getUsers());
						output.flush();
						
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		serverThread.start();
		
		// Add GUI
		for (int i = 0; i < 1; i++) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						SheduleWindow window = new SheduleWindow(creator, answer, port);
						window.getFrame().setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	
	/*public static void main(String args[]) {
	CommandAnalizer analizer = new CommandAnalizer();
//	BufferedReader br = new BufferedReader(
//			new InputStreamReader(System.in)); 
//	
//	try {
//		while (true) {
//			String command = br.readLine();
//
//			String answer = analizer.analizeCommand(command);
//			if (answer == null)
//				break;
//			else
//				System.out.println(answer);
//		}
//	} catch (IOException e) {
//		e.printStackTrace();
//	}
	
	String dt = "03.07.2014-22:58:";
	
	List<String> lstCommands = new ArrayList<String>();
	lstCommands.add("Create(Mary, GMT+4, passive)");
	lstCommands.add("Create(John, GMT+4, active)");
	lstCommands.add("Modify(Mary, GMT+4, active)");
	lstCommands.add("AddEvent(John, John todo, " + dt + "30)"); 
	lstCommands.add("AddEvent(Mary, Mary try, " + dt + "30)");
	lstCommands.add("AddEvent(Mary, 1, " + dt + "13)");
	lstCommands.add("AddEvent(Mary, 2, " + dt + "03)"); 
	lstCommands.add("AddEvent(Mary, 4, " + dt + "03)");
	lstCommands.add("AddEvent(Mary, 3, " + dt + "03)"); 
	lstCommands.add("AddEvent(John, toClone, " + dt + "00)"); 	
	lstCommands.add("RemoveEvent(John, error)");
	lstCommands.add("RemoveEvent(John,John todo)");
	lstCommands.add("AddRandomTimeEvent(John, random event, " + dt + "00, " + dt + "30)");
	lstCommands.add("CloneEvent(John, toClone, Mary)");
	lstCommands.add("ShowInfo(Mary)");
	lstCommands.add("ShowInfo(John)");
	
	lstCommands.add("StartScheduling");
	
	for (String command : lstCommands) {
		System.out.println("> " + command);
		System.out.println(analizer.analizeCommand(command));
	}
}*/

}