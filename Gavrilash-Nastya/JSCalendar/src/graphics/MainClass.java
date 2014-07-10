package graphics;

import javax.swing.JFrame;

public class MainClass {

	public static InitialWindow frame;

	public static void main(String[] args) {
		frame = new InitialWindow();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
