
public class GameState {
    
    public GameState(int width, int height, MazeDisplay mazeDisplay) {
        this.mazeDisplay = mazeDisplay;
        maze = new Maze(width, height, new SimpleMazeGenerator());
        mazeDisplay.updateDisplay(maze);
    }
    
    public void movePlayer(int direction) {
        // TODO: do things. 
        mazeDisplay.updateDisplay(maze);
    }
    
    private MazeDisplay mazeDisplay;
    private Maze maze;
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
}
