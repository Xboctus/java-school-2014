package com.javaschool2014.task1;

import java.util.HashMap;

public class Main implements Constants {

    public static void main(String[] args) {

        // Start program with interface:graphics for GUI and defaultFileName:XXXXXX to load saved data

        System.out.println("Available command list:");
        System.out.println("Create(<name>,<timezone>,<status>), Create(<name>,<timezone>,<status>), (status = active/idle) ");
        System.out.println("AddRandomTimeEvent(<name>,<text>,<dateFrom>,<dateTo>), CloneEvent(<name>,<text>,<nameTo>)");
        System.out.println("ShowInfo(<name>), SaveData(<path>), LoadData(<path>), Leave");
        System.out.println("-----------------------------------------------------------------------------------------------");

        HashMap<String, String> resArgs = new HashMap<>();

        for(String arg: args) {

            String[] arguments = arg.split(":");

            for (int i = 0; i < arguments.length; i++) {
                arguments[i] = arguments[i].trim();
            }

            resArgs.put(arguments[0], arguments[1]);

        }

        if (resArgs.containsKey("interface")) {

            if (resArgs.get("interface").equals("graphics")) {

                GUICoordinator coordinator = new GUICoordinator();
                coordinator.display();

                if (resArgs.containsKey(DEFAULT_NAME)) {
                    coordinator.connectDefaultDataFile(resArgs.get(DEFAULT_NAME));
                }

                coordinator.start();

                GUICoordinator coordinator1 = new GUICoordinator();
                coordinator1.display();

                if (resArgs.containsKey(DEFAULT_NAME)) {
                    coordinator1.connectDefaultDataFile(resArgs.get(DEFAULT_NAME));
                }

                coordinator1.start();

            } else {

                System.out.println("Wrong interface parameter");

                ConsoleCoordinator coordinator = new ConsoleCoordinator();

                if (resArgs.containsKey(DEFAULT_NAME)) {
                    coordinator.connectDefaultDataFile(resArgs.get(DEFAULT_NAME));
                }

                coordinator.start();

            }

        } else {

            ConsoleCoordinator coordinator = new ConsoleCoordinator();

            if (resArgs.containsKey(DEFAULT_NAME)) {
                coordinator.connectDefaultDataFile(resArgs.get(DEFAULT_NAME));
            }

            coordinator.start();

        }

    }

}