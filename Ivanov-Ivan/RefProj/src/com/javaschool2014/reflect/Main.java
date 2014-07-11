package com.javaschool2014.reflect;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {

    public static void main(String[] args) {

        try {

            File file = new File("C:\\Users\\TARGETrus\\Dropbox\\GIT REPO\\java-school-2014\\Ivanov-Ivan\\RefProj\\");
            URL classUrl;
            classUrl = new URL(file.toURI().toURL().toString());
            URL[] classUrls = { classUrl };
            System.out.println(classUrl);
            URLClassLoader ucl = new URLClassLoader(classUrls);
            Class c = ucl.loadClass("Secret");
            System.out.println(c.getPackage());

            for(Field f: c.getDeclaredFields()) {
                System.out.println("Field name" + f.getName());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
