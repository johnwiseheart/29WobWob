import java.util.Observable;

public class GameState extends Observable{
    
    public GameState(int width, int height, MazeDisplay mazeDisplay) {
        this.addObserver(mazeDisplay);
        maze = new Maze(width, height, new SimpleMazeGenerator());
        // On any change we just need to call setChanged() and then notifyObervers().
        // Anything observing the GameState will have it's update() method called.
        
        //this.player = new Player(width/2, 1);
        
        updateDisplay();
    }
    
    public void setPlayerVelocity(Vector velocity) {
    	maze.setPlayerVelocity(velocity);
    }
    
    public void setPlayerVelocity(int x, int y) { // TODO: perhaps this should be replaced with just the vector version?
    	setPlayerVelocity(new Vector(x, y));
    }
    
    public void tickPlayer() {
        maze.updatePlayer();
    }
    
    public void tickEnemies() {
    	// trust enemies to know what they're doing
    	maze.updateEnemies();
    }
    
    public void updateDisplay() {
        this.setChanged();
        this.notifyObservers(maze);
    }
    
    private Maze maze;
}
