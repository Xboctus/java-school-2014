package com.javaschool2014.parser;

import java.io.IOException;
import java.nio.file.*;

public class FolderMonitor implements Constants {

    private Path xmlFolder            = null;
    private WatchService watchService = null;

    // Creating java 7 WatchService to monitor folder state (with default path/user path constructor):
    public FolderMonitor() {

        xmlFolder = Paths.get(DEFAULTT_FOLDER_PATH);

        try {

            watchService = FileSystems.getDefault().newWatchService();
            xmlFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public FolderMonitor(String folderPath) {

        xmlFolder = Paths.get(folderPath);

        try {

            watchService = FileSystems.getDefault().newWatchService();
            xmlFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Returns relative file path:
    public String listenFolder() {

        String filePath = null;

        try {

            WatchKey watchKey = watchService.take();

            for (WatchEvent<?> event : watchKey.pollEvents()) {

                WatchEvent.Kind kind = event.kind();

                if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {

                    String fileName = event.context().toString();
                    filePath = DEFAULTT_FOLDER_PATH + fileName;

                }

            }

            watchKey.reset();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return filePath;

    }

}