
public class Player implements Character {

	/**
	 * Creates a new player starting at the given location in the maze.
	 * @param location starting point of player in maze
	 */
    public Player(Vector location, int lives) {
        this.location = location;
        this.velocity = new Vector(0, 0);
        this.nextVelocity = null;
        this.lives = lives;
    }
    
    /**
     * Sets the location of this player.
     * @param location the new location of the player.
     */
    public void setLocation(Vector location) {
    	this.location = location;
    }
    
    /**
     * Returns the location of this player.
     * @return the location of this player.
     */
    public Vector location() {
    	return location;
    }
    
    /**
     * Sets the amount by which the player should move when it next gets the chance
     * (that is, when move() is called on it).
     * @param velocity amount by which to move in the x and y directions
     */
    public void setVelocity(Vector velocity) {
    	//this.velocity = velocity;
    	
    	// set this as the velocity to consider next time we try to move
    	this.nextVelocity = velocity;
    }
    
    /**
     * The player makes a move by adding newVelocity to its location unless this
     * is an invalid move in which case it adds velocity instead, unless this is
     * also invalid
     * @param maze the current state of the maze
     * @param location not used
     * @return the new location of the player.
     */
    public Vector move(Maze maze, Vector unused) {
    	Vector newLocation = location.add(velocity); // using the current velocity
    	
    	if (nextVelocity != null) {
    		// there is a direction we want to move in (nextVelocity)
    		// see if we can change our direction yet
    		
    		Vector nextNewLocation = location.add(nextVelocity);
    		if (!maze.isWall(nextNewLocation)) {
    			velocity = nextVelocity;
    			nextVelocity = null;
    			
    			newLocation = nextNewLocation;
    		}
    	}
    	
    	if (!maze.isWall(newLocation)) {
    	    location = newLocation;
    		return newLocation;
    	} else {
    		return location;
    	}
    }
    
    /**
     * Make the player lose a life
     * @return the new amount of lives
     */
    public int loseLife() {
        lives--;
        return lives;
    }
    
    /**
     * Returns the lives of the player.
     * @return the lives of the player.
     */
    public int getLives() {
        return lives;
    }
    
    /**
     * Resets the player to a given location
     * @param location the location to reset the player to
     */
    public void reset(Vector location) {
        this.location = location;
        this.velocity = new Vector(0, 0);
        this.nextVelocity = null;
    }
    
    private Vector location;
    private Vector velocity; // direction we're moving in
    private Vector nextVelocity; // direction we want to try to move in as soon as we can
    private int lives;
}
