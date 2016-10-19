package generation;

import falstad.SingleRandom;

/**
 * Basic class to describe a wall which is located at a cell (x,y) and at that cell it is
 * located in a particular direction. One can compute the location of a neighboring cell,
 * however that location is only valid for internal wall, i.e. if the neighboring cell is inside the maze.
 * 
 * It is used to hold wall coordinates for Prims Maze Generation and for the logging mechanism.
 */
public class Wall {
	// Cell location (x,y) pair.
	private int x;
	private int y;
	private int[] d; // direction (dx,dy) pair

	/**
	 * Constructor, values have same effect has setWall(x,y,cd).
	 * @param x is the x coordinate, 0 <= x < width
	 * @param y is the y coordinate, 0 <= y < height
	 * @param cd is the direction of wall in the cell
	 */
	public Wall(int x, int y, CardinalDirection cd)
	{
		this.x = x;
		this.y = y;
		d = cd.getDirection();
	}
	/**
	 * Sets the internal fields to the given values for a (x,y)
	 * position and direction
	 * @param x is the x coordinate, 0 <= x < width
	 * @param y is the y coordinate, 0 <= y < height
	 * @param cd is the direction
	 */
	public void setWall(int x, int y, CardinalDirection cd)
	{
		this.x = x;
		this.y = y;
		d = cd.getDirection();
	}
	/**
	 * Get the x coordinate for the current (x,y) position.
	 * @return the x coordinate
	 */
	public int getX() {
		return x;
	}
	/**
	 * Get the y coordinate for the current (x,y) position.
	 * @return the y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Pick a random position (x,y) and a random direction within the 
	 * given limits and assign these values to this wall.
	 * @param width such that 0 <= x < width
	 * @param height such that 0 <= y < height
	 */
	public void setRandomly(int width, int height) {
		// pick position (x,y) with x being random, y being random
		SingleRandom random = SingleRandom.getRandom() ;
		x = random.nextIntWithinInterval(0, width-1) ;
		y = random.nextIntWithinInterval(0, height - 1);
		// pick a direction, 
		d = CardinalDirection.East.randomDirection().getDirection() ;
	}
	/**
	 * Computes the x coordinate of neighboring (adjacent) cell for internal walls.
	 * If the wall is a border wall to the outside, then the resulting value is 
	 * out of range as the cell does not exist.
	 * @return the x coordinate of adjacent cell
	 */
	public int getNeighborX() {
		return x+d[0] ;
	}
	/**
	 * Computes the y coordinate of neighboring (adjacent) cell for internal walls.
	 * If the wall is a border wall to the outside, then the resulting value is 
	 * out of range as the cell does not exist.
	 * @return  the y coordinate of adjacent cell
	 */
	public int getNeighborY() {
		return y+d[1] ;
	}
	/**
	 * Provides the direction for the wall with regard to the 
	 * internal position (x,y).
	 * @return the direction of this wall with regard to its cell location
	 */
	public CardinalDirection getDirection() {
		return CardinalDirection.East.getDirection(d[0], d[1]) ;
	}
}
