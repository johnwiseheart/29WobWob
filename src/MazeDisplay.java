import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JComponent;


public class MazeDisplay extends JComponent {
    
    public MazeDisplay() {
        displayMaze = null;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
        
        
        HashMap<Integer, BufferedImage> images = new HashMap<Integer, BufferedImage>();
        try {
        	images.put(Maze.WALL, ImageIO.read(new File("Texture_wall.png")));
        	images.put(Maze.SPACE, ImageIO.read(new File("dot.png")));
        	images.put(Maze.KEY, ImageIO.read(new File("key.png")));
        	images.put(Maze.DOOR, ImageIO.read(new File("door.png")));
        	images.put(Maze.P1, ImageIO.read(new File("p1.png")));
        	images.put(Maze.P2, ImageIO.read(new File("p2.png")));
        	images.put(Maze.P3, ImageIO.read(new File("p3.png")));
        	images.put(Maze.P4, ImageIO.read(new File("p4.png")));
        	images.put(Maze.E1, ImageIO.read(new File("enemy.png")));
        } catch (IOException e) {
        }
        
        
        // I dunno.
        int cellWidth = getWidth()/displayMaze.getWidth();
        int cellHeight = getHeight()/displayMaze.getHeight();
        
        g2d.setColor(Color.BLACK);
        for (int x=0; x<displayMaze.getWidth(); x++) {
            for (int y=0; y<displayMaze.getHeight(); y++) {
            	if(images.containsKey(displayMaze.getCell(x, y)))
            		g2d.drawImage(resizeImage(images.get(displayMaze.getCell(x, y)),cellWidth,cellHeight),x*cellWidth, y*cellHeight, null);
            }
        }
    }
    
    public void updateDisplay(Maze newMaze) {
        displayMaze = newMaze;
        repaint();
    }
    
    public static BufferedImage resizeImage(final Image image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
 
        return bufferedImage;
    }
    
    private Maze displayMaze;
}
