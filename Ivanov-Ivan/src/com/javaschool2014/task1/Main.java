package com.javaschool2014.task1;

public class Main implements Constants {

    public static void main(String[] args) {

        System.out.println("Available command list:");
        System.out.println("Create(<name>,<timezone>,<active>), Create(<name>,<timezone>,<active>), AddEvent(<name>,<text>,<datetime>), RemoveEvent(<name>,<text>),");
        System.out.println("AddRandomTimeEvent(<name>,<text>,<dateFrom>,<dateTo>), CloneEvent(<name>,<text>,<nameTo>), ShowInfo(<name>), StartScheduling, Leave");
        System.out.println("---------------------------------------------------------------------");

        Coordinator coordinator = new Coordinator();

        coordinator.createUser("Zvan", "America/Chihuahua", true);
        coordinator.createUser("Alex", "America/Chihuahua", true);
        coordinator.createUser("Ilya", "America/Chihuahua", true);

        coordinator.modifyUser("Alex", "America/Chihuahua", false);

        coordinator.addUserEvent("Zvan", "Task 1", "11.11.1111-11:11:11");
        coordinator.addUserEvent("Zvan", "Task 2", "30.06.2014-11:47:40");
        coordinator.addUserEvent("Zvan", "Task 3", "11.11.1111-12:11:11");

        coordinator.addUserEvent("Alex", "Task 3", "30.06.2014-11:47:40");
        coordinator.addUserEvent("Alex", "Task 2", "11.11.1111-11:11:11");
        coordinator.addUserEvent("Alex", "Task 4", "11.11.1111-11:11:11");
        coordinator.cloneUserEvent("Alex", "Task 4", "Zvan");

        coordinator.addUserEvent("Ilya", "Task 1", "30.06.2014-11:47:40");
        coordinator.addUserEvent("Ilya", "Task 2", "11.11.1111-13:11:11");
        coordinator.addUserEvent("Ilya", "Task 3", "11.11.1111-11:11:11");

        coordinator.removeUserEvent("Zvan", "Task 1");

        coordinator.showUserInfo("Zvan");

        System.out.println(coordinator.users.get("Zvan").getTimeZone().toString());

        coordinator.start();

    }

}