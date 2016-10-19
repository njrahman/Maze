package generation;

import falstad.Constants;

import java.util.ArrayList;


/**
 * This class has the responsibility to obtain the tree of BSP nodes for a given maze.
 * 
 * This code is refactored code from MazeBuilder.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 *
 */
public class BSPBuilder {
	private final int width ; 				// width of maze
	private final int height ; 				// height of maze
	private final Distance dists ; 			// distance matrix
	private final Cells cells ;				// cells in maze
	private final int colchange ;			// comes from a random number, purpose unclear, 
	// colchange: reason for randomization unclear, used to determine color of segment 
	private final int expectedPartiters ; 	// comes from Constants partct array, entry chosen according to skill level
	// only usage is in updateProgressBar to estimate progress made in the BSP tree construction
	int partiters = 0 ; // relocated from MazeBuilder attribute partiters here. 
	private final Order order ; 		// current order
	/**
	 * Constructor
	 * @param order
	 * @param dists
	 * @param cells
	 * @param width
	 * @param height
	 * @param colchange
	 * @param expectedPartiters
	 */
	public BSPBuilder(Order order, Distance dists, Cells cells, int width, int height, int colchange, int expectedPartiters) {
		super() ;
		this.order = order ;
		this.dists = dists ;
		this.cells = cells ;
		this.width = width ;
		this.height = height ;
		this.colchange = colchange ;
		this.expectedPartiters = expectedPartiters ;

		partiters = 0 ; // counter for keeping track of progress made in BSP calculation, starts at 0
	}

	/**
	 * Method generates the nodes. In every node, it has two section, left and right. It chooses the segment
	 * which has the minimum grade value and then split this node into two nodes through this segment.
	 * If all the segments in one node are partitioned, it will stop to split.
	 * @param sl
	 * @return root node for BSP tree
	 * @throws InterruptedException 
	 */
	private BSPNode genNodes(ArrayList<Seg> sl) throws InterruptedException {
		// if there is no segment with a partition bit set to false, there is nothing else to do and we are at a leaf node
		if (countNonPartitions(sl) == 0)
			return new BSPLeaf(sl);
		// from the ones that have a partition bit set to false, pick a candidate with a low grade
		Seg pe = findPartitionCandidate(sl);
		// work on segment pe
		// mark pe as partitioned
		pe.setPartition(true);
		final int x  = pe.getStartPositionX();
		final int y  = pe.getStartPositionY();
		final int dx = pe.getExtensionX();
		final int dy = pe.getExtensionY();
		final ArrayList<Seg> lsl = new ArrayList<Seg>();
		final ArrayList<Seg> rsl = new ArrayList<Seg>();
		for (int i = 0; i != sl.size(); i++) {
			Seg se = (Seg) sl.get(i);
			int df1x = se.getStartPositionX() - x;
			int df1y = se.getStartPositionY() - y;
			int sendx = se.getEndPositionX();
			int sendy = se.getEndPositionY();
			int df2x = sendx - x; 
			int df2y = sendy - y; 
			int nx = dy;
			int ny = -dx;
			int dot1 = df1x * nx + df1y * ny;
			int dot2 = df2x * nx + df2y * ny;
			if (getSign(dot1) != getSign(dot2)) {
				if (dot1 == 0)
					dot1 = dot2;
				else if (dot2 != 0) {
					// we need to split this
					int spx = se.getStartPositionX();
					int spy = se.getStartPositionY();
					if (dx == 0)
						spx = x;
					else
						spy = y;
					//Seg sps1 = new Seg(se.x, se.y, spx-se.x, spy-se.y, se.dist, colchange);
					//Seg sps2 = new Seg(spx, spy, sendx-spx, sendy-spy, se.dist, colchange);
					Seg sps1 = new Seg(se.getStartPositionX(), se.getStartPositionY(), spx-se.getStartPositionX(), spy-se.getStartPositionY(), se.getDistance(), colchange);
					Seg sps2 = new Seg(spx, spy, sendx-spx, sendy-spy, se.getDistance(), colchange);
					if (dot1 > 0) {
						rsl.add(sps1);
						lsl.add(sps2);
					} else {
						rsl.add(sps2);
						lsl.add(sps1);
					}
					sps1.setPartition(se.isPartition());
					sps2.setPartition(se.isPartition());
					continue;
				}
			}
			// decide where to add current segment
			if (dot1 > 0 || (dot1 == 0 && se.getDir() == pe.getDir())) {
				rsl.add(se);
				if (dot1 == 0)
					se.setPartition(true);
			} else if (dot1 < 0 || (dot1 == 0 && se.getDir() == -pe.getDir())) { 
				lsl.add(se);
				if (dot1 == 0)
					se.setPartition(true);
			} else {
				dbg("error xx 1 "+dot1);
			}
		}
		if (lsl.size() == 0)
			return new BSPLeaf(rsl);
		if (rsl.size() == 0)
			return new BSPLeaf(lsl);
		return new BSPBranch(x, y, dx, dy, genNodes(lsl), genNodes(rsl)); // recursion on both branches
	}

	/**
	 * Counts how many elements in the segment vector have their partition bit set to false
	 * @param sl all segments
	 * @return number of segments where the partition flag is not set
	 */
	private static int countNonPartitions(ArrayList<Seg> sl) {
		int result = 0 ;
		for (int i = 0; i != sl.size(); i++)
		{
			if (!(sl.get(i)).isPartition())
				result++;
		}
		return result;
	}

	/**
	 * It finds the segment which has the minimum grade value.
	 * @param sl vector of segment
	 * @return Segment that is best candidate according to grade partition (smallest grade)
	 * @throws InterruptedException 
	 */
	private Seg findPartitionCandidate(ArrayList<Seg> sl) throws InterruptedException {
		Seg pe = null ;
		int bestgrade = 5000; // used to compute the minimum of all observed grade values, set to some high initial value
		final int maxtries = 50; // constant, only used to determine skip
		// consider a subset of segments proportional to the number of tries, here 50, seems to randomize the access a bit
		int skip = (sl.size() / maxtries);
		if (skip == 0)
			skip = 1;
		for (int i = 0; i < sl.size(); i += skip) {
			Seg pk = (Seg) sl.get(i);
			// skip segments where the partition flag was set
			if (pk.isPartition())
				continue;
			// provide feedback for progress bar every 32 iterations
			partiters++;
			if ((partiters & 31) == 0) {
				updateProgressBar(partiters); // side effect: update progress bar
			}
			// check grade and keep track of minimum
			int grade = grade_partition(sl, pk);
			if (grade < bestgrade) {
				bestgrade = grade;
				pe = pk; // determine segment with smallest grade
			}
		}
		return pe;
	}

	/**
	 * Push information on progress into maze such that UI can update progress bar
	 * @param partiters
	 */
	private void updateProgressBar(int partiters) throws InterruptedException {
		// During maze generation, the most time consuming part needs to occasionally update the current screen
		// 
		int percentage = partiters*100/expectedPartiters ;
		if (null != order) {
			order.updateProgress(percentage) ;
			if (percentage < 100) {
				// give main thread a chance to process keyboard events
				Thread.currentThread().sleep(10);
			}
		}
	}

	/**
	 * Set the partition bit to true for segments on the border and where the direction is 0
	 * @param sl
	 */
	private void setPartitionBitForCertainSegments(ArrayList<Seg> sl) {
		for (Seg se : sl) {
			se.updatePartitionIfBorderCase(width, height);
		}
	}



	/**
	 * Identifies segments of continuous walls on the maze and fills the segment list 
	 * @return vector of segments
	 */
	private ArrayList<Seg> generateSegments() {
		ArrayList<Seg> sl = new ArrayList<Seg>();

		generateSegmentForHorizontalWalls(sl); 

		generateSegmentsForVerticalWalls(sl);
		// starting positions for segments seem to be chosen such that segments represent top or left walls
		return sl ;
	}

	/**
	 * Identify segments of continuous walls in a vertical direction
	 * @param sl
	 */
	private void generateSegmentsForVerticalWalls(ArrayList<Seg> sl) {
		int x;
		int y;
		// we search for vertical walls, so for each row
		for (x = 0; x != width; x++) {
			y = 0;
			while (y < height) {
				// find the beginning of a segment
				if (cells.hasNoWall(x,y, CardinalDirection.West)) {
					y++;
					continue;
				} 
				int starty = y;
				// find the end of a segment
				y = findEndOfVerticalSegment(x, y, CardinalDirection.West);
				// create segment with (x,starty) being being the actual start position of the segment, y-starty being the positive length
				sl.add(new Seg(x*Constants.MAP_UNIT, starty*Constants.MAP_UNIT,
						0, (y-starty)*Constants.MAP_UNIT, dists.getDistance(x, starty), colchange));
			}
			y = 0;
			while (y < height) {
				// find the beginning of a segment
				if (cells.hasNoWall(x,y, CardinalDirection.East)) {
					y++;
					continue;
				} 
				int starty = y;
				// find the end of a segment
				y = findEndOfVerticalSegment(x, y, CardinalDirection.East);
				// create segment with (x+1,y) being being one off in both directions from the last cell in this segment, starty-y being the negative length
				// since we are looking at right walls, one off in the right direction (x+1) are then cells that have this segment on its left hand side
				// for some reason the end position is used as a starting position and therefore the length & direction is inverse 
				sl.add(new Seg((x+1)*Constants.MAP_UNIT, y*Constants.MAP_UNIT,
						0, (starty-y)*Constants.MAP_UNIT, dists.getDistance(x, starty), colchange));
			}
		}
	}

	private int findEndOfVerticalSegment(int x, int y, CardinalDirection segmentOnThisSide) {
		while (cells.hasWall(x, y, segmentOnThisSide)) {
			y++;
			if (y == height)
				break;
			if (cells.hasWall(x, y, CardinalDirection.North))
				break;
		}
		return y;
	}
	private int findEndOfHorizontalSegment(int x, int y, CardinalDirection segmentOnThisSide) {
		while (cells.hasWall(x,y, segmentOnThisSide)) {
			x++;
			if (x == width)
				break;
			if (cells.hasWall(x,y, CardinalDirection.West))
				break;
		}
		return x;
	}
	/**
	 * Identify segments of continuous walls in a horizontal direction
	 * @param sl
	 */
	private void generateSegmentForHorizontalWalls(ArrayList<Seg> sl) {
		int x;
		int y;
		// we search for horizontal walls, so for each column
		for (y = 0; y != height; y++) {
			// first round through rows
			x = 0;
			while (x < width) {
				// find the beginning of a segment
				if (cells.hasNoWall(x,y, CardinalDirection.North)) {
					x++;
					continue;
				} 
				// found one
				int startx = x;
				// find the end of a segment
				// follow segment with wall on top till
				// x is the first index of a cell that has no wall on top
				// stop at outer bound or when hitting a wall (cell has wall on left)
				// such that length of the segment is startx-x, which is a negative value btw
				x = findEndOfHorizontalSegment(x, y, CardinalDirection.North);
				// create segment with (x,y) being the end positions, startx-x being the negative length
				// note the (x,y) is not part of the segment
				sl.add(new Seg(x*Constants.MAP_UNIT, y*Constants.MAP_UNIT,
						(startx-x)*Constants.MAP_UNIT, 0, dists.getDistance(startx, y), colchange));
			}
			// second round through rows, same for bottom walls
			x = 0;
			while (x < width) {
				// find the beginning of a segment
				if (cells.hasNoWall(x,y, CardinalDirection.South)) {
					x++;
					continue;
				} 
				int startx = x;
				// find the end of a segment
				x = findEndOfHorizontalSegment(x, y, CardinalDirection.South);
				// create segment with (startx,y+1) being one below the start position, x-startx being the positive length
				// so this may represent a bottom wall segment as a top wall segment one below
				sl.add(new Seg(startx*Constants.MAP_UNIT, (y+1)*Constants.MAP_UNIT,
						(x-startx)*Constants.MAP_UNIT, 0, dists.getDistance(startx, y), colchange));
			}
		}
	}



	/**
	 * Method called in genNodes to determine the minimum of all such grades. 
	 * The method is static, i.e. it does not update internal attributes and just calculates the returned value.
	 * @param sl vector of segments
	 * @param pe particular segment
	 * @return undocumented
	 */
	private int grade_partition(ArrayList<Seg> sl, Seg pe) {
		// copy attributes of parameter pe
		final int x  = pe.getStartPositionX();
		final int y  = pe.getStartPositionY();
		final int dx = pe.getExtensionX();
		final int dy = pe.getExtensionY();
		final int inc = (sl.size() >= 100) ? sl.size() / 50 : 1 ; // increment for iteration below
		// define some local counter
		int lcount = 0, rcount = 0, splits = 0;
		// check all segments, loop calculates lcount, rcount and splits
		for (int i = 0; i < sl.size(); i += inc) {
			Seg se = (Seg) sl.get(i);
			// extract information from segment
			int df1x = se.getStartPositionX()-x; // difference between beginning of segment and x
			int df1y = se.getStartPositionY()-y; // difference between beginning of segment and y
			int sendx = se.getEndPositionX(); // end of segment
			int sendy = se.getEndPositionY(); // end of segment
			int df2x = sendx - x; // difference between end of segment and x
			int df2y = sendy - y; // difference between end of segment and y
			int nx = dy;
			int ny = -dx;
			int dot1 = df1x * nx + df1y * ny;
			int dot2 = df2x * nx + df2y * ny;
			// update splits if necessary
			if (getSign(dot1) != getSign(dot2)) {
				if (dot1 == 0)
					dot1 = dot2;
				else if (dot2 != 0) {
					splits++;
					continue;
				}
			}
			// update lcount, rcount values
			if (dot1 > 0 ||
					(dot1 == 0 && se.getDir() ==  pe.getDir())) {
				rcount++;
			} else if (dot1 < 0 ||
					(dot1 == 0 && se.getDir() == -pe.getDir())) {
				lcount++;
			} else {
				dbg("grade_partition problem: dot1 = "+dot1+", dot2 = "+dot2);
			}
		}
		return Math.abs(lcount-rcount) + splits * 3;
	}
	/**
	 * Generate tree of BSP nodes for a given maze
	 * @return
	 * @throws InterruptedException 
	 */
	public BSPNode generateBSPNodes() throws InterruptedException {
		// determine segments, i.e. walls over multiple cells in a vertical or horizontal direction
		ArrayList<Seg> seglist = generateSegments();

		setPartitionBitForCertainSegments(seglist); // partition bit true means that those are not considered any further for node generation

		// TODO: check why this is done. It creates a top wall on position (0,0). This may even corrupt a maze and block its exit!
		Wall wall = new Wall(0, 0, CardinalDirection.North) ;
		cells.addWall(wall, false);

		return genNodes(seglist); // creates a data structure to quickly search for segments
	}
	/**
	 * Provides the sign of a given integer number
	 * @param num
	 * @return -1 if num < 0, 0 if num == 0, 1 if num > 0
	 */
	static int getSign(int num) {
		return (num < 0) ? -1 : (num > 0) ? 1 : 0;
	}
	/**
	 * Produce output for debugging purposes
	 * @param str
	 */
	static void dbg(String str) {
		System.out.println("BSPBuilder: "+str);
	}
}
