import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class GameRunner {
	GameFrame frame;
	JPanel menuPanel;
	JPanel gamePanel;
	
    private GameRunner() {
        gameState = null;
    }
    
    public static void main(String[] args) {
        new GameRunner().runMenu();
    }
    
    private void runMenu() {
    	
    	//add our font
    	Font joystix = null;
		try{
	    	InputStream is = new FileInputStream("joystix.ttf");
	    	joystix = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (Exception e) {
			
		}
		joystix = joystix.deriveFont(36f);
    	
		//create our green color
		Color ourGreen = new Color(0xA1FF9C);
		
		
		menuPanel = new JPanel();
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("wobby.png"));
			JLabel wobman = new JLabel(new ImageIcon(image));
			wobman.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			
			
			Border paddingBorder = BorderFactory.createEmptyBorder(70,0,100,0);
			wobman.setBorder(paddingBorder);
			
			menuPanel.add(wobman);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	
    	JButton button = new JButton("Play");
    	button.setFont(joystix);
    	button.setForeground(ourGreen);
    	button.setBackground(Color.black);
    	button.setOpaque(true);
    	button.setBorderPainted(false);
    	
		button.addActionListener(new
            ActionListener()
            {
               public void actionPerformed(ActionEvent event)
               {
            	  frame.remove(menuPanel);
            	  runGame();
               }
            });
    	
    	button.setAlignmentX(Component.CENTER_ALIGNMENT);
    	button.setFont(joystix);
    	button.setForeground(ourGreen);
    	button.setBackground(Color.black);
    	button.setOpaque(true);
    	button.setBorderPainted(false);
    	menuPanel.add(button);
    	button = new JButton("Options");
    	button.setAlignmentX(Component.CENTER_ALIGNMENT);
    	button.setFont(joystix);
    	button.setForeground(ourGreen);
    	button.setBackground(Color.black);
    	button.setOpaque(true);
    	button.setBorderPainted(false);
    	menuPanel.add(button);
    	button = new JButton("Controls");
    	button.setAlignmentX(Component.CENTER_ALIGNMENT);
    	button.setFont(joystix);
    	button.setForeground(ourGreen);
    	button.setBackground(Color.black);
    	button.setOpaque(true);
    	button.setBorderPainted(false);
    	menuPanel.add(button);
    	button = new JButton("Exit");
    	button.setAlignmentX(Component.CENTER_ALIGNMENT);
    	button.setFont(joystix);
    	button.setForeground(ourGreen);
    	button.setBackground(Color.black);
    	button.setOpaque(true);
    	button.setBorderPainted(false);
    	menuPanel.add(button);
    	
    	menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));
    	
    	
    	frame = new GameFrame();
        frame.setBackground(Color.black);
       
        frame.add(menuPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    	
    }
    
    private void runGame() {
    	
    	gamePanel = new JPanel();
    	
    	
    	
    	MazeDisplay mazeDisplay = new MazeDisplay();
        gameState = new GameState(31, 23, mazeDisplay);

        frame.add(mazeDisplay);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
        
         
         //we tick in a new thread so we can do other stuff
        Runnable r1 = new Runnable() {
        	public void run() {
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
        };
    	Thread thr1 = new Thread(r1);
    	thr1.start();
        // Game update loop.
        
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
                		case KeyEvent.VK_P:
                			
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
