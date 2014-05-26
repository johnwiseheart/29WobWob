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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GameFrame extends JFrame implements Observer {

	//TODO: stop game from farting
	public GameFrame() {
		 try{
            InputStream is = new FileInputStream("font/joystix.ttf");
            joystix = Font.createFont(Font.TRUETYPE_FONT, is);
            
            HashMap<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            joystixul = joystix.deriveFont(attributes);
        } catch (Exception e) {
        } 
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
	
	class PlayThread extends Thread {
		byte tempBuffer[] = new byte[5];
		public void run() {
			try {
				while (true) {
					audioInputStream = AudioSystem.getAudioInputStream(new File("music/wob.wav"));
					audioFormat = audioInputStream.getFormat();
					
					DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
					sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
					
					sourceDataLine.open(audioFormat);
					sourceDataLine.start();
					int cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length);
					while (cnt != -1) {
						
							sourceDataLine.write(tempBuffer, 0, cnt);
						
						cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length);
					}
					
					sourceDataLine.drain();
					sourceDataLine.close();
					System.out.println("Audio Closed");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	AudioFormat audioFormat;
	AudioInputStream audioInputStream;
	SourceDataLine sourceDataLine;
	boolean muted;
	
	public void startGame() {
		runMenu();
		menuMusic = new AudioManager("music/wob2.wav");
	    menuMusic.play(true);
		
		//** add this into your application code as appropriate
		// Open an input stream  to the audio file.
		
		
//		Runnable musicRun = new Runnable (){
//        	private volatile boolean execute;
//          	public void run() {
//          		InputStream in;
//        		AudioStream as = null;
//          		this.execute = true;
//          		while (execute) {
//          			try {
//          				in = new FileInputStream("music/wob.wav");
//          				// Create an AudioStream object from the input stream.
//          				as = new AudioStream(in);   
//          			} catch (FileNotFoundException e) {
//          				// TODO Auto-generated catch block
//          				e.printStackTrace();
//          			} catch (IOException e) {
//          				// TODO Auto-generated catch block
//          				e.printStackTrace();
//          			}
//          			AudioPlayer.player.start(as);
//          		    try {
//          		        Thread.sleep(64000);
//          		    } catch (InterruptedException e) {
//          		    	execute = false;
//          		    }
//          		}
//          	}
//        };
//        Thread musicThread = new Thread(musicRun);
//        musicThread.start();
//        
//		new PlayThread().start();
		
		
		      
		// Use the static class member "player" from class AudioPlayer to play
		// clip.
		            
		// Similarly, to stop the audio.
//		AudioPlayer.player.stop(as); 
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
            	  menuMusic.stop();
            	  runGame();
               }
            });
		
    	menuPanel.add(playButton);

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

    	JButton exitButton = makeButton("Exit", joystix, 36);
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
 
    public void runGame() {
    	
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
                	   gameMusic.stop();
                	   menuMusic.play(true);
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
    	livesLabel = makeLabel("8", 20);
    	livePanel.add(livesLabel);
    	
    	buttonPanel.add(livePanel);
    	buttonPanel.add(makeImageLabel("img/wobman.png", 337, 62));

    	JPanel scorePanel = new JPanel();
    	scorePanel.setBackground(Color.black);
    	scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.PAGE_AXIS));

    	scorePanel.add(makeLabel("Score", 20));
    	scoreLabel = makeLabel("3889", 20);
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
        gameState = new GameState(31, 19);
        gameState.addObserver(mazeDisplay);
        gameState.addObserver(this);
        //mazeDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
    	gamePanel = new JPanel();
    	gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.PAGE_AXIS));
    	gamePanel.add(metaGamePanel);
    	gamePanel.add(mazeDisplay);
        
   
    	this.add(gamePanel);
    	

        
        newTickThread();
        tickThread.start();
        
        gameMusic = new AudioManager("music/game2.wav");
	    gameMusic.play(true);
	    
	    this.repaint();
        this.setVisible(true);

    }
    
    //TODO: this is bad
    private void newTickThread() {
    	Runnable r1 = new Runnable (){
        	private volatile boolean execute;
        	private int counter = 0;
          	public void run() {
          		
          		this.execute = true;
          		while (execute) {
          			//if(counter==0) {
	          		    gameState.tickPlayer();
	          		    gameState.tickEnemies();
          			//}
          		    counter++;
          		    if(counter == 20)
          		    	counter = 0;
          		    try {
          		        Thread.sleep(200);
          		    } catch (InterruptedException e) {
          		    	execute = false;
          		    }
          		}
          	}
        };
        tickThread = new Thread(r1);
    }
    
    private void runEndGame() {
    	JPanel menuPanel = new JPanel();
		menuPanel.setBackground(Color.black);
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));

		JLabel keymaster = makeLabel("GAME OVER", 64f);
		keymaster.setBorder(BorderFactory.createEmptyBorder(80,0,80,0));
		menuPanel.add(keymaster);
		
		
		JLabel scoreLabel = makeLabel("Score: 33", 32f);
		menuPanel.add(scoreLabel);
		
		JLabel highScoreLabel = makeLabel("NEW HIGH SCORE", 32f);
		highScoreLabel.setBorder(BorderFactory.createEmptyBorder(0,0,80,0));
		menuPanel.add(highScoreLabel);

    	JButton restartButton = makeButton("Restart", joystix, 36);
    	restartButton.addActionListener(new
            ActionListener() {
               public void actionPerformed(ActionEvent event) {
            	  getContentPane().removeAll();
            	  menuMusic.stop();
            	  runGame();
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
    
    // TODO ARE WORK
    private void runPauseFrame() {

    	JPanel pausePanel = new JPanel();
    	//pausePanel.setMinimumSize(new Dimension(getWidth(), getHeight()));
    	//pausePanel.setBounds(0, 0, getWidth(), getHeight());
    	//pausePanel.setBorder(new EmptyBorder(200, 200, 200, 200));
    	pausePanel.setBackground(Color.black);
    	pausePanel.setLayout(new BoxLayout(pausePanel, BoxLayout.Y_AXIS)); // top to bottom


    	JLabel heading = makeLabel("Paused", 50);
    	heading.setBorder(BorderFactory.createEmptyBorder(70,0,70,0));
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
            	   menuMusic.stop();
            	   gameMusic.play(true); //TODO: why does this only play once
               }
            });
    	pausePanel.add(menuButton);
    	
        this.add(pausePanel);
        this.repaint();
        this.setVisible(true);
    	
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
                   
            	   for(Component component:getContentPane().getComponents()) {
            		   if (component instanceof JButton) {
            			   JButton btn = (JButton) component;
            			   if(btn.getText().equals("on")) {
            				   btn.setFont(joystix.deriveFont(36f));
            			   }
            		   }
            	   }
            	   
            	   options.setMusic(true);
            	   menuMusic.play();
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
                   
                   
            	   for(Component component:getContentPane().getComponents()) {
            		   if (component instanceof JButton) {
            			   JButton btn = (JButton) component;
            			   if(btn.getText().equals("on")) {
            				   btn.setFont(joystix.deriveFont(36f));
            			   }
            		   }
            	   }
                   
            	   options.setMusic(false);
            	   menuMusic.stop();
               }
            });
		musicOptions.add(musicOffButton);
		musicOptions.setBackground(Color.black);
    	optionsPanel.add(musicOptions);
    	
    	JPanel effectsOptions = new JPanel(); // New Panel for music options.
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
    private static AudioManager gameMusic;
    private static AudioManager menuMusic;
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

	@Override
	public void update(Observable arg0, Object arg1) {
		GameState gs = (GameState) arg1;
		System.out.println("FUK U M8");
		if(gs!=null) {
		if(gs.gameFinished()) {
			System.out.println("U WOB");
			 if (tickThread.isAlive()) {
      		   tickThread.interrupt();
      	   }
      	   gameMusic.stop();
      	   menuMusic.play(true);
      	   getContentPane().removeAll();
      	   runEndGame();
		}
		}	
		
	}
  
}
