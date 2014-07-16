package task4;

import task4.Model.Book;
import task4.Parsers.DOMParser;
import task4.Parsers.SAXBookCatalogParser;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Sunrise on 14.07.2014.
 */
public class DirectoryObserver {

  private static final int SAXMode = 1;
  private static final int DOMMode = 2;
  private static final int StAXMode = 3;

  private TreeSet<String> parsedFileNames;
  private File dir;
  private Timer timer;
  private int workMode;

  public DirectoryObserver(File targetDir, int mode) throws IllegalArgumentException {
    dir = targetDir;
    if (!dir.isDirectory())
      throw new IllegalArgumentException("Specified File is not a directory!");
    workMode = mode;
    parsedFileNames = new TreeSet<>();
  }

  public void startObserving(int period) {
    timer = new Timer();
    TimerTask observeTask = new TimerTask() {
      @Override
      public void run() {
        try {
          observe();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    };
    timer.scheduleAtFixedRate(observeTask, 0, period);
  }

  public void stopObserving() {
    timer.cancel();
  }

  private void observe() throws Exception { // todo normal ex handling
    File[] files = dir.listFiles();
    if (files == null)
      throw new IOException("IO error");

    for (File f : files) {
      String fileName = f.getName();
      if (!parsedFileNames.contains(fileName) && fileName.matches(".*(.xml)$")) {
        switch (workMode) {
          case SAXMode: {
            System.out.println("SAX parser ready to roll");
            Collection<Book> catalog = SAXBookCatalogParser.parseCatalog(f);
            if (!catalog.isEmpty()) {
              for (Book book : catalog)
                System.out.println(book);
            }
            break;
          }
          case DOMMode: {
            System.out.println("DOM parser ready to roll");
            Collection<Book> catalog = DOMParser.parseCatalog(f);
            if (!catalog.isEmpty()) {
              for (Book book : catalog)
                System.out.println(book);
            }
            break;
          }
          case StAXMode: {
            System.out.println("StAX parser ready to roll");
            break;
          }
        }
        parsedFileNames.add(fileName);
      }
    }
  }

}
