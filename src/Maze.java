
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
    public static final int WALL = 1;
    public static final int DOOR = 2;
    public static final int KEY = 3;
    
    public static final int P1 = 4;
    
    public static final int E1 = 5;
    public static final int E2 = 6;
    public static final int E3 = 7;
    public static final int E4 = 8;
}
