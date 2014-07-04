package business;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for writing current state of objects to file and
 * downloading some state from file as Map<String,User>
 * 
 * @author Gavrilash
 * 
 */
public class FileLoader implements Loader {
	/**
	 * This method save current state of all users to file as Map<String,User>.
	 * IOException throws if something wrong with file path
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	@Override
	public void writeToSource(String fileName) throws IOException {
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(Scheduler.users);
			oos.close();
		} catch (FileNotFoundException e) {
			throw new IOException("File not found, please check");
		} catch (IOException e) {
			throw new IOException("File not found, please check");
		}
	}

	/**
	 * This method download users map from file
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	@Override
	public Map<String, User> readFromSource(String fileName) throws IOException {
		ObjectInputStream ois = null;
		Map<String, User> output = null;
		try {
			File file = new File(fileName);
			FileInputStream fin = new FileInputStream(file);
			ois = new ObjectInputStream(fin);
			output = (Map<String, User>) ois.readObject();
			return output;
		} catch (IOException e) {
			throw new IOException("File not found, please check");
		} catch (ClassNotFoundException e) {
			throw new IOException("File not found, please check");
		}
	}
}
