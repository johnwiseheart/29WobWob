
public class Maze implements Cloneable{
    
    public Maze(int width, int height, MazeGenerator mazeGenerator) {
        this.width = width;
        this.height = height;
        grid = mazeGenerator.generateMaze(width, height);
    }
    
    public int getCell(int x, int y) {
        return grid[x][y];
    }
    
    public void setCell(int x, int y, int cell) {
        grid[x][y] = cell;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public Object clone() {
        try {
            Maze maze = (Maze)super.clone();
            maze.grid = new int[width][height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    maze.grid[x][y] = grid[x][y];
                }
            }
            return maze;
        } catch (CloneNotSupportedException e) {
            return null; // Doesn't happen.
        }
    }
    
    private int[][] grid;
    private int width;
    private int height;
    
    public static final int SPACE = 0;
    public static final int DOT = 1;
    public static final int WALL = 2;
    public static final int DOOR = 3;
    public static final int KEY = 4;
    
    public static final int P1 = 5;
    
    public static final int E1 = 6;
    public static final int E2 = 7;
    public static final int E3 = 8;
    public static final int E4 = 9;
    
    public static final int WALL_VERT = 20;
    public static final int WALL_HOR = 21;
    public static final int WALL_CORN_NW = 22;
    public static final int WALL_CORN_NE = 23;
    public static final int WALL_CORN_SW = 24;
    public static final int WALL_CORN_SE = 25;
    public static final int WALL_CROSS = 26;
    public static final int WALL_T_N = 27;
    public static final int WALL_T_S = 28;
    public static final int WALL_T_E = 29;
    public static final int WALL_T_W = 30;
    public static final int WALL_END_N = 31;
    public static final int WALL_END_S = 32;
    public static final int WALL_END_E = 33;
    public static final int WALL_END_W = 34;
    public static final int WALL_BLOCK = 35;
    
    
}
