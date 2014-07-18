import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;


public class GraphicScheduler extends JFrame {
    private JPanel outputPan;
    private JPanel buttonPan;
    private JTextArea textArea = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
    public static final int TEXT_ROWS = 30;
    public static final int TEXT_COLUMNS = 30;
    private UserCreater dialogCreate = null;
    private EventCreater dialogEvent = null;
    private ShowInfoCreater showInfoDialog = null;
    private FindUserCreater findUserCreater = null;
    private ServerAddressCreater serverAddressCreater = null;
    private JFileChooser chooser;
    private DataServer dataServer;
    private Client client;
    private String serverIp;
    private int serverPort;
    private static boolean eventsOutput = false;
    private static DB db = null;
    public static boolean modification = false;


    GraphicScheduler() {
        super("Graphic scheduler");
        setLayout(new FlowLayout());
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        DB.read();

        ActionListener add = new AddAction();
        ActionListener addEvent = new AddEventAction();
        ActionListener addRandomTimeEvent = new AddRandomTimeEventAction();
        ActionListener cloneEvent = new CloneEventAction();
        ActionListener create = new CreateAction();
        ActionListener createTables = new CreateTablesAction();
        ActionListener dropTables = new DropTablesAction();
        ActionListener exit = new ExitAction();
        ActionListener modify = new ModifyAction();
        ActionListener putInDB = new PutInDBAction();
        ActionListener removeEvent = new RemoveEventAction();
        ActionListener showInfo = new ShowInfoAction();
        ActionListener startScheduling = new StartSchedulingAction();
        ActionListener save = new SaveAction();
        ActionListener socket = new SocketAction();
        ActionListener startServerAction = new StartServerAction();
        ActionListener sync = new SyncAction();

        chooser = new JFileChooser();
        dataServer = new DataServer(textArea);
        outputPan = new JPanel();
        buttonPan = new JPanel();
        buttonPan.setLayout(new GridLayout(20, 1));

        addTextArea();
        addButton("Add", add);
        addButton("AddEvent", addEvent);
        addButton("AddRandomTimeEvent", addRandomTimeEvent);
        addButton("CloneEvent", cloneEvent);
        addButton("Create", create);
        addButton("CreateTables", createTables);
        addButton("DropTables", dropTables);
        addButton("Modify", modify);
        addButton("PutInDB", putInDB);
        addButton("RemoveEvent", removeEvent);
        addButton("Save", save);
        addButton("ShowInfo", showInfo);
        addButton("Socket", socket);
        addButton("StartServer", startServerAction);
        addButton("StartScheduling", startScheduling);
        addButton("Sync", sync);
        addButton("Exit", exit);

        add(outputPan);
        add(buttonPan);

        setLocationRelativeTo(null);
        pack();
    }

    private void addButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        buttonPan.add(button);
    }

    private void addTextArea() {
        textArea.append((new Date().toString()) + '\n');
        JScrollPane scrollPane = new JScrollPane(textArea);
        outputPan.add(scrollPane);
    }

    public static boolean getEventsOutput(){
        return eventsOutput;
    }

    private class CreateAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (dialogCreate == null) dialogCreate = new UserCreater();
            if (dialogCreate.showDialog(GraphicScheduler.this, "Create user")) {
                User u = dialogCreate.getUser();
                Coordinator.getUsers().add(u);
                textArea.append("Done!\n");
            }
            modification = true;
        }
    }

    private class ModifyAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {


        }
    }

    private class AddAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            ArrayList<User> users = new ArrayList<User>();
            chooser.setCurrentDirectory(new File("."));

            int result = chooser.showOpenDialog(GraphicScheduler.this);

            if (result == JFileChooser.APPROVE_OPTION) {
                String fileName = chooser.getSelectedFile().getPath();
                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
                    users = (ArrayList<User>) in.readObject();
                    Coordinator.setUsers(users);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(GraphicScheduler.this, "IO is happened");
                }
            }
        }
    }

    private class AddEventAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            dialogEvent = null;
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
            modification = true;
        }
    }

    private class AddRandomTimeEventAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {

        }
    }

    private class CloneEventAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {

        }
    }

    private class CreateTablesAction implements ActionListener{
        public void actionPerformed(ActionEvent event){
            DB.createTables();
        }
    }

    private class DropTablesAction implements ActionListener{
        public void actionPerformed(ActionEvent event){
            DB.dropTables();
        }
    }

    private class ExitAction implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if (modification == false){System.exit(0);}
            ExitDialog exitDialog = null;
            exitDialog = new ExitDialog();
            int res = exitDialog.showDialog(GraphicScheduler.this, "exit dialog");

            if (res == ExitDialog.NO_BUTTON){System.exit(0);};
            if (res == ExitDialog.YES_BUTTON){
                ArrayList<User> users = Coordinator.getUsers();
                DB.clearTables();
                for (User u : users){
                    DB.insertIntoTable("user", u);
                    for (Event e : u.events){
                        DB.insertIntoTable("event", e);
                    }
                }
                System.exit(0);
            }
        }
    }

    private class PutInDBAction implements ActionListener{
        public void actionPerformed(ActionEvent e){
            ArrayList<User> users = Coordinator.getUsers();
            DB.clearTables();
            for (User u : users){
                DB.insertIntoTable("user", u);
                for (Event event : u.events){
                    DB.insertIntoTable("event", event);
                    }
            }
        }
    }

    private class RemoveEventAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {

        }
    }

    private class SaveAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            chooser.setCurrentDirectory(new File("."));

            int result = chooser.showOpenDialog(GraphicScheduler.this);

            if (result == JFileChooser.APPROVE_OPTION) {
                String fileName = chooser.getSelectedFile().getPath();
                try {
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
                    out.writeObject(Coordinator.getUsers());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(GraphicScheduler.this, "IO is happened");
                }
            }
        }
    }

    private class ShowInfoAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            findUserCreater = null;
            findUserCreater = new FindUserCreater();
            showInfoDialog = null;
            if (findUserCreater.showDialog(GraphicScheduler.this, "find user")) {
                User user = findUserCreater.getUser();
                showInfoDialog = new ShowInfoCreater(user);
                if (showInfoDialog.showDialog(GraphicScheduler.this, user.name)) {
                    textArea.append("Done!\n");
                }
                Boolean newStatus = new Boolean(showInfoDialog.getStatus());
                if (!newStatus.equals(user.status)) {
                    if (newStatus == true) {
                        Sheduler.getSortingEvents().add(user);
                    } else {
                        Sheduler.getSortingEvents().delete(user);
                    }
                }
            }
        }
    }

    private class StartSchedulingAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            eventsOutput = true;
            Runnable r = new OutputEventsRunnable(textArea);
            Thread t = new Thread(r);
            t.start();
        }
    }

    private class StartServerAction implements ActionListener{
        public void actionPerformed(ActionEvent event){
            dataServer.startServer();
        }
    }

    private class SocketAction implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if (dataServer != null){
                int localPort = dataServer.getPort();
                JOptionPane.showMessageDialog(GraphicScheduler.this, localPort);
            }
        }
    }

    private class SyncAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                ArrayList<User> users = new ArrayList<User>();
                serverAddressCreater = new ServerAddressCreater();
                if (serverAddressCreater.showDialog(GraphicScheduler.this, "server address")) {
                    serverIp = serverAddressCreater.getServerIP();
                    serverPort = serverAddressCreater.getServerPort();
                    textArea.append("server ip: " + serverIp + '\n');
                    textArea.append("server port: " + serverPort + '\n');
                    client = new Client(serverIp, serverPort);
                    textArea.append("client is connected " + '\n');
                    ObjectInputStream in = client.getSocketIn();
                    textArea.append("data obtained" + '\n');
                    users = (ArrayList<User>) in.readObject();
                    Coordinator.setUsers(users);
                    in.close();
                    client.closeSocket();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(GraphicScheduler.this, e.getMessage());
            }
        }
    }
}
