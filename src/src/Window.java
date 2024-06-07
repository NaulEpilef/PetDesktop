package src;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Window {
	
	private BufferedImage splash;
	
	public Window(int width, int height, String title, Game game) {
		JFrame frame = new JFrame(title);
		
		// Remove title and options buttons
	    frame.setUndecorated(true);
	    
	    // Fulscreen
	    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    
	    frame.setAlwaysOnTop(true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(game);
		frame.setVisible(true);
		game.start();
	}
}
