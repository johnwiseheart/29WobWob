/**
 * A interface for classes that generate mazes.
 */
public interface MazeGenerator {

    /**
     * Generates a maze of given width and height
     * @param width the width of the maze to be generated
     * @param height the height of the maze to be generated
     * @return the maze as a 2D array of CellTypes
     */
    public CellType[][] generateMaze(int width, int height);
}
