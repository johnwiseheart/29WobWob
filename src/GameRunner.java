import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameRunner {
	GameFrame frame;
	JPanel menuPanel;
	JPanel gamePanel;
	static Font joystix = null;
	final static Color ourGreen = new Color(0xA1FF9C);
	Thread thr1;

    private GameRunner() {
        gameState = null;

    }

    public static void main(String[] args) {
		try{
	    	InputStream is = new FileInputStream("font/joystix.ttf");
	    	joystix = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (Exception e) {
		}

        new GameRunner().runMenu();
    }

    private void runMenu() {

    	frame = new GameFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.black);
        frame.setSize(800, 600);

		menuPanel = new JPanel();
		menuPanel.setBackground(Color.black);
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));

		JLabel wobman = makeImageLabel("img/wobby.png", -1, -1);
		wobman.setBorder(BorderFactory.createEmptyBorder(70,0,100,0));
		menuPanel.add(wobman);

    	JButton button = makeButton("Play", 36);
		button.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	  frame.remove(menuPanel);
            	  runGame();
               }
            });
    	menuPanel.add(button);

    	button = makeButton("Options", 36);
		button.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   //TODO: Add a options interface
               }
            });

    	menuPanel.add(button);

    	button = makeButton("Controls", 36);
		button.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   //TODO: Add a controls interface
               }
            });

    	menuPanel.add(button);

    	button = makeButton("Exit", 36);
		button.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   System.exit(0);
               }
            });

    	menuPanel.add(button);

        frame.add(menuPanel);

        frame.setVisible(true);

    }

    private void runGame() {
    	gamePanel = new JPanel();
    	gamePanel.setBackground(Color.black);

    	BoxLayout boxLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS); // top to bottom
    	frame.setLayout(boxLayout);

    	JPanel buttonPanel = new JPanel();
    	buttonPanel.setBackground(Color.black);
    	buttonPanel.setLayout(new FlowLayout());

    	JPanel leftPanel = new JPanel();
    	leftPanel.setBackground(Color.black);
    	leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

    	leftPanel.add(makeButton("Pause", 20));
    	JButton button = makeButton("Menu", 20);

    	button.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {

            	   if (thr1.isAlive())
            		   thr1.interrupt();

            	   runMenu();

               }
            });
    	leftPanel.add(button);

    	buttonPanel.add(leftPanel);
    	buttonPanel.add(makeImageLabel("img/wobby.png", 337, 62));

    	JPanel rightPanel = new JPanel();
    	rightPanel.setBackground(Color.black);
    	rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

    	rightPanel.add(makeLabel("Score", 20));
    	rightPanel.add(makeButton("3889", 20));
    	buttonPanel.add(rightPanel);

    	gamePanel.add(buttonPanel);

    	MazeDisplay mazeDisplay = new MazeDisplay();
        gameState = new GameState(31, 19, mazeDisplay);
    	gamePanel.add(mazeDisplay);


    	frame.add(gamePanel);
    	frame.repaint();
        frame.setVisible(true);

        Runnable r1 = new Runnable (){
        	private volatile boolean execute;
          	public void run() {
          		this.execute = true;
          		while (execute) {
          		    gameState.tickPlayer();
          		    gameState.tickEnemies();
          		    try {
          		        Thread.sleep(200);
          		    } catch (InterruptedException e) {
          		    	execute = false;
          		    }
          		}
          	}

        };
        thr1 = new Thread(r1);
        thr1.start();

      // we tick in a new thread so we can do other stuff


      // Game update loop.

    }
    
    // TODO: menus have different actions for buttons.
    public class GameFrame extends JFrame {
        private class MyDispatcher implements KeyEventDispatcher {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                	switch (e.getKeyCode()) {
                		case KeyEvent.VK_LEFT:
                			//System.out.println("Left");
                			gameState.setPlayerV(-1, 0);
                			break;
                		case KeyEvent.VK_RIGHT:
                			//System.out.println("Right");
                			gameState.setPlayerV(1, 0);
                			break;
                		case KeyEvent.VK_UP:
                			//System.out.println("Up");
                			gameState.setPlayerV(0, -1);
                			break;
                		case KeyEvent.VK_DOWN:
                			//System.out.println("Down");
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

    // TODO: stolen off the internet we need to rewrite this
    public static BufferedImage resizeImage( Image image, int width, int height) {

    	final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();

        return bufferedImage;
    }


    public static JButton makeButton(String text, float font_size) {
    	JButton button = new JButton(text);
    	button.setAlignmentX(Component.CENTER_ALIGNMENT);
    	button.setFont(joystix.deriveFont(font_size));
    	button.setForeground(ourGreen);
    	button.setBackground(Color.black);
    	button.setOpaque(true);
    	button.setBorderPainted(false);
    	return button;
    }

    public static JLabel makeLabel(String text, float font_size) {
    	JLabel label = new JLabel(text);
    	label.setAlignmentX(Component.CENTER_ALIGNMENT);
    	label.setFont(joystix.deriveFont(font_size));
    	label.setForeground(ourGreen);
    	label.setBackground(Color.black);
    	label.setOpaque(true);
    	return label;
    }

    public static JLabel makeImageLabel(String fileName, int height, int width) {
    	BufferedImage image = null;
    	JLabel wobman = null;
    	try {
    		image = ImageIO.read(new File(fileName));
    		if(height!=-1)
    			wobman = new JLabel(new ImageIcon(resizeImage(image, height, width)));
    		else
    			wobman = new JLabel(new ImageIcon(image));
    		wobman.setAlignmentX(Component.CENTER_ALIGNMENT);

    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return wobman;
    }

    GameState gameState;
}





