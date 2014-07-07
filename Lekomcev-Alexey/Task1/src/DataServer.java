import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DataServer {
    private ServerSocket serverSocket;
    private Socket incoming;
    private ObjectOutputStream socketOut;
    private JTextArea textArea;

    public DataServer(JTextArea textArea){
        try{
            serverSocket = new ServerSocket(0);
            this.textArea = textArea;
        }
        catch (IOException ioe){
            JOptionPane.showMessageDialog(null, ioe.getMessage());
        }
    }

    public void startServer(){
        try {
            if (serverSocket.isClosed()){serverSocket = new ServerSocket(0);}
            textArea.append("waiting...");
            incoming = serverSocket.accept();
            OutputStream outputStream = incoming.getOutputStream();
            socketOut = new ObjectOutputStream(outputStream);
            socketOut.writeObject(Coordinator.getUsers());
            socketOut.flush();
            textArea.append("data is sent" + '\n');
            socketOut.close();
            serverSocket.close();
            incoming.close();
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    public int getPort(){
        try{
            if (serverSocket.isClosed()){serverSocket = new ServerSocket(0);}
        }
        catch (IOException ioe){
            JOptionPane.showMessageDialog(null, ioe.getMessage());
        }
        return serverSocket.getLocalPort();
    }
}
