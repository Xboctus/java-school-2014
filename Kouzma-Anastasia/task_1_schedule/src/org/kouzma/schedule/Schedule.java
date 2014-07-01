package org.kouzma.schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Schedule {

	public static void main(String args[]) {
		CommandAnalizer analizer = new CommandAnalizer();
		BufferedReader br = new BufferedReader(
				new InputStreamReader(System.in)); 
		
		try {
			while (true) {
				String command = br.readLine();
	
				String answer = analizer.analizeCommand(command);
				if (answer == null)
					break;
				else
					System.out.println(answer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		String dt = "30.06.2014-11:34:";
//		
//		List<String> lstCommands = new ArrayList<String>();
//		lstCommands.add("Create(Mary, GMT+0, passive)");
//		lstCommands.add("Create(John, GMT-0, active)");
//		lstCommands.add("Modify(Mary, GMT+0, active)");
//		lstCommands.add("AddEvent(John, John todo, " + dt + "30)"); 
//		lstCommands.add("AddEvent(Mary, Mary try, " + dt + "30)");
//		lstCommands.add("AddEvent(Mary, 1, " + dt + "13)");
//		lstCommands.add("AddEvent(Mary, 2, " + dt + "03)"); 
//		lstCommands.add("AddEvent(John, toClone, " + dt + "00)"); 	
//		lstCommands.add("RemoveEvent(John, error)");
//		lstCommands.add("RemoveEvent(John,John todo)");
//		lstCommands.add("AddRandomTimeEvent(John, random event, " + dt + "00, " + dt + "30)");
//		lstCommands.add("CloneEvent(John, toClone, Mary)");
//		lstCommands.add("ShowInfo(Mary)");
//		lstCommands.add("ShowInfo(John)");
//		
//		lstCommands.add("StartScheduling");
//		
//		for (String command : lstCommands) {
//			System.out.println("> " + command);
//			System.out.println(analizer.analizeCommand(command));
//		}
	}
}