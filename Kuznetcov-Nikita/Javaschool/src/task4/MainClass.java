package task4;

import java.io.File;

/**
 * Created by Sunrise on 14.07.2014.
 */
public class MainClass {

  public static void main(String[] args) {

    DirectoryObserver observer = new DirectoryObserver(new File("C:\\Users\\Sunrise\\IdeaProjects\\java-school-2014\\Kuznetcov-Nikita\\Javaschool\\src\\task4\\Input"), 1);
    observer.startObserving(5000);

  }

}
