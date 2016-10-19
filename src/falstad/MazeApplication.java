/**
 * 
 */
package falstad;

import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JFrame;

import generation.Order;
import generation.Order.Builder;


/**
 * This class is a wrapper class to startup the Maze game as a Java application
 * 
 *
 * This code is refactored code from Maze.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 * 
 * TODO: use logger for output instead of Sys.out
 */
public class MazeApplication extends JFrame {

	// not used, just to make the compiler, static code checker happy
	private static final long serialVersionUID = 1L;

	private KeyListener kl ;

	private MazeController controller ;
	private RobotDriver robotDriver;
	private BasicRobot robot;
	/**
	 * Constructor
	 */
	public MazeApplication() {
		super() ;
		System.out.println("MazeApplication: maze will be generated with a randomized algorithm.");
		controller = new MazeController() ;
		init() ;
	}

	/**
	 * Constructor that loads a maze from a given file or uses a particular method to generate a maze
	 */
	public MazeApplication(String parameter) {
		super() ;
		// scan parameters
		
		// Case 1: Prim
		if ("Prim".equalsIgnoreCase(parameter))
		{
			System.out.println("MazeApplication: generating random maze with Prim's algorithm");
			controller = new MazeController(Order.Builder.Prim) ;
			init() ;
			return ;
		}
		
		// Case 2: Kruskal
		if ("Kruskal".equalsIgnoreCase(parameter))
		{
			System.out.println("MazeApplication: generating random maze with Kruskal's algorithm");
			controller = new MazeController(Order.Builder.Kruskal);
			init();
			return;
		}
		
		// Case 3: a file
		File f = new File(parameter) ;
		if (f.exists() && f.canRead())
		{
			System.out.println("MazeApplication: loading maze from file: " + parameter);
			controller = new MazeController(parameter) ;
			init();
			return ;
		}
		
		// Default case: 
		System.out.println("MazeApplication: unknown parameter value: " + parameter + " ignored, operating in default mode.");
		controller = new MazeController() ;
		init() ;
	}
	
	/**
	 * Same as other constructor but it also loads a robot driver
	 */
	public MazeApplication(String builder, String driver) 
	{
		if ("Prim".equalsIgnoreCase(builder)) {
			System.out.println("MazeApplication: generating random maze with Prim's algorithm");
			if ("Wallfollower".equalsIgnoreCase(driver)) {
				System.out.println("RUNNING WALLFLOWER ROBOT");
				this.robotDriver = new WallFollower();
				controller = new MazeController(Order.Builder.Prim, this.robotDriver);
			}
			init();
			return;
		}
		
		if ("Kruskal".equalsIgnoreCase(builder)) {
			System.out.println("MazeApplication: generating random maze with Kruskal's algorithm");
			controller = new MazeController(Order.Builder.Kruskal);
			if ("Wallfollower".equalsIgnoreCase(driver)) {
				System.out.println("RUNNING WALLFLOWER ROBOT");
				robotDriver = new WallFollower();
				controller = new MazeController(Order.Builder.Kruskal, this.robotDriver);

			}
			init();
			return;
		}
		
		File f = new File(builder) ;
		if (f.exists() && f.canRead())
		{
			System.out.println("MazeApplication: loading maze from file: " + builder);
			controller = new MazeController(builder) ;
			if ("Wallfollower".equalsIgnoreCase(driver)) {
				System.out.println("RUNNING WALLFLOWER ROBOT");
				robotDriver = new WallFollower();
				controller = new MazeController(builder, this.robotDriver) ;
			}
			init();
			return ;
		}
	}

	/**
	 * Initializes some internals and puts the game on display.
	 */
	private void init() {
		add(controller.getPanel()) ;
		
		kl = new SimpleKeyListener(this, controller) ;
		addKeyListener(kl) ;
		
		setSize(400, 400) ;
		setVisible(true) ;
		
		// focus should be on the JFrame of the MazeApplication and not on the maze panel
		// such that the SimpleKeyListener kl is used
		setFocusable(true) ;
		
		controller.init();
	}
	
	/**
     * Main method for Maze Application, takes care of cases where there are more than 1 parameter
     * If there are 2 Parameters its for -g builder so it takes the 2nd arg
     * If there are 3 parameters it means there is a filename and then -d driver so it takes the first and 3rd arg	
     * If there are 4 parameters then it is -g builder -d driver so it takes the 2nd and 4th arg
	 * @param args is optional, first parameter is a filename with a given maze
	 */
	public static void main(String[] args) {
		MazeApplication a ; 
		switch (args.length) {
		case 4: 
			a = new MazeApplication(args[1], args[3]);
			break;
		case 3: 
			a = new MazeApplication(args[0], args[2]);
			break;
		case 2 :
			a = new MazeApplication(args[1]);
			break ;
		case 1: 
			a = new MazeApplication(args[0]);
			break;
		case 0 : 
		default : 
			a = new MazeApplication() ;
			break ;
		}
		a.repaint() ;
	}

}
