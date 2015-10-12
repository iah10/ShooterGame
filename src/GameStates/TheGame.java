/*
 * The actual Game
 * 
 * 
 * 
 * 
 * 
 * 
 * Issa Haddar. CopyRight ©™  Issa Haddar. All Rights reserved.
 */
package GameStates;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import Entities.Bullet;
import Entities.Enemy;
import Entities.Explosion;
import Entities.FactoryMonster;
import Entities.Monster;
import Entities.Player;
import Entities.PowerUp;
import Entities.Text;
import Panel.GamePanel;

/**
 * Issa haddar
 * Sunday 22-Dec-2013
 * The game panel
 * Contains the game code
 * @author iah10
 * @version 1.1 
 */
public class TheGame extends GameState
{
	/*------ INSATNACE FIELDS ------*/
	
	public static Player player;				//the player of the game
	public static List<Bullet> bullets;	//the bullets
	public static List<Enemy> enemies;		//the enemies
	public static List<PowerUp> powerUps;	//the power ups
	public static List<Explosion> explosions;	//the explosions
	public static List<Text> texts;	//the texts

	public static long waveStartTimer;
	private long waveStartTimerDiff;	// to keep track of how much time has passed by
	public static  int waveNumber;
	private boolean waveStart;			// whether to start creating enemies or not
	private int waveDelay = 2000;

	public static long slowDownTimer;			//for the slow down timer power up
	private long slowDownTimerDiff;
	private int slowDownLength = 6000;
	private volatile boolean slow;

	public static long ExplosionTimer;			//for the explosion
	private long explosionTimerDiff;
	private int explosionLength = 1500;
	private volatile boolean explodeIn;

	public static long fastTimer;			//for time level-6
	private long fastTimerDiff;
	private int fastLength = 15000;

	public static long bulletPowerTimer;			//for the bullet upgrade
	private long bulletPowerTimerDiff;
	private int bulletPowerLength = 6000;

	private final Monster wa7esh = new Monster();		// only one monster during the game
	private final FactoryMonster masna3 = new FactoryMonster();

	public static String s = "Congratulations!" + "\n" + "YOU WON :-)";
	/*------------------------------------------------------------------------*/


	/**
	 * The Constructor
	 * @param gameStateManager 
	 */
	public TheGame(GameStateManager gameStateManager)
	{
		this.Gsm  = gameStateManager;
	}

	/*----------------------------------------------functions---------------------------------------------------------*/

	/**
	 * Initializes the screen and the variable
	 */
	@Override
	public void init()
	{
		player = new Player();
		bullets = new LinkedList<Bullet>();
		enemies = new LinkedList<Enemy>();
		powerUps = new LinkedList<PowerUp>();
		explosions = new LinkedList<Explosion>();
		texts = new LinkedList<Text>();
	}

	/**
	 * Updates everything(enemies, positions, bullets, etc) 
	 */
	@Override
	public void update() 
	{

		//new wave
		if(waveStartTimer ==0 && enemies.size() ==0)
		{
			waveNumber++;
			waveStart = false;
			waveStartTimer= System.nanoTime();
		}
		else
		{
			waveStartTimerDiff = (System.nanoTime() - waveStartTimer)/1000000;
			if(waveStartTimerDiff > waveDelay)
			{
				waveStart =true;
				waveStartTimer =0;
				waveStartTimerDiff =0;
			}
		}

		//create enemies
		if(waveStart && enemies.size()==0)
			createEneimies();

		//player update
		player.update(); 

		//bullets update
		for(int i=0; i< bullets.size(); i++)
			if( bullets.get(i).update())
				bullets.remove(i);
		//enemies update
		for(int i=0; i< enemies.size(); i++)
			enemies.get(i).update();

		//power-up update
		for (int i = 0; i < powerUps.size(); i++) 
			if(powerUps.get(i).update())
				powerUps.remove(i);
		//explosion update 
		for (int i = 0; i < explosions.size(); i++) 
			if(explosions.get(i).update())
				explosions.remove(i);
		// text update
		for(int i=0; i < texts.size(); i++)
			if(texts.get(i).update())
				texts.remove(i);
		//bullet-enemy collision/player-bullet collision
		for (int i = 0; i < bullets.size(); i++) 
		{
			Bullet  b = bullets.get(i);
			double bx = b.getX();
			double by = b.getY();
			double br = b.getR();

			for(int j=0; j < enemies.size(); j++)
			{
				if(waveNumber != 12)
				{
					Enemy e = enemies.get(j);
					double ex = e.getX();
					double ey = e.getY();
					double er = e.getR();
					/** Check for collision by Pythagoras theorem **/
					double dx = bx -ex;
					double dy = by -ey;
					double dist = Math.sqrt(dx*dx + dy*dy);
					if(dist < br + er)
					{
						e.hit(bullets.get(i).getPower());
						bullets.remove(i);
						break;
					}
				}
				if (waveNumber == 11)
				{
					Rectangle evil = masna3.getMainRectangle();
					Rectangle bulletRectangle = b.getRectangle();
					if(bulletRectangle.intersects(evil))
					{
						if(Math.random() < 0.5)
							powerUp(masna3);
						masna3.hit(bullets.get(i).getPower());
						bullets.remove(i);
						break;
					}

				}
				else if(waveNumber == 12)	//last level
				{
					//player - bullet collision
					if(player.invincibleTimer ==0 && !player.isRecovering())
					{
						int px = player.getX();
						int py = player.getY();
						double dx = bx -px;
						double dy = by -py;
						double dist = Math.sqrt(dx*dx + dy*dy);
						if(dist < br + player.getR())
						{
							player.loseLife();
							bullets.remove(i);
							break;
						}
					}

					Rectangle mainRectangle = wa7esh.getMainRectangle();
					Rectangle leftRectangle = wa7esh.getLeftRectangle();
					Rectangle rightRectangle = wa7esh.getRightRectangle();
					Rectangle bulletRectangle = b.getRectangle();

					if(bulletRectangle.intersects(mainRectangle) 
							|| bulletRectangle.intersects(leftRectangle)
							|| bulletRectangle.intersects(rightRectangle))
					{
						if(Math.random() < 0.5)
							powerUp(wa7esh);
						wa7esh.hit(bullets.get(i).getPower());
						bullets.remove(i);
					}
				}
			}
		}

		//check for dead enemies
		for(int j=0; j < enemies.size(); j++)
		{
			Enemy e = enemies.get(j);
			if(e.isDead())
			{
				powerUp(e);
				player.addScore(e.getRank() + e.getType());
				enemies.remove(j);
				if(waveNumber != 12)
				{
					if(e instanceof FactoryMonster)
					{
						enemies.clear();
						explodeIn = true;
						ExplosionTimer = System.nanoTime();
						for (int k = 0; k < 100; k++) 
						{
							explosions.add(new Explosion(masna3.getRectanglePoint().x+masna3.getWidth()/2 -2*k, 
									masna3.getRectanglePoint().y+masna3.getHeight()/2-2*k, masna3.getR()+5*k, masna3.getWidth()+10));	
							explosions.add(new Explosion(masna3.getRectanglePoint().x+masna3.getWidth()/2 -2*k, 
									masna3.getRectanglePoint().y+masna3.getHeight()/2 -2*k, masna3.getR()+10*k, masna3.getWidth()+20));
							explosions.add(new Explosion(masna3.getRectanglePoint().x+masna3.getWidth()/2 -2*k, 
									masna3.getRectanglePoint().y+masna3.getHeight()/2 -2*k, masna3.getR()+15*k, masna3.getWidth()+30));
							explosions.add(new Explosion(masna3.getRectanglePoint().x+masna3.getWidth()/2 -2*k, 
									masna3.getRectanglePoint().y+masna3.getHeight()/2 -2*k, masna3.getR()+25*k, masna3.getWidth()+40));
							explosions.add(new Explosion(masna3.getRectanglePoint().x+masna3.getWidth()/2 -2*k, 
									masna3.getRectanglePoint().y+masna3.getHeight()/2 -2*k, masna3.getR()+25*k, masna3.getWidth()+50));
							explosions.add(new Explosion(masna3.getRectanglePoint().x+masna3.getWidth()/2 -2*k, 
									masna3.getRectanglePoint().y+masna3.getHeight()/2 -2*k, masna3.getR()+25*k, masna3.getWidth()+60));	
						}
					}
					else
					{
						e.explode();
						explosions.add(new Explosion(e.getX(), e.getY(), e.getR(), e.getR()+30));
					}
				}
				else
				{
					for (int k = 0; k < 200; k++) 
					{
						explodeIn = true;
						ExplosionTimer = System.nanoTime();
						explosions.add(new Explosion(wa7esh.getRectanglePoint().x+wa7esh.getWidth()/2 -2*k, 
								wa7esh.getRectanglePoint().y+wa7esh.getHeight()/2-2*k, wa7esh.getR()+5*k, wa7esh.getWidth()+10));	
						explosions.add(new Explosion(wa7esh.getRectanglePoint().x+wa7esh.getWidth()/2 -2*k, 
								wa7esh.getRectanglePoint().y+wa7esh.getHeight()/2 -2*k, wa7esh.getR()+10*k, wa7esh.getWidth()+20));
						explosions.add(new Explosion(wa7esh.getRectanglePoint().x+wa7esh.getWidth()/2 -2*k, 
								wa7esh.getRectanglePoint().y+wa7esh.getHeight()/2 -2*k, wa7esh.getR()+15*k, wa7esh.getWidth()+30));
						explosions.add(new Explosion(wa7esh.getRectanglePoint().x+wa7esh.getWidth()/2 -2*k, 
								wa7esh.getRectanglePoint().y+wa7esh.getHeight()/2 -2*k, wa7esh.getR()+25*k, wa7esh.getWidth()+40));
						explosions.add(new Explosion(wa7esh.getRectanglePoint().x+wa7esh.getWidth()/2 -2*k, 
								wa7esh.getRectanglePoint().y+wa7esh.getHeight()/2 -2*k, wa7esh.getR()+25*k, wa7esh.getWidth()+50));
						explosions.add(new Explosion(wa7esh.getRectanglePoint().x+wa7esh.getWidth()/2 -2*k, 
								wa7esh.getRectanglePoint().y+wa7esh.getHeight()/2 -2*k, wa7esh.getR()+25*k, wa7esh.getWidth()+60));	
					}
				}
			}
		}

		// check dead player
		if(player.isDead()) 
		{
			GamePanel.running = false;
			s = "G A M E   O V E R   YOU LOSE   :-(";
		}

		/** Player Coordinates **/
		int px = player.getX();
		int py = player.getY();
		int pr = player.getR();

		//check player-enemy collision
		if(!player.isRecovering())
		{
			for (int i = 0; i < enemies.size(); i++) 
			{
				if(waveNumber !=12)
				{
					Enemy e = enemies.get(i);
					double ex = e.getX();
					double ey = e.getY();
					double er = e.getR();
					/** Check for collision by Pythagoras theorem **/
					double dx = px -ex;
					double dy = py - ey;
					double dist = Math.sqrt(dx*dx + dy*dy);
					if(dist < pr + er && player.invincibleTimer ==0)
						player.loseLife();
					else if(dist < pr + er && player.invincibleTimer !=0)
						e.hit(2);
				}
				else if(waveNumber == 11)
				{
					Rectangle playerRectangle = player.getRectangle();
					Rectangle mainRectangle = masna3.getMainRectangle();
					if(playerRectangle.intersects(mainRectangle) && player.invincibleTimer ==0)
						player.loseLife();
					else if(playerRectangle.intersects(mainRectangle) && player.invincibleTimer !=0)
						masna3.hit(2);
				}
				else if (waveNumber == 12)
				{
					Rectangle mainRectangle = wa7esh.getMainRectangle();
					Rectangle leftRectangle = wa7esh.getLeftRectangle();
					Rectangle rightRectangle = wa7esh.getRightRectangle();
					Rectangle playerRectangle = player.getRectangle();

					if(playerRectangle.intersects(mainRectangle) 
							|| playerRectangle.intersects(leftRectangle)
							|| playerRectangle.intersects(rightRectangle)) 
					{
						if(player.invincibleTimer !=0)
							wa7esh.hit(2);
						else if(player.invincibleTimer ==0)
							player.loseLife();

					}
				}
			}

		}

		//player-power-up collision
		for (int i = 0; i < powerUps.size(); i++) 
		{
			PowerUp e = powerUps.get(i);
			double ex = e.getX();
			double ey = e.getY();
			double er = e.getR();
			/** Check for collision by Pythagoras theorem **/
			double dx = px -ex;
			double dy = py - ey;
			double dist = Math.sqrt(dx*dx + dy*dy);
			//collected power-up
			if(dist < pr + er)
			{
				switch (e.getType()) 
				{
				case 1:	// +life
					player.gainLife();
					texts.add(new Text(player.getX(), player.getY(),200,"Extra Life"));
					break;
				case 2:	// +1 power
					player.increasePower(1);
					texts.add(new Text(player.getX(), player.getY(),200,"Power"));
					break;
				case 3:	// +2 power
					player.increasePower(2);
					texts.add(new Text(player.getX(), player.getY(),200,"Double Power"));
					break;
				case 4:	// Freeze
					slowDownTimer = System.nanoTime();
					slow = true;
					for (int j = 0; j < enemies.size(); j++)
						enemies.get(j).setSlow(true);
					texts.add(new Text(player.getX(), player.getY(),200,"Slow Down"));
					break;
				case 5:	//bullet power ups
					bulletPowerTimer = System.nanoTime();
					for (int j = 0; j < bullets.size(); j++)
					{
						if(bullets.get(j).getType() != 0)
						{
							bullets.get(j).setPower(3);
							bullets.get(j).setR(3);
						}
					}
					texts.add(new Text(player.getX(), player.getY(),200,"Bullet Power"));
					break;
				case 6: // invincible
					player.invincible();
					texts.add(new Text(player.getX(), player.getY(),200,"Invincible"));
					break;
				case 7:	//kill all
					for (int j = 0; j < enemies.size(); j++)
						if(!(enemies.get(j) instanceof Monster || enemies.get(j) instanceof FactoryMonster))
							enemies.get(j).hit((int) enemies.get(j).getHealth());
					ExplosionTimer = System.nanoTime();
					explodeIn = true;
					texts.add(new Text(player.getX(), player.getY(),200,"Boom"));
					break;

				}
				powerUps.remove(i);
			}
		}
		// slow down update
		if(slowDownTimer !=0)
		{
			slowDownTimerDiff = (System.nanoTime() - slowDownTimer)/1000000;
			if(slowDownTimerDiff > slowDownLength)
			{
				slowDownTimer = 0;
				slow = false;
				for (int j = 0; j < enemies.size(); j++)
					enemies.get(j).setSlow(slow);
			}
		}

		// bulletPower update
		if(bulletPowerTimer !=0)
		{
			bulletPowerTimerDiff = (System.nanoTime() - bulletPowerTimer)/1000000;
			if(bulletPowerTimerDiff > bulletPowerLength)
			{
				bulletPowerTimer = 0;
				for (int j = 0; j < bullets.size(); j++)
				{
					bullets.get(j).setPower(1);
					bullets.get(j).setR(2);
				}
			}
		}

		//boom update
		if (explodeIn) 
		{
			explosionTimerDiff = (System.nanoTime() - ExplosionTimer)/1000000;
			if(explosionTimerDiff > explosionLength)
			{
				ExplosionTimer = 0;
				explodeIn = false;
			}
		}

		//fast update - level 6
		if (fastTimer != 0)
		{
			fastTimerDiff = (System.nanoTime() - fastTimer)/1000000;
			if(fastTimerDiff > fastLength && enemies.size() !=0)
			{
				fastTimer=0;
				player.loseLife();
				waveNumber--;
				enemies.clear();
			}
		}
	}

	/**
	 * Draws to an off-screen image
	 */
	@Override
	public void draw(Graphics2D g) 
	{
		//draw Background
		g.setColor(new Color(0, 100, 255));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		//draw slow down and bullet and invincible  screen
		if(slowDownTimer != 0 && bulletPowerTimer != 0 && player.invincibleTimer != 0)
		{
			int alpha  = (int)(255*Math.sin(3.14 * bulletPowerTimerDiff/ bulletPowerLength));	//for transparency
			if(alpha > 255) alpha = 255;
			if(alpha <0) alpha = 0;
			g.setColor(new Color(153, 0, 204,alpha));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			String s = "SUPER MODE";
			int length = (int)g.getFontMetrics().getStringBounds(s, g).getWidth(); 	//get the length of the screen in pixels
			g.setColor(new Color(255,255,255,alpha));
			g.drawString(s, GamePanel.WIDTH/2-length/2, GamePanel.HEIGHT /2);
		}

		//draw slow down and invincible screen
		else if(slowDownTimer != 0 && player.invincibleTimer != 0)
		{
			int alpha  = (int)(255*Math.sin(3.14 * slowDownTimerDiff/ slowDownLength));	//for transparency
			if(alpha > 255) alpha = 255;
			if(alpha <0) alpha = 0;
			g.setColor(new Color(155, 102, 153,alpha));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			String s = "SLOW-DOWN  & Invinsible MODE";
			int length = (int)g.getFontMetrics().getStringBounds(s, g).getWidth(); 	//get the length of the screen in pixels
			g.setColor(new Color(255,255,255,alpha));
			g.drawString(s, GamePanel.WIDTH/2-length/2, GamePanel.HEIGHT /2);
		}
		//draw slow down and invincible screen
		else if(bulletPowerTimer != 0 && player.invincibleTimer != 0)
		{
			int alpha  = (int)(255*Math.sin(3.14 * bulletPowerTimerDiff/ bulletPowerLength));	//for transparency
			if(alpha > 255) alpha = 255;
			if(alpha <0) alpha = 0;
			g.setColor(new Color(255, 204, 0,alpha));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			String s = "Invicible  & BULLET-POWER MODE";
			int length = (int)g.getFontMetrics().getStringBounds(s, g).getWidth(); 	//get the length of the screen in pixels
			g.setColor(new Color(255,255,255,alpha));
			g.drawString(s, GamePanel.WIDTH/2-length/2, GamePanel.HEIGHT /2);
		}
		//draw slow down and bullet power screen
		else if(bulletPowerTimer != 0 &&  slowDownTimer != 0)
		{
			int alpha  = (int)(255*Math.sin(3.14 * bulletPowerTimerDiff/ bulletPowerLength));	//for transparency
			if(alpha > 255) alpha = 255;
			if(alpha <0) alpha = 0;
			g.setColor(new Color(255, 255, 255,alpha));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			String s = "SLOWDOWN  & BULLET-POWER MODE";
			int length = (int)g.getFontMetrics().getStringBounds(s, g).getWidth(); 	//get the length of the screen in pixels
			g.setColor(new Color(255,255,255,alpha));
			g.drawString(s, GamePanel.WIDTH/2-length/2, GamePanel.HEIGHT /2);
		}

		//draw slow down screen
		else if(slowDownTimer != 0)
		{
			int alpha  = (int)(255*Math.sin(3.14 * slowDownTimerDiff/ slowDownLength));	//for transparency
			if(alpha > 255) alpha = 255;
			if(alpha <0) alpha = 0;
			g.setColor(new Color(153, 0, 51,alpha));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			String s = "SLOW-DOWN MODE";
			int length = (int)g.getFontMetrics().getStringBounds(s, g).getWidth(); 	//get the length of the screen in pixels
			g.setColor(new Color(255,255,255,alpha));
			g.drawString(s, GamePanel.WIDTH/2-length/2, GamePanel.HEIGHT /2);
		}

		//draw slow down screen
		else if(fastTimer != 0)
		{
			int alpha  = (int)(255*Math.sin(3.14 * fastTimerDiff/fastLength));	//for transparency
			if(alpha > 255) alpha = 255;
			if(alpha <0) alpha = 0;
			g.setColor(new Color(153, 0, 51,alpha));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			String s = "TIMED-LEVEL";
			int length = (int)g.getFontMetrics().getStringBounds(s, g).getWidth(); 	//get the length of the screen in pixels
			g.setColor(new Color(200,155,35,alpha));
			g.drawString(s, GamePanel.WIDTH/2-length/2, GamePanel.HEIGHT /2);
		}

		//draw invincible mode
		else if(player.invincibleTimer != 0)
		{
			int alpha  = (int)(255*Math.sin(3.14 * player.invincibleTimeDiff/ player.invicibleLength));	//for transparency
			if(alpha > 255) alpha = 255;
			if(alpha <0) alpha = 0;
			g.setColor(new Color(51, 204, 204,alpha));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			String s = "INVINCIBLE MODE";
			int length = (int)g.getFontMetrics().getStringBounds(s, g).getWidth(); 	//get the length of the screen in pixels
			g.setColor(new Color(255,255,255,alpha));
			g.drawString(s, GamePanel.WIDTH/2-length/2, GamePanel.HEIGHT /2);
		}

		//draw bullet power screen
		else if(bulletPowerTimer != 0)
		{
			int alpha  = (int)(255*Math.sin(3.14 * bulletPowerTimerDiff/ bulletPowerLength));	//for transparency
			if(alpha > 255) alpha = 255;
			if(alpha <0) alpha = 0;
			g.setColor(new Color(0, 100, 70,alpha));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			String s = "BULLET-POWER MODE";
			int length = (int)g.getFontMetrics().getStringBounds(s, g).getWidth(); 	//get the length of the screen in pixels
			g.setColor(new Color(255,255,255,alpha));
			g.drawString(s, GamePanel.WIDTH/2-length/2, GamePanel.HEIGHT /2);
		}
		if(explodeIn)
		{
			int alpha  = (int)(255*Math.sin(3.14 * explosionTimerDiff/ explosionLength));	//for transparency
			if(alpha > 255) alpha = 255;
			if(alpha <0) alpha = 0;
			g.setColor(new Color(255,255,255,alpha));
			g.setFont(new Font("Century Gothic", Font.PLAIN, 28));
			String s = "!!!!	BoOoOoOoOoOoM	!!!!";
			int length = (int)g.getFontMetrics().getStringBounds(s, g).getWidth(); 	//get the length of the screen in pixels
			g.drawString(s, GamePanel.WIDTH/2-length/2, GamePanel.HEIGHT /2 - 40);
		}

		//draw player
		player.draw(g);

		//draw bullets
		for(int i=0; i< bullets.size(); i++)
			bullets.get(i).draw(g);

		//draw enemies
		for(int i=0; i< enemies.size(); i++)
			enemies.get(i).draw(g);

		//draw power-ups
		for (int i = 0; i < powerUps.size(); i++)
			powerUps.get(i).draw(g);

		//draw explosions
		for (int i = 0; i < explosions.size(); i++)
			explosions.get(i).draw(g);

		//draw texts
		for (int i = 0; i < texts.size(); i++)
			texts.get(i).draw(g);

		//draw wave number
		if(waveStartTimer != 0)
		{
			g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			String s = "-W A V E " + waveNumber + "	-";
			int length = (int)g.getFontMetrics().getStringBounds(s, g).getWidth(); 	//get the length of the screen in pixels
			int alpha  = (int)(255*Math.sin(3.14 * waveStartTimerDiff / waveDelay));	//for transparency
			if(alpha > 255) alpha = 255;
			if(alpha <0) alpha = 0;
			g.setColor(new Color(255,255,255,alpha));
			g.drawString(s, GamePanel.WIDTH/2-length/2-10, 60);
		}

		//draw player lives 
		for(int i =0; i < player.getLives(); i++) 
		{
			g.setColor(Color.WHITE);
			g.fillOval(20+(20*i),20, player.getR()*2, player.getR()*2);
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.WHITE.darker());
			g.drawOval(20+(20*i),20, player.getR()*2, player.getR()*2);
			g.setStroke(new BasicStroke(1));
		}

		//draw player-power;
		g.setColor(Color.YELLOW);
		g.fillRect(20, 40, player.getPower()*8, 8);
		g.setColor(Color.YELLOW.darker());
		g.setStroke(new BasicStroke(2));
		for(int i=0; i< player.getRequiredPower(); i++)
			g.drawRect(20+8*i, 40, 8, 8);
		g.drawRect(20, 40, player.getPower()*8, 8);
		g.setStroke(new BasicStroke(1));

		//draw player score
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		g.drawString("Score: "+ player.getScore(), GamePanel.WIDTH-100, 30);


		//draw level number
		g.drawString("Level: "+ waveNumber, GamePanel.WIDTH-100, 50);

		//draw slow down meter
		if(slowDownTimer != 0) 
		{
			g.setColor(Color.WHITE);
			g.drawRect(20, 60, 100, 8);
			g.fillRect(20, 60, (int) (100 - 100*slowDownTimerDiff / slowDownLength), 8);
		}

		//draw slow down meter
		if(bulletPowerTimer != 0) 
		{
			g.setColor(Color.YELLOW);
			g.drawRect(20, 80, 100, 8);
			g.fillRect(20, 80, (int) (100 - 100*bulletPowerTimerDiff / bulletPowerLength), 8);
		}

		//fast down meter
		if(fastTimer != 0) 
		{
			g.setColor(Color.WHITE);
			g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
			g.drawString("Time: ", GamePanel.WIDTH-105, 65);
			g.setColor(Color.magenta);
			g.drawRect(GamePanel.WIDTH-105, 69, 100, 8);
			g.fillRect(GamePanel.WIDTH-105, 69, (int) (100 - 100*fastTimerDiff / fastLength), 8);
		}

		//draw invincible meter
		if (player.invincibleTimer != 0) 
		{
			g.setColor(Color.cyan);
			g.drawRect(20, 100, 100, 8);
			g.fillRect(20, 100, (int) (100 - 100*player.invincibleTimeDiff / player.invicibleLength), 8);
		}
		//draw Monster lives meter
		if(waveNumber == 11)
		{
			g.setColor(Color.darkGray);
			g.drawRect(GamePanel.WIDTH-100, 60, 90, 8);
			g.fillRect(GamePanel.WIDTH-100, 60, (int) (masna3.getHealth()*90/400 ), 8);
		}
		if(waveNumber == 12)
		{
			g.setColor(Color.BLACK);
			g.drawRect(GamePanel.WIDTH-100, 60, 90, 8);
			g.fillRect(GamePanel.WIDTH-100, 60, (int) (wa7esh.getHealth()*90/600 ), 8);

		}

	}

	/**
	 * Called after the each dead enemy
	 * Gives a chance to the player to get a power up
	 * @param e
	 */
	private void powerUp(Enemy e) 
	{
		//chance for power-up
		double rand = Math.random();
		if(rand < 0.005) 
			powerUps.add(new PowerUp(e.getX(), e.getY(), 7));
		else if(rand < 0.008) 
			powerUps.add(new PowerUp(e.getX(), e.getY(), 1));
		else if(rand < 0.020)
			powerUps.add(new PowerUp(e.getX(), e.getY(), 3));
		else if(rand < 0.06 && waveNumber >=4)
			powerUps.add(new PowerUp(e.getX(), e.getY(), 5));
		else if(rand < 0.1) 
			powerUps.add(new PowerUp(e.getX(), e.getY(), 2));
		else if(rand < 0.12) 
			powerUps.add(new PowerUp(e.getX(), e.getY(), 4));
		else if(rand < 0.15 && waveNumber >=5)//invisible starting level 5
			powerUps.add(new PowerUp(e.getX(), e.getY(), 6));
	}
	
	/**
	 * Restart The Game
	 */
	@Override
	public void restart() 
	{
		waveNumber = 0;
		bullets.clear();
		enemies.clear();
		powerUps.clear();
		explosions.clear();
		texts.clear();
		bulletPowerTimer = 0;
		slowDownTimer = 0;
		ExplosionTimer = 0;
		explodeIn=false;
		waveStartTimer = 0;
		fastTimer = 0;
		player = new Player();
	}


	/**
	 * Create new enemies(the game has 8 levels)
	 */
	private void createEneimies() 
	{
		enemies.clear();
		if(waveNumber ==1)
			for(int i=0; i< 5; i++)
				enemies.add(new Enemy(1, 1,slow));
		if(waveNumber ==2)
		{
			for(int i=0; i< 8; i++)
				enemies.add(new Enemy(1, 1,slow));

		}
		if(waveNumber==3) 
		{
			for(int i=0; i< 6; i++)
				enemies.add(new Enemy(1, 1, slow));
			enemies.add(new Enemy(1, 2, slow));
			enemies.add(new Enemy(1, 2, slow));
		}
		if(waveNumber==4) 
		{
			for(int i=0; i< 4; i++)
				enemies.add(new Enemy(2, 1, slow));
			enemies.add(new Enemy(1, 4, slow));
		}
		if(waveNumber==5) 
		{
			for(int i=0; i< 4; i++)		//4 fast
				enemies.add(new Enemy(4, 1, slow));
		}
		if(waveNumber==6) 
		{
			enemies.add(new Enemy(4, 2, slow));	//very fast
			fastTimer = System.nanoTime();
		}
		if(waveNumber==7) 
		{
			fastTimer=0;
			enemies.add(new Enemy(1, 4, slow));
			enemies.add(new Enemy(1, 3, slow));
			enemies.add(new Enemy(2, 3, slow));
		}
		if(waveNumber==8) 
		{
			for(int i=0; i< 4; i++)
			{
				enemies.add(new Enemy(2, 1, slow));
				enemies.add(new Enemy(3, 1, slow));
			}
			enemies.add(new Enemy(1, 3, slow));

		}
		if(waveNumber==9) 
		{
			enemies.add(new Enemy(1, 3, slow));
			enemies.add(new Enemy(2, 3, slow));
			enemies.add(new Enemy(3, 3, slow));
		}
		if(waveNumber==10) 
		{
			enemies.add(new Enemy(1, 4, slow));
			enemies.add(new Enemy(2, 4, slow));
			enemies.add(new Enemy(3, 4, slow));
		}
		if(waveNumber==11)
			enemies.add(masna3);
		if(waveNumber==12)
			enemies.add(wa7esh);
		if(waveNumber==13)
			GamePanel.running=false;
	}

	@Override
	public void keyPressed(int k) 
	{
		switch (k) 
		{
		case KeyEvent.VK_LEFT:
			player.setLeft(true);
			break;
		case KeyEvent.VK_RIGHT: 
			player.setRight(true);
			break;
		case KeyEvent.VK_UP:
			player.setUp(true);
			break;
		case KeyEvent.VK_DOWN:
			player.setDown(true);
			break;
		case KeyEvent.VK_Z:		//for firing
			player.setFiring(true);
			break;
		case KeyEvent.VK_P:
			GamePanel.pause = true;
			break;
		case KeyEvent.VK_C:
			GamePanel.pause = false;
			break;

		}
	}

	@Override
	public void keyReleased(int k) 
	{
		switch (k) 
		{
		case KeyEvent.VK_LEFT:
			player.setLeft(false);
			break;
		case KeyEvent.VK_RIGHT:
			player.setRight(false);
			break;
		case KeyEvent.VK_UP:
			player.setUp(false);
			break;
		case KeyEvent.VK_DOWN:
			player.setDown(false);
			break;
		case KeyEvent.VK_Z:
			player.setFiring(false);
			break;
		}
	}
}