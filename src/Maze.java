
public class Maze {
    
    public Maze(int width, int height, MazeGenerator mazeGenerator, int numEnemy, 
                int numKey) {
        this.width = width;
        this.height = height;
        grid = mazeGenerator.generateMaze(width, height);
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
    
    public Vector doorLocation() {
        return new Vector(width/2, 0);
    }
    
    private CellType[][] grid;
    private int width;
    private int height;
}