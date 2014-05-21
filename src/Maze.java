import java.util.ArrayList;


public class Maze implements Cloneable{
    
    public Maze(int width, int height, MazeGenerator mazeGenerator) {
        this.width = width;
        this.height = height;
        grid = mazeGenerator.generateMaze(width, height);
        player = new Player(new Vector(width/2, 1));
        enemies = new ArrayList<Enemy>();
        enemies.add(new Enemy(new Vector(3, 3), CellType.ENEMY1));
    }
    
    public CellType getCell(int x, int y) {
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
        if (getCell(newLoc) == CellType.DOT) {
            setCell(newLoc, CellType.SPACE); // eat dot
        }else if (getCell(newLoc) == CellType.KEY) {
            setCell(newLoc, CellType.SPACE); // collect key
        }
    }

    public void updateEnemies() {
        for (Enemy enemy : enemies) {
            enemy.move(this);
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
    
    /*
    public Object clone() {
        try {
            Maze maze = (Maze)super.clone();
            maze.grid = new CellType[width][height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    maze.grid[x][y] = grid[x][y];
                }
            }
            return maze;
        } catch (CloneNotSupportedException e) {
            return null; // Doesn't happen.
        }
    }*/
    
    private CellType[][] grid;
    private int width;
    private int height;
    private Player player;
    private ArrayList<Enemy> enemies;
}