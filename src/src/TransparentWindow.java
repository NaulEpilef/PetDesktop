package src;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JWindow;

public class TransparentWindow {
	public TransparentWindow(int width, int height, String title, Game game) throws AWTException {
        JWindow window = new JWindow();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setSize(screenSize.width, screenSize.height);
        
        Game.WIDTH = screenSize.width;
        Game.HEIGHT = screenSize.height;
        
        window.setBackground(new Color(0, 0, 0, 0));
        window.setAlwaysOnTop(true);

        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Robot robot = new Robot();
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        window.addMouseMotionListener(new MouseMotionAdapter() {
        	@Override
        	public void mouseMoved(MouseEvent e) {
                game.updateMousePosition(e.getX(), e.getY());
        	}
        });

        window.add(game);
        window.setVisible(true);

        game.start();
    }
}
