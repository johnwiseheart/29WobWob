import javax.swing.UIManager;

public class GameRunner {

	/**
	 * Our main simply try's to make our game look the best it can on different os's by getting their specific look and feel
	 * classes. Then creates a new GameFrame and starts the whole system.
	 * @param args
	 */
    public static void main(String[] args) {
    	// this is required to make the menu bar change name on OSX
    	System.setProperty("apple.laf.useScreenMenuBar", "true");
    	System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Wobman");
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        GameFrame frame = new GameFrame();
        frame.startGame();
    }
    
}





