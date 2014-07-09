package business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import centralStructure.Scheduler;
import centralStructure.User;

/**
 * This class is responsible for writing current state of objects to file and
 * downloading some state from file as Map<String,User>
 * 
 * @author Gavrilash
 * 
 */
public class FileLoader {
	/**
	 * This method save current state of all users to file as Map<String,User>.
	 * IOException throws if something wrong with file path
	 * 
	 * @param sourceName
	 * @throws IOException
	 */
	public void writeToSource(String sourceName) {
		try {
			File file = new File(sourceName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(Scheduler.getUsers());
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method download users map from file
	 * 
	 * @param sourceName
	 * @return
	 * @throws IOException
	 */
	public Map<String, User> readFromSource(String sourceName) {
		ObjectInputStream ois = null;
		Map<String, User> output = null;
		try {
			File file = new File(sourceName);
			FileInputStream fin = new FileInputStream(file);
			ois = new ObjectInputStream(fin);
			output = (Map<String, User>) ois.readObject();
			ois.close();
			return output;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return output;
	}
}
