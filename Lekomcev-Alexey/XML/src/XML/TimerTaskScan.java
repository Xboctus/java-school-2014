package XML;

import java.io.File;
import java.util.ArrayList;
import java.util.TimerTask;

public class TimerTaskScan extends TimerTask {
    final static File folder = new File(System.getProperty("user.dir") + "/xmlfiles/");
    private static ArrayList<String> files = new ArrayList<String>();
    private static String parsingMethod;

    @Override
    public void run(){
        scanFolder();
    }

    private static void scanFolder(){
        for (final File fileEntry : folder.listFiles()){
            if (!fileEntry.getPath().contains("schema")) {
                files.add(fileEntry.getName());
            }
        }
        ArrayList<String> existingFiles = XmlFrame.getFiles();
        ArrayList<String> newFiles = new ArrayList<String>();
        newFiles.addAll(files);
        newFiles.removeAll(existingFiles);
        for (String file : newFiles){
            switch (parsingMethod){
                case "dom":
                    XmlClass.parseFileWithDom(file);
                    break;
                case "sax":
                    XmlClass.parseFileWithSax(file);
                    break;
            }
        }
    }

    public static void setParsingMethod(String p_parsingMethod){
        parsingMethod = p_parsingMethod;
    }
}
