/*
 * The Game Panel
 * 
 * 
 * 
 * 
 * 
 * 
 * Issa Haddar. CopyRight ©™  Issa Haddar. All Rights reserved.
 */
package Panel;
 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import GameStates.GameStateManager;
import GameStates.TheGame;

/**
 * Issa haddar
 * Sunday 22-Dec-2013
 * The game panel
 * Contains the game loop(the core of the program)
 * @author iah10
 * @version 1.1 
 */
public class GamePanel extends JPanel implements Runnable, KeyListener
{
	/*----- INSATNACE FIELDS ------*/
	public static final int HEIGHT = 400;	//dimensions
	public static final int WIDTH  = 400;

	private Thread thread;		   //the tread of the game(needs to implement runnable)
	public volatile static boolean running;	  //the running condition of the game
	public volatile static boolean quit;	//quit or reset the game
	public boolean isGameOver;
	private BufferedImage image; // the image we are drawing on
	private Graphics2D g;		//the "paint brush" that we use to draw on the image
	private int FPS = 30;		//to control the speed of the game

	//game manager
	private GameStateManager GSM;

	public static boolean pause;
	public static long pauseTimer;	//when we enter into pause to insure that the player doesn't lose his power up mode

	private static final long serialVersionUID = -6861435026335934830L;
	/*---------------------------------------------------------------------*/

	/**
	 * The Constructor
	 */
	public GamePanel()
	{
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus(); // gets input from the user
	}

	/*----------------------------------------------functions---------------------------------------------------------*/

	/**
	 * For the thread 
	 * add Key Listener
	 */
	@Override
	public void addNotify()
	{
		super.addNotify();
		if(thread == null)
		{
			thread = new Thread(this);
			thread.start();		//calls the run method
		}
		addKeyListener(this);
	}

	/**
	 * Initializes the screen and the variable
	 */
	private void init()
	{
		running = true;

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		g = (Graphics2D)image.getGraphics();
		g.setRenderingHint(			//makes the game graphics smother
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		GSM = new GameStateManager();
	}

	/**
	 * Runs the game
	 */
	@Override
	public void run()
	{
		init();

		/* 
		 * setting the speed limit
		 * our target is to make the loop runs 30 times each second
		 */
		long startTime;
		long URDTimeMillis;
		long waitTime;
		long targetTime = 1000/FPS; 	// give us the amount of time that takes one loop to maintain 30 FPS(around 33 milli sec)

		while (!quit) 
		{
			//GAME lOOP
			while (running) 
			{
				startTime = System.nanoTime();

				if (pause) 
				{
					g.setFont(new Font("TEMPUS SANS ITC", Font.ITALIC, 20));
					g.setColor(Color.GREEN);
					g.drawString("Game Paused - press c to continue...", 10, 90);
					gameDraw();
				} 
				//Every game should have these 3 methods
				else 
				{
					update();
					draw();
					gameDraw();
				}

				URDTimeMillis = (System.nanoTime() - startTime) / 1000000;
				waitTime = targetTime - URDTimeMillis; // the amount of extra time we need to wait(ex: if game render drae in 20 milli sec, we have to wait extra 10 milli sec) 

				try {
					Thread.sleep(Math.max(waitTime, 0)); // sleep the extra time
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			gameOver();
		}
	}

	/**
	 * Called when the player Loses or win
	 */
	private void gameOver() 
	{
		isGameOver=true;
		g.setColor(new Color(0,100,255));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		int length = (int) g.getFontMetrics().getStringBounds(TheGame.s, g).getWidth();
		g.drawString(TheGame.s, (GamePanel.WIDTH-length)/2,GamePanel.HEIGHT/2);
		g.drawString("Final Score: " + TheGame.player.getScore(), (GamePanel.WIDTH-length)/2,GamePanel.HEIGHT/2 + 30);
		g.drawString("Press R to restart OR Esc to quit" , (GamePanel.WIDTH-length)/2,GamePanel.HEIGHT/2 + 60);
		gameDraw();
	}

	/**
	 * The Restart Method
	 */
	private void restart() 
	{
		running=true;
		isGameOver = false;
		GSM.restart();
	}

	/**
	 * update according to different states
	 */
	private void update() {	
		GSM.update();
	}

	/**
	 * Draw according to different states
	 */
	private void draw() {
		GSM.draw(g);
	}

	private void gameDraw() 
	{
		Graphics g2 = this.getGraphics();
		g2.drawImage(image,0,0,null);
		Toolkit.getDefaultToolkit( ).sync( ); // sync the display on some systems
		g2.dispose(); 	// resembles scan.close()

	}
	/**
	 * Control according to different states
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		else if (isGameOver && e.getKeyCode()== KeyEvent.VK_R) {
			restart();
		}
		else {
			GSM.keyPressed(e.getKeyCode());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		GSM.keyReleased(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
