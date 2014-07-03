package business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * From this class console mode launched
 * 
 * @author Gavrilash
 * 
 */
public class MainClass {

	public static void main(String[] args) {
		System.out.println("Planning mode on");
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
				e.printStackTrace();
			}
		}
	}
}
