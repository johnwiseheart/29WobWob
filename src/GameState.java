
import java.util.Observable;

public class GameState extends Observable{
    
    public GameState(int width, int height, MazeDisplay mazeDisplay) {
        this.addObserver(mazeDisplay);
        maze = new Maze(width, height, new SimpleMazeGenerator());
        // On any change we just need to call setChanged() and then notifyObervers().
        // Anything observing the GameState will have it's update() method called.
        this.player = new Player(width/2, 1);
        updateDisplay();
    }
    
    public void movePlayer(int x, int y) {
        if (maze.getCell(player.getX()+x, player.getY()+y) < Maze.WALL_VERT) {
            player.move(x, y);
            updateDisplay();
        }
    }
    
    public void updateDisplay() {
        Maze displayMaze = (Maze)maze.clone();
        displayMaze.setCell(player.getX(), player.getY(), Maze.P1);
        this.setChanged();
        this.notifyObservers(displayMaze);
    }
    
    private Player player;
    private Maze maze;
}
