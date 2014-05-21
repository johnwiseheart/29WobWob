
import java.util.ArrayList;
import java.util.Observable;

public class GameState extends Observable{
    
    public GameState(int width, int height, MazeDisplay mazeDisplay) {
        this.addObserver(mazeDisplay);
        maze = new Maze(width, height, new SimpleMazeGenerator());
        // On any change we just need to call setChanged() and then notifyObervers().
        // Anything observing the GameState will have it's update() method called.
        
        //this.player = new Player(width/2, 1);
        player = new Player(new Vector(width/2, 1));
        
        enemies = new ArrayList<Enemy>();
        enemies.add(new Enemy(new Vector(3, 3), CellType.ENEMY1));
        
        updateDisplay();
    }
    
    public void setPlayerVelocity(Vector velocity) {
    	player.setVelocity(velocity);
    }
    
    public void setPlayerVelocity(int x, int y) { // TODO: perhaps this should be replaced with just the vector version?
    	setPlayerVelocity(new Vector(x, y));
    }
    
    public void tickPlayer() {
        
    	Vector oldLoc = player.location();
    	Vector newLoc = player.move(maze);
    	
    	if (!oldLoc.equals(newLoc)) {
    		// Can they move here?
    		// TODO: beef this up with key/enemy detection, just checking for walls at the moment
    		
			player.setLocation(newLoc); // update location
			
			maze.setCell(newLoc, CellType.SPACE); // eat dot
    	}
    	
    }
    
    public void tickEnemies() {
    	
    	// trust enemies to know what they're doing
    	for (Enemy enemy : enemies) {
    		enemy.setLocation(enemy.move(maze));
    	}
    	
    }
    
    public void updateDisplay() {
        Maze displayMaze = (Maze)maze.clone();
        
        // put player in the maze
        displayMaze.setCell(player.location(), CellType.PLAYER);
        
        for (Enemy enemy : enemies) {
        	displayMaze.setCell(enemy.location(), enemy.type());
        }
        
        this.setChanged();
        this.notifyObservers(displayMaze);
    }
    
    private Player player;
    private ArrayList<Enemy> enemies;
    private Maze maze;
}
