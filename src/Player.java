
public class Player implements Character {

	/**
	 * Creates a new player starting at the given location in the maze.
	 * @param location Starting point of player in maze
	 */
    public Player(Vector location) {
        this.location = location;
        this.velocity = new Vector(0, 0);
    }
    
    public void setLocation(Vector location) {
    	this.location = location;
    }
    
    public Vector location() {
    	return location;
    }
    
    /**
     * Sets the amount by which the player should move when it next gets the chance
     * (that is, when move() is called on it).
     * @param velocity Amount by which to move in the x and y directions
     */
    public void setVelocity(Vector velocity) {
    	this.velocity = velocity;
    }
    
    public Vector move(Maze maze) {
    	Vector newLocation = location.add(velocity);
    	
    	return newLocation;
    }
    
    private Vector location;
    private Vector velocity;
}
