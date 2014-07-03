package business;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.GregorianCalendar;
import java.util.List;

public class FileLoader {
	/**
	 * This method save current state of all users to file. IOException throws
	 * if something wrong with file path
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public static void writeToFile(String fileName) throws IOException {
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			for (String name : Scheduler.users.keySet()) {
				List<String> formattedInfo = CommandHandler.showInfo(name);
				for (String s : formattedInfo) {
					bw.write(s);
					bw.newLine();
				}
			}
			bw.close();
		} catch (FileNotFoundException e) {
			throw new IOException("File not found, please check");
		} catch (IOException e) {
			throw new IOException("File not found, please check");
		}
	}
}
