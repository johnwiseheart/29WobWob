import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;


public class MazeDisplay extends JComponent {
    
    public MazeDisplay() {
        displayMaze = null;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g);
        
        // I dunno.
        int cellWidth = getWidth()/displayMaze.getWidth();
        int cellHeight = getHeight()/displayMaze.getHeight();
        
        g2d.setColor(Color.BLACK);
        for (int x=0; x<displayMaze.getWidth(); x++) {
            for (int y=0; y<displayMaze.getHeight(); y++) {
                if (displayMaze.getCell(x, y) == Maze.WALL) {
                    g2d.fillRect(x*cellWidth, y*cellHeight, cellWidth, cellHeight);
                }
            }
        }
    }
    
    public void updateDisplay(Maze newMaze) {
        displayMaze = newMaze;
        repaint();
    }
    
    private Maze displayMaze;
}
