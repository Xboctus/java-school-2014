package org.kouzma.schedule.gui;

public class ActionParams {
	String title;
	String[] arrParamNames;
	InputType[] arrInputTypes;
	String command;
	
	public ActionParams(String actionTitle, String[] arrParams,
			InputType[] arrTypes, String actionCommand) {
		title = actionTitle;
		arrParamNames = arrParams;
		arrInputTypes = arrTypes;
		command = actionCommand;
	}
}
