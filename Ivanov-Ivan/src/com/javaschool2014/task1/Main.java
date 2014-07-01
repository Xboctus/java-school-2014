package com.javaschool2014.task1;

public class Main implements Constants {

    public static void main(String[] args) {

        System.out.println("Available command list:");
        System.out.println("Create(<name>,<timezone>,<status>), Create(<name>,<timezone>,<status>), (status = active/idle) AddEvent(<name>,<text>,<datetime>), RemoveEvent(<name>,<text>),");
        System.out.println("AddRandomTimeEvent(<name>,<text>,<dateFrom>,<dateTo>), CloneEvent(<name>,<text>,<nameTo>), ShowInfo(<name>), StartScheduling, Leave");
        System.out.println("---------------------------------------------------------------------");

        Coordinator coordinator = new Coordinator();

        coordinator.createUser("Ivan", "GMT", "active");
        coordinator.createUser("Alex", "GMT-10", "active");
        coordinator.createUser("Ilya", "GMT-1", "active");

        coordinator.modifyUser("Alex", "GMT+9", "idle");

        coordinator.addUserEvent("Ivan", "Task 1", "11.11.1111-11:11:11");
        coordinator.addUserEvent("Ivan", "Task 2", "30.06.2014-11:47:40");
        coordinator.addUserEvent("Ivan", "Task 3", "11.11.1111-11:11:11");

        coordinator.addUserEvent("Alex", "Task 3", "30.06.2014-11:47:40");
        coordinator.addUserEvent("Alex", "Task 2", "11.11.1111-11:11:11");
        coordinator.addUserEvent("Alex", "Task 4", "11.11.1111-11:11:11");
        coordinator.cloneUserEvent("Alex", "Task 4", "Ivan");

        coordinator.addUserEvent("Ilya", "Task 1", "30.06.2014-11:47:40");
        coordinator.addUserEvent("Ilya", "Task 2", "11.11.1111-13:11:11");
        coordinator.addUserEvent("Ilya", "Task 3", "11.11.1111-11:11:11");

        coordinator.removeUserEvent("Ivan", "Task 1");

        coordinator.showUserInfo("Ivan");

        coordinator.start();

    }

}