import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TimeZone;

public class Coordinator {
    static ArrayList<User> users = new ArrayList<User>();

    public void Create(String params){
        try{
            users.add(new User(params));
            System.out.println("Done");
        }
        catch (InputFormatException e){
            System.out.println(e);
        }
    }

    public void Modify(String params){
        try {
            String nameValue = params.substring(params.indexOf('(') + 1, params.indexOf(','));
            String tzID = params.substring(params.indexOf(',') + 1, params.lastIndexOf(','));
            User changingUser = getUser(nameValue);
            if (changingUser == null){
                throw new InputFormatException();
            }
            changingUser.tz = TimeZone.getTimeZone(tzID);
            String statusValue = params.substring(params.lastIndexOf(',') + 1, params.indexOf(')'));
            changingUser.status = new Boolean(statusValue);
            System.out.println("Done");
        }
        catch (InputFormatException e){
            System.out.println("User not found");
            System.out.println(e);
        }
    }

    public static User getUser(String p_name){
        Comparator<User> comp = new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.name.compareTo(o2.name);
            }
        };
        Collections.sort(users, comp);
        int pos = Collections.binarySearch(users, User.getUserInstance(p_name), comp);
        if (pos < 0){
            return null;
        }
        return users.get(pos);
    }

    public void CloneEvent(String params){
        try{
            String name1Value = params.substring(params.indexOf('(') + 1, params.indexOf(','));
            String name2Value = params.substring(params.lastIndexOf(',') + 1, params.indexOf(')'));
            String textValue = params.substring(params.indexOf(',') + 1, params.lastIndexOf(','));
            User user1 = getUser(name1Value);
            if (user1 == null){
                throw new InputFormatException();
            }
            User user2 = getUser(name2Value);
            if (user2 == null){
                throw new InputFormatException();
            }
            Event event = User.getUserEvent(textValue, user1);
            if (event == null){
                throw new InputFormatException();
            }
            Event event2 = User.getUserEvent(textValue, user2);
            if (event2 == null){
                throw new InputFormatException();
            }
            event2 = event.clone();
            user2.events.add(event2);
            System.out.println("Done");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}



