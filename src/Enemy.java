import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class Enemy implements Character {

	public Enemy(Vector location, Integer searchDistance, Double randomMoveProbability) {
		this.location = location;
		
		this.searchDistance = searchDistance;
		this.randomMoveProbability = randomMoveProbability;
		
		lastLocation = null;
	}
	
	public Vector location() {
		return location;
	}

	public void setLocation(Vector location) {
		this.location = location;
	}

	public Vector move(Maze maze) {
		// make a god damn move
		
		Random r = new Random();
		int[] dirs = {-1, 0, 1};
		
		Double prob = r.nextDouble();
		if (prob <= randomMoveProbability) {
			
			ArrayList<Vector> moves = new ArrayList<Vector>();
			
			for (int dx : dirs) {
				for (int dy : dirs) {
					int dist = Math.abs(dx) + Math.abs(dy);
					
					if (dist == 1) {
						
						Vector move = location.add(new Vector(dx, dy));
						if (!maze.isWall(move) && !move.equals(lastLocation) && !move.equals(maze.doorLocation())) {
							moves.add(move);
						}
						
					}
				}
			}
			
			if (!moves.isEmpty()) {
				Vector move = moves.get(r.nextInt(moves.size()));
				
				lastLocation = location;
				location = move;
			} else {
				Vector tmp = location;
				location = lastLocation;
				lastLocation = tmp;
			}
			
		} else {
			// start by BFSing for the player
			
			class BFSNode {
				Vector location;
				Vector firstMove;
				int distance;
				
				public BFSNode(Vector start) {
					location = start;
					firstMove = null;
					distance = 0;
				}
				
				public BFSNode(BFSNode prev, int dx, int dy) {
					location = prev.location.add(new Vector(dx, dy));
					
					firstMove = prev.firstMove;
					if (firstMove == null)
						firstMove = location;
				}
			};
			
			Queue<BFSNode> queue = new LinkedList<BFSNode>();
			HashSet<Vector> visited = new HashSet<Vector>();
			Vector target = maze.playerLocation();
			
			Vector optimalMove = null;
			
			// add initial node
			queue.add(new BFSNode(location));
			
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
				
				if (node.distance <= searchDistance) {
					for (int dx : dirs) {
						for (int dy : dirs) {
							int dist = Math.abs(dx) + Math.abs(dy);
							
							if (dist == 1) {
								
								BFSNode newNode = new BFSNode(node, dx, dy);
								if (!maze.isWall(newNode.location)) { 
									queue.add(newNode);
								}
								
							}
						}
					}
				}
			}
			
			lastLocation = location;
			location = optimalMove;
		}
		
		return location;
	}

	private Vector location;
	private Vector lastLocation;

	private Integer searchDistance;
	private Double randomMoveProbability;
}
