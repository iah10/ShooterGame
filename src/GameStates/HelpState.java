package GameStates;
/*
 * The Help State
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
 * The Help state helps the player to know the controls of the game 
 * 
 * @author Issa Haddar
 * @since 1.2(09-Feb-2014)
 */
public class HelpState extends GameState 
{

	/**** INSATNACE FIELDS ****/
	private int  currentChoice;	//the users choice
	private String[] options = {	//the choices
			"Start",
			"Back",
			"Quit"
	};
	private String[] Help = {	//the choices
			"How To Play: ",
			"   Use The arrows to move in all directions.",
			"   Use the z-button to shoot.",
			"   Use the p-button to pause and the c-button to resume",
			"   When you kill enemies you get a chance for a power up.",
			"The Power ups:",
			"    The pink powerup is an extra life.",
			"    The yellow powerups increase your power.",
			"    The green powerup temporarily increase the bullet power.",
			"    The blue powerup makes you temporarily invincible.",
			"    The white powerup temporarily slows the enemies down.",
			"    The big red powerup is massive boom the kills all the enemies.",
			"HAVE FUN :-)"
	};

	private Font choicesFont;
	private Color textColor;
	private Font textFont;
	/*************************/

	/**
	 * The Constructor
	 * @param gameStateManager
	 */
	public HelpState(GameStateManager gameStateManager) {
		this.Gsm = gameStateManager;
	}

	@Override
	public void init() 
	{
		textColor = new Color(102,0,51);
		textFont = new Font("Century Gothic", Font.PLAIN,12);
		choicesFont = new Font("Arial", Font.PLAIN, 24);
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
		
		//draw the Text
		g.setFont(textFont);
		g.setColor(textColor);
		for (int i = 0; i < Help.length; i++) 
		{
			g.drawString(Help[i], 10, 15 +29*i);
		}
		
		//draw menu options
		g.setFont(choicesFont);
		for(int i = 0; i< options.length; i++)
		{
			if(i == currentChoice)
				g.setColor(Color.BLACK);
			else
				g.setColor(Color.RED);
			g.drawString(options[i], 20-options[i].length()*2 + i*150, GamePanel.HEIGHT-10);

		}
	}

	/**
	 * Switch to different states
	 */
	private void select() 
	{
		if(currentChoice==0)	//start
			Gsm.setState(GameStateManager.Game);
		if(currentChoice==1)	//back
			Gsm.setState(GameStateManager.MENUSTATE);
		if(currentChoice==2)	//exit
			System.exit(0);
	}
	
	/**
	 * Mainpulation of options
	 */
	@Override
	public void keyPressed(int k) 
	{
		switch (k) 
		{
		case KeyEvent.VK_ENTER:
			select();
			break;
		case KeyEvent.VK_LEFT:
			currentChoice--;
			if(currentChoice == -1)
				currentChoice = options.length-1;
			break;
		case KeyEvent.VK_RIGHT:
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
