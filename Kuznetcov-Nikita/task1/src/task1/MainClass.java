package task1;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Sunrise on 28.06.2014.
 */
public class MainClass {

  public static void main(String[] args) throws IOException {

    JFrame schedulerFrame = new SchedulerFrame("Scheduler");

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
        int result = taskCoordinator.addNewUser(params[0], params[1], params[2].equals("active"));
        switch (result) {
          case 0: {
            System.out.println(params[0] + " successfully added!"); break;
          }
          case 1: {
            System.out.println("User with such username already exists!"); break;
          }
          case 2: {
            System.out.println("Bad GMT TimeZone format, please write like \"GMT+5\" or \"GMT-8:00\""); break;
          }
        }
      }

      if (command.matches("Modify\\((.+), (.+), (.+)\\)")) {
        // Modify(name, timezone, active)
        String[] params = command.substring(command.indexOf('(') + 1, command.lastIndexOf(')')).replaceAll(" ", "").trim().split(",");
        int result = taskCoordinator.modifyUser(params[0], params[1], params[2].equals("active"));
        switch (result) {
          case 0 : {
            System.out.println("User: " + params[0] + " with TimeZone " + params[1] + " and status " + params[2] + " added!"); break;
          }
          case 1 : {
            System.out.println("User with such username doesn't exists!"); break;
          }
          case 2 : {
            System.out.println("Bad GMT TimeZone format, please write like \"GMT+5\" or \"GMT-8:00\""); break;
          }
        }
      }

      if (command.matches("AddEvent\\((.+), (.+), (.+)\\)")) {
        // AddEvent(name, text, datetime)
        String[] params = command.substring(command.indexOf('(') + 1, command.lastIndexOf(')')).replaceAll(" ", "").trim().split(",");
        int result = taskCoordinator.addEvent(params[0], params[1], params[2]);
        switch (result) {
          case 0 : {
            System.out.println("Event successfully added to user " + params[0] + "'s event list!"); break;
          }
          case 1 : {
            System.out.println("User with such username doesn't exists!"); break;
          }
          case 2 : {
            System.out.println("Bad date format! Acceptable format: \"dd.MM.yyyy-HH:mm:ss\""); break;
          }
          case 3 : {
            System.out.println("Event with specified text already exists!"); break;
          }
        }
      }

      if (command.matches("RemoveEvent\\((.+), (.+)\\)")) {
        // RemoveEvent(name, text)
        String[] params = command.substring(command.indexOf('(') + 1, command.lastIndexOf(')')).replaceAll(" ", "").trim().split(",");
        int result = taskCoordinator.removeEvent(params[0], params[1]);
        switch (result) {
          case 0 : {
            System.out.println("Event successfully removed from " + params[0] + "'s event list!"); break;
          }
          case 1 : {
            System.out.println("User with such username doesn't exists!"); break;
          }
          case 2 : {
            System.out.println("Event with specified text didn't found in " + params[0] + "'s event list!"); break;
          }
        }
      }

      if (command.matches("AddRandomTimeEvent\\((.+), (.+), (.+), (.+)\\)")) {
        // AddRandomTimeEvent(name, text, dateFrom, dateTo)
        String[] params = command.substring(command.indexOf('(') + 1, command.lastIndexOf(')')).replaceAll(" ", "").trim().split(",");
        int result = taskCoordinator.addRandomTimeEvent(params[0], params[1], params[2], params[3]);
        switch (result) {
          case 0 : {
            System.out.println("Event successfully added to user " + params[0]); break;
          }
          case 1 : {
            System.out.println("User with such username doesn't exists!"); break;
          }
          case 2 : {
            System.out.println("DateTo you were entered is early than DateFrom"); break;
          }
          case 3 : {
            System.out.println("Event with specified text already present is user's event list"); break;
          }
        }
      }

      if (command.matches("CloneEvent\\((.+), (.+), (.+)\\)")) {
        // CloneEvent(name, text, nameTo)
        String[] params = command.substring(command.indexOf('(') + 1, command.lastIndexOf(')')).replaceAll(" ", "").trim().split(",");
        int result = taskCoordinator.cloneEvent(params[0], params[1], params[2]);
        switch (result) {
          case 0 : {
            System.out.println("Event successfully cloned to user " + params[2]); break;
          }
          case 1 : {
            System.out.println("Source user with such username doesn't exists!"); break;
          }
          case 2 : {
            System.out.println("Target user with such username doesn't exists!"); break;
          }
          case 3 : {
            System.out.println("Source event with specified text doesn't exists"); break;
          }
          case 4 : {
            System.out.println("Event with specified text already present is target user's event list"); break;
          }
        }
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
        taskCoordinator.StartScheduling();
        reader.close();
        inputStream.close();
        break;
      }

      System.out.println("Users list:");
      System.out.println(taskCoordinator.getUsersMap());

    } while (true);

  }

}
