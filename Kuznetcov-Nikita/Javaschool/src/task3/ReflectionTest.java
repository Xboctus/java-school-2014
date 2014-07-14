//package task3;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by Sunrise on 10.07.2014.
 */
public class ReflectionTest {

  public static void main(String[] args) throws Exception {

    Object instance = Class.forName("secret.Secret").newInstance();

    Field[] fields = instance.getClass().getDeclaredFields();
    Method[] methods = instance.getClass().getDeclaredMethods();

    System.out.println("FIELDS:");
    for (Field f : fields) {
      Class type = f.getType();
      String name = f.getName();
      String modifiers = Modifier.toString(f.getModifiers());
      if (modifiers.length() > 0) System.out.print(modifiers + " ");
      System.out.println(type.getName() + ' ' + name + ';');
    }

    System.out.println("METHODS:");
    for (Method m : methods) {
      Class type = m.getReturnType();
      String name = m.getName();
      String modifiers = Modifier.toString(m.getModifiers());
      if (modifiers.length() > 0) System.out.print(modifiers + " ");
      Class[] parameters = m.getParameterTypes();
      System.out.print(type.getName() + ' ' + name + "(");
      for (Class param : parameters) System.out.print(param.getName() + " arg");
      System.out.println(')');
    }

    methods[0].setAccessible(true);
    fields[0].setAccessible(true);

    System.out.print("Result of invoking secret method is ");
    System.out.println(methods[0].invoke(instance, (String)fields[0].get(instance)));

  }

}
