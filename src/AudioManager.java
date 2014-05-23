import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;


public class AudioManager {
	public AudioManager(String file_name) {
		this.file_name = file_name;
		load(file_name);
	}
	public void load(String file_name) {
		this.file_name = file_name;
		File soundFile = new File(file_name);
        AudioInputStream ais;
		try {
			ais = AudioSystem.getAudioInputStream(soundFile);
			AudioInputStream soundIn = AudioSystem
			        .getAudioInputStream(soundFile);
			        AudioFormat format = soundIn.getFormat();
			        DataLine.Info info = new DataLine.Info(Clip.class, format);

			        clip = (Clip) AudioSystem.getLine(info);
			        clip.open(ais);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((file_name == null) ? 0 : file_name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AudioManager other = (AudioManager) obj;
		if (file_name == null) {
			if (other.file_name != null)
				return false;
		} else if (!file_name.equals(other.file_name))
			return false;
		return true;
	}
	public void play(boolean loop) {
		if(clip.isRunning()) {
			return;
		}
		if(loop) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
        clip.start();
	}
	
	public void stop() {
		clip.stop();
	}
	
	public boolean isPlaying() {
		return clip.isRunning();
	}
	private String file_name;
    private Clip clip;
}
