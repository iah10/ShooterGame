package Entities;
/**
 * Issa haddar
 * Sunday 22-Dec-2013
 * @author iah10
 * @version 1.1
 * The Power-Up class
 */


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import Panel.GamePanel;

public class PowerUp
{
	/**** INSTANCE FIELDS ****/
	private double x;
	private double y;
	private int r;

	private int type;

	private Color color1;
	private Color color2;
	/************************/

	/**
	 * The Constructor
	 * 
	 * @param x
	 * @param y
	 * @param type
	 */
	public PowerUp(double x, double y, int type) 
	{
		this.x = x;
		this.y = y;
		this.type = type;

		switch (type) 
		{
		case 1:	// 1-- +1 life
			color1 = Color.PINK;
			r=5;
			break;
		case 2:	//2 -- +1 power
			color1 = Color.YELLOW;
			r=3;
			break;
		case 3: //3 -- +3 power
			color1 = Color.YELLOW;
			r=5;
			break;
		case 4: // 4--slow down time
			color1 = Color.WHITE;
			r=3;
			break;
		case 5: // 5--powers the bullet
			color1 = Color.GREEN;
			r=4;
			break;
		case 6:	//6--makes the enemy invincible against bullets and enemies
			color1 = Color.CYAN;
			r = 4;
			break;
		case 7:	//7--kills all the enemies except the monsters
			color1 = Color.red.darker();
			color2 = Color.YELLOW;
			r =10;
			break;
		}

	}

	/**----------------------------------------------functions---------------------------------------------------------**/

	public double getX() { return x; }
	public double getY() { return y; }
	public int getR() { return r; }
	public int getType() { return type; }

	/**
	 * Updates the power-up position
	 * moves downwards at a speed of 2
	 * 
	 * @return false if the power-up went out of bounds
	 */
	public boolean update()
	{
		y+=2;
		if(y> GamePanel.HEIGHT +r)
			return true;
		return false;
	}

	/**
	 * Draws the power-up
	 * @param g-the graphics to draw on
	 */
	public void draw(Graphics2D g)
	{
		g.setColor(color1);
		g.fillRect((int)(x-r), (int)(y-r), 2*r, 2*r);
		g.setStroke(new BasicStroke(3));
		if(getType() == 7)
			g.setColor(color2);
		else
			g.setColor(color1.darker());
		g.drawRect((int)(x-r), (int)(y-r), 2*r, 2*r);
		g.setStroke(new BasicStroke(1));
	}
}
