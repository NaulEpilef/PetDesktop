package src;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class Game extends JPanel implements Runnable {
	
	public static int WIDTH;
	public static int HEIGHT;
	
	private Thread thread;
	private boolean running;
	
	private List<Pet> pets;
	
	private int mouseX, mouseY;
	
	public Game() {
		this.running = false;
		
		this.pets = new ArrayList<>();
		
		setOpaque(false);
	}
	
	public synchronized void start() {
		if (running) return;
		spawnPets();
		running = true;
		this.thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		if (!running) return;
		
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		updatePet();
	}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.clearRect(0, 0, getWidth(), getHeight());
        setBackground(new Color(0, 0, 0, 0));
        
        for (Pet pet : pets) {
        	pet.render(g);
        }
    }
    
    private void updatePet() {
    	for (Pet pet : pets) {
    		pet.setMouse(mouseX, mouseY);
    		pet.update();
    	}
    }

	@Override
	public void run() {
        while (running) {
            update();
            repaint();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}
	
	public void spawnPets() {
		int petWidth = 128;
		int petHeight = 128;
		
		int petX = Game.WIDTH / 2 - petWidth;
		int petY = Game.HEIGHT / 2 - petHeight;
		
		int petSpeed = 4;
		
		pets.add(new Pet(petX, petY, petWidth, petHeight, petSpeed, Behavior.SCARED));
	}

    public void updateMousePosition(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
    }
}
