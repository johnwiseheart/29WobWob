import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 * @author Lucas & John
 *
 */
public class GameFrame extends JFrame implements Observer {

    private static final long serialVersionUID = 1L;

    //TODO: Make sure game keeps farting
	public GameFrame() {
		
		try {
            InputStream is = new FileInputStream("font/joystix.ttf");
            joystix = Font.createFont(Font.TRUETYPE_FONT, is);
            
            HashMap<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            joystixul = joystix.deriveFont(attributes);
        } catch (Exception e) {
        }
		audioManager = new AudioManager();
		options = new Options();
		setTitle("Wobman the Key Master");
		setMinimumSize(new Dimension(800,600));
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.black);
        getContentPane().setBackground(Color.black);
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    manager.addKeyEventDispatcher(new GameKeyDispatcher());   
	}
	
	/**
	 * Starter function that is called to start the visual part of this game.
	 */
	public void startGame() {
		runMenu();
		audioManager.play(AudioManager.ClipName.MENU, true);
	}
	
	/**
	 * Our implementation of KeyEvenDispatcher that handles keyboard input for our game.
	 * It only handles 8 keys, the arrow keys and wasd as this is all we use.
	 * @author Group
	 *
	 */
	private class GameKeyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
        	if (gameState != null) {
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
        	}
        	return false;
        }
    }
	
	/**
	 * Generates a menu JPanel which contains the menu title and buttons and adds it to the GameFrame then sets to visible.
	 */
	private void runMenu() {
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

    	JButton playButton = makeButton("Play", joystix, 36);
		playButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	  getContentPane().removeAll();
            	  audioManager.stop(AudioManager.ClipName.MENU);
            	  runGame(null);
               }
            });
		playButton.setBackground(Color.black);
    	menuPanel.add(playButton);
    	
    	JButton loadButton = makeButton("Load game", joystix, 36);
		loadButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	  //getContentPane().removeAll();
            	  //menuMusic.stop();
            	  //runGame();
            	  showLoadDialog();
               }
            });
		
    	menuPanel.add(loadButton);

    	JButton optionsButton = makeButton("Options", joystix, 36);
		optionsButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runOptions();
               }
            });

    	menuPanel.add(optionsButton);

    	JButton controlsButton = makeButton("Controls", joystix, 36);
		controlsButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runControls();
               }
            });

    	menuPanel.add(controlsButton);

    	JButton exitButton = makeButton("Quit", joystix, 36);
		exitButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   System.exit(0);
               }
            });

    	menuPanel.add(exitButton);
        add(menuPanel);
        repaint();
        setVisible(true);
    }
	/**
	 * 
	 */
	private void runFlash() {
		JPanel buzzPanel = new JPanel();
		buzzPanel.setBackground(Color.black);
		buzzPanel.setLayout(new BoxLayout(buzzPanel, BoxLayout.PAGE_AXIS));

		JLabel wobman = makeImageLabel("img/wobman.png", -1, -1);
		wobman.setBorder(BorderFactory.createEmptyBorder(70,0,20,0));
		buzzPanel.add(wobman);
		//TODO: fix the image for wobby
		JLabel keymaster = makeLabel("BUZZ WORD", 32f);
		keymaster.setBorder(BorderFactory.createEmptyBorder(0,0,80,0));
		buzzPanel.add(keymaster);

    	buzzPanel.setBounds(0,0,getWidth(), getHeight());
        add(buzzPanel);
        repaint();
        setVisible(true);
    }
	
	/**
	 * Generates the game screen JPanel which has the scoring panel and the MazePanel in it,
	 * then adds it to the GameFrame and sets visible to true. 
	 * This also involves setting up the whole GameState. This involves either creating a new GameState or loading
	 * one if specified in savedState. Sets up GameState to have the Observers this and MazePanel so that on any
	 * change to the state they can be updated.
	 * It also starts a tickThread, which will tick the player and enemies many times per second to keep their positions updated.
	 * 
	 * @param savedState A saved GameState to start with as opposed to a newly generated one.
	 */
    public void runGame(GameState savedState) {
    	
    	BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS); // top to bottom
    	this.setLayout(boxLayout);
    	
    	JPanel metaGamePanel = new JPanel();
    	metaGamePanel.setBackground(Color.black);

    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	buttonPanel.setBackground(Color.black);   	

    	JPanel menuPanel = new JPanel();
    	menuPanel.setBackground(Color.black);
    	menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));

    	JButton pauseButton = makeButton("Pause", joystix, 20);
    	pauseButton.addActionListener(new
                ActionListener() {
                   public void actionPerformed(ActionEvent event) {
                	   if (tickThread.isAlive()) {
                		   tickThread.interrupt();
                	   }
                	   audioManager.stop(AudioManager.ClipName.GAME);
                	   if (options.isMusic()) {
                	       audioManager.play(AudioManager.ClipName.MENU, true);
                	   }
                	   getContentPane().removeAll();
                	   runPauseFrame();
                   }
                });
    	menuPanel.add(pauseButton);
    	JButton menuButton = makeButton("Menu", joystix, 20);

    	menuButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   if (tickThread.isAlive())
            		   tickThread.interrupt();
            	   getContentPane().removeAll();
            	   audioManager.stop(AudioManager.ClipName.GAME);
            	   if (options.isMusic()) {
            	       audioManager.play(AudioManager.ClipName.MENU, true);
            	   }
            	   runMenu();
               }
            });
    	
    	menuPanel.add(menuButton);
    	buttonPanel.add(menuPanel);
    	
    	JPanel livePanel = new JPanel();
    	livePanel.setBackground(Color.black);
    	livePanel.setLayout(new BoxLayout(livePanel, BoxLayout.PAGE_AXIS));

    	livePanel.add(makeLabel("Lives", 20));
    	livesLabel = makeLabel("8", 20);
    	livePanel.add(livesLabel);
    	
    	buttonPanel.add(livePanel);
    	buttonPanel.add(makeImageLabel("img/wobman.png", 337, 62));

    	JPanel scorePanel = new JPanel();
    	scorePanel.setBackground(Color.black);
    	scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.PAGE_AXIS));

    	scorePanel.add(makeLabel("Score", 20));
    	scoreLabel = makeLabel("0", 20);
    	scorePanel.add(scoreLabel);
    	
    	
    	buttonPanel.add(scorePanel);
    	
    	JPanel levelPanel = new JPanel();
    	levelPanel.setBackground(Color.black);
    	levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.PAGE_AXIS));

    	levelPanel.add(makeLabel("Level", 20));
    	levelLabel = makeLabel("8", 20);
    	levelPanel.add(levelLabel);
    	
    	buttonPanel.add(levelPanel);

    	metaGamePanel.add(buttonPanel);
        
        metaGamePanel.setMaximumSize(new Dimension(1000,100));
    	
    	mazeDisplay = new MazePanel();
    	
    	if (savedState != null) {
    		System.out.println("Using saved state");
    		gameState = savedState;
    	} else {
    		gameState = new GameState(options.getDifficulty());
    	}

        gameState.addObserver(mazeDisplay);
        gameState.addObserver(this);
        //mazeDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
    	gamePanel = new JPanel();
    	gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.PAGE_AXIS));
    	gamePanel.setBackground(Color.black);
    	gamePanel.add(metaGamePanel);
    	gamePanel.add(mazeDisplay);
   
    	this.add(gamePanel);
        
        newTickThread();
        tickThread.start();
        
        if (options.isMusic()) {
            audioManager.play(AudioManager.ClipName.GAME, true);
        }
	   
	    mazeDisplay.repaint();
	    this.repaint();
        this.setVisible(true);
    }
    
    //TODO: this is bad
    /**
     * 
     */
    private void newTickThread() {
    	Runnable r1 = new Runnable (){
        	private volatile boolean execute;
        	private int tick = 0; // counts frames in between character moves
          	public void run() {
          		
          		this.execute = true;
          		while (execute) {
          		    
          		    tick++;
          		    if(tick == 25) {
          		    	// update characters and reset tick
          		    	tick = 0;
          		    	gameState.updateCharacters();
          		    }
          		    
          		    // update display with the current tick
          		    gameState.refreshDisplay(tick);
          		    
          		    try {
          		        Thread.sleep(8);
          		    } catch (InterruptedException e) {
          		    	execute = false;
          		    }
          		}
          	}
        };
        tickThread = new Thread(r1);
    }
    
    /**
     * 
     */
    private void runEndGame() {
    	JPanel menuPanel = new JPanel();
		menuPanel.setBackground(Color.black);
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));

		JLabel gameOver = makeLabel("GAME OVER", 64f);
		gameOver.setBorder(BorderFactory.createEmptyBorder(80,0,80,0));
		menuPanel.add(gameOver);
		
		endScoreLabel = makeLabel("Score: "+ score, 32f);
		menuPanel.add(endScoreLabel);
		
		JLabel highScoreLabel = makeLabel("NEW HIGH SCORE", 32f);
		highScoreLabel.setBorder(BorderFactory.createEmptyBorder(0,0,80,0));
		menuPanel.add(highScoreLabel);

    	JButton restartButton = makeButton("Restart", joystix, 36);
    	restartButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	  getContentPane().removeAll();
            	  audioManager.stop(AudioManager.ClipName.MENU);
            	  runGame(null);
               }
            });
		
    	menuPanel.add(restartButton);

    	JButton menuButton = makeButton("Menu", joystix, 36);
    	menuButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runMenu();
               }
            });

    	menuPanel.add(menuButton);

    	JButton quitButton = makeButton("Quit", joystix, 36);
    	quitButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   System.exit(0);
               }
            });

    	menuPanel.add(quitButton);

        add(menuPanel);
        repaint();
        setVisible(true);
    }
    
    /**
     * 
     */
    private void runPauseFrame() {

    	JPanel pausePanel = new JPanel();
    	//pausePanel.setMinimumSize(new Dimension(getWidth(), getHeight()));
    	//pausePanel.setBounds(0, 0, getWidth(), getHeight());
    	//pausePanel.setBorder(new EmptyBorder(200, 200, 200, 200));
    	pausePanel.setBackground(Color.black);
    	pausePanel.setLayout(new BoxLayout(pausePanel, BoxLayout.Y_AXIS)); // top to bottom


    	JLabel heading = makeLabel("Paused", 50);
    	heading.setBorder(BorderFactory.createEmptyBorder(70,0,30,0));
    	pausePanel.add(heading);
    	
    	JButton resumeButton = makeButton("menu", joystix, 36);
    	resumeButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runMenu();
               }
            });
    	pausePanel.add(resumeButton);

    	JButton menuButton = makeButton("resume", joystix, 36);
    	menuButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   //TODO: this is bad
            	   getContentPane().removeAll();
            	   getContentPane().add(gamePanel);
            	   getContentPane().repaint();
            	   getContentPane().setVisible(true);
            	   newTickThread();
            	   tickThread.start();
            	   audioManager.stop(AudioManager.ClipName.MENU);
            	   if (options.isMusic()) {
            	       audioManager.play(AudioManager.ClipName.GAME, true);
            	   }
               	}
            });
    	pausePanel.add(menuButton);
    	
    	JButton saveButton = makeButton("save", joystix, 36);
    	saveButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   showSaveDialog();
               }
            });
    	pausePanel.add(saveButton);
    	
    	JPanel musicOptions = genMusicOptions();
		musicOptions.setBackground(Color.black);
    	pausePanel.add(musicOptions);
    	
    	JPanel effectsOptions = genEffectsOptions();
		effectsOptions.setBackground(Color.black);
    	pausePanel.add(effectsOptions);
    	
        this.add(pausePanel);
        this.repaint();
        this.setVisible(true);
    	
    }
    
    /**
     * 
     * @return
     */
    private JPanel genMusicOptions() {
    	JPanel musicOptions = new JPanel(); // New Panel for music options.
		musicOptions.setLayout(new FlowLayout());

    	JLabel musicLabel = makeLabel("music:", 36);
		musicOptions.add(musicLabel);
    	JButton musicOnButton = makeButton("on", joystixul, 36);
		musicOnButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   Object source = event.getSource();
                   if (source instanceof JButton) {
                       JButton btn = (JButton)source;
                       btn.setFont(joystixul.deriveFont(36f));
                   }
                   // TODO: OK SO WTF, can't think of a good way to do this, WE HAVE TO GO TOO DEEP.
            	   for(Component component : getContentPane().getComponents()) {
            		   if (component instanceof JPanel) {
            			   JPanel panel = (JPanel) component;
            			   for (Component c : panel.getComponents()) {
            				   if (c instanceof JPanel) {
            					   JPanel p = (JPanel) c;
	            				   for (Component comp : p.getComponents()) {
		            				   if (comp instanceof JButton) {
		            					   JButton btn = (JButton) comp;
		            					   if(btn.getText().equals("off")) {
		                    				   btn.setFont(joystix.deriveFont(36f));
		                    			   }
		            				   }
	            				   }
            			   	   }
            			   }
            		   }
            	   }
            	   
            	   options.setMusic(true);
            	   audioManager.play(AudioManager.ClipName.MENU, true);
               }
            });
		musicOptions.add(musicOnButton);
    	JButton musicOffButton = makeButton("off", joystix, 36);
		musicOffButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   Object source = event.getSource();
                   if (source instanceof JButton) {
                       JButton btn = (JButton)source;
                       btn.setFont(joystixul.deriveFont(36f));
                       
                   }
                   
                   
                // TODO: OK SO WTF, can't think of a good way to do this, WE HAVE TO GO TOO DEEP.
            	   for(Component component : getContentPane().getComponents()) {
            		   if (component instanceof JPanel) {
            			   JPanel panel = (JPanel) component;
            			   for (Component c : panel.getComponents()) {
            				   if (c instanceof JPanel) {
            					   JPanel p = (JPanel) c;
	            				   for (Component comp : p.getComponents()) {
		            				   if (comp instanceof JButton) {
		            					   JButton btn = (JButton) comp;
		            					   if(btn.getText().equals("on")) {
		                    				   btn.setFont(joystix.deriveFont(36f));
		                    			   }
		            				   }
	            				   }
            			   	   }
            			   }
            		   }
            	   }
                   
            	   options.setMusic(false);
            	   audioManager.stop(AudioManager.ClipName.MENU);
               }
            });
		musicOptions.add(musicOffButton);
		
		return musicOptions;
    }
    
    private JPanel genEffectsOptions() {
    	JPanel effectsOptions = new JPanel();
    	effectsOptions.setLayout(new FlowLayout());

    	JLabel effectsLabel = makeLabel("sfx:", 36);
		effectsOptions.add(effectsLabel);
    	JButton effectsOnButton = makeButton("on", joystixul, 36);
		effectsOnButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   options.setEffects(true);
               }
            });
		effectsOptions.add(effectsOnButton);
    	JButton effectsOffButton = makeButton("off", joystix, 36);
		effectsOffButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   options.setEffects(false);
               }
            });
		effectsOptions.add(effectsOffButton);
		
		return effectsOptions;
    }
    
    private void runOptions() {
    	//TODO: Indication of which option is pressed
    	//TODO: Save options to a file
    	
		JPanel optionsPanel = new JPanel();
		optionsPanel.setBackground(Color.black);
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));

		JLabel heading = makeLabel("Options", 50);
		
		heading.setBorder(BorderFactory.createEmptyBorder(30,0,50,0));
		optionsPanel.add(heading);

		
		JPanel musicOptions = genMusicOptions();
		musicOptions.setBackground(Color.black);
    	optionsPanel.add(musicOptions);
    	
    	JPanel effectsOptions = genEffectsOptions();
		effectsOptions.setBackground(Color.black);
    	optionsPanel.add(effectsOptions);
		
    	JPanel difficultyOptions = new JPanel(); // New Panel for music options.
    	difficultyOptions.setLayout(new FlowLayout());

    	JLabel difficultyLabel = makeLabel("diff:", 36);
    	difficultyOptions.add(difficultyLabel);
    	JButton difficultyEasyButton = makeButton("easy", joystixul, 36);
    	
    	difficultyEasyButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   options.setDifficulty(Options.DifficultyType.EASY); 
               }
            });
		difficultyOptions.add(difficultyEasyButton);
		
    	JButton difficultyMedButton = makeButton("med", joystix, 36);
    	difficultyMedButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   options.setDifficulty(Options.DifficultyType.MEDIUM); 
               }
            });
    	difficultyOptions.add(difficultyMedButton);
    	
    	JButton difficultyHardButton = makeButton("hard", joystix, 36);
    	difficultyHardButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   options.setDifficulty(Options.DifficultyType.HARD); 
               }
            });
    	difficultyOptions.add(difficultyHardButton);
    	
		difficultyOptions.setBackground(Color.black);
    	optionsPanel.add(difficultyOptions);
    	
    	JButton backButton = makeButton("Back", joystix, 36);
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

		JPanel optionsPanel = new JPanel();
		optionsPanel.setBackground(Color.black);
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));

		JLabel heading = makeLabel("Controls", 50);
		heading.setBorder(BorderFactory.createEmptyBorder(30,0,70,0));
		optionsPanel.add(heading);

		JLabel controls = makeImageLabel("img/controls.png", -1, -1);
		controls.setBorder(BorderFactory.createEmptyBorder(0,0,70,0));
		optionsPanel.add(controls);
		
    	JButton backButton = makeButton("Back", joystix, 36);
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
    
    private final String WOBMAN_FILE_DESC = "Wobman save file (*.wob)";
    private final String WOBMAN_FILE_EXT = "wob";
    
    private void showLoadDialog() {
    	JFileChooser fileChooser = new JFileChooser();
    	
    	FileNameExtensionFilter filter = new FileNameExtensionFilter(WOBMAN_FILE_DESC, WOBMAN_FILE_EXT);
    	fileChooser.setFileFilter(filter);
    	
    	if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
    		File f = fileChooser.getSelectedFile();
    		String path = f.getAbsolutePath();
    		
    		GameState state = SaveManager.loadGame(path);
    		
    		if (state != null) {
    			getContentPane().removeAll();
    			audioManager.stop(AudioManager.ClipName.MENU);
          	  	
          	  	runGame(state);
    		} else {
    			// TODO: chuck a fit: didn't load properly
    			JOptionPane.showMessageDialog(this, "Save file is damaged!");
    		}
    	}  	
    }
    
    private void showSaveDialog() {
    	JFileChooser fileChooser = new JFileChooser();
    	
    	FileNameExtensionFilter filter = new FileNameExtensionFilter(WOBMAN_FILE_DESC, WOBMAN_FILE_EXT);
    	fileChooser.setFileFilter(filter);
    	
    	if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
    		File f = fileChooser.getSelectedFile();
    		String path = f.getAbsolutePath();
    		
    		// append file extension
    		if (!path.endsWith("." + WOBMAN_FILE_EXT)) {
    			path += "." + WOBMAN_FILE_EXT;
    		}
    		
    		SaveManager.saveGame(gameState, path);
    	}
    }
    
    public void updateScore(int score) {
    	scoreLabel.setText(score + "");
    	
    }
    
    public void updateLives(int lives) {
    	livesLabel.setText(lives + "");
    }
    
    public void updateLevel(int level) {
    	levelLabel.setText(level + "");
    }
    
    // TODO: stolen off the Internet we need to rewrite this
    public static BufferedImage resizeImage( Image image, int width, int height) {

    	final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();

        return bufferedImage;
    }

    public static JButton makeButton(String text, Font font, float font_size) {
    	JButton button = new JButton(text);
    	button.setAlignmentX(Component.CENTER_ALIGNMENT);
    	button.setFont(font.deriveFont(font_size));
    	button.setForeground(ourGreen);
    	button.setBackground(Color.black);
    	button.setOpaque(true);
    	button.setBorderPainted(false);
    	
    	return button;
    }

    public  static JLabel makeLabel(String text, float font_size) {
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

	@Override
	public void update(Observable arg0, Object arg1) {
		GameState gs = (GameState) arg0;
		if(gs!=null) {
			if (gs.getTick() == 0) {
				// character positions have updated, therefore interesting stuff can happen!
				
				if(gs.gameFinished()) {
				    if (tickThread.isAlive()) {
	                    tickThread.interrupt();
	                }
				    // TODO: the audio chucks a shit here for some reason.
	                audioManager.stop(AudioManager.ClipName.GAME);
	                if (options.isMusic()) {
	                    audioManager.play(AudioManager.ClipName.MENU, true);
	                }
		      	    getContentPane().removeAll();
		      	    runEndGame();
				}
				if (options.isEffects()) {
	    			if (gs.lastCollected()==CellType.DOT) {
	    			    audioManager.play(AudioManager.ClipName.DOT);
	    			}
	    			if (gs.lastCollected()==CellType.KEY) {
	    			    audioManager.play(AudioManager.ClipName.KEY);
	    			}
	    			if (gs.hasDied()) {
	    			    audioManager.play(AudioManager.ClipName.DIE);
	    			}
				}
				
				score = gs.getScore();
				updateScore(score);
				updateLevel(gs.getLevel());
				updateLives(gs.getLives());
			}
		}
		repaint();
	}
	
    AudioManager audioManager;
    public static Font joystix = null;
    public static Font joystixul = null;
    private final static Color ourGreen = new Color(0xA1FF9C);
    private Thread tickThread;
    private GameState gameState;
    private MazePanel mazeDisplay;
    private JPanel gamePanel;
    private Options options;
    
    private JLabel livesLabel;
    private JLabel scoreLabel;
    private JLabel levelLabel;
    private JLabel endScoreLabel;
    private int score;
}
