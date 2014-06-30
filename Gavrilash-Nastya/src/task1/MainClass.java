package task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MainClass {

	public static void main(String[] args) {
		System.out.println("Planning mode on");
		Scheduler.users = new HashMap<String, User>();
		while (true) {
			try {
				BufferedReader bufferRead = new BufferedReader(
						new InputStreamReader(System.in));
				String s = bufferRead.readLine();
				try {
					CommandAnalisis.analizeQuery(s);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
