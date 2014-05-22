import java.util.Observable;

public class GameState extends Observable{
    
    public GameState(int width, int height, MazeComponent mazeDisplay) {
        this.addObserver(mazeDisplay);
        maze = new Maze(width, height, new SimpleMazeGenerator(), 3, 0);
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
        if (getNumKeysCollected() >= 2) {
            maze.setCell(maze.getWidth()/2, 0, CellType.SPACE);
        }
        if (maze.playerLocation().equals(new Vector(maze.getWidth()/2, 0))) {
            nextLevel();
        }
    }
    
    public void tickEnemies() {
    	// trust enemies to know what they're doing
    	maze.updateEnemies();
    }
    
    public void updateDisplay() {
        this.setChanged();
        this.notifyObservers(maze);
    }
    
    public int getNumKeysCollected() {
        return maze.getNumKeysCollected();
    }
    
    public int getScore() {
        return maze.getScore();
    }
    
    private void nextLevel() {
        maze = new Maze(maze.getWidth(), maze.getHeight(), new SimpleMazeGenerator(), 3, maze.getScore());
    }
    
    private Maze maze;
}
