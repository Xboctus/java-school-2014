package com.javaschool2014.task1;

public interface Constants {

    public static final String DATA_SAVED             = "Serialized data saved!";
    public static final String DATA_LOADED            = "Serialized data loaded!";
    public static final String USER_EXISTS            = "Such user already exists!";
    public static final String WRONG_NAME             = " user not exists!";
    public static final String WRONG_DATE             = "Incorrect date/time!";
    public static final String EVENT_MISSING          = "No such event in existence!";
    public static final String WRONG_STATUS           = "Wrong status!";
    public static final String WRONG_COMMAND          = "Wrong command!";
    public static final String NO_TIMEZONE            = "No timezone set!";
    public static final String NO_DEFAULT_FILE_LOADED = "Default data file not found!";
    public static final String ERROR                  = "Error occurred!";
    public static final String DATE_FORMAT            = "dd.MM.yyyy-HH:mm:ss";
    public static final String DEFAULT_FILE_NAME      = "schedule.ser";

    public static final String createUserPattern             = "Create\\s*\\(.+\\,\\s*GMT(\\+|\\-)\\d*\\,.+\\)\\s*";
    public static final String modifyUserPattern             = "Modify\\s*\\(.+\\,\\s*GMT(\\+|\\-)\\d*\\,.+\\)\\s*";
    public static final String addUserEventPattern           = "AddEvent\\s*\\(.+\\,.+\\,.+\\)\\s*";
    public static final String addRandomTimeUserEventPattern = "AddRandomTimeEvent\\s*\\(.+\\,.+\\,.+\\,.+\\)\\s*";
    public static final String removeUserEventPattern        = "RemoveEvent\\s*\\(.+\\,.+\\)\\s*";
    public static final String cloneUserEventPattern         = "CloneEvent\\s*\\(.+\\,.+\\,.+\\)\\s*";
    public static final String showUserInfoPattern           = "ShowInfo\\s*\\(.+\\)\\s*";
    public static final String saveUserDataPattern           = "SaveData\\s*\\(.+\\)\\s*";
    public static final String loadUserDataPattern           = "LoadData\\s*\\(.+\\)\\s*";
    public static final String leavePattern                  = "Leave";

}