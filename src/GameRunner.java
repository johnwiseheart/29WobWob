import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class GameRunner {
	GameFrame frame;
	JPanel buttonPanel;
	
    private GameRunner() {
        gameState = null;
    }
    
    public static void main(String[] args) {
        new GameRunner().runGame();
    }
    
    private void runGame() {
    	
    	GridBagLayout gbl = new GridBagLayout();
    	
    	buttonPanel = new JPanel();
    	JButton button = new JButton("Play");
    	buttonPanel.add(button);    	
    	
    	frame = new GameFrame();
        frame.setBackground(Color.black);
        //frame.setLayout(new FlowLayout());
        
        
        MazeDisplay mazeDisplay = new MazeDisplay();
        gameState = new GameState(31, 23, mazeDisplay);
        
        //frame.add(buttonPanel);
        frame.add(mazeDisplay);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
 
        //press on the keyboard now
    }
    
    public class GameFrame extends JFrame {    
        private class MyDispatcher implements KeyEventDispatcher {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                	switch (e.getKeyCode()) {
                		case KeyEvent.VK_LEFT:
                			System.out.println("Left");
                			gameState.movePlayer(GameState.LEFT);
                			break;
                		case KeyEvent.VK_RIGHT:
                			System.out.println("Right");
                			gameState.movePlayer(GameState.RIGHT);
                			break;
                		case KeyEvent.VK_UP:
                			System.out.println("Up");
                			gameState.movePlayer(GameState.UP);
                			break;
                		case KeyEvent.VK_DOWN:
                			System.out.println("Down");
                			gameState.movePlayer(GameState.DOWN);
                			break;
                	} 
                }
                return false;
            }
        }
        public GameFrame() {
            KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            manager.addKeyEventDispatcher(new MyDispatcher());
        }
    }
    
    GameState gameState;
}
