package org.kouzma.schedule.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;

import org.kouzma.schedule.User;
/**
 * @author Anastasya Kouzma
 */
public class SocketLoader {
	public static boolean sync(String adress, HashMap<String, User> hashMap) {
		String[] arrAdress = adress.split(":");
		if (arrAdress.length != 2)
			return false;
		
		String host = arrAdress[0];
		int port;
		try {
			port = Integer.parseInt(arrAdress[1]);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		
		Socket socket;
		try {
			socket = new Socket(host, port);
			InputStream is = socket.getInputStream();
			InputStream isb = new BufferedInputStream(is);
			ObjectInputStream ois = new ObjectInputStream(isb);
			
			try {
				hashMap = (HashMap<String, User>) ois.readObject();
				return true;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			finally {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
