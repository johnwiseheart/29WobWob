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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public class GameFrame extends JFrame {

	//TODO: stop game from farting
	public GameFrame() {
		 try{
            InputStream is = new FileInputStream("font/joystix.ttf");
            joystix = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception e) {
        }
		runMenu();
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    manager.addKeyEventDispatcher(new GameKeyDispatcher());
	}
	
	private class GameKeyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
        	 switch (event.getKeyCode()) {
             case KeyEvent.VK_LEFT:
             case KeyEvent.VK_A:
                 //System.out.println("Left");
                 gameState.setPlayerVelocity(-1, 0);
                 break;
             case KeyEvent.VK_RIGHT:
             case KeyEvent.VK_D:
                 //System.out.println("Right");
                 gameState.setPlayerVelocity(1, 0);
                 break;
             case KeyEvent.VK_UP:
             case KeyEvent.VK_W:
                 //System.out.println("Up");
                 gameState.setPlayerVelocity(0, -1);
                 break;
             case KeyEvent.VK_DOWN:
             case KeyEvent.VK_S:
                 //System.out.println("Down");
                 gameState.setPlayerVelocity(0, 1);
                 break;
                
             }
        	 return false;
        }
    }
	
	private void runMenu() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.black);
        this.getContentPane().setBackground(Color.black);
        this.setSize(800, 600);
        this.setResizable(false);

		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(Color.black);
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));

		JLabel wobman = makeImageLabel("img/wobby.png", -1, -1);
		wobman.setBorder(BorderFactory.createEmptyBorder(70,0,100,0));
		menuPanel.add(wobman);

    	JButton playButton = makeButton("Play", 36);
		playButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	  getContentPane().removeAll();
            	  menuMusic.interrupt();
            	  runGame();
               }
            });
		
    	menuPanel.add(playButton);

    	JButton optionsButton = makeButton("Options", 36);
		optionsButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runOptions();
               }
            });

    	menuPanel.add(optionsButton);

    	JButton controlsButton = makeButton("Controls", 36);
		controlsButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runControls();
               }
            });

    	menuPanel.add(controlsButton);

    	JButton exitButton = makeButton("Exit", 36);
		exitButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   System.exit(0);
               }
            });

    	menuPanel.add(exitButton);
        this.add(menuPanel);
        this.repaint();
        this.setVisible(true);
        
        
        
        Runnable musicRunnable = new Runnable (){
        	private volatile boolean execute;
        	InputStream in = null;
            AudioStream as = null;
            
          	public void run() {
          		this.execute = true;
          		while (execute) {
          			try{
          				in = new FileInputStream ("music/menu.wav");
          				as = new AudioStream (in);
          			} catch (FileNotFoundException e) {
          				e.printStackTrace();
          			} catch (IOException e) {
          				e.printStackTrace();
          			}
          			AudioPlayer.player.start (as); 
          	        
          		    try {
          		        Thread.sleep(16000);
          		    } catch (InterruptedException e) {
          		    	AudioPlayer.player.stop(as);
          		    	execute = false;
          		    }
          		}
          	}
        };
        
        menuMusic = new Thread(musicRunnable);
        menuMusic.start();
    }
	


 
    private void runGame() {
    	JPanel gamePanel = new JPanel();
    	gamePanel.setBackground(Color.black);

    	BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS); // top to bottom
    	this.setLayout(boxLayout);

    	JPanel buttonPanel = new JPanel();
    	buttonPanel.setBackground(Color.black);
    	buttonPanel.setLayout(new FlowLayout());

    	JPanel leftPanel = new JPanel();
    	leftPanel.setBackground(Color.black);
    	leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

    	leftPanel.add(makeButton("Pause", 20));
    	JButton menuButton = makeButton("Menu", 20);

    	menuButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   if (tickThread.isAlive())
            		   tickThread.interrupt();
            	   getContentPane().removeAll();
            	   gameMusic.interrupt();
            	   runMenu();
               }
            });
    	
    	leftPanel.add(menuButton);

    	buttonPanel.add(leftPanel);
    	buttonPanel.add(makeImageLabel("img/wobby.png", 337, 62));

    	JPanel rightPanel = new JPanel();
    	rightPanel.setBackground(Color.black);
    	rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

    	rightPanel.add(makeLabel("Score", 20));
    	rightPanel.add(makeButton("3889", 20));
    	buttonPanel.add(rightPanel);

    	gamePanel.add(buttonPanel);

    	MazeComponent mazeDisplay = new MazeComponent();
        gameState = new GameState(31, 19, mazeDisplay);
    	gamePanel.add(mazeDisplay);

    	this.add(gamePanel);
    	this.repaint();
        this.setVisible(true);

        
        Runnable r1 = new Runnable (){
        	private volatile boolean execute;
          	public void run() {
          		this.execute = true;
          		while (execute) {
          		    gameState.tickPlayer();
          		    gameState.tickEnemies();
          		    gameState.updateDisplay();
          		    try {
          		        Thread.sleep(200);
          		    } catch (InterruptedException e) {
          		    	execute = false;
          		    }
          		}
          	}
        };
        tickThread = new Thread(r1);
        tickThread.start();
        
        Runnable musicRunnable = new Runnable (){
        	private volatile boolean execute;
        	InputStream in = null;
            AudioStream as = null;
            
          	public void run() {
          		this.execute = true;
          		while (execute) {
          			try{
          				in = new FileInputStream ("music/game.wav");
          				as = new AudioStream (in);
          			} catch (FileNotFoundException e) {
          				e.printStackTrace();
          			} catch (IOException e) {
          				e.printStackTrace();
          			}
          			AudioPlayer.player.start (as); 
          	        
          		    try {
          		        Thread.sleep(8000);
          		    } catch (InterruptedException e) {
          		    	AudioPlayer.player.stop(as);
          		    	execute = false;
          		    }
          		}
          	}
        };
        
        gameMusic = new Thread(musicRunnable);
        gameMusic.start();

    }
    
    private void runOptions() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.black);
        this.getContentPane().setBackground(Color.black);
        this.setSize(800, 600);
        this.setResizable(false);

		JPanel optionsPanel = new JPanel();
		optionsPanel.setBackground(Color.black);
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));

		JLabel heading = makeLabel("Options", 50);
		heading.setBorder(BorderFactory.createEmptyBorder(70,0,100,0));
		optionsPanel.add(heading);

		
		
    	JButton backButton = makeButton("Back", 36);
		backButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runMenu();
               }
            });

    	optionsPanel.add(backButton);
        this.add(optionsPanel);
        this.repaint();
        this.setVisible(true);
    }
    
    private void runControls() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.black);
        this.getContentPane().setBackground(Color.black);
        this.setSize(800, 600);
        this.setResizable(false);

		JPanel optionsPanel = new JPanel();
		optionsPanel.setBackground(Color.black);
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));

		JLabel heading = makeLabel("Controls", 50);
		heading.setBorder(BorderFactory.createEmptyBorder(70,0,100,0));
		optionsPanel.add(heading);

		
		
    	JButton backButton = makeButton("Back", 36);
		backButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runMenu();
               }
            });

    	optionsPanel.add(backButton);
        this.add(optionsPanel);
        this.repaint();
        this.setVisible(true);
    }

    // TODO: stolen off the Internet we need to rewrite this
    private static BufferedImage resizeImage( Image image, int width, int height) {

    	final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();

        return bufferedImage;
    }

    private static JButton makeButton(String text, float font_size) {
    	JButton button = new JButton(text);
    	button.setAlignmentX(Component.CENTER_ALIGNMENT);
    	button.setFont(joystix.deriveFont(font_size));
    	button.setForeground(ourGreen);
    	button.setBackground(Color.black);
    	button.setOpaque(true);
    	button.setBorderPainted(false);
    	return button;
    }

    private static JLabel makeLabel(String text, float font_size) {
    	JLabel label = new JLabel(text);
    	label.setAlignmentX(Component.CENTER_ALIGNMENT);
    	label.setFont(joystix.deriveFont(font_size));
    	label.setForeground(ourGreen);
    	label.setBackground(Color.black);
    	label.setOpaque(true);
    	return label;
    }

    private static JLabel makeImageLabel(String fileName, int height, int width) {
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
    
    private static Font joystix = null;
    private final static Color ourGreen = new Color(0xA1FF9C);
    private Thread tickThread;
    private Thread menuMusic;
    private Thread gameMusic;
    private GameState gameState;
}
