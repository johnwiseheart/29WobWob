import javax.swing.JFrame;


public class GameRunner {
    private GameRunner() {
        frame = new GameFrame();
    }

    public static void main(String[] args) {
        new GameRunner();
    }
    
    private JFrame frame;
    
}





