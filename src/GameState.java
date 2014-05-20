
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
    
    public void setPlayerV(int xV, int yV) {
        // Only change direction if we can move in that direction.
        if (maze.getCell(player.getX()+xV, player.getY()+yV) < Maze.WALL_VERT) {
            player.setV(xV, yV);
        }
    }
    
    public void tickPlayer() {
        
        // Update player position if they can move to the next position based on
        // direction.
        if (maze.getCell(player.nextPositionX(), player.nextPositionY()) < Maze.WALL_VERT) {
            player.move();
            maze.setCell(player.getX(), player.getY(), Maze.SPACE);
            updateDisplay();
        }
    }
    
    public void tickEnemies() {
        // TODO: this.
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
