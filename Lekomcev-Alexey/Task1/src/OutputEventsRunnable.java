import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.Date;
import java.util.Timer;
import java.util.TreeSet;

public class OutputEventsRunnable implements Runnable{
    private Timer timer;
    private JTextArea component = null;
    private static final int STEPS = 1000;

    public OutputEventsRunnable(JTextArea p_component){
        component = p_component;
    }

    public void run(){
        try{
            for(int i = 1; i <= STEPS; i++){
                Output output = SortingEvents.getOutput();
                while (output == null){
                    wait();
                }
                timer = new Timer();
                timer.schedule(new TimerSchedule(output.user, output.description, component), output.date);
            }
        }
        catch (Exception e){}
    }
}

