import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * An enemy that moves by itself, doesn't collect dots and kills the player on
 * contact.
 */
public class Enemy implements Character, Serializable {
    
    /**
     * Creates a new Enemy
     * @param location The location the enemy starts at
     * @param searchDistance The distance from the player at which it will stop
     * moving towards the player and will instead move randomly
     * @param randomMoveProbability The probability that the enemy will make a
     * @param huntDuration Number of moves for which the enemy will hunt the player
     * @param scrambleDuration Number of moves for which the enemy will move randomly
     * random move
     */
	public Enemy(Vector location, Integer searchDistance, Double randomMoveProbability, Integer huntDuration, Integer scrambleDuration) {
		this.location = location;
		
		this.searchDistance = searchDistance;
		this.randomMoveProbability = randomMoveProbability;
		this.huntDuration = huntDuration;
		this.scrambleDuration = scrambleDuration;
		this.timer = 0;
		
		lastLocation = null;
	}
	
	/**
	 * Returns the enemy's location
	 * @return the enemy's location
	 */
	public Vector location() {
		return location;
	}

	/**
	 * Sets the enemy's location
	 * @param location the enemy's new location
	 */
	public void setLocation(Vector location) {
		this.location = location;
	}

	/**
	 * Moves the enemy.
	 * Either moves the enemy towards the player using a BFS or moves randomly
	 * (without going backwards) depending on a random probability and how far
	 * the enemy is from the player
	 * @param maze the current state of the maze
	 * @param location the location of the player
	 * @return where the enemy moved to
	 */
	public Vector move(Maze maze, Vector playerLocation) {
		
		Random r = new Random();
		int[] dirs = {-1, 0, 1};
		
		Double prob = r.nextDouble();
		// If we're far away or because of probability.
		if (prob <= randomMoveProbability ||
		    location.distanceTo(playerLocation) >= searchDistance) {
		    // Make a random move.
			
			ArrayList<Vector> moves = new ArrayList<Vector>();
			
			for (int dx : dirs) {
				for (int dy : dirs) {
					int dist = Math.abs(dx) + Math.abs(dy);
					
					if (dist == 1) {
						
						Vector move = location.add(new Vector(dx, dy));
						if (!maze.isWall(move) && !move.equals(lastLocation) && 
						    !move.equals(maze.doorLocation())) {
							moves.add(move);
						}
						
					}
					
				}
			}
			
			if (!moves.isEmpty()) {
			    // Move to a random free space as long as we didn't just come
			    // from there.
				Vector move = moves.get(r.nextInt(moves.size()));
				
				lastLocation = location;
				location = move;
			} else {
			    // Move backwards because there's nowhere else to go
				Vector tmp = location;
				location = lastLocation;
				lastLocation = tmp;
			}
		
		} else {
			// Move towards the player using a BFS.
			
		    /**
		     * A node to be used in a BFS search.
		     */
			class BFSNode {
				Vector location;
				// Only store the first move, it's all we need to move the enemy.
				Vector firstMove;
				
				/**
				 * Creates the first BFS node in the search
				 * @param start the node's location in the maze
				 */
				public BFSNode(Vector start) {
					location = start;
					firstMove = null;
				}
				
				/**
				 * Creates a subsequent BFS node
				 * @param prev the previous BFS node in the search
				 * @param dx how much we moved horizontally from the previous
				 * node
				 * @param dy how much we moved vertically from the previous node
				 */
				public BFSNode(BFSNode prev, int dx, int dy) {
					location = prev.location.add(new Vector(dx, dy));
					
					firstMove = prev.firstMove;
					if (firstMove == null)
						firstMove = location;
				}
			};
			
			Queue<BFSNode> queue = new LinkedList<BFSNode>();
			HashSet<Vector> visited = new HashSet<Vector>();
			Vector target = playerLocation;
			
			Vector optimalMove = null;
			
			// Add initial node.
			queue.add(new BFSNode(location));
			
			// BFS.
			while (!queue.isEmpty()) {
				BFSNode node = queue.remove();
				
				if (visited.contains(node.location)) {
					continue;
				}
				
				visited.add(node.location);
				
				if (target.equals(node.location)) {
					optimalMove = node.firstMove;
					break;
				}
				
				// Expand the node.
				for (int dx : dirs) {
					for (int dy : dirs) {
						int dist = Math.abs(dx) + Math.abs(dy);
						
						if (dist == 1) {
							
							BFSNode newNode = new BFSNode(node, dx, dy);
							// Don't move into walls or backwards.
							if (!maze.isWall(newNode.location) &&
							    !newNode.location.equals(lastLocation)) { 
								queue.add(newNode);
							}
							
						}
					}
				}
			}
			
			if (optimalMove != null) {
			    lastLocation = location;
	            location = optimalMove;
			}
		}
	
		// increment timer
		timer++;
		if (timer >= huntDuration + scrambleDuration) {
			timer = 0;
		}
		
		return location;
	}
	
	/**
	 * Forces the enemy to enter its scrambling phase (i.e. randomly moving).
	 */
	public void scramble() {
		// reset timer
		timer = 0;
	}

	private Vector location;
	private Vector lastLocation;

	private Integer searchDistance;
	private Double randomMoveProbability;
	
	private Integer huntDuration;
	private Integer scrambleDuration;
	private Integer timer;
}
