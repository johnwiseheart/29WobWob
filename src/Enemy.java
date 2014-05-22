import java.util.ArrayList;
import java.util.Random;


public class Enemy implements Character {

	public Enemy(Vector location) { // TODO: difficulty stuff?
		this.location = location;
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
					if (!maze.isWall(newLoc) && !newLoc.equals(lastLocation) && !newLoc.equals(maze.doorLocation())) { // Don't move into walls or backwards.
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
			lastLocation = location;
			location = possibilities.get(r.nextInt(possibilities.size()));
		}
		return location;
	}

	private Vector location;
	private Vector lastLocation;
}
