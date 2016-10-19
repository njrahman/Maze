package falstad;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class implements a wrapper for the user input handled by the Maze class. 
 * The MazeApplication attaches the listener to the GUI, such that user keyboard input
 * flows from GUI to the listener.keyPressed to the MazeController.keyDown method.
 *
 * This code is refactored code from Maze.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 */
public class SimpleKeyListener implements KeyListener {

	private Container parent ;
	private MazeController controller ;
	
	SimpleKeyListener(Container parent, MazeController controller){
		this.parent = parent ;
		this.controller = controller ;
	}
	/**
	 * Translates keyboard input to the corresponding characters for the Maze.keyDown method.
	 * 
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
		//System.out.println("1: Communicating key: " + arg0.getKeyText(arg0.getKeyCode()) + " with key char: " + arg0.getKeyChar() + " with code: " + arg0.getKeyCode());
		int key = arg0.getKeyChar() ;
		int code = arg0.getKeyCode() ;

		if (KeyEvent.CHAR_UNDEFINED == key)
		{
			if ((KeyEvent.VK_0 <= code && code <= KeyEvent.VK_9)||(KeyEvent.VK_A <= code && code <= KeyEvent.VK_Z))
				key = code ;
			if (KeyEvent.VK_ESCAPE == code)
				key = Constants.ESCAPE ; // use internal encoding for escape signal
			if (KeyEvent.VK_UP == code)
				key = 'k' ; // reduce duplicate encodings
			if (KeyEvent.VK_DOWN == code)
				key = 'j' ; // reduce duplicate encodings
			if (KeyEvent.VK_LEFT == code)
				key = 'h' ; // reduce duplicate encodings
			if (KeyEvent.VK_RIGHT == code)
				key = 'l' ; // reduce duplicate encodings
		}
		//System.out.println("Calling keydown with " + key) ;
		// possible inputs for key: unicode char value, 0-9, A-Z, Escape, 'k','j','h','l' plus CTRL-W
		controller.keyDown(key) ;
		parent.repaint() ;
	
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// nothing to do
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// NOTE FOR THIS TYPE OF EVENT IS getKeyCode always 0, so Escape etc is not recognized	
		// this is why we work with keyPressed
	}

}
