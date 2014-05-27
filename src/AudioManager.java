import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 * Represents and manages a piece of audio that can be played in the game.
 * 
 * @author john
 * 
 */
public class AudioManager {

	public AudioManager(String file_name) {
		load(file_name);
	}

	/**
	 * Loads a piece of audio into the clip.
	 * 
	 * @param file_name
	 *            The location and name of the file in the local file system.
	 */
	public void load(String file_name) {
		File file = new File(file_name);
		AudioInputStream audio;
		try {
			audio = AudioSystem.getAudioInputStream(file);
			AudioInputStream soundIn = AudioSystem.getAudioInputStream(file);
			AudioFormat format = soundIn.getFormat();
			clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class,
					format));
		    clip.open(audio);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Starts the clip playing.
	 * 
	 * @param loop
	 *            Whether or not the clip should loop
	 */
	public void play(boolean loop) {
		if (clip.isRunning())
			return;
		if (loop)
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		clip.start();
	}
	
	public void play() {
		clip.start();
	}

	/**
	 * Stops the clip from playing
	 */
	public void stop() {
		clip.stop();
	}

	private Clip clip;
}
