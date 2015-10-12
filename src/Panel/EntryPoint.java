/*
 * The Entry point
 * main method
 * 
 * 
 * 
 * 
 * 
 * Issa Haddar. CopyRight ©™  Issa Haddar. All Rights reserved.
 */
package Panel;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * Issa haddar
 * Sunday 22-Dec-2013
 * @author iah10
 * @version 1.1
 * The entry point of the game
 */
public class EntryPoint 
{
	/**
	 * The main method
	 * @param args
	 */
	public static void main(String[] args) 
	{
		//Creating  a new frame and passing to it Game Panel
		JFrame window = new JFrame("First Game");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//set a blank cursor
		BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank cursor");
		
		window.setCursor(blank);
		window.getContentPane().add(new GamePanel());
		window.setIgnoreRepaint(true); // turn off paint events since doing active rendering
		window.setResizable(false);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}