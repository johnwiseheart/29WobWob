import java.awt.Color;
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
    	buttonPanel = new JPanel();
    	JButton button = new JButton("Play");
    	buttonPanel.add(button);    	
    	
    	frame = new GameFrame();
        frame.setBackground(Color.black);
        //frame.setLayout(new FlowLayout());
        
        MazeDisplay mazeDisplay = new MazeDisplay();
        gameState = new GameState(7, 30, mazeDisplay);
        
        //frame.add(buttonPanel);
        frame.add(mazeDisplay);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
 
        // Game update loop.
        while (true) {
            gameState.tickPlayer();
            gameState.tickEnemies();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // Doesn't happen.
            }
        }
    }
    
    public class GameFrame extends JFrame {    
        private class MyDispatcher implements KeyEventDispatcher {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                	switch (e.getKeyCode()) {
                		case KeyEvent.VK_LEFT:
                			System.out.println("Left");
                			gameState.setPlayerV(-1, 0);
                			break;
                		case KeyEvent.VK_RIGHT:
                			System.out.println("Right");
                			gameState.setPlayerV(1, 0);
                			break;
                		case KeyEvent.VK_UP:
                			System.out.println("Up");
                			gameState.setPlayerV(0, -1);
                			break;
                		case KeyEvent.VK_DOWN:
                			System.out.println("Down");
                			gameState.setPlayerV(0, 1);
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
