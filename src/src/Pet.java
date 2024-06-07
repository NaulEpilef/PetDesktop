package src;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Pet extends GameObject {

    private double speed;

    private int mouseX, mouseY;

    private Behavior behavior;
    private PetStatus status;

    private int targetX, targetY;
    private Random random;
    private long stopTime, lastTime;

    private Sprite sprite;
    private int frameIndex;
    private long lastFrameTime;
    private long frameDelay = 100;

    private boolean animationCompleted;

    public Pet(int x, int y, int width, int height, double speed, Behavior behavior) {
        super(x, y, width, height);

        this.speed = speed;
        this.behavior = behavior;
        this.random = new Random();
        this.lastTime = System.currentTimeMillis();
        this.status = PetStatus.IDLE;
        this.animationCompleted = false;
        generateRandomTarget();

        try {
            int[] framesPerAnimation = { 6, 5, 8, 3, 6, 8 };
            String[] paths = {"/images/CatSealPoint.png", "/images/CatCreme.png", "/images/CatCalico.png"};
            int randomInt = this.random.nextInt(paths.length);
            this.sprite = new Sprite(paths[randomInt], width, height, framesPerAnimation);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.frameIndex = 0;
        this.lastFrameTime = System.currentTimeMillis();
    }

    public void setMouse(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public void update() {
        switch (this.behavior) {
            case CUDDLER:
                followMouse();
                break;
            case SCARED:
                scaredBehavior();
                break;
            default:
                System.out.println("Something is wrong!");
        }

        updateAnimation();
    }

    public void render(Graphics g) {
        BufferedImage frame = getCurrentFrame();
        g.drawImage(frame, x, y, null);
    }

    private BufferedImage getCurrentFrame() {
//    	System.out.println("GET CURRENT FRAME");
        int direction = getDirection();
        int animationType = (status == PetStatus.WALKING) ? 5 : 0;
        return sprite.getFrame(direction, animationType, frameIndex);
    }

    private int getDirection() {
//    	System.out.println("GET DIRECTION");
        float angle = (float) Math.toDegrees(Math.atan2(targetY - y, targetX - x));
    
        if(angle < 0){
            angle += 360;
        }
        
        if (angle >= 337.5 || angle < 22.5) {
//            System.out.println("Direita baixo");
            return 6; // Direita
        } else if (angle >= 22.5 && angle < 67.5) {
//            System.out.println("Direita cima");
            return 7; // Direita baixo
        } else if (angle >= 67.5 && angle < 112.5) {
//            System.out.println("Cima");
            return 0; // Baixo
        } else if (angle >= 112.5 && angle < 157.5) {
//            System.out.println("Esquerda baixo");
            return 1; // Esquerda baixo
        } else if (angle >= 157.5 && angle < 202.5) {
//            System.out.println("Esquerda");
            return 2; // Esquerda
        } else if (angle >= 202.5 && angle < 247.5) {
//            System.out.println("Esquerda cima");
            return 3; // Esquerda cima
        } else if (angle >= 247.5 && angle < 292.5) {
//            System.out.println("Baixo");
            return 4; // Cima
        } else if (angle >= 292.5 && angle < 337.5) {
//            System.out.println("Direita");
            return 5; // Direita cima
        }
        
        return 0;
    }

    private void followMouse() {
        double diffX = (mouseX - (x + width / 2));
        double diffY = (mouseY - (y + height / 2));

        double distance = Math.sqrt(diffX * diffX + diffY * diffY);

        if (distance <= 50) return;

        int velX = (int) ((diffX / distance) * speed);
        int velY = (int) ((diffY / distance) * speed);

        x += velX;
        y += velY;
    }

    private void scaredBehavior() {
//    	System.out.println("SCARED BEHAVIOR");
        if (isMouseNear()) {
            runAwayFromMouse();
            resetMousePos();
            this.status = PetStatus.WALKING;
        } else {
            if (System.currentTimeMillis() - lastTime > stopTime) {
                if (hasReachedTarget()) {
                    generateRandomTarget();
                    stopTime = random.nextInt(2000) + 1000;
                    lastTime = System.currentTimeMillis();
                } else {
                    moveToTarget();
                }
                this.status = PetStatus.WALKING;
            } else {
                this.status = PetStatus.IDLE;
            }
        }
        avoidScreenEdges();
    }

    private void generateRandomTarget() {
//    	System.out.println("GENERATE RANDOM TARGET");
        targetX = random.nextInt(Game.WIDTH - width);
        targetY = random.nextInt(Game.HEIGHT - height);
    }

    private void moveToTarget() {
//    	System.out.println("MOVE TO TARGET");
        double diffX = targetX - x;
        double diffY = targetY - y;

        double distance = Math.sqrt(diffX * diffX + diffY * diffY);

        if (distance <= 50) return;

        int velX = (int) ((diffX / distance) * speed);
        int velY = (int) ((diffY / distance) * speed);

        x += velX;
        y += velY;
    }

    private boolean hasReachedTarget() {
//    	System.out.println("HAS REACHED TARGET");
//    	System.out.println("GENERATE RANDOM TARGET");
        return Math.abs(x - targetX) <= 50 && Math.abs(y - targetY) <= 50;
    }

    private boolean isMouseNear() {
//    	System.out.println("IS MOUSE NEAR");
        double diffX = (mouseX - (x + width / 2));
        double diffY = (mouseY - (y + height / 2));

        double distance = Math.sqrt(diffX * diffX + diffY * diffY);

        return distance <= 50;
    }

    private void runAwayFromMouse() {
//    	System.out.println("RUN AWAY FROM MOUSE");
        double diffX = (x + width / 2) - mouseX;
        double diffY = (y + height / 2) - mouseY;

        double distance = Math.sqrt(diffX * diffX + diffY * diffY);
        
//        System.out.println(distance);

        if (distance <= 5) return;

        int velX = (int) ((diffX / distance) * speed);
        int velY = (int) ((diffY / distance) * speed);

        x += velX;
        y += velY;
    }

    private void avoidScreenEdges() {
//    	System.out.println("AVOID SCREEN EDGES");
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > Game.WIDTH - width) x = Game.WIDTH - width;
        if (y > Game.HEIGHT - height) y = Game.HEIGHT - height;
    }

    private void resetMousePos() {
        this.mouseX = 0;
        this.mouseY = 0;
    }

    private void updateAnimation() {
        if (System.currentTimeMillis() - lastFrameTime > frameDelay) {
            int direction = getDirection();
            int animationType = (status == PetStatus.WALKING) ? 5 : 0;

            if (status == PetStatus.WALKING) {
                frameIndex++;
                if (frameIndex >= sprite.framesPerAnimation[animationType]) {
                    frameIndex = 0;
                }
                animationCompleted = false; // Reset when walking
            } else {
                if (!animationCompleted) {
                    frameIndex++;
                    if (frameIndex >= sprite.framesPerAnimation[animationType]) {
                        animationCompleted = true;
                        frameIndex = sprite.framesPerAnimation[animationType] - 1;
                    }
                }
            }
            
            lastFrameTime = System.currentTimeMillis();
        }
    }
}
