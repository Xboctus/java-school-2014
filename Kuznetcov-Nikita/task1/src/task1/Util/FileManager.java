package task1.Util;

import java.io.*;

/**
 * Created by Sunrise on 08.07.2014.
 */
public class FileManager {

  /**
   * Utility method for read text data from file using BufferedReader
   * @param file Target file
   * @return String contains text data from file
   * @throws IOException
   */
  public static String readTextDataFromRile(File file) throws IOException {
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = new BufferedReader(new FileReader(file));
    while (reader.ready()) {
      sb.append(reader.readLine());
    }
    reader.close();
    return sb.toString();
  }

  /**
   * Utility method for writing text data in file with specified path
   * @param data String contains data which need to be written
   * @param filePath Target file path
   * @throws IOException
   */
  public static boolean writeStringToFile(String data, String filePath) throws IOException {
    File outputFile = new File(filePath);
    if (outputFile.createNewFile()) {
      BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
      outputStream.write(data.getBytes());
      outputStream.flush();
      outputStream.close();
      return true;
    } else {
      return false;
    }

  }
}
