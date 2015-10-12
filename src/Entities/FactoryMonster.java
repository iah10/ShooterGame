package Entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import GameStates.TheGame;
import Panel.GamePanel;


/**
 * A monster that produces enemies in a given duration of time
 * @author  Issa Haddar
 */
public class FactoryMonster extends Enemy 
{
	/**** INSTANCE FIELDS ****/
	//the left circle and the rectangle points
	private Point rectangleCoordinates;
	private Point circle;

	private int radius;
	private int width;
	private int height;

	private boolean left;	
	private boolean right;

	private boolean producing;
	private long producingTimer;
	private long producingDelay;

	private Color circlesColor;
	private Color rectangleColor;
	/************************/
	/**
	 * The Constructor
	 */
	public FactoryMonster() 
	{
		super(40, 40, false);
		health = 400;
		speed = 6;

		width = 70;
		height = 70;
		radius = 35;

		right = true;

		rectangleCoordinates = new Point(2*radius, 60);
		circle = new Point((int) (rectangleCoordinates.getX()+width/2-1 - radius), (int) (rectangleCoordinates.getY()-radius));
		x = rectangleCoordinates.x + width/2;
		y= rectangleCoordinates.y + height/2;

		producing = true;
		producingTimer = System.nanoTime();
		producingDelay = 4000;

		circlesColor = Color.PINK;
		rectangleColor = Color.RED;
	}

	public Rectangle getMainRectangle(){
		return new Rectangle(rectangleCoordinates.x, rectangleCoordinates.y-radius, width, height+radius);
	}

	@Override
	public void update() 
	{
		//position update
		if (left)
			dx  =  slow ?-0.3*speed : -speed;
		if (right)
			dx = slow ? 0.3*speed:speed;

		rectangleCoordinates.setLocation(rectangleCoordinates.getX() + dx, rectangleCoordinates.getY());
		circle.setLocation(circle.getX()+dx, circle.getY());
		x +=dx;

		//left bounds
		if(rectangleCoordinates.getX()< 1)
		{
			left = false;
			right = true;
		}
		//right down
		if (rectangleCoordinates.getX() + width > GamePanel.WIDTH)
		{
			left = true;
			right = false;
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
		//firing update
		if(producing)
		{
			long elapsed = slow ? (System.nanoTime() - producingTimer)/3000000:(System.nanoTime() - producingTimer)/1000000;
			if(elapsed > producingDelay)
			{
				producingTimer= System.nanoTime();
				Random rnd = new Random();
				int type = rnd.nextInt(3)+1;
				int rank = rnd.nextInt(4)+1;
				Enemy e = new Enemy(type,rank, false);
				TheGame.enemies.add(e);
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
			g.fillOval((int)circle.getX(), (int)circle.getY(), 2*radius, 2*radius);
		}
		else
		{
			g.setColor(rectangleColor);
			g.fillRect((int)rectangleCoordinates.getX(), (int)rectangleCoordinates.getY(),width,height);
			g.setColor(circlesColor);
			g.fillOval((int)circle.getX(), (int)circle.y , 2*radius,2* radius);
		}
	}

	public Point getRectanglePoint() {
		return rectangleCoordinates;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
