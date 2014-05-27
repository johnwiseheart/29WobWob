
public class Options {

	public Options() {
		this.effects = true;
		this.music = true;
		this.difficulty = DifficultyType.EASY;
	}
	
	
	public boolean isEffects() {
		return effects;
	}
	
	public void setEffects(boolean effects) {
		this.effects = effects;
	}
	
	public boolean isMusic() {
		return music;
	}
	
	public void setMusic(boolean music) {
		this.music = music;
	}
	
	public DifficultyType getDifficulty() {
		return difficulty;
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
