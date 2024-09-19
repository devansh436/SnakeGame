import javax.swing.*;

public class App {  
    public static void main(String []args) {
        int boardWidth = 600;
        int boardHeight = boardWidth;
        
        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight); // to set its dimensions
        frame.setResizable(false); // to make it unresizable
        frame.setLocationRelativeTo(null); // to show in middle of screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // to end program on exiting box

        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame); // add snakeGame instance to frame
        frame.pack(); // to exclude title bar from dimension measuring 
        snakeGame.requestFocus();
    }
}
