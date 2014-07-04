package com.javaschool2014.task1;

public class Main implements Constants {

    public static void main(String[] args) {

        System.out.println("Available command list:");
        System.out.println("Create(<name>,<timezone>,<status>), Create(<name>,<timezone>,<status>), (status = active/idle) ");
        System.out.println("AddRandomTimeEvent(<name>,<text>,<dateFrom>,<dateTo>), CloneEvent(<name>,<text>,<nameTo>)");
        System.out.println("ShowInfo(<name>), SaveData(<path>), LoadData(<path>), Leave");
        System.out.println("-----------------------------------------------------------------------------------------------");

        if (args.length > 0) {

            if (args[0].equals("-g")) {
                GUICoordinator coordinator = new GUICoordinator();
                coordinator.start();
                coordinator.display();
            }

        } else {

            GUICoordinator coordinator = new GUICoordinator();

            coordinator.createUser("Ivan", "GMT+0", "active");
            coordinator.addUserEvent("Ivan", "Task 3", "30.06.2014-11:10:10");
            coordinator.addUserEvent("Ivan", "Task 2", "02.07.2014-0:50:20");
            coordinator.addUserEvent("Ivan", "Task 1", "30.06.2014-11:10:10");

            coordinator.showUserInfo("Ivan");

            coordinator.display();
            coordinator.start();

        }

    }

}