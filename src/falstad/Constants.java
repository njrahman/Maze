package falstad;
/**
 * This class contains all constants that are used in the maze package and shared among several classes.
 * TODO: localize CW_* settings in generation package.
 * @author Kemper
 *
 */
public class Constants {

	// Integer constants to encode 4 possible walls for a single cell (CW = cell wall) on top, bottom, left, right in a single byte of an integer
	// WARNING: The numerical values are used for bit operations and thus matter, they encode a particular bit pattern for & and | operations 
	public static final int CW_TOP = 1;  // 2^0
	public static final int CW_BOT = 2;  // 2^1
	public static final int CW_LEFT = 4; // 2^2
	public static final int CW_RIGHT = 8;// 2^3
	public static final int CW_VISITED = 16;	 // 2^4 is used as a flag to indicate if a cell is new (1) or if it has been visited before (0)
	public static final int CW_ALL = CW_TOP|CW_BOT|CW_LEFT|CW_RIGHT; // constant to simplify checking if all walls are present
	// Integer constants to encode 4 possible sides that touch a border (or bound)
	// a separate encoding of borders allows for flexible layouts (not just rectangles)
	// Note: encoding matches the wall encoding with respect to directions such that same encoding applies plus a shift
	public static final int CW_BOUND_SHIFT = 5; // used to shift encoding from dirsx, dirsy below from wall range to bound range 
	public static final int CW_TOP_BOUND = 32; // 2^5
	public static final int CW_BOT_BOUND = 64; // 2^5
	public static final int CW_LEFT_BOUND = 128; // 2^7
	public static final int CW_RIGHT_BOUND = 256; // 2^8
	public static final int CW_ALL_BOUNDS = CW_TOP_BOUND|CW_BOT_BOUND|CW_LEFT_BOUND|CW_RIGHT_BOUND; // constant to simplify check if all all bounds are present
	public static final int CW_IN_ROOM = 512; // 2^9
	// we put all encodings into a single array such that it is easier to iterate over the array
	// note that the numerical values are used for bitwise calculations so a refactoring with other values in an enumeration can break the code
	public static final int[] MASKS = { CW_RIGHT, CW_BOT, CW_LEFT,  CW_TOP };
	
	// The panel used to display the maze has a fixed dimension
	public static final int VIEW_WIDTH = 400;
	public static final int VIEW_HEIGHT = 400;
	public static final int MAP_UNIT = 128;
	public static final int VIEW_OFFSET = MAP_UNIT/8;
	public static final int STEP_SIZE = MAP_UNIT/4;
	// Skill-level 
	// The user picks a skill level between 0 - 9, a-f 
	// The following arrays transform this into corresponding dimensions (x,y) for the resulting maze as well as the number of rooms and parts
	public static int[] SKILL_X =     { 4, 12, 15, 20, 25, 25, 35, 35, 40, 60, 70, 80, 90, 110, 150, 300 };
	public static int[] SKILL_Y =     { 4, 12, 15, 15, 20, 25, 25, 35, 40, 60, 70, 75, 75,  90, 120, 240 };
	public static int[] SKILL_ROOMS = { 0,  2,  2,  3,  4,  5, 10, 10, 20, 45, 45, 50, 50,  60,  80, 160 };
	public static int[] SKILL_PARTCT = { 60, 600, 900, 1200, 2100, 2700, 3300,
	5000, 6000, 13500, 19800, 25000, 29000, 45000, 85000, 85000*4 };
	// Directions:
	// columns mean right, bottom, left, top (as implemented in getBit())
	// note that multiplication with -1 to a column switches directions
	public static int[] DIRS_X = { 1, 0, -1, 0 };
	public static int[] DIRS_Y = { 0, 1, 0, -1 };
	// Possible states of the GUI
	// these are states of an automaton that the graphical user interface implements
	public enum StateGUI { STATE_TITLE, STATE_GENERATING, STATE_PLAY, STATE_FINISH; }
	// fixing a value matching the escape key
	final static int ESCAPE = 27;
	

}
