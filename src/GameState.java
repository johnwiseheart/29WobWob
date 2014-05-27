import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class GameState extends Observable{
    
    /**
     * Creates a new GameState
     * @param width the starting width of the maze
     * @param height the starting height of the maze
     */
    public GameState(int width, int height) {
        maze = new Maze(width, height, new BraidedMazeGenerator(), 6, 4);
        
        this.numKey = 4;
        placeKeys(numKey);
        
        enemies = new ArrayList<Enemy>();
        placeNewEnemies(3);
        
        player = new Player(new Vector(width/2, 1), 3);
        
        level = 1;
        hasDied = false;
        score = 0;
        numKeysCollected = 0;
        gameFinished = false;
        
        // On any change we just need to call setChanged() and then notifyObervers().
        // Anything observing the GameState will have it's update() method called.
        updateDisplay();
    }
    
    /**
     * Sets the player's velocity
     * @param velocity the player's new velocity
     */
    public void setPlayerVelocity(Vector velocity) {
    	player.setVelocity(velocity);
    }
    
    /**
     * Sets the player's velocity.
     * @param x the player's new x velocity.
     * @param y the player's new y velocity.
     */
    public void setPlayerVelocity(int x, int y) {
    	setPlayerVelocity(new Vector(x, y));
    }
    
    /**
     * Update the player on each tick
     */
    public void tickPlayer() {
        Vector newLoc = player.move(maze, null);
        // Check if the player touched an enemy.
        for (Vector v : enemyLocations()) {
            if (v.equals(newLoc)) {
                hasDied = true;
            }
        }
        
        updateDisplay();
        
        if (hasDied) { // Lose a life.
            loseLife();
            return;
        } else if (hasWon()) { // Move on to the next level.
            nextLevel();
            return;
        }
        
        // Consume dot/key
        CellType walkedOver = maze.getCell(newLoc);
        maze.setCell(newLoc, CellType.SPACE);
        if (walkedOver == CellType.DOT) {
            // Eat dot.
            // TODO: This crashes the game sometimes for me (Adam)
            AudioManager dotSound = new AudioManager("music/kk.wav");
            dotSound.play();
            score++;
        } else if (walkedOver == CellType.KEY) {
            // Collect key.
            AudioManager keySound = new AudioManager("music/keypickup.wav");
            keySound.play();
            numKeysCollected++;
            if (numKeysCollected >= numKey) {
                maze.setCell(maze.getWidth()/2, 0, CellType.SPACE); // Remove door.
            }
            score += 10;
        }
    }
    
    /**
     * Update all the enemies each tick
     */
    public void tickEnemies() {
    	// Trust enemies to know what they're doing.
        for (Enemy enemy : enemies) {
            if(enemy.move(maze, playerLocation()).equals(playerLocation())) {
                hasDied = true;
            }
        }
        updateDisplay();
    }
    
    /**
     * Make the player lose a life, resetting their position or ending the game
     */
    private void loseLife() {
    	//TODO: put this in the right place.
    	AudioManager dieSound = new AudioManager("music/dieing.wav");
    	dieSound.play();
        if (player.loseLife() == 0) {
            finishGame();
        } else {
            resetPlayer();
        }
    }
    
    /**
     * Finish the game once the player loses all lives
     */
    private void finishGame() {
        gameFinished = true;
        updateDisplay();
    }
    
    /**
     * Notify all observers that the GameState has changed
     */
    private void updateDisplay() {
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Returns the number of keys the player has collected
     * @return the number of keys the player has collected
     */
    public int getNumKeysCollected() {
        return numKeysCollected;
    }
    
    /**
     * Returns the current score of the player
     * @return the current score of the player
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Returns the current state of the maze
     * @return the current state of the maze
     */
    public Maze getMaze() {
        return maze;
    }
    
    /**
     * Returns the player's current location
     * @return the player's current location
     */
    public Vector playerLocation() {
        return player.location();
    }
    
    /**
     * Returns the current locations of all the enemies
     * @return the current locations of all the enemies
     */
    public ArrayList<Vector> enemyLocations() {
        ArrayList<Vector> locations = new ArrayList<Vector>();
        for (Enemy enemy : enemies) {
            locations.add(enemy.location());
        }
        return locations;
    }
    
    public boolean gameFinished() {
        return gameFinished;
    }
    
    /**
     * Resets the player after they lose a life or a new level starts
     */
    private void resetPlayer() {
        hasDied = false;
        player.reset(new Vector(maze.getWidth()/2, 1));
        
        // scramble enemies
        for (Enemy enemy : enemies) {
        	enemy.scramble();
        }
    }
    
    /**
     * Places a number of enemies randomly, sufficiently far away from the
     * starting location of the player
     * @param numEnemy the number of enemies to place
     */
    private void placeNewEnemies(int numEnemy) {
        enemies.clear();
        Random random = new Random();
        // Place each enemy in one of the cells of the map that always starts
        // empty.
        for (int i=0; i<numEnemy; i++) {
            int x = random.nextInt(maze.getWidth()/2)*2 + 1;
            // Place enemies in upper half so they don't start near the player.
            int y = random.nextInt(maze.getHeight()/4)*2 + 1;
            if (y%2 == 0) {
                y += 1;
            }
            Vector v = new Vector(x, y);
            
            for (Enemy enemy : enemies) {
                if (enemy.location().equals(v)) {
                    i--;
                    continue;
                }
            }
            
            enemies.add(new Enemy(v, 10, 0.3, 10, 5));
        }
    }
    
    private void placeKeys(int numKey) {
        Random random = new Random();
        // Place each key in one of the cells of the map that always starts
        // empty.
        for (int i=0; i<numKey; i++) {
            int x = random.nextInt(maze.getWidth()/2)*2 + 1;
            int y = random.nextInt(maze.getHeight()/2)*2 + 1;
            Vector v = new Vector(x, y);
            
            if (maze.getCell(v) == CellType.KEY) {
                i--;
                continue;
            }
            
            maze.setCell(v, CellType.KEY);
        }
    }
    
    private void nextLevel() {
        level++;
        score += 50;
        maze = new Maze(maze.getWidth(), maze.getHeight(), new BraidedMazeGenerator(), 3, 4);
        placeNewEnemies(level);
        numKey++;
        placeKeys(numKey);
        resetPlayer();
    }
    
    private boolean hasWon() {
        return player.location().equals(maze.doorLocation());
    }
    
    public int getLevel() {
    	return level;
    }
    
    public int getLives() {
    	return player.getLives();
    }
    
    private Maze maze;
    private Player player;
    private ArrayList<Enemy> enemies;
    private int numKey;
    private int level;
    private boolean hasDied;
    private int numKeysCollected;
    private int score;
    private boolean gameFinished;
}
