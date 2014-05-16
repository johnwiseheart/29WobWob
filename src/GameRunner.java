import javax.swing.JFrame;


public class GameRunner {

    private GameRunner() {
        gameState = null;
    }
    
    public static void main(String[] args) {
        new GameRunner().runGame();
    }
    
    private void runGame() {
        MazeDisplay mazeDisplay = new MazeDisplay();
        gameState = new GameState(23, 31, mazeDisplay);
        JFrame frame = new JFrame();
        frame.add(mazeDisplay);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(810, 825);
        frame.setVisible(true);
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            gameState.movePlayer(GameState.UP);
        }
    }
    
    GameState gameState;
}
