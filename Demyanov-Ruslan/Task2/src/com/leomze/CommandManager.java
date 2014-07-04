package com.leomze;




import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandManager {

    private static final String CREATE = "1";
    private static final String MODIFY = "2";
    private static final String ADD_EVENT = "3";
    private static final String ADD_RANDOME_EVENT = "4";
    private static final String REMOVE_EVENT = "5";
    private static final String CLONE_EVENT = "6";
    private static final String SHOW_INFO = "7";
    private static final String START_SCHEDULING = "8";
    private static final String WRONG_COMMAND = "Wrong command!";
    private static final String SHOW_USERS = "9";
    boolean editMode = false;
    TaskHandler taskHandler = new TaskHandler();

    public void sart(){

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        try{
            while (!editMode){

                System.out.println("\n Please choose command: \n");
                System.out.println("\t 1 - Create new user\n");
                System.out.println("\t 2 - Modify user\n");
                System.out.println("\t 3 - AddEvent\n");
                System.out.println("\t 4 - AddRandomEvent\n");
                System.out.println("\t 5 - RemoveEvent\n");
                System.out.println("\t 6 - CloneEvent\n");
                System.out.println("\t 7 - ShowInfo\n");
                System.out.println("\t 8 - StartScheduling\n");
                String command = bufferedReader.readLine();
                System.out.println(parseCommand(command));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private String parseCommand(String command) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String answer;


            switch (command) {
                case CREATE:
                {
                    String user, timezone, active;
                    System.out.println("\nWrite user name:");
                    answer = bufferedReader.readLine();
                    user = answer;
                    System.out.println("\nWrite timezone for user: ");
                    answer = bufferedReader.readLine();
                    timezone = answer;
                    System.out.println("\nWrite status for user: ");
                    answer = bufferedReader.readLine();
                    active = answer;
                   return taskHandler.create(user, timezone, active);


                }
                case MODIFY:
                {
                    String user, timezone, active;
                    System.out.println("\nWrite user name: ");
                    answer = bufferedReader.readLine();
                    user = answer;
                    System.out.println("\nWrite timezone to modify: ");
                    answer = bufferedReader.readLine();
                    timezone = answer;
                    System.out.println("\nWrite status to modify: ");
                    answer = bufferedReader.readLine();
                    active = answer;
                    return taskHandler.modify(user, timezone, active);


                }
                case ADD_EVENT:
                {
                    String user, text, date;
                    System.out.println("Write user name:");
                    answer = bufferedReader.readLine();
                    user = answer;
                    System.out.println("Write text for Event: ");
                    answer = bufferedReader.readLine();
                    text = answer;
                    System.out.println("Write date for event: ");
                    answer = bufferedReader.readLine();
                    date = answer;
                    return taskHandler.addEvent(user, text, date);


                }
                case ADD_RANDOME_EVENT:
                {
                    String user, text, dateFrom, dateTo;
                    System.out.println("Write user name:");
                    answer = bufferedReader.readLine();
                    user = answer;
                    System.out.println("Write text for Event: ");
                    answer = bufferedReader.readLine();
                    text = answer;
                    System.out.println("Write start date interval: ");
                    answer = bufferedReader.readLine();
                    dateFrom = answer;
                    System.out.println("Write end date interval: ");
                    answer = bufferedReader.readLine();
                    dateTo = answer;
                    return taskHandler.addRandomEvent(user, text, dateFrom, dateTo);


                }
                case REMOVE_EVENT:
                {
                    String user, text, date;
                    System.out.println("Write user name:");
                    answer = bufferedReader.readLine();
                    user = answer;
                    System.out.println("Write text for Event you want remove: ");
                    answer = bufferedReader.readLine();
                    text = answer;
                    return taskHandler.removeEvent(user, text);


                }
                case CLONE_EVENT:
                {
                    String user, text, userTo;
                    System.out.println("Write user name:");
                    answer = bufferedReader.readLine();
                    user = answer;
                    System.out.println("Write text for Event: ");
                    answer = bufferedReader.readLine();
                    text = answer;
                    System.out.println("Write user for what clone Event: ");
                    answer = bufferedReader.readLine();
                    userTo = answer;
                    return taskHandler.cloneEvent(user, text, userTo);


                }
                case SHOW_INFO:
                {
                    taskHandler.showUsers();
                    String user;
                    System.out.println("Write user name:");
                    answer = bufferedReader.readLine();
                    user = answer;
                    return taskHandler.showInfo(user);


                }
                case START_SCHEDULING:
                     new ShowTasks().start(taskHandler.users);
                    break;
                case SHOW_USERS:
                    return taskHandler.showUsers();
                default:
                    answer = WRONG_COMMAND;
            }




        return null;

    }




}
