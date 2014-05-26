import java.util.Observable;

public class GameState extends Observable{
    
    public GameState(int width, int height, MazePanel mazeDisplay) {
        this.addObserver(mazeDisplay);
        maze = new Maze(width, height, new SimpleMazeGenerator(), 3, 0, 3);
        // On any change we just need to call setChanged() and then notifyObervers().
        // Anything observing the GameState will have it's update() method called.
        updateDisplay();
        level = 0;
    }
    
    public void setPlayerVelocity(Vector velocity) {
    	maze.setPlayerVelocity(velocity);
    }
    
    public void setPlayerVelocity(int x, int y) { // TODO: perhaps this should be replaced with just the vector version?
    	setPlayerVelocity(new Vector(x, y));
    }
    
    public void tickPlayer() {
        maze.updatePlayer();
        if (maze.hasDied()) { // Lose a life.
            loseLife();
        } else if (maze.hasWon()) { // Move on to the next level.
            nextLevel();
        }
    }
    
    public void tickEnemies() {
    	// trust enemies to know what they're doing
    	maze.updateEnemies();
    }
    
    public void loseLife() {
    	//TODO: put this in the right place
    	AudioManager dieSound = new AudioManager("music/dieing.wav");
    	dieSound.play();
        if (maze.loseLife() == 0) {
            finishGame();
        } else {
            maze.resetPlayer();
        }
    }
    
    public void finishGame() {
        // TODO: not this.
        System.exit(1);
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
        level++;
        maze = new Maze(maze.getWidth(), maze.getHeight(), new SimpleMazeGenerator(), 3, maze.getScore(), maze.getLives());
    }
    
    private Maze maze;
    private int level;
}
