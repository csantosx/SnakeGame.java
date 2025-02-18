import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame {
    private final int TILE_SIZE = 20;
    private final int BOARD_WIDTH = 600;
    private final int BOARD_HEIGHT = 600;
    private final LinkedList<Point> snake;
    private Point food;
    private char direction = 'R'; // Right by default
    private boolean isGameOver = false;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the game panel for rendering
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        gamePanel.setBackground(Color.PINK);

        // Initialize the snake
        snake = new LinkedList<>();
        snake.add(new Point(5, 5)); // Initial snake position
        placeFood();

        // Key listener for controlling the snake's direction
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isGameOver) {
                    // Reset the game if 'R' is pressed after game over
                    if (e.getKeyCode() == KeyEvent.VK_R) {
                        resetGame();
                    }
                    return;
                }

                // Controls for snake direction
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        if (direction != 'D') direction = 'U'; // Up
                        break;
                    case KeyEvent.VK_S:
                        if (direction != 'U') direction = 'D'; // Down
                        break;
                    case KeyEvent.VK_A:
                        if (direction != 'R') direction = 'L'; // Left
                        break;
                    case KeyEvent.VK_D:
                        if (direction != 'L') direction = 'R'; // Right
                        break;
                }
            }
        });

        // Start game loop
        Timer timer = new Timer(100, e -> {
            if (!isGameOver) {
                moveSnake();
                checkCollision();
                gamePanel.repaint();  // Repaint the game panel during each loop
            }
        });
        timer.start();
    }

    private void moveSnake() {
        Point head = snake.getFirst();
        Point newHead = new Point(head);

        // Move in the direction of the snake
        switch (direction) {
            case 'U': newHead.translate(0, -1); break; // Up
            case 'D': newHead.translate(0, 1); break; // Down
            case 'L': newHead.translate(-1, 0); break; // Left
            case 'R': newHead.translate(1, 0); break; // Right
        }

        // If the snake eats food
        if (newHead.equals(food)) {
            snake.addFirst(food);  // Grow the snake
            placeFood();  // Reposition the food
        } else {
            snake.addFirst(newHead);
            snake.removeLast();  // Remove the last segment
        }
    }

    private void checkCollision() {
        Point head = snake.getFirst();

        // Check if the snake collides with the walls
        if (head.x < 0 || head.x >= BOARD_WIDTH / TILE_SIZE || head.y < 0 || head.y >= BOARD_HEIGHT / TILE_SIZE) {
            isGameOver = true;
        }

        // Check if the snake collides with itself
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                isGameOver = true;
                break;
            }
        }
    }

    private void placeFood() {
        Random rand = new Random();
        int x = rand.nextInt(BOARD_WIDTH / TILE_SIZE);
        int y = rand.nextInt(BOARD_HEIGHT / TILE_SIZE);
        food = new Point(x, y);

        // Ensure the food doesn't spawn on the snake
        while (snake.contains(food)) {
            x = rand.nextInt(BOARD_WIDTH / TILE_SIZE);
            y = rand.nextInt(BOARD_HEIGHT / TILE_SIZE);
            food = new Point(x, y);
        }
    }

    // Reset game state to start a new game
    private void resetGame() {
        snake.clear();
        snake.add(new Point(5, 5));  // Reset snake to initial position
        direction = 'R';  // Reset direction to right
        isGameOver = false;  // Reset game over flag
        placeFood();  // Place food again
        repaint();  // Force a repaint to reset the game screen
    }

    // Inner class for the game panel where the game components will be drawn
    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Draw the snake
            g2d.setColor(Color.black);
            for (Point p : snake) {
                g2d.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }

            // Draw the food
            g2d.setColor(Color.RED);
            g2d.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            // Display Game Over
            if (isGameOver) {
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 30));
                g2d.drawString("Game Over! D:", 200, 300);

                // Draw "Try Again" text
                g2d.setFont(new Font("Arial", Font.PLAIN, 20));
                g2d.drawString("Press 'R' to Try Again! :D", 200, 350);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame game = new SnakeGame();
            game.setVisible(true);
        });
    }
}


