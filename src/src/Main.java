package src;

import java.awt.AWTException;

public class Main {
	public static void main(String[] args) {
		Game game = new Game();
		try {
			new TransparentWindow(800, 600, "Desktop Pet", game);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
