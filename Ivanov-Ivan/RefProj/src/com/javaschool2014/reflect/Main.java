package com.javaschool2014.reflect;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {

    public static void main(String[] args) {


        try {

            Class sClass = Class.forName("secret.Secret");

            Method method = sClass.getDeclaredMethod("crypt", String.class);
            method.setAccessible(true);

            Field field = sClass.getDeclaredField("secret");
            field.setAccessible(true);

            Object value = (String) field.get(sClass);
            String result = method.invoke(sClass, value).toString();

            System.out.println("The decrypted answer is: " + result);

        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException |
                IllegalAccessException | InvocationTargetException e) {
            System.out.println(e.getMessage() + " error");
        }


        /*
        try {

            File file = new File("C:\\Users\\TARGETrus\\Dropbox\\GIT REPO\\java-school-2014\\Ivanov-Ivan\\RefProj\\out\\production\\RefProj\\secret\\");
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

        } catch (MalformedURLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        */

    }

}
