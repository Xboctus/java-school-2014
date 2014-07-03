//import javax.management.Descriptor;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Sheduler {

    private static SortingEvents sortingEvents = new SortingEvents();

    public static void main(String[] args){
        Coordinator coordinator = new Coordinator();
        String command = "";
        Scanner in = new Scanner(System.in);
        do{
            System.out.println("Enter the command");
            command = in.nextLine();
            if (command.equals("GraphicScheduler")){
                GraphicScheduler gs = new GraphicScheduler();
                return;
            }
            int index = command.indexOf('(');
            if (!command.equals("StartScheduling") && index == -1){
                System.out.println("incorrect command");
                continue;
            }
            String shortCommand = command.substring(0, command.indexOf('('));
            if (shortCommand.equals("Create")){
                coordinator.Create(command);
            }
            if (shortCommand.equals("Modify")){
                coordinator.Modify(command);
            }
            if (shortCommand.equals("AddEvent")){
                try{
                    String nameValue = command.substring(command.indexOf('(') + 1, command.indexOf(','));
                    User user = coordinator.getUser(nameValue);
                    if (user != null) {
                        user.addEvent(command, coordinator);
                    }
                    else {System.out.println("User not found");}
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (shortCommand.equals("RemoveEvent")){
                try{
                    String nameValue = command.substring(command.indexOf('(') + 1, command.indexOf(','));
                    User user = coordinator.getUser(nameValue);
                    if (user != null) {
                        user.removeEvent(command, coordinator);
                    }
                    else {System.out.println("User not found");}
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (shortCommand.equals("AddRandomTimeEvent")){
                try{
                    String nameValue = command.substring(command.indexOf('(') + 1, command.indexOf(','));
                    User user = coordinator.getUser(nameValue);
                    if (user != null) {
                        user.addRandomTimeEvent(command, coordinator);
                    }
                    else {System.out.println("User not found");}
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (shortCommand.equals("CloneEvent")){
                coordinator.CloneEvent(command);
            }
            if (shortCommand.equals("ShowInfo")){
                try{
                    String nameValue = command.substring(command.indexOf('(') + 1, command.indexOf(')'));
                    User user = coordinator.getUser(nameValue);
                    if (user != null) {
                        user.showInfo(command, coordinator);
                    }
                    else {System.out.println("User not found");}
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        }while(!command.equals("StartScheduling"));
    }

    public static SortingEvents getSortingEvents(){
        return sortingEvents;
    }
}


