
import java.util.Observable;

public class GameState extends Observable{
    
    public GameState(int width, int height, MazeDisplay mazeDisplay) {
        this.mazeDisplay = mazeDisplay;
        this.addObserver(mazeDisplay);
        maze = new Maze(width, height, new SimpleMazeGenerator());
        // On any change we jsut need to call setChanged() and then notifyObervers().
        // Anything will Observing the GameState will have it's update() method called.
        this.setChanged();
        this.notifyObservers(this.maze);
    }
    
    public void movePlayer(int direction) {
        // TODO: do things.
        this.setChanged();
        this.notifyObservers(this.maze);
    }
    
    private MazeDisplay mazeDisplay;
    private Maze maze;
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
}
