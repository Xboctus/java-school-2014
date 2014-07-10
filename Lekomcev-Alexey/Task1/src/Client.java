import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Client {
    Socket socClient;
    String serverIp;
    int serverPort;
    ObjectInputStream socketIn;
    public Client(String serverIp, int serverPort){
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        try {
            socClient = new Socket(serverIp, serverPort);
            socketIn = new ObjectInputStream(socClient.getInputStream());
        }
        catch (IOException ioe){
            JOptionPane.showMessageDialog(null, ioe.getMessage());
        }

    }
    public ObjectInputStream getSocketIn(){
        return socketIn;
    }

    public void closeSocket(){
        try{
            socClient.close();
        }
        catch (IOException ioe){
            JOptionPane.showMessageDialog(null, "IOException is happened");
        }
    }
}
