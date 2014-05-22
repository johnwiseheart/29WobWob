import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GameRunner {

    public static void main(String[] args) {
    	// this is required to make the menu bar change name on OSX
    	System.setProperty("apple.laf.useScreenMenuBar", "true");
    	System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Wobman");
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        new GameFrame();
    }
    
}





