import java.io.Serializable;

/**
 * Represents a maze including walls, spaces, collectibles and a door.
 */
public class Maze implements Serializable {
    
    private static final long serialVersionUID = 1L;
    /**
     * Creates a new Maze
     * @param width the width of the maze
     * @param height the height of the maze
     * @param mazeGenerator the maze generator to use to generate the maze
     */
    public Maze(int width, int height, MazeGenerator mazeGenerator) {
        this.width = width;
        this.height = height;
        grid = mazeGenerator.generateMaze(width, height);
    }
    
    /**
     * Gets the CellType of a particular cell given by integer coordinates
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @return the CellType of that cell
     */
    public CellType getCell(int x, int y) {
    	if (x < 0 || x >= this.width || y < 0 || y >= this.height)
    		return CellType.WALL;
    	
        return grid[x][y];
    }
    
    /**
     * Gets the CellType of a particular cell given by a Vector
     * @param location the location of the cell
     * @return the CellType of that cell
     */
    public CellType getCell(Vector location) {
    	return getCell(location.x(), location.y());
    }
    
    /**
     * Whether the cell at a particular location is considered a wall
     * @param location the location of the cell
     * @return whether that cell is considered a wall
     */
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
    
    /**
     * Sets the CellType of a particular cell given by integer coordinates
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @param cell the CellType to change that cell to
     */
    public void setCell(int x, int y, CellType cell) {
        grid[x][y] = cell;
    }
    
    /**
     * Sets the CellType of a particular cell given by a Vector
     * @param location the location of the cell
     * @param cell the CellType to change that cell to
     */
    public void setCell(Vector location, CellType cell) {
    	setCell(location.x(), location.y(), cell);
    }
    
    /**
     * Returns the width of the maze
     * @return the width of the maze
     */
    public int getWidth() {
        return width;
    }
  
    /**
     * Returns the height of the maze
     * @return the height of the maze
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Returns the location of the door
     * @return the location of the door
     */
    public Vector doorLocation() {
        return new Vector(width/2, 0);
    }
    
    private CellType[][] grid;
    private int width;
    private int height;
}