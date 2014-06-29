package schedule;

interface MessagesAndRegularExpressions {

	public static final String activeName = "active";
	public static final String passiveName = "passive";
	public static final String dateTimeFormat = "dd.MM.yyyy-HH:mm:ss";
	
	
	public static final String pStartScheduling ="(StartScheduling)";
	
	public static final String pShowInfo ="(ShowInfo)(\\()"
			+ "(.+)"
			+ "(\\))";
	public static final int[] gShowInfo = {3};
	
	public static final String pCloneEvent ="(CloneEvent)(\\()"
			+ "(.+)"
			+ "(,)"
			+ "(.+)"
			+ "(,)"
			+ "(.+)"
			+ "(\\))";
	public static final int[] gCloneEvent = {3,5,7};
	
	public static final String pAddRandomTimeEvent ="(AddRandomTimeEvent)(\\()"
			+ "(.+)"
			+ "(,)"
			+ "(.+)"
			+ "(,)"
			+ "(.+)"
			+ "(,)"
			+ "(.+)"
			+ "(\\))";
	public static final int[] gAddRandomTimeEvent = {3,5,7,9};
	
	
	public static final String pRemoveEvent ="(RemoveEvent)(\\()"
			+ "(.+)"
			+ "(,)"
			+ "(.+)"
			+ "(\\))";
	public static final int[] gRemoveEvent = {3,5};
	
	public static final String pAddEvent ="(AddEvent)(\\()"
			+ "(.+)"
			+ "(,)"
			+ "(.+)"
			+ "(,)"
			+ "(.+)"
			+ "(\\))";
	public static final int[] gAddEvent = {3,5,7};
		
	public static final String pCreate ="(Create)(\\()"
			+ "(.+)"
			+ "(,)"
			+ "(.+)"
			+ "(,)"
			+ "(.+)"
			+ "(\\))";
	public static final int[] gCreate = {3,5,7};
	
	public static final String pModify ="(Modify)(\\()"
			+ "(.+)"
			+ "(,)"
			+ "(.+)"
			+ "(,)"
			+ "(.+)"
			+ "(\\))";
	public static final int[] gModify = {3,5,7};
	
	public static final String pGMT = "GMT([\\+\\-]\\d+)?";
	public static final int[] gGMT = {1};
	
	public static final String pActivness = "(("+activeName+")|("+passiveName+"))";
	public static final int[] gActivness = {0};
	
	
	public static final String msgNoFunction = "No such function or syntax error";
	public static final String msgGmtError = "Can't parse GMT, type like 'GMT+1'";
	public static final String msgNoGmt = "No such GMT, must be between 'GMT-12' and 'GMT+14'";
	public static final String msgActiveStatusError = "Wrong active stat, must have value 'active' or 'passive'";
	public static final String msgNameExists = "Such name already exists";
	public static final String msgNameNotExists = "No user for specified name";
	public static final String msgDateError = "Incorrect date format, must be " + dateTimeFormat;
	public static final String msgEventAlreadyExists = "Such event already exists fo specified user";
	public static final String msgNoEvent = "No such event for specified user";
	public static final String msgDone = "Done";
}
