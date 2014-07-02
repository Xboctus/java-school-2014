package com.javaschool2014.task1;

public class Main implements Constants {

    public static void main(String[] args) {

        System.out.println("Available command list:");
        System.out.println("Create(<name>,<timezone>,<status>), Create(<name>,<timezone>,<status>), (status = active/idle) AddEvent(<name>,<text>,<datetime>), RemoveEvent(<name>,<text>),");
        System.out.println("AddRandomTimeEvent(<name>,<text>,<dateFrom>,<dateTo>), CloneEvent(<name>,<text>,<nameTo>), ShowInfo(<name>), StartScheduling, Leave");
        System.out.println("---------------------------------------------------------------------");

        Coordinator coordinator = new Coordinator();

        /*
        coordinator.createUser("Ivan", "GMT+0", "active");
        coordinator.createUser("Alex", "GMT-10", "active");
        coordinator.createUser("Ilya", "GMT-1", "active");
        coordinator.addUserEvent("Ivan", "Task 1", "30.06.2014-11:10:10");
        coordinator.addUserEvent("Ivan", "Task 2", "02.07.2014-0:50:20");
        coordinator.addUserEvent("Ivan", "Task 3", "30.06.2014-11:10:10");
        coordinator.addUserEvent("Alex", "Task 3", "01.07.2014-12:55:10");
        coordinator.addUserEvent("Alex", "Task 2", "30.06.2014-11:10:10");
        coordinator.addUserEvent("Alex", "Task 4", "30.06.2014-11:10:10");
        coordinator.cloneUserEvent("Alex", "Task 4", "Ivan");
        coordinator.addUserEvent("Ilya", "Task 1", "30.06.2014-11:10:10");
        coordinator.addUserEvent("Ilya", "Task 2", "30.06.2014-11:10:10");
        coordinator.addUserEvent("Ilya", "Task 3", "01.07.2014-12:55:10");
        coordinator.removeUserEvent("Ivan", "Task 1");
        coordinator.showUserInfo("Ivan");
        */

        coordinator.start();


    }

}