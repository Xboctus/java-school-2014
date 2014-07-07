import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;

public class OutputEventsRunnable implements Runnable{
    private TimerScheduler timerScheduler;
    private JTextArea component = null;
    private static final int STEPS = 1000;
    private static ArrayList<TimerScheduler> timers = new ArrayList<TimerScheduler>();

    public static void changeTimers(User user, String oldDescription, String newDescription){
        if (timers.contains(new TimerScheduler(user, oldDescription))){
            Timer timer = timers.get(timers.indexOf(new TimerScheduler(user, oldDescription)));
            timer.cancel();
            timer.purge();
            timers.remove(new TimerScheduler(user, oldDescription));
            Event event = user.events.get(user.events.indexOf(new Event(newDescription)));
            Sheduler.getSortingEvents().add(user, event.date, event.description);
        }
    }

    public OutputEventsRunnable(JTextArea p_component){
        component = p_component;
    }

    public void run() {
        Object monitor = SortingEvents.getMonitor();
        synchronized (monitor) {
            try {
                for (int i = 1; i <= STEPS; i++) {
                    Output output;
                    while ((output = SortingEvents.getOutput()) == null) {
                        monitor.wait();
                    }
                    timerScheduler = new TimerScheduler(output.user, output.description);
                    timerScheduler.schedule(new TimerSchedule(output.user, output.description, component), output.date);
                    timers.add(timerScheduler);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    private static class TimerScheduler extends Timer{
        private User user;
        private String description;

        public TimerScheduler(User user, String description){
            super();
            this.user = user;
            this.description = description;
        }

        public boolean equals(Object otherObject){
            if (this == otherObject) return true;

            if (otherObject == null) return false;

            if (getClass() != otherObject.getClass())
                return false;

            TimerScheduler other = (TimerScheduler) otherObject;

            return (user.equals(other.user) && description.equals(other.description));
        }
    }
}

