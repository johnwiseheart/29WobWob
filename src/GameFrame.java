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

public class GameFrame extends JFrame {

	//TODO: stop game from farting
	public GameFrame() {
		 try{
            InputStream is = new FileInputStream("font/joystix.ttf");
            joystix = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception e) {
        } 
		options = new Options();
		this.setTitle("Wobman the Key Master");
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    manager.addKeyEventDispatcher(new GameKeyDispatcher());   
	}
	
	public void startGame() {
		runMenu();
		menuMusic = new AudioManager("music/wob2.wav");
	    menuMusic.play(true);
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

		JLabel wobman = makeImageLabel("img/wobman.png", -1, -1);
		wobman.setBorder(BorderFactory.createEmptyBorder(70,0,20,0));
		menuPanel.add(wobman);
		//TODO: fix the image for wobby
		JLabel keymaster = makeLabel("The Key Master", 32f);
		keymaster.setBorder(BorderFactory.createEmptyBorder(0,0,80,0));
		menuPanel.add(keymaster);

    	JButton playButton = makeButton("Play", 36);
		playButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	  getContentPane().removeAll();
            	  menuMusic.stop();
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
    }
	
	

 
    private void runGame() {
    	JPanel gamePanel = new JPanel();
    	gamePanel.setBackground(Color.black);

    	BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS); // top to bottom
    	this.setLayout(boxLayout);

    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	buttonPanel.setBackground(Color.black);
    	

    	JPanel menuPanel = new JPanel();
    	menuPanel.setBackground(Color.black);
    	menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));

    	JButton pauseButton = makeButton("Pause", 20);
    	pauseButton.addActionListener(new
                ActionListener() {
                   public void actionPerformed(ActionEvent event) {
                	   if (tickThread.isAlive()) {
                		   //thr1.interrupt();
                	   }
                	   runPauseFrame();
                   }
                });
    	menuPanel.add(pauseButton);
    	JButton menuButton = makeButton("Menu", 20);

    	menuButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   if (tickThread.isAlive())
            		   tickThread.interrupt();
            	   getContentPane().removeAll();
            	   gameMusic.stop();
            	   menuMusic.play(true);
            	   runMenu();
               }
            });
    	
    	menuPanel.add(menuButton);
    	buttonPanel.add(menuPanel);
    	
    	JPanel livePanel = new JPanel();
    	livePanel.setBackground(Color.black);
    	livePanel.setLayout(new BoxLayout(livePanel, BoxLayout.PAGE_AXIS));

    	livePanel.add(makeLabel("Lives", 20));
    	livePanel.add(makeButton("3", 20));
    	
    	
    	
    	buttonPanel.add(livePanel);
    	buttonPanel.add(makeImageLabel("img/wobman.png", 337, 62));

    	JPanel scorePanel = new JPanel();
    	scorePanel.setBackground(Color.black);
    	scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.PAGE_AXIS));

    	scorePanel.add(makeLabel("Score", 20));
    	scorePanel.add(makeButton("3889", 20));
    	buttonPanel.add(scorePanel);
    	
    	JPanel levelPanel = new JPanel();
    	levelPanel.setBackground(Color.black);
    	levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.PAGE_AXIS));

    	levelPanel.add(makeLabel("Level", 20));
    	levelPanel.add(makeButton("8", 20));
    	
    	buttonPanel.add(levelPanel);

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
        
        gameMusic = new AudioManager("music/game.wav");
	    gameMusic.play(true);

    }
    
    // TODO ARE WORK
    private void runPauseFrame() {
    	
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.black);
        this.getContentPane().setBackground(Color.black);
        this.setSize(800, 600);
        this.setResizable(false);

    	JPanel pausePanel = new JPanel();
    	pausePanel.setBackground(Color.black);
    	pausePanel.setLayout(new BoxLayout(pausePanel, BoxLayout.PAGE_AXIS));

    	JLabel heading = makeLabel("Paused", 50);
    	heading.setBorder(BorderFactory.createEmptyBorder(70,0,70,0));
    	pausePanel.add(heading);

    	/*
    	JLabel controls = makeImageLabel("img/controls.png", -1, -1);
    	controls.setBorder(BorderFactory.createEmptyBorder(0,0,70,0));
    	optionsPanel.add(controls);
    	*/
    	
    	JButton resumeButton = makeButton("resume", 36);
    	resumeButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runMenu();
               }
            });

    	pausePanel.add(resumeButton);
        this.add(pausePanel);
        this.repaint();
        this.setVisible(true);
    	
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
		heading.setBorder(BorderFactory.createEmptyBorder(70,0,50,0));
		optionsPanel.add(heading);

		
		JPanel musicOptions = new JPanel(); // New Panel for music options.
		musicOptions.setLayout(new FlowLayout());

    	JLabel musicLabel = makeLabel("music:", 36);
		musicOptions.add(musicLabel);
    	JButton musicOnButton = makeButton("on", 36);
		musicOnButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   options.setMusic(true);
            	   //TODO: Start Music
               }
            });
		musicOptions.add(musicOnButton);
    	JButton musicOffButton = makeButton("off", 36);
		musicOffButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   options.setMusic(false);
            	   //TODO: Stop Music
               }
            });
		musicOptions.add(musicOffButton);
		musicOptions.setBackground(Color.black);
    	optionsPanel.add(musicOptions);
    	
    	JPanel effectsOptions = new JPanel(); // New Panel for music options.
		effectsOptions.setLayout(new FlowLayout());

    	JLabel effectsLabel = makeLabel("sfx:", 36);
		effectsOptions.add(effectsLabel);
    	JButton effectsOnButton = makeButton("on", 36);
		effectsOnButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   options.setEffects(true); 
               }
            });
		effectsOptions.add(effectsOnButton);
    	JButton effectsOffButton = makeButton("off", 36);
		effectsOffButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   options.setEffects(false);
               }
            });
		effectsOptions.add(effectsOffButton);
		effectsOptions.setBackground(Color.black);
    	optionsPanel.add(effectsOptions);
		
    	JPanel difficultyOptions = new JPanel(); // New Panel for music options.
    	difficultyOptions.setLayout(new FlowLayout());

    	JLabel difficultyLabel = makeLabel("diff:", 36);
    	difficultyOptions.add(difficultyLabel);
    	JButton difficultyEasyButton = makeButton("easy", 36);
    	difficultyEasyButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   options.setDifficulty(options.EASY); 
               }
            });
		difficultyOptions.add(difficultyEasyButton);
    	JButton difficultyMedButton = makeButton("med", 36);
    	difficultyMedButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   options.setDifficulty(options.MEDIUM); 
               }
            });
    	difficultyOptions.add(difficultyMedButton);
    	JButton difficultyHardButton = makeButton("hard", 36);
    	difficultyHardButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   options.setDifficulty(options.HARD); 
               }
            });
    	difficultyOptions.add(difficultyHardButton);
    	/*
    	JButton difficultyInsaneButton = makeButton("INSANE", 36);
    	difficultyInsaneButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   options.setDifficulty(options.HARD); 
               }
            });
    	difficultyOptions.add(difficultyInsaneButton);
    	*/
		difficultyOptions.setBackground(Color.black);
    	optionsPanel.add(difficultyOptions);
    	
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
		heading.setBorder(BorderFactory.createEmptyBorder(70,0,70,0));
		optionsPanel.add(heading);

		JLabel controls = makeImageLabel("img/controls.png", -1, -1);
		controls.setBorder(BorderFactory.createEmptyBorder(0,0,70,0));
		optionsPanel.add(controls);
		
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
    private static AudioManager gameMusic;
    private static AudioManager menuMusic;
    private static Font joystix = null;
    private final static Color ourGreen = new Color(0xA1FF9C);
    private Thread tickThread;
    private GameState gameState;
    private Options options;

}
