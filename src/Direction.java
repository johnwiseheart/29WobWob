/**
 * This enum is used to aid representing the directions that characters can face. Our Vectors are set up to be able to return 
 * a direction either based purely off the vector (when used as a velocity) or based off the difference of two vectors.
 * These are then, at the last step before rendering our maze, used to translate the direction a certain character is facing 
 * into the corresponding image.
 * @author Lucas
 *
 */
public enum Direction {
	NORTH,
	SOUTH,
	EAST,
	WEST,
	NONE,
}
