import java.util.ArrayList;
import java.util.Random;

public class Maze {
    
    public Maze(int width, int height, MazeGenerator mazeGenerator, int numEnemy, int score, int lives) {
        this.width = width;
        this.height = height;
        grid = mazeGenerator.generateMaze(width, height);
        player = new Player(new Vector(width/2, 1), lives);
        enemies = new ArrayList<Enemy>();
        placeEnemies(numEnemy);
        this.numKeysCollected = 0;
        this.score = score;
        this.hasDied = false;
    }
    
    private void placeEnemies(int numEnemy) {
        Random random = new Random();
        // Place each ghost in one of the cells of the map that always starts
        // empty.
        for (int i=0; i<numEnemy; i++) {
            int x = random.nextInt(width/2)*2 + 1;
            // Place ghosts in lower half so they don't start near the player.
            int y = random.nextInt(height/4)*2 + height/2 + 1;
            if (y%2 == 0) {
                y += 1;
            }
            Vector v = new Vector(x, y);
            
            if (isEnemy(v)) {
                i--;
                continue;
            }
            
            enemies.add(new Enemy(v));
        }
    }
    
    public CellType getCell(int x, int y) {
    	if (x < 0 || x >= this.width || y < 0 || y >= this.height)
    		return CellType.WALL;
    	
        return grid[x][y];
    }
    
    public CellType getCell(Vector location) {
    	return getCell(location.x(), location.y());
    }
    
    public boolean isWall(Vector location) {
    	CellType cell = getCell(location);
    	
    	switch (cell) {
	    	case WALL:
	    	case WALL_VERT:
	    	case WALL_HOR:
	    	case WALL_CORN_NW:
	    	case WALL_CORN_NE:
	    	case WALL_CORN_SW:
	    	case WALL_CORN_SE:
	    	case WALL_CROSS:
	    	case WALL_T_N:
	    	case WALL_T_S:
	    	case WALL_T_E:
	    	case WALL_T_W:
	    	case WALL_END_N:
	    	case WALL_END_S:
	    	case WALL_END_E:
	    	case WALL_END_W:
	    	case WALL_BLOCK:
	    	case DOOR:
	    		return true;
    		default:
    			return false;
    	}
    }
    
    public boolean isEnemy(Vector location) {
        CellType cell = getCell(location);
        
        switch (cell) {
            case ENEMY1:
            case ENEMY2:
            case ENEMY3:
            case ENEMY4:
                return true;
            default:
                return false;
        }
    }
    
    public void setCell(int x, int y, CellType cell) {
        grid[x][y] = cell;
    }
    
    public void setCell(Vector location, CellType cell) {
    	setCell(location.x(), location.y(), cell);
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void updatePlayer() {
        Vector newLoc = player.move(this);
        for (Vector v : enemyLocations()) {
            if (v.equals(newLoc)) {
                hasDied = true;
                return;
            }
        }
        
        if (getCell(newLoc) == CellType.DOT) {
            setCell(newLoc, CellType.SPACE); // eat dot
        } else if (getCell(newLoc) == CellType.KEY) {
            setCell(newLoc, CellType.SPACE); // collect key
            numKeysCollected++;
            if (numKeysCollected == 2) {
                setCell(width/2, 0, CellType.SPACE); // Remove door.
            }
            score++;
        }
    }
    
    public int loseLife() {
        return player.loseLife();
    }
    
    public int getLives() {
        return player.getLives();
    }
    
    public void resetPlayer() {
        hasDied = false;
        player.reset(new Vector(width/2, 1));
    }

    public void updateEnemies() {
        for (Enemy enemy : enemies) {
            if(enemy.move(this).equals(playerLocation())) {
                hasDied = true;
            }
        }
    }

    public void setPlayerVelocity(Vector velocity) {
        player.setVelocity(velocity);
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
    
    public Vector doorLocation() {
        return new Vector(width/2, 0);
    }
    
    public int getNumKeysCollected() {
        return this.numKeysCollected;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public boolean hasWon() {
        return playerLocation().equals(doorLocation());
    }
    
    public boolean hasDied() {
        return hasDied;
    }
    
    private CellType[][] grid;
    private int width;
    private int height;
    private Player player;
    private ArrayList<Enemy> enemies;
    private int numKeysCollected;
    private int score;
    private boolean hasDied;
}