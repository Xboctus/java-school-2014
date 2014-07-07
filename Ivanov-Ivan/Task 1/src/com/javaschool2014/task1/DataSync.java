package com.javaschool2014.task1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.TreeMap;

public class DataSync {

    private Socket socket;
    private ServerSocket serverSocket;
    private ObjectInput input;
    private ObjectOutput output;
    private int serverPort;

    public void server() {

        serverPort = findFreePort();

        Thread server = new Thread(new Runnable(){

            @Override
            public void run() {

                try {

                    serverSocket = new ServerSocket(serverPort);

                } catch (IOException e) {

                    System.out.println(e.getMessage());

                }

                while (true) {

                    connect();

                }

            }

        });

        server.start();

    }

    public TreeMap<String,User> synchronize(String ip, int port) {

        try {


            try {

                socket = new Socket(ip, port);

            } catch (UnknownHostException e) {

                System.out.println(e.getMessage());

                return null;

            }

            OutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
            output = new ObjectOutputStream(outputStream);
            output.writeObject("Refresh");
            output.flush();

            InputStream inputStream = new BufferedInputStream(socket.getInputStream());
            input = new ObjectInputStream (inputStream);

            TreeMap<String,User> serverData;

            try {

                serverData = (TreeMap<String,User>)input.readObject();

            } catch (ClassNotFoundException e) {

                System.out.println(e.getMessage());
                return null;

            }

            return serverData;

        } catch (IOException e) {

            System.out.println(e.getMessage());
            return null;

        }

    }

    private void connect(){

        try {

            final Socket socket = serverSocket.accept();

            Thread connect = new Thread(new Runnable(){

                @Override
                public void run() {

                    try {

                        OutputStream buffer = new BufferedOutputStream(socket.getOutputStream());
                        ObjectOutput output = new ObjectOutputStream(buffer);

                        ObjectInput input = new ObjectInputStream (socket.getInputStream());

                        while(true){

                            if (input.readObject().equals("Refresh")) {

                                output.writeObject(AbstractCoordinator.getUsers());
                                output.flush();

                            } else {

                                output.writeObject(null);
                                output.flush();

                            }

                        }

                    } catch (ClassNotFoundException e) {

                        System.out.println(e.getMessage());

                    } catch (IOException e) {

                        System.out.println(e.getMessage());

                    }

                }

            });

            connect.start();

        } catch (IOException e) {

            System.out.println(e.getMessage());

        }

    }

    private static int findFreePort() {

        ServerSocket socket = null;

        try {

            socket = new ServerSocket(0);
            socket.setReuseAddress(true);

            int port = socket.getLocalPort();

            try {

                socket.close();

            } catch (IOException e) {

                System.out.println(e.getMessage());

            }

            return port;

        } catch (IOException e) {

            System.out.println(e.getMessage());

        } finally {

            if (socket != null) {

                try {

                    socket.close();

                } catch (IOException e) {

                    System.out.println(e.getMessage());

                }

            }

        }

        throw new IllegalStateException("Could not find a free TCP/IP port to start embedded Jetty HTTP Server on");

    }

    public String getServerPort() {

        return Integer.toString(serverPort);

    }

}