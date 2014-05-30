import java.io.Serializable;

/**
 * Represents a 2D quantity with integer components, such as a point in the maze, the velocity of a character, etc.
 * @author george
 *
 */
public class Vector implements Serializable {

	/**
	 * Creates a vector with the given x and y components.
	 * @param x x component
	 * @param y y component
	 */
	public Vector(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Gets the x component.
	 * @return x component
	 */
	public int x() {
		return x;
	}
	
	/**
	 * Gets the y component.
	 * @return y component
	 */
	public int y() {
		return y;
	}
	
	/**
	 * Sets the x component.
	 * @param x New x component
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Sets the y component.
	 * @param y New y component
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Returns a Direction enum indicating the current direction of this vector if it is N, S, E or W.
	 * Otherwise return NONE.
	 * @return Direction enum of vector.
	 */
	public Direction getDirection() {
		if (this.x < 0) {
			return Direction.WEST;
		} else if (this.x > 0) {
			return Direction.EAST;
		} else if (this.y > 0) {
			return Direction.SOUTH;
		} else if (this.y < 0) {
			return Direction.NORTH;
		} else {
			return Direction.NONE;
		}
	}
	
	public Direction getDirection(Vector v) {
		if (this.x == v.x()) {
			if (this.y < v.y()) {
				return Direction.SOUTH;
			} else if (this.y > v.y()) {
				return Direction.NORTH;
			} else {
				return Direction.NONE;
			}
		} else if (this.y == v.y()) {
			if (this.x < v.x()) {
				return Direction.EAST;
			} else if (this.x > v.x()) {
				return Direction.WEST;
			} else {
				return Direction.NONE;
			}			
		} else {
			return Direction.NONE;
		}
	}
	
	/**
	 * Adds two vectors together component-wise.
	 * @param other Vector to add to this one
	 * @return Sum of the two vectors
	 */
	public Vector add(Vector other) {
		return new Vector(this.x + other.x, this.y + other.y);
	}
	
	/**
	 * Subtracts a vector from this vector component-wise.
	 * @param other Vector to subtract from this one
	 * @return Result of subtraction
	 */
	public Vector subtract(Vector other) {
		return new Vector(this.x - other.x, this.y - other.y);
	}
	
	/**
	 * Multiplies this vector by a scalar.
	 * @param scalar Scalar to multiply by
	 * @return Result of multiplication
	 */
	public Vector multiply(int scalar) {
		return new Vector(scalar*x, scalar*y);
	}
	
	/**
	 * Returns the length of the vector.
	 * @return Length
	 */
	public Double length() {
		return Math.sqrt(x*x + y*y);
	}
	
	/**
	 * Finds the distance between the end-points of two vectors
	 * @param the other vector involved
	 * @return the distance between the end points of the two vectors
	 */
	public int distanceTo(Vector other) {
	    return (int) Math.round(Math.sqrt((this.x - other.x)*(this.x - other.x) +
	                                      (this.y - other.y)*(this.y - other.y)));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector other = (Vector) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	private int x;
	private int y;
	
}
