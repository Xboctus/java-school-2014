import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.TimerTask;


class TimerSchedule extends TimerTask {
    User user;
    String description;
    JTextArea component;

    TimerSchedule(User p_user, String p_description){
        user = p_user;
        description = p_description;
    }

    TimerSchedule(User p_user, String p_description, JTextArea p_component){
        this(p_user, p_description);
        component = p_component;
    }
    @Override
    public void run(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy:hh:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        if (component!=null){
            component.append("\n" + user.name +"\n" + " " + description + " " + new Date() + '\n');
            return;
        }
        System.out.println(sdf.format(new Date()));
        System.out.println(user.name);
        System.out.println(description);
    }
}
