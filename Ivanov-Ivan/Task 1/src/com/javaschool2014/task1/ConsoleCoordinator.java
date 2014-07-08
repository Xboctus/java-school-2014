package com.javaschool2014.task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleCoordinator extends AbstractCoordinator {

    private Pattern createPattern              = Pattern.compile(createUserPattern);
    private Pattern modifyPattern              = Pattern.compile(modifyUserPattern);
    private Pattern addEventPattern            = Pattern.compile(addUserEventPattern);
    private Pattern addRandomTimeEventPattern  = Pattern.compile(addRandomTimeUserEventPattern);
    private Pattern removeEventPattern         = Pattern.compile(removeUserEventPattern);
    private Pattern cloneEventPattern          = Pattern.compile(cloneUserEventPattern);
    private Pattern showInfoPattern            = Pattern.compile(showUserInfoPattern);
    private Pattern saveDataPattern            = Pattern.compile(saveUserDataPattern);
    private Pattern loadDataPattern            = Pattern.compile(loadUserDataPattern);
    private Pattern syncDataPattern            = Pattern.compile(syncUserDataPattern);
    private Pattern showPortPattern            = Pattern.compile(showUserPortPattern);
    private Pattern exitPattern                = Pattern.compile(leavePattern);

    @Override
    protected void printOutput(String string) {
        System.out.println(string);
    }

    @Override
    public void start() {

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        getTimer().scheduleAtFixedRate(this, 0, 1000);
        createServer();

        while (true) {

            try {

                String command = input.readLine();
                String[] arguments = null;

                Matcher matcher = createPattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (createUser(arguments[0], arguments[1], arguments[2])) {
                        System.out.println(USER_CREATED);
                    } else {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                matcher = modifyPattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (modifyUser(arguments[0], arguments[1], arguments[2])) {
                        System.out.println(USER_MODIFIED);
                    } else {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                matcher = addEventPattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (addUserEvent(arguments[0], arguments[1], arguments[2])) {
                        System.out.println(EVENT_ADDED);
                    } else {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                matcher = addRandomTimeEventPattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (addRandomTimeUserEvent(arguments[0], arguments[1], arguments[2], arguments[3])) {
                        System.out.println(RANDOM_EVENT_ADDED);
                    } else {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                matcher = removeEventPattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (removeUserEvent(arguments[0], arguments[1])) {
                        System.out.println(EVENT_REMOVED);
                    } else {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                matcher = cloneEventPattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (cloneUserEvent(arguments[0], arguments[1], arguments[2])) {
                        System.out.println(EVENT_CLONED);
                    } else {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                matcher = showInfoPattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (!showUserInfo(arguments[0])) {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                matcher = saveDataPattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (!saveUserData(arguments[0])) {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                matcher = loadDataPattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (!loadUserData(arguments[0])) {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                matcher = syncDataPattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (!synchronizeData(arguments[0], arguments[1])) {
                        System.out.println(ERROR);
                    }

                    continue;
                }


                matcher = showPortPattern.matcher(command);

                if (matcher.matches()) {

                    getServerPort();

                    continue;
                }

                matcher = exitPattern.matcher(command);

                if (matcher.matches()) {

                    getTimer().cancel();
                    System.exit(0);

                }

                System.out.println(WRONG_COMMAND);

            } catch (IOException e) {

                System.out.println(e.getMessage());

            }

        }

    }

}