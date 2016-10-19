/**
 * 
 */
package generation;



/**
 * A MazeConfiguration encapsulates all relevant information about a maze 
 * that can be explored in a game. 
 * A maze has dimensions width and height.
 * 
 * @author pk
 *
 */
public interface MazeConfiguration {
	/**
	 * Set the width of the maze.
	 * @param width is greater or equal zero.
	 */
	void setWidth(int width);
	/**
	 * Set the height of the maze.
	 * @param height is greater or equal zero.
	 */
	void setHeight(int height);
	/**
	 * Get the height of the maze.
	 * @return the height 
	 */
	int getHeight();
	/**
	 * Get the width of the maze.
	 * @return the width 
	 */
	int getWidth();
	
	/**
	 * Gets the cells which describe where walls are in the current maze.
	 * @return the mazecells
	 */
	Cells getMazecells();

	/**
	 * Sets the cells which describe where walls are in the current maze.
	 * @param mazecells the mazecells to set
	 */
	void setMazecells(Cells mazecells);

	/**
	 * Gets a distance object for this maze to describe 
	 * for each position how many steps it is towards the exit.
	 * @return the mazedists
	 */
	Distance getMazedists();

	/**
	 * Sets the distance values towards the exit for this maze.
	 * Note that the dimensions of the distance matrix needs to match 
	 * with the cells.
	 * @param mazedists the distances to set
	 */
	void setMazedists(Distance mazedists) ;

	/**
	 * Gets access to a tree of nodes for segments of walls which is
	 * used for drawing the currently visible part.
	 * @return the rootnode
	 */
	BSPNode getRootnode();

	/**
	 * Sets the tree of nodes for segments of walls.
	 * @param rootnode the rootnode to set
	 */
	void setRootnode(BSPNode rootnode);
	/**
	 * Tells if (x,y) coordinate is within range.
	 * @param x is on the horizontal axis
	 * @param y is on the vertical axis
	 * @return true if 0 <= x < width, 0 <= y < height
	 */
	boolean isValidPosition(int x, int y);
	/**
	 * Tells how many steps it is from the given (x,y) coordinate
	 * to the exit position. 
	 * @param x is on the horizontal axis, 0 <= x < width
	 * @param y is on the vertical axis, 0 <= y < height
	 * @return the length of path to the exit
	 */
	int getDistanceToExit(int x, int y);
	/**
	 * Tells if one faces a wall at position (x,y) looking into the 
	 * given direction. Note that the cardinal direction is absolute
	 * and not relative to the current direction such as right or left.  
	 * @param x is on the horizontal axis, 0 <= x < width
	 * @param y is on the vertical axis, 0 <= y < height
	 * @param dir is the direction in terms of North, East, South, West
	 * @return true if (x,y) is valid and there is a wall in the given direction, false otherwise
	 */
	public boolean hasWall(int x, int y, CardinalDirection dir) ;
	/**
	 * Provides coordinates of a position adjacent to the given (x,y)
	 * position that has a distance to the exit that is less than
	 * the distance for the given (x,y) position. For a maze that has a 
	 * solution for (x,y) such a position always exists with the 
	 * exception of the exit position.
	 * @param x is on the horizontal axis, 0 <= x < width
	 * @param y is on the vertical axis, 0 <= y < height
	 * @return int array of length 2 with (x',y') coordinates for neighbor
	 */
	public int[] getNeighborCloserToExit(int x, int y);
	/**
	 * Provides coordinates (x,y) of the starting position for this maze.
	 * Maze generation algorithms are expected to use the position
	 * that is the farthest away from the exit as the starting position, 
	 * i.e., the one with the maximum distance.
	 */	
	public int[] getStartingPosition();
	/**
	 * Sets coordinates (x,y) of the starting position for this maze.
	 * Maze generation algorithms are expected to use the position
	 * that is the farthest away from the exit as the starting position, 
	 * i.e., the one with the maximum distance.
	 * @param x is on the horizontal axis, 0 <= x < width
	 * @param y is on the vertical axis, 0 <= y < height
	 */
	public void setStartingPosition(int x, int y);
	
	/**
	 * Tells if at the given (x,y) position, the bitmask agrees
	 * with the internally stored bitmask. 
	 * TODO: this method operates at a different level of abstraction as others. Legacy trouble.
	 * @param x is on the horizontal axis, 0 <= x < width
	 * @param y is on the vertical axis, 0 <= y < height
	 * @param bitmask
	 * @return true if (x,y) is valid and a bitwise AND with bitmask is not zero
	 */
	public boolean hasMaskedBitsTrue(int x, int y, int bitmask) ;
}
