package com.javaschool2014.task1;

import java.io.*;
import java.util.TreeMap;

public class DataLoader implements Constants {

    public boolean saveData(String filename) {

        try {

            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(AbstractCoordinator.getUsers());
            out.close();
            fileOut.close();

            return true;

        } catch(IOException e) {

            return false;

        }

    }

    public boolean loadData(String filename) {

        try  {

            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            AbstractCoordinator.setUsers((TreeMap<String, User>) in.readObject());
            in.close();
            fileIn.close();

            return true;

        } catch(IOException e)  {

            System.out.println(e.getMessage());
            return false;

        } catch(ClassNotFoundException e) {

            System.out.println(e.getMessage());
            return false;

        }

    }

}