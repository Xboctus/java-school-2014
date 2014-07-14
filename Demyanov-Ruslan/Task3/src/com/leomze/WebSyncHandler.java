package com.leomze;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;import java.util.Locale;


public class WebSyncHandler implements Runnable{


    private static String ip;
    private static int port;


    public static int getPort() {
        return port;
    }

    public void setIp(String ip) {
        this.ip = ip ;
    }



    public void server(TaskHandler tv) throws IOException {  //sender
        TaskerView.textArea.append("\n\nServer Start\n");
        System.out.println("Sender Start");

        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        ssChannel.configureBlocking(true);
        ssChannel.socket().bind(new InetSocketAddress(0));
        port = ssChannel.socket().getLocalPort();
        InetAddress locale = InetAddress.getLocalHost();
        TaskerView.textArea.append("\nYour IP: " + locale.getHostAddress() + "\nYour port: " + port + " \n\n");



        while (true) {
            System.out.println("1");
            SocketChannel sChannel = ssChannel.accept();

            ObjectOutputStream oos = new
                    ObjectOutputStream(sChannel.socket().getOutputStream());
            oos.writeObject(tv);
            oos.close();
            TaskerView.textArea.append("\nConnection ended\n");
            System.out.println("Connection ended");
        }
    }

    public void client(String ip, int port) {  ///receiver



            System.out.println("Receiver Start");
            TaskerView.textArea.append("\nSender Start\n");

            try{
            SocketChannel sChannel = SocketChannel.open();
            sChannel.configureBlocking(true);
            if (sChannel.connect(new InetSocketAddress(ip, port))) {

                ObjectInputStream ois = new ObjectInputStream(sChannel.socket().getInputStream());

                TaskerView.taskHandler = (TaskHandler)ois.readObject();
                System.out.println("Sync successfully!");
            }
            }
            catch (IOException e){
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        System.out.println("End Receiver");

    }



@Override
    public void run() {

    }
}

