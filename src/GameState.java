import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class GameState extends Observable implements Serializable {
    
    /**
     * Creates a new GameState
     * @param width the starting width of the maze
     * @param height the starting height of the maze
     */
    public GameState(Options.DifficultyType difficulty) {
        this.difficulty = difficulty;
        int width = 0, height = 0;
        switch (this.difficulty) {
        case EASY:
            width = 23;
            height = 15;
            numEnemies = 1;
            numKeys = 2;
            searchDistance = 5;
            randomMoveProbability = 0.6;
            huntDuration = 5;
            scrambleDuration = 10;
            break;
        case MEDIUM:
            width = 31;
            height = 19;
            numEnemies = 2;
            numKeys = 3;
            searchDistance = 10;
            randomMoveProbability = 0.4;
            huntDuration = 6;
            scrambleDuration = 8;
            break;
        case HARD:
            width = 43;
            height = 25;
            numEnemies = 3;
            numKeys = 4;
            searchDistance = 15;
            randomMoveProbability = 0.2;
            huntDuration = 7;
            scrambleDuration = 6;
            break;
        }

        maze = new Maze(width, height, new BraidedMazeGenerator());
        player = new Player(new Vector(width/2, height-2), 3);
        
        enemies = new ArrayList<Enemy>();
        placeNewEnemies(numEnemies, searchDistance, randomMoveProbability,
                        huntDuration, scrambleDuration);
        
        placeKeys(numKeys);
        
        level = 1;
        hasDied = false;
        score = 0;
        numKeysCollected = 0;
        gameFinished = false;
        lastCollected = CellType.SPACE;
        
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
     * Update the characters in each tick
     */
    public void tickCharacters() {
        hasDied = false;
        tickEnemies();
        tickPlayer();
        updateDisplay();
    }
    
    /**
     * Returns the direction of the player.
     * @return the direction of the player.
     */
    public Direction getPlayerDirection() {
    	return player.getDirection();
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
    
    /**
     * Returns whether there is an enemy at the given location.
     * @param location Location to test
     * @return Whether there is an enemy here
     */
    public boolean isEnemy(Vector location) {
    	for (Vector enemyLoc : enemyLocations()) {
    		if (enemyLoc.equals(location)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean gameFinished() {
        return gameFinished;
    }
    
    public CellType lastCollected() {
        return lastCollected;
    }
    
    /**
     * Make the player lose a life, resetting their position or ending the game
     */
    private void loseLife() {
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
    }
    
    /**
     * Update the player on each tick
     */
    private void tickPlayer() {
        // Check if the player touched an enemy.
        for (Vector v : enemyLocations()) {
            if (v.equals(player.location())) {
                hasDied = true;
                lastCollected = CellType.SPACE;
            }
        }
        
        Vector newLoc = player.move(this);
        
        if (hasDied) { // Lose a life.
            loseLife();
            return;
        } else if (hasWon()) { // Move on to the next level.
            nextLevel();
            return;
        }
        
        // Consume dot/key
        CellType walkedOver = maze.getCell(newLoc);
        if (walkedOver == CellType.DOT) {
            // Eat dot.
            maze.setCell(newLoc, CellType.SPACE);
            score += level;
        } else if (walkedOver == CellType.KEY) {
            maze.setCell(newLoc, CellType.SPACE);
            numKeysCollected++;
            if (numKeysCollected >= numKeys) {
                maze.setCell(maze.getWidth()/2, 0, CellType.SPACE); // Remove door.
            }
            score += 10*level;
        }
        lastCollected = walkedOver;
    }
    
    /**
     * Update all the enemies each tick
     */
    private void tickEnemies() {
        // Trust enemies to know what they're doing.
        for (Enemy enemy : enemies) {
        	if (enemy.move(this).equals(playerLocation())) {
                hasDied = true;
            }
        }
    }
    
    /**
     * Notify all observers that the GameState has changed
     */
    private void updateDisplay() {
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Resets the player after they lose a life or a new level starts
     */
    private void resetPlayer() {
        player.reset(new Vector(maze.getWidth()/2, maze.getHeight()-2));
        
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
    private void placeNewEnemies(int numEnemy, int searchDistance, 
                                 Double randomMoveProbability, int huntDuration,
                                 int scrambleDuration) {
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
            enemies.add(new Enemy(v, searchDistance, randomMoveProbability, 
                                  huntDuration, scrambleDuration));
        }
    }
    
    /**
     * Places a number of keys randomly
     * @param numKey the number of keys to place
     */
    private void placeKeys(int numKey) {
        Random random = new Random();
        // Place each key in one of the cells of the map that always starts
        // empty.
        for (int i=0; i<numKey; i++) {
            int x = random.nextInt(maze.getWidth()/2)*2 + 1;
            int y = random.nextInt(maze.getHeight()/2)*2 + 1;
            Vector v = new Vector(x, y);
            
            // Don't place two keys in the same place or where the player starts.
            if (maze.getCell(v) == CellType.KEY || v.equals(player.location())) {
                i--;
                continue;
            }
            
            maze.setCell(v, CellType.KEY);
        }
    }
    
    /**
     * Starts the next level, regenerating the maze, placing the enemies again,
     * placing the keys again and resetting the player
     */
    private void nextLevel() {
        level++;
        score += 50;
        maze = new Maze(maze.getWidth(), maze.getHeight(), new BraidedMazeGenerator());
        if (level%2 == 1) {
            numEnemies++;
        }
        if (scrambleDuration > 1) {
            scrambleDuration--;
            huntDuration++;
        }
        placeNewEnemies(numEnemies, searchDistance, randomMoveProbability,
                        huntDuration, scrambleDuration);
        numKeys++;
        placeKeys(numKeys);
        numKeysCollected = 0;
        resetPlayer();
    }
    
    /**
     * Whether the player has won (if it has exited the maze)
     * @return whether the player has won
     */
    private boolean hasWon() {
        return player.location().equals(maze.doorLocation());
    }
    
    public int getLevel() {
    	return level;
    }
    
    public int getLives() {
    	return player.getLives();
    }
    
    public boolean hasDied() {
    	return hasDied;
    }
    
    private Maze maze;
    private Options.DifficultyType difficulty;
    private Player player;
    private ArrayList<Enemy> enemies;
    private int numEnemies;
    private int searchDistance;
    private Double randomMoveProbability;
    private int huntDuration;
    private int scrambleDuration;
    private int numKeys;
    private int level;
    private boolean hasDied;
    private int numKeysCollected;
    private int score;
    private boolean gameFinished;
    private CellType lastCollected;
}
