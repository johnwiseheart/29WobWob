/**
 * Represents a generic character in the maze, such as a player or an enemy.
 */
public interface Character {
    
    /**
     * Gets the current location of the character in the maze.
     * @return Location of character
     */
    Vector location();
    
    /**
     * Sets the current location of the character in the maze.
     * @param location New location of character
     */
    void setLocation(Vector location);
    
    /**
     * Instructs the character to move by one step, and returns where they want to move.
     * Note that this may not necessarily be valid! Appropriate checks must be made first as to whether
     * they can actually move there, and as to the consequences of the move (e.g. run into enemy, collect key).
     * @param maze state of the maze
     * @param a location of interest to the move (eg. the player's location)
     * @return Potential new location of character
     */
    Vector move(Maze maze, Vector location);
}
