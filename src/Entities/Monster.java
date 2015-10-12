package Entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import GameStates.TheGame;
import Panel.GamePanel;

/**
 * Issa Haddar
 * 201307043
 * @author iah10
 * @version 1.2(08-Feb-14)
 * The Monster of the last level
 * It fires bullets and has a huge power of 30
 */
public class Monster extends Enemy
{

	/**** INSTANCE FIELDS ****/
	//the left circle and the rectangle points
	private Point rectangleCoordinates;
	private Point rightCircle;

	private int radius;
	private int width;
	private int height;

	private boolean left;	
	private boolean right;

	private Color circlesColor;
	private Color rectangleColor;

	private boolean firing;
	private long firingTimer;
	private long firingDelay;
	/************************/
	public Monster()
	{
		super(50, 50, false);
		health = 600;
		speed = 4;

		radius = 25;
		width = 90;
		height = 60;

		left = true;
		right = false;

		rectangleCoordinates = new Point(2*radius, 60);
		rightCircle = new Point((int) (rectangleCoordinates.getX()+width-1 - radius), (int) (rectangleCoordinates.getY()-radius));
		x = rectangleCoordinates.x + width/2;
		y= rectangleCoordinates.y + height/2;

		firing = true;
		firingTimer = System.nanoTime();
		firingDelay = 300;

		circlesColor = Color.orange;
		rectangleColor = Color.green;
	}

	public int getWidth(){ return width; }
	public int getHeight(){return height;}
	public Point getRectanglePoint(){ return rectangleCoordinates;}

	public Rectangle getMainRectangle(){
		return new Rectangle(rectangleCoordinates.x, rectangleCoordinates.y, width, height);
	}

	public Rectangle getLeftRectangle(){
		return new Rectangle(rectangleCoordinates.x, rectangleCoordinates.y, 2*radius, 2*radius);
	}

	public Rectangle getRightRectangle(){
		return new Rectangle(rightCircle.x, rightCircle.y, 2*radius, 2*radius);
	}

	@Override
	public void update()
	{
		//position update
		if (left) dx  =  slow ?-0.3*speed : -speed;
		if (right) dx = slow ? 0.3*speed:speed;
		x+=dx;

		rectangleCoordinates.setLocation(rectangleCoordinates.getX() + dx, rectangleCoordinates.getY());
		rightCircle.setLocation(rightCircle.getX()+dx, rightCircle.getY());

		if(rectangleCoordinates.getX()< radius) {
			left = false;
			right = true;
		}
		if (rightCircle.getX() + 2*radius > GamePanel.WIDTH) {
			left = true;
			right = false;
		}

		//firing update
		if(firing)
		{
			long elapsed = slow? (System.nanoTime() - firingTimer)/3000000:(System.nanoTime() - firingTimer)/1000000;
			if(elapsed > firingDelay)
			{
				int x = (int) rectangleCoordinates.getX();
				int y = (int) (rectangleCoordinates.getY()+ height);
				firingTimer = System.nanoTime();
				TheGame.bullets.add(new Bullet(90, x+7,y,2,1,0));
				TheGame.bullets.add(new Bullet(90, x-7,y,2,1,0));
				TheGame.bullets.add(new Bullet(90, x+7+width,y,2,1,0));
				TheGame.bullets.add(new Bullet(90, x-7+width,y,2,1,0));
			}
		}

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

	@Override
	public void draw(Graphics2D g)
	{
		if (hit)
		{
			g.setColor(Color.WHITE);
			g.fillRect((int)rectangleCoordinates.getX(), (int)rectangleCoordinates.getY(),width,height);
			g.fillOval((int)rectangleCoordinates.getX() - radius, (int)rectangleCoordinates.getY() - radius, 2*radius, 2*radius);
			g.fillOval((int)rightCircle.getX(), (int)rightCircle.getY(), 2*radius, 2*radius);
		}
		else
		{
			if(Math.random() > 0.86)
			{
				int r1 = (int)(Math.random()*256);           
				int g1 =(int) (Math.random()*256);    //generating colors according to RGB                       
				int b1 = (int)(Math.random()*256);
				circlesColor = new Color(r1 ,g1 ,b1);
			}
			g.setColor(rectangleColor);
			g.fillRect((int)rectangleCoordinates.getX(), (int)rectangleCoordinates.getY(),width,height);
			g.setColor(circlesColor);
			g.fillOval((int)rectangleCoordinates.getX() - radius, (int)rectangleCoordinates.getY() - radius, 2*radius, 2*radius);
			g.fillOval((int)rightCircle.getX(), (int)rightCircle.getY(), 2*radius, 2*radius);
		}
	}

}
