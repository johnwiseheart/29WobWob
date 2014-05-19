
public class Maze {
    
    public Maze(int width, int height, MazeGenerator mazeGenerator) {
        this.width = width;
        this.height = height;
        grid = mazeGenerator.generateMaze(width, height);
    }
    
    public int getCell(int x, int y) {
        return grid[x][y];
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
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
