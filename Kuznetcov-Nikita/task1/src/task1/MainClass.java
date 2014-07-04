package task1;

import task1.Util.ResponseStatus;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Sunrise on 28.06.2014.
 */
public class MainClass {

  public static void main(String[] args) throws IOException {

    if (args.length > 0 && args[0].equals("-g")) {
      JFrame SchedulerFrame = new task1.GUI.SchedulerFrame("Scheduler");
      return;
    }

    Coordinator taskCoordinator = new Coordinator();
    String command;
    InputStreamReader inputStream = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(inputStream);
    System.out.println("Scheduler started at editor mode!");

    do {
      command = reader.readLine();

      if (command.matches("Create\\((.+), (.+), (.+)\\)")) {
        // Create(name, timezone, active)
        String[] params = command.substring(command.indexOf('(') + 1, command.lastIndexOf(')')).replaceAll(" ", "").trim().split(",");
        ResponseStatus status = taskCoordinator.addNewUser(params[0], params[1], params[2].equals("active"));
        System.out.println(parseResponseStatus(status));
      }

      if (command.matches("Modify\\((.+), (.+), (.+)\\)")) {
        // Modify(name, timezone, active)
        String[] params = command.substring(command.indexOf('(') + 1, command.lastIndexOf(')')).replaceAll(" ", "").trim().split(",");
        ResponseStatus status = taskCoordinator.modifyUser(params[0], params[1], params[2].equals("active"));
        System.out.println(parseResponseStatus(status));
      }

      if (command.matches("AddEvent\\((.+), (.+), (.+)\\)")) {
        // AddEvent(name, text, datetime)
        String[] params = command.substring(command.indexOf('(') + 1, command.lastIndexOf(')')).replaceAll(" ", "").trim().split(",");
        ResponseStatus status = taskCoordinator.addEvent(params[0], params[1], params[2]);
        System.out.println(parseResponseStatus(status));
      }

      if (command.matches("RemoveEvent\\((.+), (.+)\\)")) {
        // RemoveEvent(name, text)
        String[] params = command.substring(command.indexOf('(') + 1, command.lastIndexOf(')')).replaceAll(" ", "").trim().split(",");
        ResponseStatus status = taskCoordinator.removeEvent(params[0], params[1]);
        System.out.println(parseResponseStatus(status));
      }

      if (command.matches("AddRandomTimeEvent\\((.+), (.+), (.+), (.+)\\)")) {
        // AddRandomTimeEvent(name, text, dateFrom, dateTo)
        String[] params = command.substring(command.indexOf('(') + 1, command.lastIndexOf(')')).replaceAll(" ", "").trim().split(",");
        ResponseStatus status = taskCoordinator.addRandomTimeEvent(params[0], params[1], params[2], params[3]);
        System.out.println(parseResponseStatus(status));
      }

      if (command.matches("CloneEvent\\((.+), (.+), (.+)\\)")) {
        // CloneEvent(name, text, nameTo)
        String[] params = command.substring(command.indexOf('(') + 1, command.lastIndexOf(')')).replaceAll(" ", "").trim().split(",");
        ResponseStatus status = taskCoordinator.cloneEvent(params[0], params[1], params[2]);
        System.out.println(parseResponseStatus(status));
      }

      if (command.matches("ShowInfo\\((.+)\\)")) {
        // ShowInfo(name)
        String[] params = command.substring(command.indexOf('(') + 1, command.lastIndexOf(')')).replaceAll(" ", "").trim().split(",");
        User targetUser = taskCoordinator.getUsersMap().get(params[0]);
        if (targetUser != null) {
          System.out.println(targetUser.toString());
        } else {
          System.out.println("User with such username doesn't existst");
        }
      }

      if (command.matches("StartScheduling")) {
        System.out.println("Start planning...");
        taskCoordinator.startScheduling();
        reader.close();
        inputStream.close();
        break;
      }

      System.out.println("Users list:");
      System.out.println(taskCoordinator.getUsersMap());

    } while (true);

  }

  private static String parseResponseStatus(ResponseStatus status) {
    String result = "Wrong response code!";
    switch (status) {
      case USER_ADDED: {
        result = "User successfully added!"; break;
      }
      case USER_MODIFIED: {
        result = "User successfully modified"; break;
      }
      case EVENT_ADDED: {
        result = "Event successfully added!"; break;
      }
      case EVENT_REMOVED: {
        result = "Event successfully removed!"; break;
      }
      case EVENT_CLONED: {
        result = "Event successfully cloned!"; break;
      }
      case USER_ALREADY_EXISTS: {
        result = "User with this username already exists!"; break;
      }
      case USER_NOT_FOUND: {
        result = "User with such username not found!"; break;
      }
      case TARGET_USER_NOT_FOUND: {
        result = "Target user not found!"; break;
      }
      case EVENT_NOT_FOUND: {
        result = "Event with that text not found!"; break;
      }
      case EVENT_ALREADY_EXISTS: {
        result = "Event with that text already exists"; break;
      }
      case BAD_DATE_FORMAT: {
        result = "Wrong date format! Try to write like \"dd.MM.yyyy-hh.mm.ss\""; break;
      }
      case BAD_TIMEZONE_FORMAT: {
        result = "Wrong timezone format! Try to write like \"GMT Sign Hours\" or \"GMT Sign Hours : Minutes\""; break;
      }
      case WRONG_DATE_DIFFERENCE: {
        result = "DateTo you were entered is early than DateFrom"; break;
      }
    }
    return result;
  }

}
