package org.kouzma.schedule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Anastasya Kouzma
 *
 */
public class CommandAnalizer {

	private Pattern commandPattern = Pattern.compile("(\\w+)(\\((.*)\\))?");
	
	private final String CREATE = "Create";
	private final String MODIFY = "Modify";
	private final String ADD_EVENT = "AddEvent";
	private final String REMOVE_EVENT = "RemoveEvent";
	private final String ADD_RANDOME_EVENT = "AddRandomTimeEvent";
	private final String CLONE_EVENT = "CloneEvent";
	private final String SHOW_INFO = "ShowInfo";
	private final String START_SCHEDULING = "StartScheduling";

	private final String ERROR_WRONG_COMMAND = "No such command";

	private ScheduleCreator creator;
	
	public CommandAnalizer() {
		creator = new ScheduleCreator();
	}
	
	public String analizeCommand(String command) {
		String answer;
		
		Matcher matcher = commandPattern.matcher(command);
		if (matcher.matches()) {
			String commandName = matcher.group(1);
			String[] arrArgs = null;
			
			if (matcher.groupCount() == 3 &&  matcher.group(3) != null) {
				arrArgs = matcher.group(3).split(",");
				for (int i = 0; i < arrArgs.length; i++)
					arrArgs[i] = arrArgs[i].trim();
			}
			
			switch (commandName) {
			case CREATE:
				answer = createUser(arrArgs);
				break;
			case MODIFY:
				answer = ModifyUser(arrArgs);
				break;
			case ADD_EVENT:
				answer = AddEvent(arrArgs);
				break;
			case REMOVE_EVENT:
				answer = RemoveEvent(arrArgs);
				break;
			case ADD_RANDOME_EVENT:
				answer = AddRandomTimeEvent(arrArgs);
				break;
			case CLONE_EVENT:
				answer = CloneEvent(arrArgs);
				break;
			case SHOW_INFO:
				answer = ShowInfo(arrArgs);
				break;
			case START_SCHEDULING:
				answer = StartScheduling(arrArgs);
				break;
			default:
				answer = ERROR_WRONG_COMMAND;
			}
		}
		else {
			answer = ERROR_WRONG_COMMAND;
		}
		
		return answer;
	}


	private String createUser(String[] arrArgs) {
		if (arrArgs == null || arrArgs.length != 3)
			return ERROR_WRONG_COMMAND;
		
		return creator.createUser(arrArgs[0], arrArgs[1], arrArgs[2]);
	}


	private String ModifyUser(String[] arrArgs) {
		if (arrArgs == null || arrArgs.length != 3)
			return ERROR_WRONG_COMMAND;

		return creator.ModifyUser(arrArgs[0], arrArgs[1], arrArgs[2]);
	}

	private String AddEvent(String[] arrArgs) {
		if (arrArgs == null || arrArgs.length != 3)
			return ERROR_WRONG_COMMAND;

		return creator.AddEvent(arrArgs[0], arrArgs[1], arrArgs[2]);
	} 

	private String RemoveEvent(String[] arrArgs) {
		if (arrArgs == null || arrArgs.length != 2)
			return ERROR_WRONG_COMMAND;

		return creator.RemoveEvent(arrArgs[0], arrArgs[1]);
	}

	private String AddRandomTimeEvent(String[] arrArgs) {
		if (arrArgs == null || arrArgs.length != 4)
			return ERROR_WRONG_COMMAND;
		
		return creator.AddRandomTimeEvent(arrArgs[0], arrArgs[1], arrArgs[2], arrArgs[3]);
	}

	private String CloneEvent(String[] arrArgs) {
		if (arrArgs == null || arrArgs.length != 3)
			return ERROR_WRONG_COMMAND;

		return creator.CloneEvent(arrArgs[0], arrArgs[1], arrArgs[2]);
	}

	private String ShowInfo(String[] arrArgs) {
		if (arrArgs == null || arrArgs.length != 1)
			return ERROR_WRONG_COMMAND;

		return creator.ShowInfo(arrArgs[0]);
	}

	private String StartScheduling(String[] arrArgs) {
		if (arrArgs != null && arrArgs.length > 0)
			return ERROR_WRONG_COMMAND;
		
		return creator.StartScheduling();
	}
}
