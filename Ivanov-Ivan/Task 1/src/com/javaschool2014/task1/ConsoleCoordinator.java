package com.javaschool2014.task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleCoordinator extends AbstractCoordinator {

    @Override
    protected void printOutput(String string) {
        System.out.println(string);
    }

    @Override
    protected void connectDefaultDataFile() {

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        try {

            printOutput("Load default file: Y/N ?");

            while (true) {

                String filename = input.readLine();

                if (filename.equals("Y")) {

                    if (!loadUserData(DEFAULT_FILE_NAME)) {
                        printOutput(NO_DEFAULT_FILE_LOADED);
                        return;
                    }

                    return;

                }

                if (filename.equals("N")) {
                    return;
                }

            }

        } catch ( IOException e) {

            printOutput(e.getMessage());

        }

    }

}