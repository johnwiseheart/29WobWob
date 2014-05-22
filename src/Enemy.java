import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;


public class Enemy implements Character {

	public Enemy(Vector location, CellType type) { // TODO: difficulty stuff?
		this.location = location;
		this.type = type;
	}
	
	public Vector location() {
		return location;
	}

	public void setLocation(Vector location) {
		this.location = location;
	}

	public Vector move(Maze maze) {
		// make a god damn move
		
		/*int[] directions = {1, -1, 0};
		
		ArrayList<Vector> possibilities = new ArrayList<Vector>();
		
		for (int dx : directions) {
			for (int dy : directions) {
				int manhattan = Math.abs(dx) + Math.abs(dy);
				
				if (manhattan != 0 && manhattan != 2) { // don't go diagonally/stay still
					Vector newLoc = location.add(new Vector(dx, dy));
					if (!maze.isWall(newLoc) && !newLoc.equals(lastLocation)) { // Don't move into walls or backwards.
						possibilities.add(newLoc);
					}
				}
			}
		}
		
		if (possibilities.size() == 0) {
			//Move backwards;
		    Vector temp = location;
		    location = lastLocation;
		    lastLocation = temp;
		} else {
			Random r = new Random();
			return possibilities.get(r.nextInt(possibilities.size()));
		}*/
		
		// BFS for wobman
		
		class Node {
			Vector location;
			Vector firstMove;
			
			public Node(Vector start) {
				location = start;
				firstMove = null;
			}
			
			public Node(Node prev, int dx, int dy) {
				location = prev.location.add(new Vector(dx, dy));
				
				if (prev.firstMove != null) {
					firstMove = prev.firstMove;
				} else {
					firstMove = location;
				}
			}
		};
		
		Queue<Node> q = new LinkedList<Node>();
		HashSet<Vector> visited = new HashSet<Vector>();
		
		int[] directions = {-1, 0, 1};

		q.add(new Node(location));
		
		while (!q.isEmpty()) {
			Node n = q.remove();
			
			if (visited.contains(n.location)) {
				continue;
			}
			visited.add(n.location);
			
			System.out.println("testing " + n.location.x() + " " + n.location.y() + " " + maze.getCell(n.location).name());
			
			// did we find the wobman?
			if (maze.getCell(n.location) == CellType.PLAYER) {
				// yeah we did, so use this move
				System.out.println("sweet");
				return n.firstMove;
			}
			
			// expand node
			for (int dx : directions) {
				for (int dy : directions) {
					int manhattan = Math.abs(dx) + Math.abs(dy);
					if (manhattan == 1) {
						Node n2 = new Node(n, dx, dy);
						
						if (!maze.isWall(n2.location)) {
							q.add(n2);
						}
					}
				}
			}
		}
		
		// u wob
		System.out.println("U wob");
		return location;
	}
	
	public CellType type() {
		return type;
	}

	private Vector location;
	private Vector lastLocation;
	private CellType type;
}
