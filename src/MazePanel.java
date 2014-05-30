import java.awt.AlphaComposite;
import java.awt.Dimension;
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
import javax.swing.JPanel;
/**
 * This class extends JPanel and contains helper functions to add the maze and it's contents to the panel.
 * It updates its self every time the GameState is changed as it observes GameState.
 * 
 * @author Group
 *
 */

public class MazePanel extends JPanel implements Observer {
    
	private static final long serialVersionUID = -600678118735264063L;
	
	/**
	 * On creation MazePanel sets up a large HashMap which converts from CellType's to actual images.
	 */
	public MazePanel() {
        gameState = null;
        images = new HashMap<CellType, BufferedImage>();
        try {
        	images.put(CellType.DOT, resizeImage(ImageIO.read(new File("files/img/dot.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.KEY, resizeImage(ImageIO.read(new File("files/img/key.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.DOOR, resizeImage(ImageIO.read(new File("files/img/door.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.PLAYER1_E, resizeImage(ImageIO.read(new File("files/img/p1_east.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.PLAYER1_N, resizeImage(ImageIO.read(new File("files/img/p1_north.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.PLAYER1_S, resizeImage(ImageIO.read(new File("files/img/p1_south.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.PLAYER1_W, resizeImage(ImageIO.read(new File("files/img/p1_west.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY1_E, resizeImage(ImageIO.read(new File("files/img/e1_east.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY1_N, resizeImage(ImageIO.read(new File("files/img/e1_north.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY1_S, resizeImage(ImageIO.read(new File("files/img/e1_south.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY1_W, resizeImage(ImageIO.read(new File("files/img/e1_west.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY2_E, resizeImage(ImageIO.read(new File("files/img/e2_east.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY2_N, resizeImage(ImageIO.read(new File("files/img/e2_north.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY2_S, resizeImage(ImageIO.read(new File("files/img/e2_south.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY2_W, resizeImage(ImageIO.read(new File("files/img/e2_west.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY3_E, resizeImage(ImageIO.read(new File("files/img/e3_east.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY3_N, resizeImage(ImageIO.read(new File("files/img/e3_north.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY3_S, resizeImage(ImageIO.read(new File("files/img/e3_south.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY3_W, resizeImage(ImageIO.read(new File("files/img/e3_west.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY4_E, resizeImage(ImageIO.read(new File("files/img/e4_east.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY4_N, resizeImage(ImageIO.read(new File("files/img/e4_north.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY4_S, resizeImage(ImageIO.read(new File("files/img/e4_south.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY4_W, resizeImage(ImageIO.read(new File("files/img/e4_west.png")),CELL_SIZE, CELL_SIZE));
        	
        	images.put(CellType.WALL_VERT, resizeImage(ImageIO.read(new File("files/img/wall_straight.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.WALL_HOR, rotateImage(resizeImage(ImageIO.read(new File("files/img/wall_straight.png")),CELL_SIZE, CELL_SIZE), 1));
        	images.put(CellType.WALL_CORN_NW, rotateImage(resizeImage(ImageIO.read(new File("files/img/wall_corner.png")),CELL_SIZE, CELL_SIZE), 3));
        	images.put(CellType.WALL_CORN_NE, resizeImage(ImageIO.read(new File("files/img/wall_corner.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.WALL_CORN_SW, rotateImage(resizeImage(ImageIO.read(new File("files/img/wall_corner.png")),CELL_SIZE, CELL_SIZE), 2));
        	images.put(CellType.WALL_CORN_SE, rotateImage(resizeImage(ImageIO.read(new File("files/img/wall_corner.png")),CELL_SIZE, CELL_SIZE), 1));
        	images.put(CellType.WALL_CROSS, resizeImage(ImageIO.read(new File("files/img/wall_cross.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.WALL_T_N, resizeImage(ImageIO.read(new File("files/img/wall_t.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.WALL_T_S, rotateImage(resizeImage(ImageIO.read(new File("files/img/wall_t.png")),CELL_SIZE, CELL_SIZE), 2));
        	images.put(CellType.WALL_T_E, rotateImage(resizeImage(ImageIO.read(new File("files/img/wall_t.png")),CELL_SIZE, CELL_SIZE), 1));
        	images.put(CellType.WALL_T_W, rotateImage(resizeImage(ImageIO.read(new File("files/img/wall_t.png")),CELL_SIZE, CELL_SIZE), 3));
        	images.put(CellType.WALL_END_N, resizeImage(ImageIO.read(new File("files/img/wall_end.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.WALL_END_S, rotateImage(resizeImage(ImageIO.read(new File("files/img/wall_end.png")),CELL_SIZE, CELL_SIZE), 2));
        	images.put(CellType.WALL_END_E, rotateImage(resizeImage(ImageIO.read(new File("files/img/wall_end.png")),CELL_SIZE, CELL_SIZE), 1));
        	images.put(CellType.WALL_END_W, rotateImage(resizeImage(ImageIO.read(new File("files/img/wall_end.png")),CELL_SIZE, CELL_SIZE), 3));
        	images.put(CellType.WALL_BLOCK, resizeImage(ImageIO.read(new File("files/img/wall_block.png")),CELL_SIZE, CELL_SIZE));
    
        } catch (IOException e) {
        }
    }

	/**
	 * This function is called on a repaint(). It gets the current state of the maze then draws it as images using the
	 * the HashMap created in the constructor that converts from the Maze stored as CellType's into the actual images
	 * to be drawn.
	 * Then the player and enemies are drawn on top of the maze. They have their own methods of converting from directions
	 * to the corresponding image. Player calls a function to convert it and the enemies do it with a large switch case 
	 * statement as there wasn't really any better way we could think of.
	 */
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        if (gameState == null) {
            return;
        }
        Maze displayMaze = gameState.getMaze();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);

        g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
        
        
        
        // Display maze.
        for (int x=0; x<displayMaze.getWidth(); x++) {
            for (int y=0; y<displayMaze.getHeight(); y++) {
            	if(images.containsKey(displayMaze.getCell(x, y)))
            		g2d.drawImage(images.get(displayMaze.getCell(x, y)),x*CELL_SIZE, y*CELL_SIZE, null);
            }
        }
        
        // Display player.
        Vector playerGridLoc = gameState.getPlayer().location(); // location in grid coords
        Vector playerGridPrevLoc = gameState.getPlayer().previousLocation(); // prev location in grid coords
        
        Vector playerSmoothLoc = playerGridLoc.multiply(CELL_SIZE); // default
        
        if (playerGridPrevLoc != null) {
	        // difference between current and prev locations, multiplied by tick for smooth interpolation between
	        // previous and current locations
	        Vector playerSmoothOffset = playerGridLoc.subtract(playerGridPrevLoc);
	        
	        // make sure they're not "flying"
	        if (playerSmoothOffset.length() <= 1) {
	        	playerSmoothOffset = playerSmoothOffset.multiply(gameState.getTick());
	        	playerSmoothLoc = playerGridPrevLoc.multiply(CELL_SIZE).add(playerSmoothOffset); // location in pixels
	        }
        }
        
        g2d.drawImage(images.get(directionTOPlayerImage(gameState.getPlayerDirection())),
                      playerSmoothLoc.x(),
                      playerSmoothLoc.y(), null);
        
        // Display enemies.
        int i = 0;
        for (Enemy enemy : gameState.getEnemies()) {
            CellType c = CellType.ENEMY1_N;
            Direction d;
            switch(i%4) {
            case 0:
            	d = enemy.getDirection();
            	switch(d) {
            	case EAST:
            		c = CellType.ENEMY1_E;
            		break;
            	case NORTH:
            		c = CellType.ENEMY1_N;
            		break;
            	case SOUTH:
            		c = CellType.ENEMY1_S;
            		break;
            	case WEST:
            		c = CellType.ENEMY1_W;
            		break;
                default:
                    c = null;
                    break;
            	}
                break;
            case 1:
            	d = enemy.getDirection();
            	switch(d) {
            	case EAST:
            		c = CellType.ENEMY2_E;
            		break;
            	case NORTH:
            		c = CellType.ENEMY2_N;
            		break;
            	case SOUTH:
            		c = CellType.ENEMY2_S;
            		break;
            	case WEST:
            		c = CellType.ENEMY2_W;
            		break;
            	default:
                    c = null;
                    break;
            	}
                break;
            case 2:
            	d = enemy.getDirection();
            	switch(d) {
            	case EAST:
            		c = CellType.ENEMY3_E;
            		break;
            	case NORTH:
            		c = CellType.ENEMY3_N;
            		break;
            	case SOUTH:
            		c = CellType.ENEMY3_S;
            		break;
            	case WEST:
            		c = CellType.ENEMY3_W;
            		break;
            	default:
                    c = null;
                    break;
            	}
                break;
            case 3:
            	d = enemy.getDirection();
            	switch(d) {
            	case EAST:
            		c = CellType.ENEMY4_E;
            		break;
            	case NORTH:
            		c = CellType.ENEMY4_N;
            		break;
            	case SOUTH:
            		c = CellType.ENEMY4_S;
            		break;
            	case WEST:
            		c = CellType.ENEMY4_W;
            		break;
            	default:
                    c = null;
                    break;
            	}
                break;
            }
            
            Vector enemyGridLoc = enemy.location(); // location in grid coords
            Vector enemyGridPrevLoc = enemy.previousLocation(); // prev location in grid coords
            
            Vector enemySmoothLoc;
            if (enemyGridPrevLoc == null) {
            	enemySmoothLoc = enemyGridLoc.multiply(CELL_SIZE);
            } else {
	            Vector enemySmoothOffset = enemyGridLoc.subtract(enemyGridPrevLoc).multiply(gameState.getTick());
	            enemySmoothLoc = enemyGridPrevLoc.multiply(CELL_SIZE).add(enemySmoothOffset); // location in pixels
            }
            
            g2d.drawImage(images.get(c),
            			  enemySmoothLoc.x(),
                          enemySmoothLoc.y(), null);
            i++;
        }
        
        setMaximumSize(new Dimension(displayMaze.getWidth()*CELL_SIZE, displayMaze.getHeight()*CELL_SIZE));
    }
    
    /**
     * Small helper function to convert from Direction enum into the Player direction CellType.
     * 
     * @param direction Direction that player is currently facing.
     * @return CellType representing the corresponding direction the Player is facing.
     */
    private CellType directionTOPlayerImage(Direction direction) {
    	switch (direction) {
    	case NORTH:
    		return CellType.PLAYER1_N;
    	case EAST:
    		return CellType.PLAYER1_E;
    	case SOUTH:
    		return CellType.PLAYER1_S;
    	case WEST:
    		return CellType.PLAYER1_W;
    	default:
    		return CellType.PLAYER1_E;
    	}
    }
    
    /**
     * Part of the Observer pattern. This method is called when an object being observed notifies it has been changed.
     */
    public void update(Observable o, Object arg) {
    	gameState = (GameState) o;
    	repaint();
    }
    
    /**
     * Resizes a given image to a given width height.
     * 
     * @param image Image to resize.
     * @param width New width.
     * @param height New height.
     * @return A new BufferedImage that is a resized version of the given Image.
     */
    public static BufferedImage resizeImage( Image image, int width, int height) {

    	final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        
        return bufferedImage;
    }
    
    /**
     * Rotates a given BufferedImage clockwise as many times as given. 
     * @param oldImage BufferedImage to rotate.
     * @param numberOfTimes Number of times to rotate.
     * @return New rotated BufferedImage.
     */
    public static BufferedImage rotateImage( BufferedImage oldImage, int numberOfTimes ) {
		int	width = oldImage.getWidth();
		int	height = oldImage.getHeight();
		
		BufferedImage currImg = oldImage;
		BufferedImage image = null;
		
		for(int h=0;h<numberOfTimes;h++) {	
			image = new BufferedImage(height, width, currImg.getType());
			for( int i=0 ; i < width ; i++ ) {
				for( int j=0 ; j < height ; j++ ) {
					image.setRGB( height-1-j, i, currImg.getRGB(i,j) );
				}
			}
			currImg = image;
		}

		return image;
    }
    
    private GameState gameState;
    public static final int CELL_SIZE = 25;
    HashMap<CellType, BufferedImage> images;
}
