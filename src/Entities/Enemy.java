package Entities;
/**
 * Issa haddar
 * Sunday 22-Dec-2013
 * @author iah10
 * @version 1.1
 * The Enemy class
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import GameStates.TheGame;
import Panel.GamePanel;

public class Enemy 
{
	/**** INSTANCE FIELDS ****/
	protected double x;
	protected double y;
	private int r;

	protected double dx;
	protected double dy;
	protected double rad;
	protected double speed;

	protected int health;
	private int type;
	private int rank;

	private Color color1;

	private boolean ready;
	protected boolean dead;
	protected boolean hit;
	protected long hitTimer;

	protected boolean slow;
	/************************/

	/**
	 * The Constructor
	 * 
	 * @param type
	 * @param rank
	 */
	public Enemy(int type , int rank, boolean slow)
	{
		this.type = type;
		this.rank = rank;
		this.slow = slow;
		if(type==1)
		{
			//BLUE;
			color1 = new Color(0,0,100,128);
			if(rank ==1)
			{
				speed = 2;
				r = 5;
				health = 1;
			}
			if(rank ==2)
			{
				speed=2;
				r=10;
				health= 2;
			}
			if(rank ==3)
			{
				speed=1.5;
				r=20;
				health= 3;
			}
			if(rank ==4)
			{
				speed=1.5;
				r=30;
				health= 4;
			}
		}
		//stronger , faster 
		if(type==2)
		{
			//RED;
			color1 = new Color(255,0,0,128);
			if(rank ==1)
			{
				speed=8;
				r=5;
				health =2;
			}
			if(rank ==2)
			{
				speed=5;
				r=10;
				health= 3;
			}
			if(rank ==3)
			{
				speed=5;
				r=20;
				health= 3;
			}
			if(rank ==4)
			{
				speed=5;
				r=30;
				health= 4;
			}
		}
		//slow, but hard to kill
		if(type==3)
		{
			//GREEN;
			color1 = new Color(0,255,0,128);
			if(rank ==1)
			{
				speed=1.5;
				r=5;
				health =5;
			}
			if(rank ==2)
			{
				speed=1.5;
				r=10;
				health= 6;
			}
			if(rank ==3)
			{
				speed=1.5;
				r=25;
				health= 7;
			}
			if(rank ==4)
			{
				speed=1.5;
				r=45;
				health= 8;
			}
		}
		if (type==4) 
		{
			if (rank==1) {
				//gray
				color1 = Color.gray;
				speed = 15;
				health = 2;
				r = 4;
			}
			else
			{
				color1 = Color.orange;
				speed = 20;
				health = 4;
				r = 4;
			}
		}
		x = Math.random() * GamePanel.WIDTH/2 + GamePanel.WIDTH /4;
		y = -r;

		double angle = Math.random() * 140 +20;
		rad = Math.toRadians(angle);

		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;
	}

	/*-----------------------------------------------functions----------------------------------------------------------*/

	public double getX() { return x; }
	public double getY() { return y; }
	public int getR() { return r; }
	public int getType() { return type; }
	public int getRank() { return rank; }
	public boolean getSlow() { return slow; }
	public boolean isDead() { return dead; } 
	public double getHealth(){ return health; }

	public void setSlow(boolean b) { slow = b; }


	/**
	 * hit the enemy
	 */
	public void hit(int power)
	{
		health-=power;
		if(health <=0)
			dead =true;
		hit = true;
		hitTimer = System.nanoTime();
	}

	/**
	 * 
	 */
	public void explode()
	{
		if(rank > 1)
		{
			int amount =0;
			if(type == 1 || type==2) 
				amount = 3;
			if(type == 3) 
				amount = 4;

			for(int i=0; i < amount; i++)
				newEnemies();
		}
	}
	
	public void newEnemies() 
	{
		Enemy e = new Enemy(getType(), getRank()-1, getSlow());
		e.x= this.x;
		e.y = this.y;
		double angle = 0;

		if(!ready)
			angle = Math.random()*140 +20;
		else 
			angle = Math.random() * 360;

		e.rad = Math.toRadians(angle);
		TheGame.enemies.add(e);
	}

	/**
	 * update the enemy 
	 */
	public void update()
	{
		if(slow)
		{
			x += dx * 0.3;
			y += dy * 0.3;
		}
		else 
		{
			x+= dx;
			y += dy;
		}
		if(!ready && x > r && x < GamePanel.WIDTH -r && y > r && y <GamePanel.HEIGHT - r) 
			ready = true;
		if(x < r && dx < 0) dx = -dx;
		if(y < r && dy < 0) dy = -dy;
		if(x > GamePanel.WIDTH -r && dx > 0) dx = -dx;
		if(y > GamePanel.HEIGHT -r && dy > 0) dy = -dy;

		if(hit) 
		{
			long elapsed = (System.nanoTime() - hitTimer)/1000000;
			if(elapsed > 50)
			{
				hit = false;
				hitTimer=0;
			}
		}
	}

	/**
	 * Draws an enemy
	 * @param g - to draw on
	 */
	public void draw(Graphics2D g)
	{
		if(hit)
		{
			g.setColor(Color.WHITE);
			g.fillOval((int)(x-r), (int)(y-r), 2*r, 2*r);
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.WHITE.darker());
			g.fillOval((int)(x-r), (int)(y-r), 2*r, 2*r);
			g.setStroke(new BasicStroke(1));
		}
		else
		{
			g.setColor(color1);
			g.fillOval((int)(x-r), (int)(y-r), 2*r, 2*r);
			g.setStroke(new BasicStroke(3));
			g.setColor(color1.darker());
			g.fillOval((int)(x-r), (int)(y-r), 2*r, 2*r);
			g.setStroke(new BasicStroke(1));
		}
	}
}