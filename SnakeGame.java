import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    // specifies position of snakehead and food
    private class Tile {
        int x, y;
        Tile(int x, int y) {
            this.x = x;
            this.y = y; 
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    // snake
    Tile snakeHead;

    // food
    Tile food;
    Random random;

    // game logic
    Timer gameLoop;
    int velocityX, velocityY;
    ArrayList<Tile> snakeBody;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {
        // visual display of game "GUI"
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(new Color(0x888888));

        // initial positions of snakehead and food
        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10,10);
        addKeyListener(this);
        setFocusable(true);

        random = new Random();
        placeFood();

        gameLoop = new Timer(50, this);
        gameLoop.start();
        velocityX = 0;
        velocityY = 0;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // draw grid
        g.setColor(Color.lightGray);
       
        
        // draw food
        g.setColor(Color.red);
        g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);

        // draw snakehead
        g.setColor(Color.green);
        g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);

        // draw snakebody
        for (int i = 0, n = snakeBody.size(); i < n; i++) {
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
        }

        // score
        g.setFont(new Font("Arial", Font.PLAIN, 23));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize-16, tileSize);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize-16,tileSize);
        }
    }
    
    public void placeFood() {
        food.x = random.nextInt(boardWidth/tileSize);  // random num [0-24]
        food.y = random.nextInt(boardHeight/tileSize); // random num [0-24]
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        // eaten food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        // elongate snake body
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);

            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);                
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }


        // snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;
        
        // game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        if (snakeHead.x > boardWidth/tileSize) gameOver = true;
        else if (snakeHead.y > boardHeight/tileSize) gameOver = true;
        else if (snakeHead.x < 0) gameOver = true; 
        else if (snakeHead.y < 0) gameOver = true; 
    }

    private void resetGame() {
        snakeHead = new Tile(5, 5);
        snakeBody.clear();
        food = new Tile(10, 10);
        placeFood();
        velocityX = 0;
        velocityY = 0;
        gameOver = false;
        gameLoop.start();
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        // calls draw() method repeatedly every 100ms
        move();
        repaint();
        if (gameOver) gameLoop.stop();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        else if (e.getKeyCode() == KeyEvent.VK_R) {
            resetGame();
        }
    }

    // do not need
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
