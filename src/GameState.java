
import java.util.Observable;

public class GameState extends Observable{
    
    public GameState(int width, int height, MazeDisplay mazeDisplay) {
        this.addObserver(mazeDisplay);
        maze = new Maze(width, height, new SimpleMazeGenerator());
        // On any change we just need to call setChanged() and then notifyObervers().
        // Anything observing the GameState will have it's update() method called.
        
        //this.player = new Player(width/2, 1);
        player = new Player(new Vector(width/2, 1));
        
        updateDisplay();
    }
    
    public void setPlayerVelocity(Vector velocity) {
    	player.setVelocity(velocity);
    }
    
    public void setPlayerVelocity(int x, int y) {
    	setPlayerVelocity(new Vector(x, y));
    }
    
    public void tickPlayer() {
        
    	Vector oldLoc = player.location();
    	Vector newLoc = player.move(maze);
    	
    	if (!oldLoc.equals(newLoc)) {
    		// Can they move here?
    		// TODO: beef this up with key/enemy detection, just checking for walls at the moment
    		if (!maze.isWall(newLoc)) {
    			// all good (Y)
    			
    			player.setLocation(newLoc); // update location
    			
    			maze.setCell(newLoc, CellType.SPACE); // eat dot
    		}
    	}
    	
    }
    
    public void tickEnemies() {
        // TODO: this.
    }
    
    public void updateDisplay() {
        Maze displayMaze = (Maze)maze.clone();
        
        // put player in the maze
        displayMaze.setCell(player.location(), CellType.PLAYER);
        
        this.setChanged();
        this.notifyObservers(displayMaze);
    }
    
    private Player player;
    private Maze maze;
}
