/**
 * 
 */
package generation;

import falstad.Constants;

/**
 * Class encapsulates access to all information that constitutes a maze.
 * 
 * @author pk
 *
 */
public class MazeContainer implements MazeConfiguration {
	// properties of the current maze
	private int width; // width of maze
	private int height; // height of maze
	private Cells mazecells ; // maze as a matrix of cells which keep track of the location of walls
	private Distance mazedists ; // a matrix with distance values for each cell towards the exit
	private BSPNode rootnode ; // a binary tree type search data structure to quickly locate a subset of segments
	// a segment is a continuous sequence of walls in vertical or horizontal direction
	// a subset of segments need to be quickly identified for drawing
	// the BSP tree partitions the set of all segments and provides a binary search tree for the partitions
	private int[] start ;
	/**
	 * 
	 */
	public MazeContainer() {
		// TODO Auto-generated constructor stub
	}

	public void setWidth(int width) {
		this.width = width;
	}
	public int getWidth() {
		return width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getHeight() {
		return height;
	}

	/**
	 * Gives the cells.
	 * Warning, returns direct access to internal field.
	 * @return the mazecells
	 */
	public Cells getMazecells() {
		return mazecells;
	}

	/**
	 * @param mazecells the mazecells to set
	 */
	public void setMazecells(Cells mazecells) {
		this.mazecells = mazecells;
	}

	/**
	 * Gives the distance. 
	 * Warning, returns direct access to internal field.
	 * @return the mazedists
	 */
	public Distance getMazedists() {
		return mazedists;
	}

	/**
	 * Sets the distance.
	 * @param mazedists the mazedists to set
	 */
	public void setMazedists(Distance mazedists) {
		this.mazedists = mazedists;
	}

	/**
	 * Gives the rootnode for the tree of BSPnodes.
	 * Warning, returns direct access to internal field.
	 * @return the rootnode
	 */
	public BSPNode getRootnode() {
		return rootnode;
	}

	/**
	 * Sets the root for the tree of BSPnodes
	 * @param rootnode the rootnode to set
	 */
	public void setRootnode(BSPNode rootnode) {
		this.rootnode = rootnode;
	}
	/**
	 * Tells if given (x,y) position is valid, i.e. within legal range of values
	 * @param x is on the horizontal axis 
	 * @param y is on the vertical axis 
	 * @return true if 0 <= x < width and 0 <= y < height, false otherwise
	 */
	public boolean isValidPosition(int x, int y) {
		return ((0 <= x && x < width) && (0 <= y && y < height));
	}
	/**
	 * Gives the number of steps or moves needed to get to the exit.
	 * @param x is on the horizontal axis, 0 <= x < width
	 * @param y is on the vertical axis, 0 <= y < height
	 * @return number of steps to exit
	 */
	public int getDistanceToExit(int x, int y) {
		return mazedists.getDistance(x, y) ;
	}
	/**
	 * Tells if at position (x,y) and looking into given direction faces a wall.
	 * @param x is on the horizontal axis, 0 <= x < width
	 * @param y is on the vertical axis, 0 <= y < height
	 * @return true if there is a wall, false otherwise
	 */
	public boolean hasWall(int x, int y, CardinalDirection dir) {
		return this.mazecells.hasWall(x, y, dir) ;
	}
	/**
	 * Tells if at position (x,y) bits are set as given by bitmask.
	 * @param x is on the horizontal axis, 0 <= x < width
	 * @param y is on the vertical axis, 0 <= y < height
	 * @return true if bits are set, false otherwise
	 */ 
	public boolean hasMaskedBitsTrue(int x, int y,int bitmask) {
		//return this.mazecells.hasMaskedBitsTrue(x,y,Constants.MASKS[n]) ;
		//System.out.println("MazeContainer.hasMaskedBitsTrue: " + x + ", " + y + ", " + bitmask + " returns: " + mazecells.hasMaskedBitsTrue(x,y,bitmask)) ;
		return this.mazecells.hasMaskedBitsTrue(x,y,bitmask) ;
	}
	/**
	 * Gives a (x',y') neighbor for given (x,y) that is closer to exit
	 * if it exists. 
	 * @param x is on the horizontal axis, 0 <= x < width
	 * @param y is on the vertical axis, 0 <= y < height
	 * @return array with neighbor coordinates if neighbor exists, null otherwise
	 */
	public int[] getNeighborCloserToExit(int x, int y) {
		int[] result = new int[2] ;
		int[] dir = getDirectionToExit(x, y);
		if (null == dir)
			return null ;
		result[0] = x+dir[0] ;
		result[1] = y+dir[1] ;
		return result ;
	}
	/**
	 * Find (dx,dy) for direction towards exit
	 * @param x is on the horizontal axis, 0 <= x < width
	 * @param y is on the vertical axis, 0 <= y < height
	 * @return int array for (dx,dy) if neighbor closer to exit exists, null otherwise
	 */
	private int[] getDirectionToExit(int x, int y) {
		//System.out.println("MapDrawer: new getDirectionToSolution at: " + x + ", "+ y ) ;
		// find best candidate
		int dnext = getDistanceToExit(x, y) ;
		int[] dir = new int[2] ;
		for (int n = 0; n < 4; n++) {
			if (hasMaskedBitsTrue(x,y,Constants.MASKS[n]))
				continue; // there is a wall
			// no wall, let's check the distance
			int dx = Constants.DIRS_X[n];
			int dy = Constants.DIRS_Y[n];
			int dn = getDistanceToExit(x+dx, y+dy);
			if (dn < dnext) {
				// update minimum
				dir[0] = dx ;
				dir[1] = dy ;
				dnext = dn ;
			}	
		}
		if (getDistanceToExit(x, y) <= dnext)
		{
			System.out.println("ERROR: MazeContainer.getDirectionToSolution cannot identify direction towards solution: stuck at: " + x + ", "+ y ) ;
			// TODO: perform proper error handling here
			return null ;
		}
		return dir;
	}

	/**
	 * Provides the (x,y) starting position.
	 * The starting position is typically chosen to by furthest away from the exit.
	 * @return the start
	 */
	public int[] getStartingPosition() {
		return start;
	}

	/**
	 * Sets the starting position
	 * @param start the start to set
	 */
	public void setStartingPosition(int[] start) {
		assert (null != start && start.length == 2) : "MazeContainer.start illegal parameter value";
		assert this.isValidPosition(start[0], start[1]) : "Invalid starting position";
		this.start = start;
	}
	/** 
	 * Sets the starting position
	 * @param x is on the horizontal axis, 0 <= x < width
	 * @param y is on the vertical axis, 0 <= y < height
	 */
	public void setStartingPosition(int x, int y) {
		assert this.isValidPosition(x,y) : "Invalid starting position";
		if (null == start)
			start = new int[2] ;
		start[0] = x ;
		start[1] = y ;
	}
}
