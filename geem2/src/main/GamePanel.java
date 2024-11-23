package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GamePanel extends JPanel implements Runnable {
    // Screen settings
    final int originalTileSize = 16; // 16*16 tile
    final int scale = 3;
    final int tileSize = originalTileSize * scale; //48
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels
    
    int score = 0;
    
    // FPS
    int FPS = 60;
    
    KeyHandler keyH = new KeyHandler();
    
    Thread gameThread;
    
    // Character attributes
    int playerX = 100;
    int playerY = screenHeight - tileSize * 3;
    int speed = 5;
    
    int gravity = 2;
    int jumpStrength = 30;
    boolean isJumping = false;
    boolean isFalling = false;
    boolean plus = false;
    boolean Gover = false;
    
    private Image[] images; // = new Image[12];
    private int imageIndex = 0;
    
    // Animation frame control
    int frameCounter = 0;
    int frameDelay = 20; // Delay frames for 20 FPS animation
	
    List<Obstacle> obstacles = new ArrayList<>();
    int obstacleSpawnInterval = 200;
    int obstacleCounter = 0;
    
    JButton replayButton;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.PINK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.setLayout(null); // Set layout to null for manual positioning
        loadImages();
        
        replayButton = new JButton("Replay");
        replayButton.setBounds(screenWidth / 2 - 50, screenHeight / 2 - 15, 100, 30);
        replayButton.setVisible(false);
        replayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
        this.add(replayButton); // Add button
    }

    private void loadImages() {
        String imgNames = "obs,land1,land2,land3,cactus1,cactus2,right-stand,right1,right2,bg"; // List your image names here
        String[] names = imgNames.replaceAll("\\s+", "").split(",");

        images = new Image[names.length];

        for (int i = 0; i < names.length; i++) {
            // Load images from resources
            images[i] = new ImageIcon(getClass().getResource("/icon/" + names[i] + ".png")).getImage();
            if (images[i] == null) {
                System.out.println("Image not found: " + names[i]);
            } else {
            	if (i<3) {
            		images[i] = images[i].getScaledInstance(71, 29, Image.SCALE_SMOOTH);
            	} else {
            		images[i] = images[i].getScaledInstance(80, 120, Image.SCALE_SMOOTH);
            	}
                if (i==9) images[i] = images[i].getScaledInstance(screenHeight - tileSize * 2, screenWidth, Image.SCALE_SMOOTH);
            }
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS; // Time between draws
        double nextDrawTime = System.nanoTime() + drawInterval;
        frameCounter = 0;

        while (gameThread != null) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frameCounter += 1;
        }
    }

    public void update() {
        if (keyH.spacePressed && !isJumping && !isFalling) {
            isJumping = true;
        }
        if (isJumping) {
            playerY -= jumpStrength;
            if (playerY <= screenHeight - tileSize * 6) {
                isJumping = false;
                isFalling = true;
                //imageIndex = 6;
            }
            
        } else if (isFalling) {
            playerY += gravity;
            if (playerY >= screenHeight - tileSize * 3) {
                playerY = screenHeight - tileSize * 3;
                isFalling = false;
            }
        }

        if (obstacleCounter >= obstacleSpawnInterval) {
            spawnObstacle();
            obstacleCounter = 0;
        } else {
            obstacleCounter++;
        }

        Iterator<Obstacle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            obstacle.x -= speed;
            if (obstacle.x + obstacle.width < 0) {
                iterator.remove();
            }
            if (playerX < obstacle.x + obstacle.width 
            		&& playerX + tileSize > obstacle.x 
            		&& playerY < obstacle.y + obstacle.height 
            		&& playerY + tileSize > obstacle.y) {
                // Game over condition
                Gover = true;
                //handleGameOver();
            }
            if (playerX > obstacle.x + obstacle.width && playerY < obstacle.y + obstacle.height && plus == false) {
                score += 1;
                plus = true;
            }
        }
        
    }
    
    private void spawnObstacle() {
        Random rand = new Random();
        int height = tileSize * (1 + rand.nextInt(2));
        obstacles.add(new Obstacle(screenWidth, screenHeight - tileSize * 2 - height, tileSize, height));
        plus = false;
    }
    
    
    private void resetGame() {
        score = 0;
        playerX = 100;
        playerY = screenHeight - tileSize * 3;
        isJumping = false;
        isFalling = false;
        obstacles.clear();
        Gover = false;
        //SwingUtilities.invokeLater(() -> replayButton.setVisible(false));
        replayButton.setVisible(false);
        startGameThread();
    }

    /*private void handleGameOver() {
        SwingUtilities.invokeLater(() -> {
            replayButton.setVisible(true);
            repaint();
        });
    }*/

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        //Random rand = new Random();
        //int land = 1+rand.nextInt(2);

        // Draw ground
        g2.setColor(Color.WHITE);
        g2.fillRect(0, screenHeight - tileSize * 2, screenWidth, tileSize * 2);
        g2.drawImage(images[9], 0, 0, screenWidth, screenHeight - tileSize * 2, this);
        
        // Draw dino
        if (frameCounter % frameDelay == 0) {            	
            imageIndex = (imageIndex == 7) ? 8 : 7;
        }
        
        g2.drawImage(images[imageIndex], playerX, playerY, tileSize, tileSize, this);        
        g2.setColor(Color.YELLOW);
        g2.drawString("Score: " + score, tileSize, tileSize);
        
        if (Gover == true) {
            g2.drawString("Game Over!!!", tileSize * (maxScreenCol / 2 - 1) + 10, tileSize * (maxScreenRow / 2 - 1));
            g2.drawString("Score: "+score, tileSize * (maxScreenCol / 2 - 1) + 26, tileSize * (maxScreenRow / 2 - 1)+16);
            replayButton.setVisible(true);
            gameThread = null;
        }
        
        // Draw obstacles
        g2.setColor(Color.RED);
        for (Obstacle obstacle : obstacles) {
            g2.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
            g2.drawImage(images[0],obstacle.x, obstacle.y, obstacle.width, obstacle.height, this);
        }

        g2.dispose();
    }
}