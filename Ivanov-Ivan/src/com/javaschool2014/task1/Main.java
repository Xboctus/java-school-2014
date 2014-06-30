package com.javaschool2014.task1;

import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {

        System.out.println("Available command list:");
        System.out.print("Create <name><timezone><active>, Create <name><timezone><active>, AddEvent <name><text><datetime>, RemoveEvent <name><text>,");
        System.out.println("AddRandomTimeEvent <name><text><dateFrom><dateTo>, CloneEvent <name><text><nameTo>, ShowInfo <name>, StartScheduling");
        System.out.println("---------------------------------------------------------------------");

        Coordinator coordinator = new Coordinator();

        coordinator.startScheduling();

        coordinator.createUser("Ivan", "America/Chihuahua", true);
        coordinator.addUserEvent("Ivan", "Task 1", "11.11.1111-11:11:11");
        coordinator.addUserEvent("Ivan", "Task 2", "11.11.1111-11:11:11");
        coordinator.addUserEvent("Ivan", "Task 3", "11.11.1111-11:11:11");
        coordinator.addUserEvent("Ivan", "Task 4", "11.11.1111-11:11:11");
        coordinator.showUserInfo("Ivan");
        System.out.println(coordinator.users.get("Ivan").getTimeZone().toString());

    }

}