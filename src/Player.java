
public class Player implements Character {

	/**
	 * Creates a new player starting at the given location in the maze.
	 * @param location Starting point of player in maze
	 */
    public Player(Vector location, int lives) {
        this.location = location;
        this.velocity = new Vector(0, 0);
        this.nextVelocity = null;
        this.lives = lives; // TODO: Adjust for difficulty.
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
    	//this.velocity = velocity;
    	
    	// set this as the velocity to consider next time we try to move
    	this.nextVelocity = velocity;
    }
    
    public Vector move(Maze maze) {
    	Vector newLocation = location.add(velocity); // using the current velocity
    	
    	if (nextVelocity != null) {
    		// there is a direction we want to move in (nextVelocity)
    		// see if we can change our direction yet
    		
    		Vector nextNewLocation = location.add(nextVelocity);
    		if (!maze.isWall(nextNewLocation)) {
    			// yes we can, america
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
    
    public int loseLife() {
        lives--;
        return lives;
    }
    
    public int getLives() {
        return lives;
    }
    
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
