package Entities;
/**
 * Issa haddar
 * Sunday 22-Dec-2013
 * @author iah10
 * @version 1.1
 * The text class
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Text 
{
	/**** INSTANCE FIELDS ****/
	private double x;
	private double y;
	private long time;
	private String s;
	private long start;
	/************************/

	/**
	 * The Constructor
	 * 
	 * @param x
	 * @param y
	 * @param time
	 * @param s
	 */
	public Text(double x, double y, long time, String s) 
	{
		this.x = x;
		this.y = y;
		this.time = time;
		this.s = s;
		start = System.nanoTime();
	}
	
	/**----------------------------------------------functions---------------------------------------------------------**/
	
	/**
	 * Updates the text on the screen
	 * @return true if we want to remove the text
	 */
	public boolean update()
	{
		long elapsed = (System.nanoTime() - start)/1000000;
		if(elapsed > time)
			return true;
		return false;
	}
	
	/**
	 * Draws the text on the screen
	 * @param g
	 */
	public void draw(Graphics2D g)
	{
		g.setFont(new Font("Century Gothic", Font.PLAIN, 12));
		long elapsed = (System.nanoTime() - start)/1000000;
		int alpha = (int)(255*Math.sin(3.14 *elapsed/time));
		if(alpha >=255 || alpha <= 0) alpha = 254;
		g.setColor(new Color(255,255,255,alpha));
		int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, (int)(x- (length/2)),(int) y);
	}
	
	
}
