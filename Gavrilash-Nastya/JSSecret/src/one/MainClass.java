package one;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import secret.Secret;

public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Secret sample = new Secret();

		try {
			Method m = sample.getClass().getDeclaredMethod("crypt",
					String.class);
			Field f = sample.getClass().getDeclaredField("secret");

			m.setAccessible(true);
			f.setAccessible(true);
			System.out.println(m.invoke(sample, (String) f.get(sample)));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		// printMethods();
		// printFields();
	}

	public static void printMethods() {
		Method[] methods = Secret.class.getDeclaredMethods();
		for (Method m : methods) {
			if (Modifier.isPublic(m.getModifiers()))
				System.out.print("public ");
			if (Modifier.isProtected(m.getModifiers()))
				System.out.print("protected ");
			if (Modifier.isPrivate(m.getModifiers()))
				System.out.print("private ");
			if (Modifier.isStatic(m.getModifiers()))
				System.out.print("static ");
			System.out.print(m.getReturnType().getName() + " ");
			Class[] params = m.getParameterTypes();
			System.out.print(m.getName() + "(");
			for (Class p : params) {
				System.out.print(p.getName() + " ");
			}
			System.out.println(")");
			Class[] exceprions = m.getExceptionTypes();
			for (Class e : exceprions) {
				System.out.print(e.getName() + " ");
			}
		}
	}

	public static void printFields() {
		Field[] fields = Secret.class.getDeclaredFields();
		for (Field f : fields) {
			if (Modifier.isPublic(f.getModifiers()))
				System.out.print("public ");
			if (Modifier.isProtected(f.getModifiers()))
				System.out.print("protected ");
			if (Modifier.isPrivate(f.getModifiers()))
				System.out.print("private ");
			if (Modifier.isStatic(f.getModifiers()))
				System.out.print("static ");
			if (Modifier.isFinal(f.getModifiers()))
				System.out.print("final ");
			System.out.print(f.getType().getName() + " ");
			System.out.println(f.getName());
		}
	}
}
