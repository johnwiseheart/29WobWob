import javax.swing.JFrame;


public class GameRunner {
    private GameRunner() {
        frame = new GameFrame();
        frame.setFocusable(true);
    }

    public static void main(String[] args) {
        new GameRunner();
    }

    

    private JFrame frame;
    
}





