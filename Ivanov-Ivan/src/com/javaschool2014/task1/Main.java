package com.javaschool2014.task1;

import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {

        System.out.println("Available command list:");
        System.out.print("Create <name><timezone><active>, Create <name><timezone><active>, AddEvent <name><text><datetime>, RemoveEvent <name><text>,");
        System.out.println("AddRandomTimeEvent <name><text><dateFrom><dateTo>, CloneEvent <name><text><nameTo>, ShowInfo <name>, StartScheduling");
        System.out.println("---------------------------------------------------------------------");

        Coordinator coordinator = new Coordinator();

        coordinator.createUser("Zvan", "America/Chihuahua", true);
        coordinator.createUser("Alex", "America/Chihuahua", false);
        coordinator.createUser("Ilya", "America/Chihuahua", true);

        coordinator.addUserEvent("Zvan", "Task 1", "11.11.1111-11:11:11");
        coordinator.addUserEvent("Zvan", "Task 2", "30.06.2014-11:47:40");
        coordinator.addUserEvent("Zvan", "Task 3", "11.11.1111-12:11:11");

        coordinator.addUserEvent("Alex", "Task 3", "30.06.2014-11:47:40");
        coordinator.addUserEvent("Alex", "Task 2", "11.11.1111-11:11:11");
        coordinator.addUserEvent("Alex", "Task 1", "11.11.1111-12:11:11");

        coordinator.addUserEvent("Ilya", "Task 1", "30.06.2014-11:47:40");
        coordinator.addUserEvent("Ilya", "Task 2", "11.11.1111-13:11:11");
        coordinator.addUserEvent("Ilya", "Task 3", "11.11.1111-11:11:11");

        coordinator.showUserInfo("Zvan");

        System.out.println(coordinator.users.get("Zvan").getTimeZone().toString());

        coordinator.startScheduling();

    }

}