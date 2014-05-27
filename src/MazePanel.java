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


public class MazePanel extends JPanel implements Observer {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -600678118735264063L;
	public MazePanel() {
        gameState = null;
    }

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
        
        HashMap<CellType, BufferedImage> images = new HashMap<CellType, BufferedImage>();
        try {
        	//images.put(CellType.WALL, resizeImage(ImageIO.read(new File("wall.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.DOT, resizeImage(ImageIO.read(new File("img/dot.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.KEY, resizeImage(ImageIO.read(new File("img/key.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.DOOR, resizeImage(ImageIO.read(new File("img/door.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.PLAYER1_E, resizeImage(ImageIO.read(new File("img/p1_east.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.PLAYER1_N, resizeImage(ImageIO.read(new File("img/p1_north.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.PLAYER1_S, resizeImage(ImageIO.read(new File("img/p1_south.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.PLAYER1_W, resizeImage(ImageIO.read(new File("img/p1_west.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY1, resizeImage(ImageIO.read(new File("img/e1.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY2, resizeImage(ImageIO.read(new File("img/e2.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY3, resizeImage(ImageIO.read(new File("img/e3.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.ENEMY4, resizeImage(ImageIO.read(new File("img/e4.png")),CELL_SIZE, CELL_SIZE));
        	
        	images.put(CellType.WALL_VERT, resizeImage(ImageIO.read(new File("img/wall_straight.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.WALL_HOR, rotateCw(resizeImage(ImageIO.read(new File("img/wall_straight.png")),CELL_SIZE, CELL_SIZE)));
        	images.put(CellType.WALL_CORN_NW, rotateCw(rotateCw(rotateCw(resizeImage(ImageIO.read(new File("img/wall_corner.png")),CELL_SIZE, CELL_SIZE)))));
        	images.put(CellType.WALL_CORN_NE, resizeImage(ImageIO.read(new File("img/wall_corner.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.WALL_CORN_SW, rotateCw(rotateCw(resizeImage(ImageIO.read(new File("img/wall_corner.png")),CELL_SIZE, CELL_SIZE))));
        	images.put(CellType.WALL_CORN_SE, rotateCw(resizeImage(ImageIO.read(new File("img/wall_corner.png")),CELL_SIZE, CELL_SIZE)));
        	images.put(CellType.WALL_CROSS, resizeImage(ImageIO.read(new File("img/wall_cross.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.WALL_T_N, resizeImage(ImageIO.read(new File("img/wall_t.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.WALL_T_S, rotateCw(rotateCw(resizeImage(ImageIO.read(new File("img/wall_t.png")),CELL_SIZE, CELL_SIZE))));
        	images.put(CellType.WALL_T_E, rotateCw(resizeImage(ImageIO.read(new File("img/wall_t.png")),CELL_SIZE, CELL_SIZE)));
        	images.put(CellType.WALL_T_W, rotateCw(rotateCw(rotateCw(resizeImage(ImageIO.read(new File("img/wall_t.png")),CELL_SIZE, CELL_SIZE)))));
        	images.put(CellType.WALL_END_N, resizeImage(ImageIO.read(new File("img/wall_end.png")),CELL_SIZE, CELL_SIZE));
        	images.put(CellType.WALL_END_S, rotateCw(rotateCw(resizeImage(ImageIO.read(new File("img/wall_end.png")),CELL_SIZE, CELL_SIZE))));
        	images.put(CellType.WALL_END_E, rotateCw(resizeImage(ImageIO.read(new File("img/wall_end.png")),CELL_SIZE, CELL_SIZE)));
        	images.put(CellType.WALL_END_W, rotateCw(rotateCw(rotateCw(resizeImage(ImageIO.read(new File("img/wall_end.png")),CELL_SIZE, CELL_SIZE)))));
        	images.put(CellType.WALL_BLOCK, resizeImage(ImageIO.read(new File("img/wall_block.png")),CELL_SIZE, CELL_SIZE));
        	
    
        } catch (IOException e) {
        }
        
        // Display maze.
        for (int x=0; x<displayMaze.getWidth(); x++) {
            for (int y=0; y<displayMaze.getHeight(); y++) {
            	if(images.containsKey(displayMaze.getCell(x, y)))
            		g2d.drawImage(images.get(displayMaze.getCell(x, y)),x*CELL_SIZE, y*CELL_SIZE, null);
            }
        }
        // Display player.
        g2d.drawImage(images.get(directionTOImage(gameState.getPlayerDirection())),
                      gameState.playerLocation().x()*CELL_SIZE,
                      gameState.playerLocation().y()*CELL_SIZE, null);
        
        // Display enemies.
        int i = 0;
        for (Vector enemyLocation : gameState.enemyLocations()) {
            CellType c = CellType.ENEMY1;
            switch(i%4) {
            case 0:
                c = CellType.ENEMY1;
                break;
            case 1:
                c = CellType.ENEMY2;
                break;
            case 2:
                c = CellType.ENEMY3;
                break;
            case 3:
                c = CellType.ENEMY4;
                break;
            }
            g2d.drawImage(images.get(c), enemyLocation.x()*CELL_SIZE,
                          enemyLocation.y()*CELL_SIZE, null);
            i++;
        }
        
        //TODO: fix this shit its not responsive
        // this.setSize(displayMaze.getWidth()*CELL_SIZE,displayMaze.getHeight()*CELL_SIZE);
        setMaximumSize(new Dimension(displayMaze.getWidth()*CELL_SIZE, displayMaze.getHeight()*CELL_SIZE));
        //this.setBounds(12, 100, displayMaze.getWidth()*CELL_SIZE, displayMaze.getHeight()*CELL_SIZE);
        //this.setBounds(0, 100, displayMaze.getWidth()*CELL_SIZE, displayMaze.getHeight()*CELL_SIZE);
    }
    
    private CellType directionTOImage(Direction direction) {
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
    
    // Part of the Observer pattern. This method is called when an object being observed notify's it has been changed.
    public void update(Observable o, Object arg) {
    	gameState = (GameState) o;
    	repaint();
    }
    
    /*
    public void updateDisplay(Maze newMaze) {
        displayMaze = newMaze;
        repaint();
    }
    */
    
    // TODO: stolen off the internet we need to rewrite this
    public static BufferedImage resizeImage( Image image, int width, int height) {

    	final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        
        return bufferedImage;
    }
 // TODO: stolen off the internet we need to rewrite this.
    public static BufferedImage rotateCw( BufferedImage img )
    {
		int			width  = img.getWidth();
		int			height = img.getHeight();
		BufferedImage	newImage = new BufferedImage( height, width, img.getType() );

		for( int i=0 ; i < width ; i++ )
			for( int j=0 ; j < height ; j++ )
				newImage.setRGB( height-1-j, i, img.getRGB(i,j) );

		return newImage;
    }
    
    private GameState gameState;
    public static final int CELL_SIZE = 25;
}
