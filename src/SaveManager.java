import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Contains functions that aid in loading and saving data to and from
 * the file system for persistence of game states and options.
 * @author george
 *
 */
public class SaveManager {

	/**
	 * Saves a game state to a file.
	 * @param state Game state
	 * @param path File path
	 * @return Whether saving was successful
	 */
	public static boolean saveGame(GameState state, String path) {		
		FileOutputStream stream;
		
		try {
			stream = new FileOutputStream(path);
		} catch (FileNotFoundException e) {
			return false;
		}
		
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(stream);
			out.writeObject(state);
			out.close();
			stream.close();
			
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * Loads a game state from a file.
	 * @param path File path
	 * @return Game state stored in the file (null if there was an error loading)
	 */
	public static GameState loadGame(String path) {
		FileInputStream stream;
		
		try {
			stream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			return null;
		}
		
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(stream);
			Object obj = in.readObject();
			
			in.close();
			stream.close();
			
			if (GameState.class.isInstance(obj)) {
				GameState state = (GameState)obj;
				return state;
			}
		} catch (Exception e) {
			return null;
		}
		
		return null;
	}
	
}
