import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;


public class GameRunner {

    private GameRunner() {
        gameState = null;
    }
    
    public static void main(String[] args) {
        new GameRunner().runGame();
    }
    
    private void runGame() {
        MazeDisplay mazeDisplay = new MazeDisplay();
        gameState = new GameState(31, 31, mazeDisplay);
        GameFrame frame = new GameFrame();
        frame.add(mazeDisplay);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
 
        //press on the keyboard now
        
//        while (true) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            gameState.movePlayer(GameState.UP);
//        }
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
