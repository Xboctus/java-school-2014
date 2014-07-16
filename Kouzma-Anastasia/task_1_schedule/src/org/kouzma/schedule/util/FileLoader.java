package org.kouzma.schedule.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.kouzma.schedule.User;
/**
 * @author Anastasya Kouzma
 */
public class FileLoader {
	public static boolean loadFromFile(File file, HashMap<String, User> lstUsers) {
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		try {
			lstUsers.putAll((HashMap<String, User>) in.readObject());
			return true;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean SaveToFile(File file, HashMap<String, User> lstUsers) {
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(lstUsers);
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		
		try {
			out.writeObject(lstUsers);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
