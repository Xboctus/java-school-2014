package Secret;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionClass {

    public static void main(String[] args){
        Class c1 = null;
        try {
            c1 = Class.forName("Secret.Secret");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Secret secretInstance = null;
        String secretValue = null;
        Field field = null;
        Method method = null;
        try{
            secretInstance = (Secret)c1.newInstance();
        }
        catch (IllegalAccessException e){
            e.printStackTrace();
        }
        catch (InstantiationException e){
            e.printStackTrace();
        }
        try {
            field = c1.getDeclaredField("secret");
            field.setAccessible(true);
        }
        catch (NoSuchFieldException e){
            e.printStackTrace();
        }
        if (secretInstance != null){
            try {
                secretValue = (String)field.get(secretInstance);
            }
            catch (IllegalAccessException e){
                e.printStackTrace();
            }
        }
        try {
            method = c1.getDeclaredMethod("crypt", String.class);
            method.setAccessible(true);
        }
        catch (NoSuchMethodException e){
            e.printStackTrace();
        }
        try {
            method.invoke(secretInstance, secretValue);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
