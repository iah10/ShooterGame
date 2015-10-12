package Entities;
/**
 * Issa haddar
 * Sunday 22-Dec-2013
 * @author iah10
 * @version 1.1
 * The Explosion class
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import GameStates.TheGame;

public class Explosion
{
	/**** INSTANCE FIELDS ****/
	private double x;
	private double y;
	private int r;
	private int maxRadius;
	/************************/

	/**
	 * The Constructor
	 * @param x
	 * @param y
	 * @param r
	 * @param maxRadius
	 */
	public Explosion(double x, double y, int r, int maxRadius) 
	{
		this.x = x;
		this.y = y;
		this.r = r;
		this.maxRadius = maxRadius;
	}

	/**----------------------------------------------functions---------------------------------------------------------**/

	/**
	 * Updates the explosion
	 * @return true if we want to remove the explosion
	 */
	public boolean update()
	{
		r +=3;
		if(r >=maxRadius)
			return true;
		return false;
	}

	/**
	 * Draws the Explosion
	 * @param g
	 */
	public void draw(Graphics2D g)
	{
		Color c = Color.white;
		if(TheGame.waveNumber==11 || TheGame.waveNumber==12 || TheGame.waveNumber==13)
		{
			int r1 = (int)(Math.random()*256);           
			int g1 =(int) (Math.random()*256);    //generating colors according to RGB                       
			int b1 = (int)(Math.random()*256);
			c = new Color(r1 ,g1 ,b1);
		}
		g.setColor(c);
		g.setStroke(new BasicStroke(2));
		g.drawOval((int)(x-r), (int)(y-r), 2*r, 2*r);
		g.setStroke(new BasicStroke(1));
	}

}
