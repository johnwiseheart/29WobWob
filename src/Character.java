/**
 * Represents a generic character in the maze, such as a player or an enemy.
 */
public interface Character {
    
	/**
	 * Gets the previous location of the character in the maze (used
	 * for interpolating the character sprite between two locations).
	 * @return Previous location of character
	 */
	Vector previousLocation();
	
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
     * @param state Current state of the game
     * @return Potential new location of character
     */
    Vector move(GameState state);
}
