package generation;

import falstad.Constants;

/**
 * This class has the responsibility to provide the distance for each cell to the exit of a maze.
 * It encapsulates the distance matrix.  
 * All methods assume that given (x,y) coordinates are with its legal range [0,width-1],[0,height-1] such that no 
 * additional parameter checks are performed. This is reasonable as this class is used only internally to the package.
 * 
 * This code is refactored code from MazeBuilder.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 */
public class Distance {
	// use of indices between Distance and Cells internal 2D matrices is consistent such that 
	// (i,j) in dists is the same position as in cells
	private int[][] dists; // matrix of dimension width x height with distance values to the exit of a maze
	private int width ; 	// width of distance matrix == width of maze and cells
	private int height ;	// height of distance matrix == height of maze and cells
	private int[] exitposition = null ;
	private int[] startposition = null ;
	private int maxDistance = 0 ;
	
	/**
	 * Constructor
	 * @param w is the width
	 * @param h is the height
	 */
	public Distance(int w, int h) {
		width = w ;
		height = h ;
		dists = new int[w][h] ;
	}
	/**
	 * Constructor
	 * @param w is the width
	 * @param h is the height
	 */
	public Distance(int[][] distances) {
		width = distances.length ;
		height = distances[0].length ;
		dists = distances ;
	}
	/**
	 * Gets access to a width x height array of distances. 
	 * Warning, this exposes the internal attribute for read access only. 
	 * Do not modify entries of the returned array.
	 * @return array with distance values
	 */
	public int[][] getDists() {
		return dists;
	}

	/**
	 * Sets the internal attribute to the given parameter value.
	 * Warning, the array is not copied but directly shared with the environment
	 * that provides it! This can have undesired side effects. Handle with care!
	 * @param dists
	 */
	public void setDists(int[][] dists) {
		this.dists = dists;
	}
	
	/**
	 * Gets the distance value for the given (x,y) position
	 * @param x
	 * @param y
	 * @return
	 */
	public int getDistance(int x, int y) {
		return dists[x][y] ;
	}

	/**
	 * Finds the most remote point in the maze somewhere on the border. 
	 * Requires that distances have been computed beforehand.
	 * @return array of length 2 gives the location (x,y)=(array[0],array[1])
	 */
	private int[] getPositionWithMaxDistanceOnBorder() {
		// (x,y) gives the current position
		int x; 
		int y;
		// find most remote point in maze somewhere on the border
		int remoteX = -1 ;
		int remoteY = -1 ;
		int remoteDist = 0;
		for (x = 0; x != width; x++) {
			y = 0 ;
			if (dists[x][y] > remoteDist) {
				remoteX = x;
				remoteY = y;
				remoteDist = dists[x][y];
			}
			y = height-1 ;
			if (dists[x][y] > remoteDist) {
				remoteX = x;
				remoteY = y;
				remoteDist = dists[x][y];
			}
		}
		for (y = 0; y != height; y++) {
			x = 0 ;
			if (dists[x][y] > remoteDist) {
				remoteX = x;
				remoteY = y;
				remoteDist = dists[x][y];
			}
			x = width-1 ;
			if (dists[x][y] > remoteDist) {
				remoteX = x;
				remoteY = y;
				remoteDist = dists[x][y];
			}
		}
		// return result in an array of length 2
		int[] result = new int[2] ;
		result[0] = remoteX ;
		result[1] = remoteY ;
		return result;
	}

	/**
	 * Get the position of the entry with the highest value
	 * @return array of length 2 encodes position (x,y)=(array[0],array[1])
	 */
	private int[] getPositionWithMaxDistance() {
		int x = 0 ;
		int y = 0 ;
		int d = 0;
		int[] result = new int[2] ;
		for (x = 0; x != width; x++)
			for (y = 0; y != height; y++) {
				if (dists[x][y] > d) {
					result[0] = x;
					result[1] = y;
					d = dists[x][y];
				}
			}
		maxDistance = d ; // memorize maximal distance for other purposes
		return result ;
	}
	/**
	 * Get the position of the entry with the smallest value. 
	 * If the values of the distance matrix are accurate, the result is the exit position.
	 * @return position with smallest distance
	 */
	private int[] getPositionWithMinDistance() {
		int x = 0 ;
		int y = 0 ;
		int d = INFINITY ;
		int[] result = new int[2] ;
		for (x = 0; x != width; x++)
			for (y = 0; y != height; y++) {
				if (dists[x][y] < d) {
					result[0] = x;
					result[1] = y;
					d = dists[x][y];
				}
			}
		return result ;
	}
	
	static final int INFINITY = Integer.MAX_VALUE; 

	/**
	 * Computes distances to the given position (ax,ay) for all cells in array dists.
	 * @param cells provide information on walls between positions
	 * @param ax, position, x coordinate
	 * @param ay, position, y coordinate
	 */
	private void computeDists(Cells cells, int ax, int ay) {
		int x, y;
		// initialize the distance array with a value for infinity 
		setAllDistanceValues(INFINITY) ;
		// set the final distance at the exit position
		dists[ax][ay] = 1;
		boolean done;
		// go over this array as long as we can find something to do
		// MEMO: there are likely to be much smarter ways to distribute distances in a breadth first manner...
		// why not push identified cells with infinite distance on a "work to do" heap
		do {
			done = true;
			// check all entries in the distance array
			for (x = 0; x != width; x++)
				for (y = 0; y != height; y++) 
				{
					int sx = x;
					int sy = y;
					if (dists[sx][sy] == INFINITY) { // found work to do. 
						// Since the maze must have a way to the exit from any position the distance cannot be infinite
						done = false;
						continue;
					}
					// if the distance is not infinite, let's see if the cell has a neighbor that we can update and 
					// perform a depth first search on.
					//int run = 0;
					while (true) {
						int nextn = updateNeighborDistancesAndGetSuccessor(cells, sx, sy);
						//run++;
						if (nextn == -1)
							break; // exit the loop if we cannot find another cell to proceed with
						// update coordinates for next cell
						sx += Constants.DIRS_X[nextn];
						sy += Constants.DIRS_Y[nextn];
						// follow the nextn node on a depth-first-search path
					}
				}
		} while (!done);
	}
	/**
	 * Sets all values in dists to given value
	 * @param value
	 */
	private void setAllDistanceValues(int value) {
		for (int x = 0; x != width; x++) {
			for (int y = 0; y != height; y++) {
				dists[x][y] = value ;
			}
		}
	}
	/**
	 * Updates distance values for adjacent cells that are reachable
	 * if value can be reduced to current distance plus 1. 
	 * @param cells
	 * @param currentX x coordinate of current position
	 * @param currentY y coordinate of current position
	 * @return direction index for a neighbor that has been updated or -1 if there is none
	 */
	private int updateNeighborDistancesAndGetSuccessor(Cells cells, int currentX, int currentY) {
		int result = -1;
		int nextDistance = dists[currentX][currentY] + 1; // distance of a neighbor
		// check all four directions, update distance as needed
		for (int i = 0; i != 4; i++) {
			int nextX = currentX+Constants.DIRS_X[i];
			int nextY = currentY+Constants.DIRS_Y[i];
			// check if cell at (nextX,nextY) is within bounds
			if ((0 <= nextX && nextX < width) && (0 <= nextY && nextY < height)) {
				// if there is no wall in this direction and 
				// the reachable neighbor cell has a higher distance value
				// update the neighbor's distance value and mark that cell as the next one
				if (cells.hasMaskedBitsFalse(currentX, currentY, Constants.MASKS[i]) &&
						dists[nextX][nextY] > nextDistance) {
					dists[nextX][nextY] = nextDistance;
					result = i;
				}
			}
		}
		return result;
	}
	
	/**
	 * Compute distances for given cells object of a maze
	 * @param cells with maze
	 * @return exit position somewhere on the  border
	 */
	public int[] computeDistances(Cells cells) {
		// compute temporary distances for a starting point (x,y) = (width/2,height/2) 
		// which is located in the center of the maze
		computeDists(cells, width/2, height/2);
		// figure out which position is the furthest on the border to find an exit position
		exitposition = getPositionWithMaxDistanceOnBorder();
		// recompute distances for an exit point (x,y) = (remotex,remotey)
		computeDists(cells, exitposition[0], exitposition[1]);

		return exitposition ;
	}

	/**
	 * Gets start position
	 * @precondition computeDistances() was called before
	 * @return start position somewhere within maze
	 */
	public int[] getStartPosition() {
		if (null == startposition)
			startposition = getPositionWithMaxDistance() ;
		return startposition ;
	}
	/**
	 * Gets exit position
	 * @precondition computeDistances() was called before
	 * @return exit position somewhere on the border
	 */
	public int[] getExitPosition() {
		if (null == exitposition)
			exitposition = getPositionWithMinDistance() ;
		return exitposition ;
	}
	/**
	 * Gets maximum distance present in maze
	 * @precondition computeDistances() was called before
	 * @return maximum distance
	 */
	public int getMaxDistance() {
		return maxDistance ;
	}
	/**
	 * Determines if given position is the exit position
	 */
	public boolean isExitPosition(int x, int y){
		return ((x == exitposition[0]) && (y == exitposition[1])) ;
	}
}