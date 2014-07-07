import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;


public class GraphicScheduler extends JFrame{
    private JPanel outputPan;
    private JPanel buttonPan;
    private JTextArea textArea;
    public static final int TEXT_ROWS = 30;
    public static final int TEXT_COLUMNS = 30;
    private UserCreater dialogCreate = null;
    private EventCreater dialogEvent = null;
    private ShowInfoCreater showInfoDialog = null;
    private FindUserCreater findUserCreater = null;
    private JFileChooser chooser;

    GraphicScheduler(){
        super("Graphic scheduler");
        setLayout(new FlowLayout());
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        ActionListener create = new CreateAction();
        ActionListener modify = new ModifyAction();
        ActionListener addEvent = new AddEventAction();
        ActionListener removeEvent = new RemoveEventAction();
        ActionListener addRandomTimeEvent = new AddRandomTimeEventAction();
        ActionListener cloneEvent = new CloneEventAction();
        ActionListener showInfo = new ShowInfoAction();
        ActionListener startScheduling = new StartSchedulingAction();
        ActionListener save = new SaveAction();
        ActionListener add = new AddAction();

        chooser = new JFileChooser();
        outputPan = new JPanel();
        buttonPan = new JPanel();
        buttonPan.setLayout(new GridLayout(10, 1));

        addTextArea();
        addButton("Create", create);
        addButton("Modify", modify);
        addButton("AddEvent", addEvent);
        addButton("RemoveEvent", removeEvent);
        addButton("AddRandomTimeEvent", addRandomTimeEvent);
        addButton("CloneEvent", cloneEvent);
        addButton("ShowInfo", showInfo);
        addButton("StartScheduling", startScheduling);
        addButton("Save", save);
        addButton("Add", add);
        add(outputPan);
        add(buttonPan);

        setLocationRelativeTo(null);
        pack();
    }

    private void addButton(String label, ActionListener listener){
        JButton button = new JButton(label);
        button.addActionListener(listener);
        buttonPan.add(button);
    }

    private void addTextArea(){
        textArea = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(textArea);
        outputPan.add(scrollPane);
    }

    private class CreateAction implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if (dialogCreate == null) dialogCreate = new UserCreater();
            if (dialogCreate.showDialog(GraphicScheduler.this, "Create user")){
                User u = dialogCreate.getUser();
                Coordinator.users.add(u);
                textArea.append("Done!\n");
            }
        }
    }

    private class ModifyAction implements ActionListener{
        public void actionPerformed(ActionEvent event){


        }
    }

    private class AddEventAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (dialogEvent == null) dialogEvent = new EventCreater();
            if (dialogEvent.showDialog(GraphicScheduler.this, "Create event")) {
                User user = Coordinator.getUser(dialogEvent.getUserName());
                if (user == null) {
                    textArea.append("User not found!");
                    return;
                }
                if ((user.addEvent(dialogEvent.getDate(), dialogEvent.getDescription())) == true) {
                    textArea.append("Done!\n");
                } else {
                    textArea.append("Event already exist or parse error is happened!\n");
                }
            }
        }
    }

    private class RemoveEventAction implements ActionListener{
        public void actionPerformed(ActionEvent event){

        }
    }

    private class AddRandomTimeEventAction implements ActionListener{
        public void actionPerformed(ActionEvent event){

        }
    }

    private class CloneEventAction implements ActionListener{
        public void actionPerformed(ActionEvent event){

        }
    }

    private class ShowInfoAction implements ActionListener{
        public void actionPerformed(ActionEvent event){
            findUserCreater = null;
            findUserCreater = new FindUserCreater();
            showInfoDialog = null;
            if (findUserCreater.showDialog(GraphicScheduler.this, "find user")){
                User user = findUserCreater.getUser();
                showInfoDialog = new ShowInfoCreater(user);
                if (showInfoDialog.showDialog(GraphicScheduler.this, user.name)){
                    textArea.append("Done!\n");
                }
                Boolean newStatus = new Boolean(showInfoDialog.getStatus());
                if (!newStatus.equals(user.status)){
                    if (newStatus == true){
                        Sheduler.getSortingEvents().add(user);
                    }
                    else {
                        Sheduler.getSortingEvents().delete(user);
                    }
                }
            }
        }
    }

    private class StartSchedulingAction implements ActionListener{
        public void actionPerformed(ActionEvent event){
            Runnable r = new OutputEventsRunnable(textArea);
            Thread t = new Thread(r);
            t.start();
        }
    }

    private class SaveAction implements ActionListener{
        public void actionPerformed(ActionEvent event){
            chooser.setCurrentDirectory(new File("."));

            int result = chooser.showOpenDialog(GraphicScheduler.this);

            if (result == JFileChooser.APPROVE_OPTION){
                String fileName = chooser.getSelectedFile().getPath();
                try {
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
                    out.writeObject(Coordinator.getUsers());
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(GraphicScheduler.this, "IO is happened");
                }
            }
        }
    }

    private class AddAction implements ActionListener{
        public void actionPerformed(ActionEvent event){
            ArrayList<User> users = new ArrayList<User>();
            chooser.setCurrentDirectory(new File("."));

            int result = chooser.showOpenDialog(GraphicScheduler.this);

            if (result == JFileChooser.APPROVE_OPTION){
                String fileName = chooser.getSelectedFile().getPath();
                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
                    users = (ArrayList<User>) in.readObject();
                    Coordinator.setUsers(users);
                    SortingEvents.setTreeSet(users);
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(GraphicScheduler.this, "IO is happened");
                }
            }
        }
    }
}
