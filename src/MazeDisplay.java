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
import java.util.Observer;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.JComponent;


public class MazeDisplay extends JComponent implements Observer {
    
    public MazeDisplay() {
        displayMaze = null;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        ;
        super.paintComponent(g);
        
        
        HashMap<Integer, BufferedImage> images = new HashMap<Integer, BufferedImage>();
        try {
        	images.put(Maze.WALL, resizeImage(ImageIO.read(new File("wall.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.SPACE, resizeImage(ImageIO.read(new File("dot.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.KEY, resizeImage(ImageIO.read(new File("key.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.DOOR, resizeImage(ImageIO.read(new File("door.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.P1, resizeImage(ImageIO.read(new File("p1.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.E1, resizeImage(ImageIO.read(new File("e1.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.E2, resizeImage(ImageIO.read(new File("e2.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.E3, resizeImage(ImageIO.read(new File("e3.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.E4, resizeImage(ImageIO.read(new File("e4.png")),CELL_SIZE, CELL_SIZE));
        	
        	images.put(Maze.WALL_VERT, resizeImage(ImageIO.read(new File("wall_straight.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.WALL_HOR, resizeImage(ImageIO.read(new File("wall_straight.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.WALL_CORN_NW, resizeImage(ImageIO.read(new File("wall_corner.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.WALL_CORN_NE, resizeImage(ImageIO.read(new File("wall_corner.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.WALL_CORN_SW, resizeImage(ImageIO.read(new File("wall_corner.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.WALL_CORN_SE, resizeImage(ImageIO.read(new File("wall_corner.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.WALL_CROSS, resizeImage(ImageIO.read(new File("wall_cross.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.WALL_T_N, resizeImage(ImageIO.read(new File("wall_t.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.WALL_T_S, resizeImage(ImageIO.read(new File("wall_t.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.WALL_T_E, resizeImage(ImageIO.read(new File("wall_t.png")),CELL_SIZE, CELL_SIZE));
        	images.put(Maze.WALL_T_W, resizeImage(ImageIO.read(new File("wall_t.png")),CELL_SIZE, CELL_SIZE));
        	
        	
//            public static final int WALL_CORN_VERT = 20;
//            public static final int WALL_CORN_HOR = 21;
//            public static final int WALL_CORN_NW = 22;
//            public static final int WALL_CORN_NE = 23;
//            public static final int WALL_CORN_SW = 24;
//            public static final int WALL_CORN_SE = 25;
//            public static final int WALL_CROSS = 26;
//            public static final int WALL_T_N = 27;
//            public static final int WALL_T_S = 28;
//            public static final int WALL_T_E = 29;
//            public static final int WALL_T_W = 30;
        } catch (IOException e) {
        }
        
       
        for (int x=0; x<displayMaze.getWidth(); x++) {
            for (int y=0; y<displayMaze.getHeight(); y++) {
            	if(images.containsKey(displayMaze.getCell(x, y)))
            		g2d.drawImage(images.get(displayMaze.getCell(x, y)),x*CELL_SIZE, y*CELL_SIZE, null);
            }
        }
    }
    
    // Part of the Observer pattern. This method is called when an object being observed notify's it has been changed.
    public void update(Observable o, Object arg) {
    	Maze newMaze = (Maze) arg;
    	displayMaze = newMaze;
    	repaint();
    }
    
    /*
    public void updateDisplay(Maze newMaze) {
        displayMaze = newMaze;
        repaint();
    }
    */
    
    // stolen off the internet we need to rewrite this
    public static BufferedImage resizeImage( Image image, int width, int height) {

    	final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
 
        return bufferedImage;
    }
    
    private Maze displayMaze;
    public static final int CELL_SIZE = 25;
}
