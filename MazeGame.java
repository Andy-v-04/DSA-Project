import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;

public class MazeGame extends JPanel implements KeyListener {
    private int width, height, cellSize;
    private Cell[][] maze;
    private Point playerPos, endPos;
    private Random rand = new Random();

    public MazeGame(int difficulty) {
        
        // adjust the difficulty level
        this.width = difficulty * 10;
        this.height = difficulty * 10;

        this.cellSize = 600 / (difficulty * 10); // Assuming a fixed panel size of 600x600
        this.maze = new Cell[width][height];
        generateMaze();
        this.playerPos = new Point(0, 0);
        addKeyListener(this);
        setFocusable(true);
    }

    private void generateMaze() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                maze[x][y] = new Cell();
            }
        }
        generatePaths(0, 0);
        endPos = new Point(width - 1, height - 1);
    }

    private void generatePaths(int x, int y) {
        maze[x][y].visited = true;
        ArrayList<String> directions = new ArrayList<>();
        Collections.addAll(directions, "N", "S", "E", "W");
        Collections.shuffle(directions);

        for (String direction : directions) {
            int nx = x, ny = y;
            if (direction.equals("N") && y > 0) ny--;
            else if (direction.equals("S") && y < height - 1) ny++;
            else if (direction.equals("E") && x < width - 1) nx++;
            else if (direction.equals("W") && x > 0) nx--;

            if (!maze[nx][ny].visited) {
                maze[x][y].breakWall(direction);
                maze[nx][ny].breakWall(opposite(direction));
                generatePaths(nx, ny);
            }
        }
    }

    private String opposite(String direction) {
        return switch (direction) {
            case "N" -> "S";
            case "S" -> "N";
            case "E" -> "W";
            case "W" -> "E";
            default -> "";
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                maze[x][y].draw(g, x * cellSize, y * cellSize, cellSize);
            }
        }
        g.setColor(Color.BLUE);
        g.fillOval(playerPos.x * cellSize + cellSize / 4, playerPos.y * cellSize + cellSize / 4, cellSize / 2, cellSize / 2);

        g.setColor(Color.RED);
        g.fillRect(endPos.x * cellSize + cellSize / 4, endPos.y * cellSize + cellSize / 4, cellSize / 2, cellSize / 2);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int px = playerPos.x, py = playerPos.y;

        if (key == KeyEvent.VK_UP && maze[px][py].north) playerPos.y--;
        else if (key == KeyEvent.VK_DOWN && maze[px][py].south) playerPos.y++;
        else if (key == KeyEvent.VK_LEFT && maze[px][py].west) playerPos.x--;
        else if (key == KeyEvent.VK_RIGHT && maze[px][py].east) playerPos.x++;

        if (playerPos.equals(endPos)) {
            JOptionPane.showMessageDialog(this, "You Win!");
            System.exit(0);
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Game");
        MazeGame game = new MazeGame(10); // Difficulty level
        frame.add(game);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static class Cell {
        boolean north = false, south = false, east = false, west = false, visited = false;

        void breakWall(String direction) {
            switch (direction) {
                case "N" -> north = true;
                case "S" -> south = true;
                case "E" -> east = true;
                case "W" -> west = true;
            }
        }

        void draw(Graphics g, int x, int y, int size) {
            g.setColor(Color.BLACK);
            if (!north) g.drawLine(x, y, x + size, y);
            if (!south) g.drawLine(x, y + size, x + size, y + size);
            if (!east) g.drawLine(x + size, y, x + size, y + size);
            if (!west) g.drawLine(x, y, x, y + size);
        }
    }
}