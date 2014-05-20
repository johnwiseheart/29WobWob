/**
 * Represents a 2D quantity with integer components, such as a point in the maze, the velocity of a character, etc.
 * @author george
 *
 */
public class Vector {

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
	 * Adds two vectors together component-wise.
	 * @param other Vector to add to this one
	 * @return Sum of the two vectors
	 */
	public Vector add(Vector other) {
		return new Vector(this.x + other.x, this.y + other.y);
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
