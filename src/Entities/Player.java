package Entities;
/**
 * Issa haddar
 * Sunday 22-Dec-2013
 * @author iah10
 * @version 1.1
 * The player class
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import GameStates.TheGame;
import Panel.GamePanel;



public class Player 
{
	/**** INSTANCE FILEDS ****/
	private int x;
	private int y;
	private int r;	// radius (the player is going to be a circle)

	private int dx;
	private int dy;
	private int speed; 

	private boolean firing;
	private long firingTimer;
	private long firingDelay;

	private boolean recovering;
	private long recoveryTimer;

	private volatile boolean invicible;
	public long invincibleTimer;
	public long invincibleTimeDiff;
	public int invicibleLength = 6000;

	//tells us in which direction the player is moving
	private boolean left;	
	private boolean right;
	private boolean up; 
	private boolean down; 

	private int lives;
	private int score;

	private int powerLevel;
	private int power;
	private int[] requiredPower = {1,2,3,4,5};

	private Color color1;
	private Color color2;
	private Color color3;

	/**************************/

	/**
	 * The Constructor
	 */
	public Player()
	{
		x = GamePanel.WIDTH/2;
		y = GamePanel.HEIGHT /2;
		r = 5;

		dx = 0;
		dy = 0;
		speed = 5;

		lives = 3;
		color1 = Color.WHITE;
		color2 = Color.RED;
		color3 = new Color(244,204,153);
		
		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 200;

		recovering = false;
		recoveryTimer = 0;

		score =0;

	}

	/**----------------------------------------------functions---------------------------------------------------------**/

	public int getX() { return x; }
	public int getY() { return y; }
	public int getR() { return r; }

	public int getLives() {	return lives; }
	public int getScore() { return score; }

	public int getPowerLevel() { return powerLevel; }
	public int getPower(){ return power; }
	public int getRequiredPower(){ return requiredPower[powerLevel]; }
	public Rectangle getRectangle(){ return new Rectangle(x, y, r, r);}

	public boolean isDead(){ return lives <= 0; }
	public boolean isRecovering() {return recovering; }


	public void setLeft(boolean left) { this.left = left; }
	public void setRight(boolean right) { this.right = right; }
	public void setUp(boolean up) { this.up = up; }
	public void setDown(boolean down) { this.down = down; }
	public void setScore(int score) { this.score = score; }

	public void setFiring(boolean firing) {	this.firing = firing; }

	public void addScore(int i ) { score +=i; }

	/**
	 * When the player is hit
	 */
	public void loseLife()
	{
		lives--;
		recovering = true;
		recoveryTimer = System.nanoTime();
	}

	public void invincible()
	{
		invicible = true;
		invincibleTimer = System.nanoTime();
	}

	/**
	 * when the player collects a power-up
	 */
	public void gainLife() {
		lives++;
	}

	/**
	 * Increase the power of the player when he picks a power up
	 * 
	 * @param i
	 */
	public void increasePower(int i) 
	{
		power +=i;
		if(powerLevel == 4)
		{
			if(power > requiredPower[powerLevel])
				power = requiredPower[powerLevel];
			return;
		}
		if(power >= requiredPower[powerLevel])
		{
			power -= requiredPower[powerLevel];
			powerLevel++;
		}
	}

	/**
	 * Updates the fields of the player (positions, lives,..)
	 */
	public void update()
	{
		if(left)  dx = -speed;
		if(right) dx = speed;
		if(up)    dy = -speed;
		if(down)  dy = speed;

		x += dx;
		y += dy;

		if(x < r) x +=r;//left
		if(y < r) y += r;// up
		if(x > GamePanel.WIDTH -r)  x = GamePanel.WIDTH -r;	//right
		if(y > GamePanel.HEIGHT -r) y = GamePanel.HEIGHT -r;//down

		dx =0; 
		dy =0;

		if(firing)
		{
			long elapsed = (System.nanoTime() - firingTimer)/1000000;
			if(elapsed > firingDelay)
			{
				firingTimer = System.nanoTime();
				if(powerLevel < 2)
				{
					if(TheGame.bulletPowerTimer != 0)	//During bullet power up
						TheGame.bullets.add(new Bullet(270, x,y,3,3, 1));
					else
						TheGame.bullets.add(new Bullet(270, x,y,2,1,1));
				}
				else if(powerLevel < 4) 
				{
					if(TheGame.bulletPowerTimer != 0)	//During bullet power up
					{
						TheGame.bullets.add(new Bullet(270, x+5,y,3,3,1));
						TheGame.bullets.add(new Bullet(270, x-5,y,3,3,1));
					}
					else 
					{
						TheGame.bullets.add(new Bullet(270, x+5,y,2,1,1));
						TheGame.bullets.add(new Bullet(270, x-5,y,2,1,1));
					}
				}
				else
				{
					if(TheGame.bulletPowerTimer != 0)	//During bullet power up
					{
						TheGame.bullets.add(new Bullet(270, x+5,y,3,3,1));
						TheGame.bullets.add(new Bullet(277, x-5,y,3,3,1));
						TheGame.bullets.add(new Bullet(263, x+5,y,3,3,1));
					}
					else
					{
						TheGame.bullets.add(new Bullet(270, x+5,y,2,1,1));
						TheGame.bullets.add(new Bullet(277, x-5,y,2,1,1));
						TheGame.bullets.add(new Bullet(263, x+5,y,2,1,1));
					}
				}

			}
		}
		if(recovering)
		{
			long elapsed = (System.nanoTime() - recoveryTimer)/1000000;
			if(elapsed > 2000)
			{
				recovering =false;
				recoveryTimer = 0;
			}
		}
		if(invicible)
		{
			invincibleTimeDiff = (System.nanoTime() - invincibleTimer)/1000000;
			if(invincibleTimeDiff > invicibleLength)
			{
				invicible =false;
				invincibleTimer = 0;
			}
		}

	}

	/**
	 * Draws the player
	 * 
	 * @param		g		the graphics to draw on
	 */
	public void draw(Graphics2D g)
	{
		Color color = recovering ? color2: (invicible ? color3: color1);
		g.setColor(color);
		g.fillOval(x-r, y-r, 2*r, 2*r);
		g.setStroke(new BasicStroke(3)); //makes our lines 3 pixels wide
		g.setColor(color.darker());
		g.drawOval(x-r, y-r, 2*r, 2*r);
		g.setStroke(new BasicStroke(1));
	}

}
