import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

final public class Main extends JFrame {

	private JPanel contentPane;
	private MyPanel gamePanel = null;
    private boolean isPaused = false;
    private long score = 0;
    private int level = 1;
    private long timeRemainingInLevel = 0;				//MILLISECONDS
    private int livesRemaining = 3;
    private boolean levelCompleted = false;
    private boolean gameOver = false;
	
	final int FRAMES_PER_SECOND = 60;
	final int SCREEN_HEIGHT = 600;
	final int SCREEN_WIDTH = 585;
	final int STANDARD_BARRIER_WIDTH = 20;
	final int STANDARD_LEVEL_TIME_ALLOTMENT = 60 * 1000;
	private int current_key_pressed = 0;

	long current_time = 0;								//MILLISECONDS
	long next_refresh_time = 0;							//MILLISECONDS
	long last_refresh_time = 0;
	long minimum_delta_time = 1000 / FRAMES_PER_SECOND;	//MILLISECONDS
	long actual_delta_time = 0;							//MILLISECONDS
    
    ArrayList<Barrier> barriers = null;
	ArrayList<Barrier> destroyedBarriers = null;
    ArrayList<Sprite> sprites = null;
    Paddle paddle = null;

    private JLabel lblLevel;
    private JLabel lblScore;
    private JLabel lblTime;
    private JLabel lblBalls;
    private JLabel lblGameOver;
    
    public Main()
    {
        addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent arg0) {
        		this_keyPressed(arg0);
        	}
        	@Override
        	public void keyReleased(KeyEvent arg0) {
        		this_keyReleased(arg0);
        	}
        });

        setSize(SCREEN_WIDTH + 16, SCREEN_HEIGHT + 40);
        //very important!! a JFrame can receive key events but needs to explicitly be set to allow focus
        this.setFocusable(true);

		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
        //contentPane.setBackground(Color.BLACK);
       
        gamePanel = new MyPanel();
        gamePanel.setLocation(0, 0);
        gamePanel.setLayout(null);
        gamePanel.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        contentPane.add(gamePanel, BorderLayout.CENTER);
        
        lblLevel = new JLabel("Level:");
        lblLevel.setFont(new Font("Courier New", Font.BOLD, 18));
        lblLevel.setForeground(Color.WHITE);
        lblLevel.setBounds(20, 11, 84, 32);
        contentPane.add(lblLevel);
        
        lblScore = new JLabel("Score:");
        lblScore.setFont(new Font("Courier New", Font.BOLD, 18));
        lblScore.setForeground(Color.WHITE);
        lblScore.setBounds(361, 11, 214, 32);
        contentPane.add(lblScore);
        
        lblTime = new JLabel("Time:");
        lblTime.setForeground(Color.WHITE);
        lblTime.setFont(new Font("Courier New", Font.BOLD, 18));
        lblTime.setBounds(231, 11, 120, 32);
        contentPane.add(lblTime);
        
        lblBalls = new JLabel("Balls:");
        lblBalls.setForeground(Color.WHITE);
        lblBalls.setFont(new Font("Courier New", Font.BOLD, 18));
        lblBalls.setBounds(114, 11, 107, 32);
        contentPane.add(lblBalls);
        
        lblGameOver = new JLabel("GAME OVER!");
        lblGameOver.setHorizontalAlignment(SwingConstants.CENTER);
        lblGameOver.setForeground(Color.RED);
        lblGameOver.setFont(new Font("Courier New", Font.BOLD, 36));
        lblGameOver.setBounds(10, 259, 565, 70);
        contentPane.add(lblGameOver);
        
        JLabel lblBackground = new JLabel("New label");
        lblBackground.setIcon(new ImageIcon(".\\res\\stellar-background.jpg"));
        lblBackground.setBounds(0, 0, (int)SCREEN_WIDTH, (int)SCREEN_HEIGHT);
        contentPane.add(lblBackground);
        
        sprites = new ArrayList<Sprite>();
    	barriers = new ArrayList<Barrier>();
    	destroyedBarriers = new ArrayList<Barrier>();

    }

	public static void main(String[] args)
    {
    	Main m = new Main();
    	m.setVisible(true);
    	
        Thread loop = new Thread()
        {
           public void run()
           {
              m.gameLoop();
           }
        };
        loop.start();
    	
    }
	
	class MyPanel extends JPanel {
		 public void paintComponent(Graphics g)
	     {
	        //BS way of clearing out the old rectangle to save CPU.
	        g.setColor(getBackground());
	        
	        g.setColor(Color.WHITE);
	        for (Sprite sprite : sprites) {
		        g.drawImage(sprite.getImage(), (int)sprite.getLeft(), (int)sprite.getTop(), (int)sprite.getWidth(), (int)sprite.getHeight(), null);
	        }

	        for (Barrier barrier : barriers) {
		        g.setColor(barrier.getColor());
	            g.fillRect((int)barrier.getLeft(),(int) barrier.getTop(), (int)barrier.getWidth(), (int)barrier.getHeight());       	
	        }
	        	        
	        g.setColor(Color.RED);
	        
	        if (paddle != null) {
	        	g.fillRect((int)paddle.getLeft(),(int) paddle.getTop(), (int)paddle.getWidth(), (int)paddle.getHeight());
	        }
	        	       
	     }
	}
    
    private void gameLoop() {
    	
    	while ((level < 3) && (gameOver == false)) {

    		createLevel(level, false);
    		isPaused = true;
    		
    		while ((levelCompleted == false) && (gameOver == false)) { // main game loop

    			//adapted from http://www.java-gaming.org/index.php?topic=24220.0
    			last_refresh_time = System.currentTimeMillis();
    		    next_refresh_time = current_time + minimum_delta_time;

    		    while (current_time < next_refresh_time)
                {
                   Thread.yield();

                   try {Thread.sleep(1);}
                   catch(Exception e) {} 
                
                   current_time = System.currentTimeMillis();
                }
    		    
    		    //UPDATE STATE
    		    updateTime();
    		    updateSprites();
    		    updateBarriers();
    		    updatePaddle();
    		    testLevelCompletion();
    		    testSpritesLost();

    		    //REFRESH
    		    this.lblLevel.setText("LEVEL:" + this.level);
    		    this.lblBalls.setText("LIVES:" + this.livesRemaining);
    		    this.lblScore.setText("SCORE:" + this.score);
    		    this.lblTime.setText("TIME:" + this.timeRemainingInLevel);
    		    this.lblGameOver.setVisible(this.gameOver);
    		    this.repaint();

    		}
    		
    		level++;
    		
    	}
    	
    }
    
    private void updateTime() {

        current_time = System.currentTimeMillis();
        actual_delta_time = (isPaused ? 0 : current_time - last_refresh_time);
//	    System.out.println(String.format("%d, %d, %d", last_refresh_time, current_time, actual_delta_time));
	    last_refresh_time = current_time;
	    timeRemainingInLevel -= actual_delta_time;
    }
    
	public void updateSprites() {
		
		for (Sprite sprite : sprites) {
			sprite.update(barriers, paddle, actual_delta_time);
		}

	}

	public void updateBarriers() {
				
		for (Barrier barrier : barriers) {
			if ((barrier.isPermanent() == false) && (barrier.getHitsRemaining() <= 0)) {
				//game keeps track of score...
				score += barrier.getPoints();
				//remove barrier from play;add to a temporary list to avoid ConcurrentModificationException
				destroyedBarriers.add(barrier);				
			}
		}
		
		if (destroyedBarriers.size() > 0) {
			barriers.removeAll(destroyedBarriers);
			destroyedBarriers.clear();
		}

	}
	
	private void updatePaddle() {
		//important that we set the velocity only when updating, as key events are not guaranteed to occur synchronously
		
		switch (current_key_pressed) {		
			case 37:	//LEFT
				paddle.updatePosition(-1, actual_delta_time);
				break;
			case 39:	//RIGHT
				paddle.updatePosition(1, actual_delta_time);
				break;
			default:
				paddle.updatePosition(0, actual_delta_time);
		}		
	}

	private void testLevelCompletion() {
		if ( (barriers.size() <= 4) || (timeRemainingInLevel < 0)) {
			levelCompleted = true;
		}
	}
	
	private void testSpritesLost() {

		boolean spriteInPlay = false;
		
		for (Sprite sprite : sprites) {
			if (sprite.getTop() + sprite.getHeight() <= paddle.getTop()) {
				spriteInPlay = true;
			}
			else {
				System.out.printf("paddle left: %f; paddle right: %f;\nsprite left: %f; sprite right: %f\n",paddle.getLeft(), paddle.getRight(), sprite.getLeft(), sprite.getLeft() + sprite.getWidth() );
				System.out.printf("paddle top: %f; sprite bottom: %f",paddle.getTop(), sprite.getTop() + sprite.getHeight());
			}
		}
		
		if (spriteInPlay == false) {
			this.livesRemaining--;
			this.gameOver = (this.livesRemaining < 1);
			this.isPaused = true;
			createLevel(level, true);
			
		}
		
	}
	
	protected void btnPauseRun_mouseClicked(MouseEvent arg0) {
		if (isPaused) {
			isPaused = false;
		}
		else {
			isPaused = true;
		}
	}
	protected void this_keyPressed(KeyEvent arg0) {
		//keep track of which key is currently pressed
		current_key_pressed = arg0.getKeyCode();
		if (current_key_pressed == 32) {
			btnPauseRun_mouseClicked(null);
		}
	}
	protected void this_keyReleased(KeyEvent arg0) {
		//keep track of which key is currently pressed
		current_key_pressed = 0;
	}
	
	private void createLevel(int level, boolean spritesOnly) {

        sprites.clear();

        if (this.livesRemaining > 0) {
	        //ball
	        sprites.add(new Sprite(20, 20, STANDARD_BARRIER_WIDTH , SCREEN_HEIGHT - (SCREEN_WIDTH / 2) - STANDARD_BARRIER_WIDTH * 2  , 200 + (level * 50), 200 + (level * 50), 0, 0));	    	    
	    	//paddle
	        int paddle_width = 100;
	    	paddle = new Paddle(paddle_width, 20,  ((SCREEN_WIDTH - paddle_width) / 2), SCREEN_HEIGHT - 20, 0, 0, 0, 0, 20, (int) (SCREEN_WIDTH - STANDARD_BARRIER_WIDTH));
        }

    	if (spritesOnly == false) {

    		barriers.clear();
        	destroyedBarriers.clear();

    		//left wall
	    	barriers.add(new Barrier(STANDARD_BARRIER_WIDTH,SCREEN_HEIGHT, 0, 0, Color.BLUE));
	    	//right wall
	    	barriers.add(new Barrier(STANDARD_BARRIER_WIDTH,SCREEN_HEIGHT, SCREEN_WIDTH - STANDARD_BARRIER_WIDTH, 0, Color.BLUE));
	    	//bottom wall
	//    	barriers.add(new Barrier(SCREEN_WIDTH,  STANDARD_BARRIER_WIDTH, 0, SCREEN_HEIGHT - STANDARD_BARRIER_WIDTH,Color.BLUE));
	    	//top wall
	    	barriers.add(new Barrier(SCREEN_WIDTH, STANDARD_BARRIER_WIDTH, 0, 0,Color.BLUE));	
	    		
        	if (level == 1) {
    	    	for (int row = 3; row < 8; row++) {
    	    		for (int col = 0; col < 12; col++) {
    	    	    	barriers.add(new Barrier( 40, 20, 25 + col * 45, STANDARD_BARRIER_WIDTH + row * 25, Color.GREEN));
    	    		}
    	    	}
    	    	timeRemainingInLevel = STANDARD_LEVEL_TIME_ALLOTMENT;
        	}
        	else if (level == 2) {
    	    	for (int row = 3; row < 8; row++) {
    	    		for (int col = 0; col < 12; col++) {
    	    			if (row < 7) {
    		    	    	barriers.add(new Barrier( 40, 20, 25 + col * 45, STANDARD_BARRIER_WIDTH + row * 25, Color.GREEN));
    	    			}
    	    			else {
    		    	    	barriers.add(new Barrier( 40, 20, 25 + col * 45, STANDARD_BARRIER_WIDTH + row * 25, Color.YELLOW));
    	    			}
    	    		}
    	    	}
    	    	timeRemainingInLevel = STANDARD_LEVEL_TIME_ALLOTMENT;
        	}
        	else if (level == 3) {
    	    	for (int row = 3; row < 8; row++) {
    	    		for (int col = 0; col < 12; col++) {
    	    			if (row < 6) {
    		    	    	barriers.add(new Barrier( 40, 20, 25 + col * 45, STANDARD_BARRIER_WIDTH + row * 25, Color.GREEN));
    	    			}
    	    			else if (row == 6){
    		    	    	barriers.add(new Barrier( 40, 20, 25 + col * 45, STANDARD_BARRIER_WIDTH + row * 25, Color.YELLOW));
    	    			}
    	    			else if (row == 7){
    		    	    	barriers.add(new Barrier( 40, 20, 25 + col * 45, STANDARD_BARRIER_WIDTH + row * 25, Color.ORANGE));
    	    			}
    	    		}
    	    	}
    	    	timeRemainingInLevel = STANDARD_LEVEL_TIME_ALLOTMENT;
        	}
        	
    	}
    	levelCompleted = false;    		
	}
}

