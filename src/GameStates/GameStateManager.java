package GameStates;
/*
 * The Game Manager
 *  Switch between the game states
 * 
 * 
 * 
 * 
 * 
 * Issa Haddar. CopyRight ©™  Issa Haddar. All Rights reserved.
 */

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Issa Haddar
 * 201307043
 * @author iah10
 * @version 1.1 (09-Feb-2014)
 * A class that has that controls the levels of the game
 */
public class GameStateManager 
{

	/**** INSATNACE FIELDS ****/
	private ArrayList<GameState> gameStates;	//the levels of the game 
	private int currentState;					//current level
	
	public static final int MENUSTATE = 0;
	public static final int Game  = 1;
	public static final int HELP = 2;
	/**************************/
	
	/**
	 * The Constructor
	 */
	public GameStateManager()
	{
		gameStates = new ArrayList<GameState>();
		currentState = MENUSTATE;
		gameStates.add(new MenuState(this));
		gameStates.add(new TheGame(this));
		gameStates.add(new HelpState(this));
		
	}
	
	/**
	 * Method setState.
	 * @param state int
	 */
	public void setState(int state)
	{
		currentState = state;
		gameStates.get(currentState).init();
	}
	
	public void update()
	{
		gameStates.get(currentState).update();
	}
	
	/**
	 * Method draw.
	 * @param g Graphics2D
	 */
	public void draw(Graphics2D g)
	{
		gameStates.get(currentState).draw(g);
	}
	
	/**
	 * Method keyPressed.
	 * @param k int
	 */
	public void keyPressed(int k)
	{
		gameStates.get(currentState).keyPressed(k);
	}
	
	/**
	 * Method keyReleased.
	 * @param k int
	 */
	public void keyReleased(int k)
	{
		gameStates.get(currentState).keyReleased(k);
	}

	/**
	 * Restart The Game
	 */
	public void restart() 
	{
		gameStates.get(Game).restart();
	}

}
