package Entities;
/**
 * Issa haddar
 * Sunday 22-Dec-2013
 * @author iah10
 * @version 1.1
 * The bullet class
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import Panel.GamePanel;

public class Bullet 
{
	/**** INSTANCE FILEDS ****/

	private double x;
	private double y;
	private int type;
	private int r;

	private double rad;
	private double speed;
	private int power;
	private double dx;
	private double dy;

	private Color color1;
	private Color color2;
	/**********************/

	/**
	 * The Constructor
	 * 
	 * @param angle of shooting (always upwards)
	 * @param x - player position
	 * @param y - player position 
	 */
	public Bullet(double angle, int x, int y,int radius, int power,int tpye)
	{
		this.x = x;
		this.y = y;
		type = tpye;
		r = radius;

		rad = Math.toRadians(angle);
		this.power = power;
		speed= 10;
		dx = Math.cos(rad)*speed;
		dy = Math.sin(rad)*speed;
		
		color1 = Color.YELLOW;
		color2 = Color.BLACK;

	}

	/**----------------------------------------------functions---------------------------------------------------------**/

	public double getX() { return x; }
	public double getY() { return y; }
	public double getR() { return r; }
	public int getType() { return type; }
	public int getPower() { return power; }

	public void setR(int r) { this.r =r; }
	public void setPower(int pow){ power = pow; }
	public Rectangle getRectangle(){
		return new Rectangle((int)x, (int)y, r,r);
	}

	/**
	 * Updates the position of the bullet
	 */
	public boolean update()
	{
		x += dx;
		y += dy;

		if( x < -r || x > GamePanel.WIDTH + r || y < -r  || y > GamePanel.HEIGHT + r)
			return true;
		return false;

	}
	/**
	 * Draws the bullet
	 * @param g
	 */
	public void draw(Graphics2D g)
	{
		if(type != 0)	//type zero is for the enemy of the last level
		{
			g.setColor(color1);
			g.fillOval((int)(x-r), (int)(y-r), 2*r, 2*r);
		}
		else
		{
			g.setColor(color2);
			g.fillRect((int)(x-r), (int)(y-r), 2*r, 2*r);
		}

	}
}
