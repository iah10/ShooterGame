package GameStates;
/*
 * The Menu State
 * 
 * 
 * 
 * 
 * 
 * 
 * Issa Haddar. CopyRight ©™  Issa Haddar. All Rights reserved.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Panel.GamePanel;


/**
 * The Menu state introduces the player to the game
 * 
 * @author Issa Haddar
 * @since 1.2(09-Feb-2014)
 */
public class MenuState extends GameState
{

	/**** INSATNACE FIELDS ****/
	private int  currentChoice;	//the users choice
	private String[] options = {	//the choices
			"Start",
			"Help",
			"Quit"
	};
	private Color titleColor;
	private Font titleFont;
	private Font font;
	/*************************/

	public MenuState(GameStateManager gameStateManager) 
	{
		this.Gsm = gameStateManager;
		titleColor = new Color(128, 0, 0);
		titleFont = new Font("Century Gothic", Font.PLAIN,40);
		font = new Font("Arial", Font.PLAIN, 24);
	}

	/*----------------------------------------------functions---------------------------------------------------------*/

	/**
	 * Switch to different states
	 */
	private void select() 
	{
		if(currentChoice==0)	//start
			Gsm.setState(GameStateManager.Game);
		if(currentChoice==1)	//help
			Gsm.setState(GameStateManager.HELP);
		if(currentChoice==2)	//exit
			System.exit(0);
	}

	@Override
	public void init() {

	}

	@Override
	public void update() {

	}

	@Override
	public void draw(Graphics2D g)
	{
		//draw Background
		g.setColor(new Color(0, 204, 255));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		//draw My Name
		g.setColor(new Color(102,0,51));
		g.setFont(new Font("Century Gothic", Font.ITALIC, 12));
		g.drawString("Issa Haddar. CopyRight ©™  Issa Haddar. All Rights reserved.", 10, GamePanel.HEIGHT-10);

		//draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Shooter Game", 80 - "Shooter Game".length()*2, 80);

		//draw menu options
		g.setFont(font);
		for(int i = 0; i< options.length; i++)
		{
			if(i == currentChoice)
				g.setColor(Color.BLACK);
			else
				g.setColor(Color.RED);
			g.drawString(options[i], GamePanel.WIDTH/2-options[i].length()*4, GamePanel.HEIGHT/2 + i*30);

		}
	}

	@Override
	public void keyPressed(int k) 
	{
		switch (k) 
		{
		case KeyEvent.VK_ENTER:
			select();
			break;
		case KeyEvent.VK_UP:
			currentChoice--;
			if(currentChoice == -1)
				currentChoice = options.length-1;
			break;
		case KeyEvent.VK_DOWN:
			currentChoice++;
			if(currentChoice == options.length)
				currentChoice = 0;
			break;

		}
	}

	@Override
	public void keyReleased(int k) {

	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}
}