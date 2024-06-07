package src;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	
	private BufferedImage spriteSheet;
	private BufferedImage[][][] animations;
	private int frameWidth, frameHeight;
	private int directions = 8;
	private int animationsPerDirection = 6;

    int[] framesPerAnimation;
    
	public Sprite(String path, int frameWidth, int frameHeight, int[] framesPerAnimation) throws IOException {
		this.spriteSheet = ImageIO.read(getClass().getResource(path));
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.framesPerAnimation = framesPerAnimation;
		this.animations = new BufferedImage[directions][animationsPerDirection][];
		loadFrames();
	}
	
	private void loadFrames() {
		for (int direction = 0; direction < directions; direction++) {
	        animations[direction] = new BufferedImage[animationsPerDirection][];
	        for (int animationType = 0; animationType < animationsPerDirection; animationType++) {
	            int framesCount = framesPerAnimation[animationType];
	            animations[direction][animationType] = new BufferedImage[framesCount];
	            
	            for (int frame = 0; frame < framesCount; frame++) {
	                int x = ((frame % 4) + (animationType * 4)) * frameWidth; // Considera o deslocamento do tipo de animação
	                int y = frameHeight + (direction * frameHeight * 2) + (((int)(frame / 4)) * frameHeight); // Ajusta a altura corretamente

	                // Verifique se x e y estão dentro dos limites da imagem
	                if (x + frameWidth <= spriteSheet.getWidth() && y + frameHeight <= spriteSheet.getHeight()) {
	                	x = clamp(x, 0, spriteSheet.getWidth());
	                	y = clamp(y, 0, spriteSheet.getHeight());
	                    animations[direction][animationType][frame] = spriteSheet.getSubimage(x, y, frameWidth, frameHeight);
	                } else {
	                    System.err.println("Frame out of bounds: direction=" + direction + ", animationType=" + animationType + ", frame=" + frame);
	                    System.err.println("x: " + x + ", y: " + y);
	                    System.err.println("spriteSheet Width: " + spriteSheet.getWidth() + ", Height: " + spriteSheet.getHeight());
	                }
	            }
	        }
	    }
	}
	
    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    } 

	public BufferedImage getFrame(int direction, int animationType, int frameIndex) {
		return animations[direction][animationType][frameIndex % animations[direction][animationType].length];
	}
	
	public int getFrameWidth() {
		return frameWidth;
	}
	
	public int getFrameHeight() {
		return frameHeight;
	}
}
