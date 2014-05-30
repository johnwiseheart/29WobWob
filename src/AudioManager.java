import java.io.File;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 * Manages all sounds that can be played in the game.
 */
public class AudioManager {

	public AudioManager() {
		// Create a clip for every sound.
	    clips = new HashMap<ClipName, Clip>();
	    clips.put(ClipName.DIE, load("music/dieing.wav"));
	    clips.put(ClipName.DOT, load("music/kk.wav"));
	    clips.put(ClipName.KEY, load("music/keypickup.wav"));
	    clips.put(ClipName.MENU, load("music/wob2.wav"));
	    clips.put(ClipName.GAME, load("music/game2.wav"));
	    clips.put(ClipName.LEVELUP, load("music/levelup.wav"));
	    clips.put(ClipName.END, load("music/end.wav"));
	}

	/**
	 * Loads a piece of audio into a clip.
	 * 
	 * @param file_name
	 *            The location and name of the file in the local file system.
	 * @return a clip containing the audio in that file.
	 */
	public Clip load(String file_name) {
		File file = new File(file_name);
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(file);
			AudioFormat format = audio.getFormat();
			Clip clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class,
					                          format));
		    clip.open(audio);
		    return clip;
		} catch (Exception e) {
		    return null;
		}
	}

	/**
	 * Starts a clip playing.
	 * @param name the clip to play
	 * @param loop
	 *            Whether or not the clip should loop
	 */
	public void play(ClipName name, boolean loop) {
	    Clip clip = clips.get(name);
		if (loop) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
		    play(name);
		}
	}
	
	/**
     * Starts a clip playing
     * @param name the clip to play
     */
	public void play(ClipName name) {
	    Clip clip = clips.get(name);
	    
	    // Java is platform independent, right?
	    if (System.getProperty("os.name").equals("Linux")) {
	        if (clip.isActive()) {
	            clip.loop(1);
	        } else {
	            clip.loop(0);
	        }
	    } else {
	        clip.stop();
	        clip.start();
	    }
	}

	/**
	 * Stops a clip from playing
	 * @param name the clip to stop
	 */
	public void stop(ClipName name) {
	    Clip clip = clips.get(name);
	    if (clip.isActive()) {
	        clip.stop();
	    }
	}
	
	public enum ClipName {
	    DIE,
	    DOT,
	    KEY,
	    MENU,
	    GAME,
	    LEVELUP,
	    END
	}

	private HashMap<ClipName, Clip> clips;
}
