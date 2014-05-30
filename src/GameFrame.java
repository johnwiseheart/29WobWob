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
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The frame that holds all of the game's UI.
 * @author Lucas & John
 *
 */
public class GameFrame extends JFrame implements Observer {

    private static final long serialVersionUID = 1L;

	public GameFrame() {

		 try{
            InputStream is = new FileInputStream("files/font/joystix.ttf");
            joystix = Font.createFont(Font.TRUETYPE_FONT, is);
            
            HashMap<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            joystixul = joystix.deriveFont(attributes);
        } catch (Exception e) {
        }
		audioManager = new AudioManager();
		options = new Options();
		setTitle("Wobman the Key Master");
		setMinimumSize(new Dimension(1200,750));
		setSize(1000, 800);

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
		setLocationRelativeTo(null);
		runMenu();
		audioManager.play(AudioManager.ClipName.MENU, true);
	}
	
	/**
	 * Our implementation of KeyEventDispatcher that handles keyboard input for our game.
	 * It only handles 8 keys, the arrow keys and wasd as this is all we use.
	 */
	private class GameKeyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
        	if (gameState != null) {
        	    switch (event.getKeyCode()) {
        	    case KeyEvent.VK_LEFT:
        	    case KeyEvent.VK_A:
        	        gameState.setPlayerVelocity(-1, 0);
        	        break;
        	    case KeyEvent.VK_RIGHT:
        	    case KeyEvent.VK_D:
        	        gameState.setPlayerVelocity(1, 0);
        	        break;
        	    case KeyEvent.VK_UP:
        	    case KeyEvent.VK_W:
        	        gameState.setPlayerVelocity(0, -1);
        	        break;
        	    case KeyEvent.VK_DOWN:
        	    case KeyEvent.VK_S:
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

		// Title image.
		JLabel wobman = makeImageLabel("files/img/wobman.png", -1, -1);
		wobman.setBorder(BorderFactory.createEmptyBorder(50,0,20,0));
		menuPanel.add(wobman);

		// Subtitle.
		JLabel keymaster = makeLabel("The Key Master", 32f);
		keymaster.setBorder(BorderFactory.createEmptyBorder(0,0,70,0));
		menuPanel.add(keymaster);

		// Play button.
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
    	
    	// Load button.
    	JButton loadButton = makeButton("Load game", joystix, 36);
		loadButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	  showLoadDialog();
               }
            });
		
    	menuPanel.add(loadButton);

    	// Instructions button.
    	JButton instructionsButton = makeButton("Instructions", joystix, 36);
		instructionsButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runInstructions();
               }
            });
		instructionsButton.setBackground(Color.black);
    	menuPanel.add(instructionsButton);

    	// Options button.
    	JButton optionsButton = makeButton("Options", joystix, 36);
		optionsButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runOptions();
               }
            });

    	menuPanel.add(optionsButton);

    	// Controls button.
    	JButton controlsButton = makeButton("Controls", joystix, 36);
		controlsButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runControls();
               }
            });

    	menuPanel.add(controlsButton);

    	// Exit button.
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

    	// Pause button.
    	JButton pauseButton = makeButton("Pause", joystix, 20);
    	pauseButton.addActionListener(new
                ActionListener() {
                   public void actionPerformed(ActionEvent event) {
                       tickThread.interrupt();
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

    	// Menu button.
    	menuButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
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
    	
    	// Lives.
    	JPanel livePanel = new JPanel();
    	livePanel.setBackground(Color.black);
    	livePanel.setLayout(new BoxLayout(livePanel, BoxLayout.PAGE_AXIS));

    	livePanel.add(makeLabel("Lives", 20));
    	livesLabel = makeLabel("8", 20);
    	livePanel.add(livesLabel);
    	
    	buttonPanel.add(livePanel);
    	buttonPanel.add(makeImageLabel("files/img/wobman.png", 337, 62));

    	// Score.
    	JPanel scorePanel = new JPanel();
    	scorePanel.setBackground(Color.black);
    	scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.PAGE_AXIS));

    	scorePanel.add(makeLabel("Score", 20));
    	scoreLabel = makeLabel("0", 20);
    	scorePanel.add(scoreLabel);
    	
    	buttonPanel.add(scorePanel);
    	
    	// Level.
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
        repaint();
	    mazeDisplay.repaint();
	   
        setVisible(true);
    }
    
    /**
     *  Recreates the tick thread that keeps the player and enemies moving.
     */
    private void newTickThread() {
    	Runnable r1 = new Runnable (){
        	private volatile boolean execute;
        	private int tick = 0; // counts frames in between character moves
          	public void run() {
          		
          		execute = true;
          		while (execute) {
          		    if(tick == 0) {
          		    	// update characters and reset tick
          		    	int level = gameState.getLevel();
          		    	gameState.updateCharacters();
          		    	if(level!=gameState.getLevel()) {
          		    		if(options.isEffects()) {
          		    			audioManager.play(AudioManager.ClipName.LEVELUP);
          		    		}
          		    	}
          		    }
          		    
          		    // update display with the current tick
          		    gameState.refreshDisplay(tick);
          		    
          		    tick = (tick + 1) % 25;
          		    
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
     * Runs the end of game screen
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

		// Restart button.
    	JButton restartButton = makeButton("Restart", joystix, 36);
    	restartButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	  getContentPane().removeAll();
            	  runGame(null);
               }
            });
		
    	menuPanel.add(restartButton);

    	// Main menu button.
    	JButton menuButton = makeButton("Menu", joystix, 36);
    	menuButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runMenu();
               }
            });

    	menuPanel.add(menuButton);

    	// Quit button.
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
        tickThread.interrupt();
        setVisible(true);
    }
    
    /**
     * Runs the pause game screen.
     */
    private void runPauseFrame() {

    	JPanel pausePanel = new JPanel();
    	pausePanel.setBackground(Color.black);
    	pausePanel.setLayout(new BoxLayout(pausePanel, BoxLayout.Y_AXIS)); // top to bottom


    	JLabel heading = makeLabel("Paused", 50);
    	heading.setBorder(BorderFactory.createEmptyBorder(70,0,30,0));
    	pausePanel.add(heading);
    	
    	pausePanel.add(Box.createVerticalGlue());

    	JButton resumeButton = makeButton("resume", joystix, 36);
    	resumeButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
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
    	pausePanel.add(resumeButton);
    	
    	pausePanel.add(Box.createVerticalGlue());
    	
    	// Instructions button.
    	JButton instructionsButton = makeButton("Instructions", joystix, 36);
		instructionsButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runInstructions();
               }
            });
		instructionsButton.setBackground(Color.black);
		
		
    	pausePanel.add(instructionsButton);
    	
    	pausePanel.add(Box.createVerticalGlue());
    	
    	// Save button.
    	JButton saveButton = makeButton("save", joystix, 36);
    	saveButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   showSaveDialog();
               }
            });
    	pausePanel.add(saveButton);
    	
    	pausePanel.add(Box.createVerticalGlue());
    	
    	pausePanel.add(genMusicOptions());
    	
    	pausePanel.add(Box.createVerticalGlue());
    	pausePanel.add(genEffectsOptions());
    	pausePanel.add(Box.createVerticalGlue());
        
    	JButton menuButton = makeButton("menu", joystix, 36);
    	menuButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runMenu();
               }
            });
    	pausePanel.add(menuButton);
    	pausePanel.add(Box.createVerticalGlue());
    	
    	this.add(pausePanel);
        this.repaint();
        this.setVisible(true);
    }
    
    /**
     * Generates the music options panel
     * @return the music options panel
     */
    private JPanel genMusicOptions() {
    	JPanel musicOptions = new JPanel(); // New Panel for music options.
		musicOptions.setLayout(new FlowLayout());

    	JLabel musicLabel = makeLabel("music:", 36);
		musicOptions.add(musicLabel);
		ButtonGroup group = new ButtonGroup();
		
		JRadioButton musicOnButton = new JRadioButton("On", options.isMusic());
		musicOnButton.setFont(joystix.deriveFont(36f));
		musicOnButton.setForeground(ourGreen);
		musicOnButton.setBackground(Color.black);
		group.add(musicOnButton);
		musicOptions.add(musicOnButton);
		
		JRadioButton musicOffButton = new JRadioButton("Off", !options.isMusic());
		musicOffButton.setFont(joystix.deriveFont(36f));
		musicOffButton.setForeground(ourGreen);
		musicOffButton.setBackground(Color.black);
		group.add(musicOffButton);
		musicOptions.add(musicOffButton);
		
		// Mutes or unmutes all music.
		ActionListener musicActionListener = new ActionListener() {
		      public void actionPerformed(ActionEvent actionEvent) {
		        AbstractButton aButton = (AbstractButton) actionEvent.getSource();
		        if(aButton.getText().equals("On")) {
		        	options.setMusic(true);
		        	audioManager.play(AudioManager.ClipName.MENU, true);
		        } else {
		        	options.setMusic(false);
		        	audioManager.stop(AudioManager.ClipName.MENU);
		        }
		        
		      }
		};
		
		musicOnButton.addActionListener(musicActionListener);
		musicOffButton.addActionListener(musicActionListener);
		
		musicOptions.setBackground(Color.black);
		return musicOptions;
    }
    
    /**
     * Generates the effects options panel
     * @return the effects options panel
     */
    private JPanel genEffectsOptions() {
    	
    	JPanel effectsOptions = new JPanel(); // New Panel for music options.
		effectsOptions.setLayout(new FlowLayout());

    	JLabel effectsLabel = makeLabel("effects:", 36);
		effectsOptions.add(effectsLabel);
		ButtonGroup group = new ButtonGroup();
		
		JRadioButton effectsOnButton = new JRadioButton("On", options.isEffects());
		effectsOnButton.setFont(joystix.deriveFont(36f));
		effectsOnButton.setForeground(ourGreen);
		effectsOnButton.setBackground(Color.black);
		group.add(effectsOnButton);
		effectsOptions.add(effectsOnButton);
		
		JRadioButton effectsOffButton = new JRadioButton("Off", !options.isEffects());
		effectsOffButton.setFont(joystix.deriveFont(36f));
		effectsOffButton.setForeground(ourGreen);
		effectsOffButton.setBackground(Color.black);
		group.add(effectsOffButton);
		effectsOptions.add(effectsOffButton);
		
		// Mutes or unmutes all effects.
		ActionListener effectsActionListener = new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
		        AbstractButton aButton = (AbstractButton) actionEvent.getSource();
		        if(aButton.getText().equals("On")) {
		        	options.setEffects(true);
		        } else {
		        	options.setEffects(false);
		        }
		        
		    }
		};
		
		effectsOnButton.addActionListener(effectsActionListener);
		effectsOffButton.addActionListener(effectsActionListener);
		
		effectsOptions.setBackground(Color.black);
		
		return effectsOptions;
    }
    
    /**
     * Generates the difficulty options panel
     * @return the difficulty options panel
     */
    private JPanel genDifficultyOptions() {
    	JPanel difficultyOptions = new JPanel(); // New Panel for music options.
    	difficultyOptions.setLayout(new FlowLayout());
    	
    	JLabel difficultyLabel = makeLabel("difficulty:", 36);
		difficultyOptions.add(difficultyLabel);
		ButtonGroup group = new ButtonGroup();
		
		JRadioButton diffEasyButton = new JRadioButton("Easy", options.isDifficulty(Options.DifficultyType.EASY));
		diffEasyButton.setFont(joystix.deriveFont(36f));
		diffEasyButton.setForeground(ourGreen);
		diffEasyButton.setBackground(Color.black);
		group.add(diffEasyButton);
		difficultyOptions.add(diffEasyButton);
		
		JRadioButton diffMedButton = new JRadioButton("Med", options.isDifficulty(Options.DifficultyType.MEDIUM));
		diffMedButton.setFont(joystix.deriveFont(36f));
		diffMedButton.setForeground(ourGreen);
		diffMedButton.setBackground(Color.black);
		group.add(diffMedButton);
		difficultyOptions.add(diffMedButton);
		
		JRadioButton diffHardButton = new JRadioButton("Hard", options.isDifficulty(Options.DifficultyType.HARD));
		diffHardButton.setFont(joystix.deriveFont(36f));
		diffHardButton.setForeground(ourGreen);
		diffHardButton.setBackground(Color.black);
		group.add(diffHardButton);
		difficultyOptions.add(diffHardButton);
		
		// Sets the chosen difficulty.
		ActionListener diffActionListener = new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
		        AbstractButton aButton = (AbstractButton) actionEvent.getSource();
		        if(aButton.getText().equals("Easy")) {
		        	options.setDifficulty(Options.DifficultyType.EASY); 
		        } else if(aButton.getText().equals("Med")) {
		        	options.setDifficulty(Options.DifficultyType.MEDIUM); 
		        } else {
		        	options.setDifficulty(Options.DifficultyType.HARD); 
		        }
		    }
		};
		
		diffEasyButton.addActionListener(diffActionListener);
		diffMedButton.addActionListener(diffActionListener);
		diffHardButton.addActionListener(diffActionListener);
		
		difficultyOptions.setBackground(Color.black);
		
		return difficultyOptions;
    }
    
    /**
     * Runs the options screen
     */
    private void runOptions() {
		JPanel optionsPanel = new JPanel();
		optionsPanel.setBackground(Color.black);
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));

		JLabel heading = makeLabel("Options", 50);
		
		heading.setBorder(BorderFactory.createEmptyBorder(30,0,50,0));
		optionsPanel.add(Box.createVerticalGlue());
		optionsPanel.add(heading);
		optionsPanel.add(Box.createVerticalGlue());
    	optionsPanel.add(genMusicOptions());
    	optionsPanel.add(Box.createVerticalGlue());
    	optionsPanel.add(genEffectsOptions());
    	optionsPanel.add(Box.createVerticalGlue());
    	optionsPanel.add(genDifficultyOptions());
    	optionsPanel.add(Box.createVerticalGlue());
    	
    	// Back button.
    	JButton backButton = makeButton("Back", joystix, 36);
		backButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runMenu();
               }
            });

    	optionsPanel.add(backButton);
    	optionsPanel.add(Box.createVerticalGlue());
        this.add(optionsPanel);
        this.repaint();
        this.setVisible(true);
    }
    
    /**
     * Runs the controls screen
     */
    private void runControls() {
		JPanel controlsPanel = new JPanel();
		controlsPanel.setBackground(Color.black);
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.PAGE_AXIS));

		JLabel heading = makeLabel("Controls", 50);
		controlsPanel.add(Box.createVerticalGlue());
		controlsPanel.add(heading);

		JLabel controls = makeImageLabel("files/img/controls.png", -1, -1);
		controlsPanel.add(Box.createVerticalGlue());
		controlsPanel.add(controls);
		
		// Back button.
    	JButton backButton = makeButton("Back", joystix, 36);
		backButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runMenu();
               }
            });
		controlsPanel.add(Box.createVerticalGlue());

    	controlsPanel.add(backButton);
    	controlsPanel.add(Box.createVerticalGlue());
        this.add(controlsPanel);
        this.repaint();
        this.setVisible(true);
    }
    
    /**
     * Runs the controls screen
     */
    private void runInstructions() {
		JPanel instructionsPanel = new JPanel();
		instructionsPanel.setBackground(Color.black);
		instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.PAGE_AXIS));
		instructionsPanel.add(Box.createVerticalGlue());
		JLabel heading = makeLabel("Instructions", 50);
		heading.setBorder(BorderFactory.createEmptyBorder(30,0,70,0));
		instructionsPanel.add(heading);
		instructionsPanel.add(Box.createVerticalGlue());
		JLabel text = makeLabel("<html><center>You are Wobman. You must get to the exit of the maze, but not without getting all the keys - else you may not unlock the door. Make sure to avoid the enemies as they try to stop you.</center></html>", 20);
		text.setAlignmentX(Component.CENTER_ALIGNMENT);
		text.setMaximumSize(new Dimension(600,200));
		instructionsPanel.add(text);
		instructionsPanel.add(Box.createVerticalGlue());
		// Back button.
    	JButton backButton = makeButton("Back", joystix, 36);
		backButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	   getContentPane().removeAll();
            	   runMenu();
               }
            });
		instructionsPanel.add(Box.createVerticalGlue());
    	instructionsPanel.add(backButton);
    	instructionsPanel.add(Box.createVerticalGlue());
        add(instructionsPanel);
        repaint();
        setVisible(true);
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
    			// Didn't load properly.
    			JOptionPane.showMessageDialog(this, "Save file is damaged!");
    		}
    	}  	
    }
    
    /**
     * Shows a loading dialog for loading a saved game.
     */
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
    
    /**
     * Updates the score label.
     * @param score the new score.
     */
    public void updateScore(int score) {
    	scoreLabel.setText(score + "");
    }
    
    /**
     * Updates the lives label.
     * @param score the new number of lives.
     */
    public void updateLives(int lives) {
    	livesLabel.setText(lives + "");
    }
    
    /**
     * Updates the level label.
     * @param score the new level.
     */
    public void updateLevel(int level) {
    	levelLabel.setText(level + "");
    }
    
    /**
     * Resizes an image.
     * @param image the image to resize.
     * @param width the width to resize to.
     * @param height the height to resize to.
     * @return the resized image.
     */
    public static BufferedImage resizeImage( Image image, int width, int height) {

    	final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        
        return bufferedImage;
    }

    /**
     * Makes a button.
     * @param text the text on the button.
     * @param font the font to use for the button.
     * @param font_size the size of the font to use for the button.
     * @return the button created.
     */
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

    /**
     * Makes a text label.
     * @param text the text on the label.
     * @param font_size the size of the font to use for the label.
     * @return the label created.
     */
    public  static JLabel makeLabel(String text, float font_size) {
    	JLabel label = new JLabel(text);
    	label.setAlignmentX(Component.CENTER_ALIGNMENT);
    	label.setFont(joystix.deriveFont(font_size));
    	label.setForeground(ourGreen);
    	label.setBackground(Color.black);
    	label.setOpaque(true);
    	return label;
    }

    /**
     * Makes an image label.
     * @param fileName the file name of the image to put on the label.
     * @param height the height of the label.
     * @param width the width of the label.
     * @return the label created.
     */
    public static JLabel makeImageLabel(String fileName, int height, int width) {
    	BufferedImage image = null;
    	JLabel label = null;
    	try {
    		image = ImageIO.read(new File(fileName));
    		if(height!=-1)
    			label = new JLabel(new ImageIcon(resizeImage(image, height, width)));
    		else
    			label = new JLabel(new ImageIcon(image));
    		label.setAlignmentX(Component.CENTER_ALIGNMENT);

    	} catch (IOException e) {
    	    // File not found.
    	}
    	return label;
    }

	@Override
	public void update(Observable arg0, Object arg1) {
		GameState gs = (GameState) arg0;
		if(gs!=null) {
			if (gs.getTick() == 0) {
				// character positions have updated, therefore interesting stuff can happen!
				
				if(gs.gameFinished()) {
				    // Finish game.
	                audioManager.stop(AudioManager.ClipName.GAME);
	                if (options.isMusic()) {
	                    audioManager.play(AudioManager.ClipName.END);
	                }
		      	    getContentPane().removeAll();
		      	    runEndGame();
				}
				// Play appropriate effect sound.
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
				// Update labels
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
