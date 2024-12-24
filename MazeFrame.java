import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MazeFrame extends JFrame {
    private MazeGame mazeGame;
    private JPanel controlPanel;
    private JButton startButton;
    private JComboBox<String> difficultyComboBox;
    private JPanel mazePanel;

    public MazeFrame() {
        setTitle("Maze Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Control Panel for difficulty and start button
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        difficultyComboBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        controlPanel.add(difficultyComboBox);

        startButton = new JButton("Start");
        controlPanel.add(startButton);

        add(controlPanel, BorderLayout.NORTH);

        mazePanel = new JPanel(new BorderLayout());
        add(mazePanel, BorderLayout.CENTER);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int difficulty = difficultyComboBox.getSelectedIndex() + 1;
                mazeGame = new MazeGame(difficulty);
                mazePanel.removeAll();
                mazePanel.add(mazeGame, BorderLayout.CENTER);
                mazePanel.revalidate();
                mazePanel.repaint();

                mazeGame.requestFocusInWindow();
            }
        });

        // Set the initial frame size
        setSize(800, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MazeFrame();
            }
        });
    }
}