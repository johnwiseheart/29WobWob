import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class GameState extends Observable{
    
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
        
        // On any change we just need to call setChanged() and then notifyObervers().
        // Anything observing the GameState will have it's update() method called.
        updateDisplay();
    }
    
    /**
     * Sets the player's velocity.
     * @param velocity the player's new velocity.
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
     * Update the player on each tick.
     */
    public void tickPlayer() {
        Vector newLoc = player.move(maze, null);
        for (Vector v : enemyLocations()) {
            if (v.equals(newLoc)) {
                hasDied = true;
            }
        }
        
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
     * 
     */
    public void tickEnemies() {
    	// Trust enemies to know what they're doing.
        for (Enemy enemy : enemies) {
            if(enemy.move(maze, playerLocation()).equals(playerLocation())) {
                hasDied = true;
            }
        }
    }
    
    public void loseLife() {
    	//TODO: put this in the right place.
    	AudioManager dieSound = new AudioManager("music/dieing.wav");
    	dieSound.play();
        if (player.loseLife() == 0) {
            finishGame();
        } else {
            resetPlayer();
        }
    }
    
    public void finishGame() {
        // TODO: not this.
        System.exit(1);
    }
    
    public void updateDisplay() {
        this.setChanged();
        this.notifyObservers();
    }
    
    public int getNumKeysCollected() {
        return numKeysCollected;
    }
    
    public int getScore() {
        return score;
    }
    
    public Maze getMaze() {
        return maze;
    }
    
    public Vector playerLocation() {
        return player.location();
    }
    
    public ArrayList<Vector> enemyLocations() {
        ArrayList<Vector> locations = new ArrayList<Vector>();
        for (Enemy enemy : enemies) {
            locations.add(enemy.location());
        }
        return locations;
    }
    
    public void resetPlayer() {
        hasDied = false;
        player.reset(new Vector(maze.getWidth()/2, 1));
        
        // scramble enemies
        for (Enemy enemy : enemies) {
        	enemy.scramble();
        }
    }
    
    private void placeNewEnemies(int numEnemy) {
        enemies.clear();
        Random random = new Random();
        // Place each enemy in one of the cells of the map that always starts
        // empty.
        for (int i=0; i<numEnemy; i++) {
            int x = random.nextInt(maze.getWidth()/2)*2 + 1;
            // Place enemies in lower half so they don't start near the player.
            int y = random.nextInt(maze.getHeight()/4)*2 + maze.getHeight()/2 + 1;
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
    
    private Maze maze;
    private Player player;
    private ArrayList<Enemy> enemies;
    private int numKey;
    private int level;
    private boolean hasDied;
    private int numKeysCollected;
    private int score;
}
