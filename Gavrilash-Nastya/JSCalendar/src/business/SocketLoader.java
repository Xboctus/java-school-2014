package business;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import centralStructure.Scheduler;
import centralStructure.User;

public class SocketLoader {

	public void writeToSource() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(8190);
			while (true) {
				Socket incoming = serverSocket.accept();
				ObjectOutputStream oos = new ObjectOutputStream(
						incoming.getOutputStream());
				oos.writeObject(Scheduler.getUsers());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<String, User> readFromSource() {
		Socket outcoming = null;
		try {
			outcoming = new Socket("localhost", 8190);
			ObjectInputStream ois = new ObjectInputStream(
					outcoming.getInputStream());
			Map<String, User> map = (Map<String, User>) ois.readObject();
			return map;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outcoming != null) {
				try {
					outcoming.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
