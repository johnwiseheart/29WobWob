/**
 * This enum was created to aid in representing our maze. These are sued to represent all the different types of squares that
 * can occur in our maze. In the last step of rendering our maze these are translated using a HashMap, into the actual images 
 * to be drawn.
 * @author Group
 *
 */
public enum CellType {
	SPACE,
    DOT,
    WALL,
    DOOR,
    KEY,

    PLAYER1_E,
    PLAYER1_N,
    PLAYER1_S,
    PLAYER1_W,

    ENEMY1_E,
    ENEMY1_N,
    ENEMY1_S,
    ENEMY1_W,
    ENEMY2_E,
    ENEMY2_N,
    ENEMY2_S,
    ENEMY2_W,
    ENEMY3_E,
    ENEMY3_N,
    ENEMY3_S,
    ENEMY3_W,
    ENEMY4_E,
    ENEMY4_N,
    ENEMY4_S,
    ENEMY4_W,

    WALL_VERT,
    WALL_HOR,
    WALL_CORN_NW,
    WALL_CORN_NE,
    WALL_CORN_SW,
    WALL_CORN_SE,
    WALL_CROSS,
    WALL_T_N,
    WALL_T_S,
    WALL_T_E,
    WALL_T_W,
    WALL_END_N,
    WALL_END_S,
    WALL_END_E,
    WALL_END_W,
    WALL_BLOCK,
}
