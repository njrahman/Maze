package generation;

import falstad.Constants;
import falstad.SingleRandom;

/**
 * Represents absolute directions as for a map to match with the orientation
 * on the screen, i.e. North is no the top of the screen.
 * In addition to limit the set of possible values, this enum supports operations
 * such as rotateClockwise and getOppositeDirection.
 * Several translation methods between cardinal directions and CW constants and 
 * dx,dy pairs are supported to facilitate its integration in current code base.
 * 
 * States: March 2016, coded, needs integration and testing
 * 
 * @author pk
 *
 */
public enum CardinalDirection {
	North, East, South, West ;
	/** 
	 * Gives the direction that results from a 90 degree clockwise rotation
	 * applied to the current direction. 
	 * @return direction after 90 degree clockwise rotation
	 */
	public CardinalDirection rotateClockwise() {
		switch(this) {
		case North : 
			return CardinalDirection.East ;
		case East : 
			return CardinalDirection.South ;
		case South : 
			return CardinalDirection.West ;
		case West : 
			return CardinalDirection.North ;
		default:
			throw new RuntimeException("Inconsistent enum type") ;
		}
	}
	/** 
	 * Gives the direction that results from a 90 degree counter clockwise rotation
	 * applied to the current direction. 
	 * @return direction after 90 degree counter clockwise rotation
	 */
	public CardinalDirection rotateCounterClockwise(){
		switch(this) {
		case North:
			return CardinalDirection.West;
		case East:
			return CardinalDirection.North;
		case West:
			return CardinalDirection.South;
		case South:
			return CardinalDirection.East;
		default:
			throw new RuntimeException("Inconsistent enum type");
		}
	}

	/** 
	 * Gives the opposite direction which is the same as applying a 180 degree
	 * rotation. 
	 * @return direction that is opposite to the current direction
	 */
	public CardinalDirection oppositeDirection() {
		switch(this) {
		case North : 
			return CardinalDirection.South ;
		case East : 
			return CardinalDirection.West ;
		case South : 
			return CardinalDirection.North ;
		case West : 
			return CardinalDirection.East ;
		default:
			throw new RuntimeException("Inconsistent enum type") ;
		}
	}
	/**
	 * Gives a random direction. Values are picked with equal probabilities.
	 * @return a random direction, distribution is uniform
	 */
	public CardinalDirection randomDirection() {
		int i = SingleRandom.getRandom().nextIntWithinInterval(0, 3) ;
		switch(i) {
		case 0 : 
			return CardinalDirection.North ;
		case 1 : 
			return CardinalDirection.East ;
		case 2 : 
			return CardinalDirection.South ;
		case 3 : 
			return CardinalDirection.West ;
		default:
			throw new RuntimeException("Random variable out of bounds: " + i) ;
		}
	}
	
	/**
	 * Gives the matching direction for a bit encoded value.
	 * Matches with CW_TOP, BOT, LEFT, RIGHT constants in Constants.java.
	 * TOP is matched with North, Right is matched with East.
	 * @param i is a bit encoded value for a direction
	 * @return matching cardinal direction
	 */
	public CardinalDirection getDirectionForCWConstant(int i) {
		/* compare with Constants.java for consistency
		static final int CW_TOP = 1;  // 2^0
		static final int CW_BOT = 2;  // 2^1
		static final int CW_LEFT = 4; // 2^2
		static final int CW_RIGHT = 8;// 2^3
		 */
		//System.out.println("CardinalDirection.getDirection: Warning: check consistency with direction on screen") ;
		switch(i) {
		case Constants.CW_TOP : 
			return CardinalDirection.North ;
		case Constants.CW_RIGHT : 
			return CardinalDirection.East ;
		case Constants.CW_BOT : 
			return CardinalDirection.South ;
		case Constants.CW_LEFT : 
			return CardinalDirection.West ;
		default:
			throw new IllegalArgumentException("Illegal input value: " + i) ;
		}
	}
	/**
	 * Gives the matching bit encoded value, i.e.,
	 * the matching CW_TOP, BOT, LEFT, RIGHT constants in Constants.java
	 * for the current direction.
	 * TOP is matched with North, Right is matched with East.
	 * @return the matching integer value (CW_ constant)
	 */ 
	public int getCWConstantForDirection() {
		/* compare with Constants.java for consistency
		static final int CW_TOP = 1;  // 2^0
		static final int CW_BOT = 2;  // 2^1
		static final int CW_LEFT = 4; // 2^2
		static final int CW_RIGHT = 8;// 2^3
		 */
		switch(this) {
		case North : 
			return Constants.CW_TOP ;
		case East : 
			return Constants.CW_RIGHT ;
		case South : 
			return Constants.CW_BOT ;
		case West : 
			return Constants.CW_LEFT ;
		default:
			throw new RuntimeException("Unsupported value in enum type") ;
		}
	}
	/**
	 * Gives the matching direction for array position index in Constants.DIRS arrays.
	 * @param i array index to obtain a (dx,dy) pair from DIRS_X, DIRS_Y
	 * @return matching cardinal direction
	 */
	public CardinalDirection getDirectionForDirsArrayIndex(int i) {
		/* Compare with Constants.java for consistency
		 Directions: right=east, down=south, left=west, up=north
		public static int[] DIRS_X = { 1, 0, -1, 0 };
		public static int[] DIRS_Y = { 0, 1, 0, -1 };
		 */
		System.out.println("CardinalDirection.getDirection: Warning: check consistency with direction on screen") ;
		switch(i) {
		case 0 : 
			return CardinalDirection.East ;
		case 1 : 
			return CardinalDirection.South ;
		case 2 : 
			return CardinalDirection.West ;
		case 3 : 
			return CardinalDirection.North ;
		default:
			throw new IllegalArgumentException("Illegal input value: " + i) ;
		}
	}
	/**
	 * Gives the matching direction for (dx,dy) pair as in Constants.DIRS arrays.
	 * @param (dx,dy) pair from DIRS_X, DIRS_Y
	 * @return matching cardinal direction
	 */
	public CardinalDirection getDirection(int dx, int dy) {
		/* Compare with Constants.java for consistency
		 Directions: right=east, down=south, left=west, up=north
		public static int[] DIRS_X = { 1, 0, -1, 0 };
		public static int[] DIRS_Y = { 0, 1, 0, -1 };
		 */
		//System.out.println("CardinalDirection.getDirection: Warning: check consistency with direction on screen") ;
		switch(dx) {
		case -1 : // must by (-1,0)
			return CardinalDirection.West ;
		case 0 : 
			if (dy == 1) // is (0,1)
				return CardinalDirection.South ;
			if (dy == -1) // is (0,-1)
				return CardinalDirection.North ;
			throw new IllegalArgumentException("Illegal input value for dx: " + dx) ;
		case 1 : // must be (1,0)
			return CardinalDirection.East ;
		default:
			throw new IllegalArgumentException("Illegal input value for dx: " + dx) ;
		}
	}
	/**
	 * Gives the (dx,dy) pair as in Constants.DIRS arrays for the current direction
	 * @return (dx,dy) pair from DIRS_X, DIRS_Y
	 */
	public int[] getDirection() {
		/* Compare with Constants.java for consistency
		 Directions: right=east, down=south, left=west, up=north
		public static int[] DIRS_X = { 1, 0, -1, 0 };
		public static int[] DIRS_Y = { 0, 1, 0, -1 };
		 */
		int[] result = new int[2] ;
		switch(this) {
		case North : 
			result[0] = 0 ;
			result[1] = -1 ;
			break ;
		case East : 
			result[0] = 1 ;
			result[1] = 0 ;
			break ;
		case South : 
			result[0] = 0 ;
			result[1] = 1 ;
			break ;
		case West : 
			result[0] = -1 ;
			result[1] = 0 ;
			break ;
		default:
			throw new RuntimeException("Inconsistent enum type") ;
		}
		return result ;
	}
}
