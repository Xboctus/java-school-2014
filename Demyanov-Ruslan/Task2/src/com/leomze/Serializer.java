package com.leomze;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {

    public void serialize(TaskHandler th){

        try {
            String filename = "dump.ser";
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(th);
            TaskerView.textArea.append("\nBase save successfully!\n");
        } catch (Exception e) {
           e.printStackTrace();
        }

    }

    public void deserialize(){
        try {

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("dump.ser"));
            TaskerView.taskHandler = (TaskHandler)ois.readObject();
            TaskerView.textArea.append("\nLoaded old base!\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
