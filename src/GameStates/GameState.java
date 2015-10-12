package GameStates;

import java.awt.Graphics2D;

public abstract class GameState
{
	/*------ INSATNACE FIELDS ------*/
	
	protected GameStateManager Gsm; // the game state manager

	/*------------------------------*/

	/*------ ABSTRACT METHODS -------*/
	public abstract void init(); // Initialize the backgrounds an d other images

	public abstract void update(); // updates

	public abstract void draw(Graphics2D g); // draw

	public abstract void keyPressed(int k); // input from the user

	public abstract void keyReleased(int k);

	public abstract void restart();
	/*----------------------------------------*/
}
