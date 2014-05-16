
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
    
    public static final int WALL = 1;
    public static final int SPACE = 0;
}
