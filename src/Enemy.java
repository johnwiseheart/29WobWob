import java.util.ArrayList;
import java.util.Random;


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
		
		int[] directions = {1, -1, 0};
		
		ArrayList<Vector> possibilities = new ArrayList<Vector>();
		
		for (int dx : directions) {
			for (int dy : directions) {
				int manhattan = Math.abs(dx) + Math.abs(dy);
				
				if (manhattan != 0 && manhattan != 2) { // don't go diagonally/stay still
					Vector newLoc = location.add(new Vector(dx, dy));
					if (!maze.isWall(newLoc)) {
						possibilities.add(newLoc);
					}
				}
			}
		}
		
		if (possibilities.size() == 0) {
			// shit, can't move, wtf
			System.out.println("Can't move!!");
			return location;
		} else {
			Random r = new Random();
			location = possibilities.get(r.nextInt(possibilities.size()));
			return location;
		}
	}
	
	public CellType type() {
		return type;
	}

	private Vector location;
	private CellType type;
}
