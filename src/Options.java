/**
 * This class represents the different options that can be set by the user for the game.
 * It stores them while the program is running.
 * @author Lucas
 */
public class Options {
	
	/**
	 * By default all music and effects are set to be on and difficulty is set to easy.
	 */
	public Options() {
		this.effects = true;
		this.music = true;
		this.difficulty = DifficultyType.EASY;
	}
	
	/**
	 * Returns whether or not sound effects are currently on.
	 * @return Boolean true if effects are on, false if off.
	 */
	public boolean isEffects() {
		return effects;
	}
	
	public void setEffects(boolean effects) {
		this.effects = effects;
	}
	
	/**
	 * Returns whether or not music is currently on.
	 * @return Boolean true if music is on, false if off.
	 */
	public boolean isMusic() {
		return music;
	}
	
	public void setMusic(boolean music) {
		this.music = music;
	}
	
	public DifficultyType getDifficulty() {
		return difficulty;
	}
	
	/**
	 * Returns whether or not difficulty equals a given DifficultyType.
	 * @return Boolean true if current difficulty is the given DifficultyType, false if not.
	 */
	public boolean isDifficulty(DifficultyType diff) {
		return difficulty.equals(diff);
	}
	
	public void setDifficulty(DifficultyType difficulty) {
		this.difficulty = difficulty;
	}

	private boolean effects;
	private boolean music;
	private DifficultyType difficulty;
	public enum DifficultyType {
		EASY,
		MEDIUM,
		HARD
	}
}
