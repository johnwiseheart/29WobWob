
public class Options {

	public Options() {
		this.effects = true;
		this.music = true;
		this.difficulty = EASY;
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
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	private boolean effects;
	private boolean music;
	private int difficulty;
	public final static int EASY = 0;
	public final static int MEDIUM = 1;
	public final static int HARD = 2;
	public final static int IMPOSSIBLE = 3;
}
