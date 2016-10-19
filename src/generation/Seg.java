package generation;

import java.awt.Color;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import falstad.MazeFileWriter;

/**
 * A segment is a continuous sequence of walls in the maze.
 * 
 * This code is refactored code from Maze.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 */
public class Seg {
	// The following fields are all read-only and set by constructor
	private int x; 		// x coordinate of starting position of segment
	private int y; 		// y coordinate of starting position of segment
	private int dx;  	// direction and length of segment in x coordinate
	private int dy ; 	// direction and length of segment in y coordinate
	// Side condition: either dx != 0 and dy == 0 or vice versa
	// the coordinates of the end position is calculated as (x+dx, y+dy)
	
	private int dist; 	// distance of starting position of this segment to exit position of maze
	
	// Fields with read/write access
	private Color col; 	// color of segment, only set by constructor and file reader
	private boolean partition;
	private boolean seen; // if the segment has been seen by the user on its path through the maze

	/**
	 * Constructor
	 * @param psx x coordinate of starting position of segment
	 * @param psy y coordinate of starting position of segment
	 * @param pdx direction and length of segment in x coordinate
	 * @param pdy direction and length of segment in y coordinate
	 * @param distance of starting position of this segment to exit position of maze
	 * @param cc used to decide which color is assigned to segment, apparently it asks for a color change when a segment is split into two
	 */
	public Seg(int psx, int psy, int pdx, int pdy, int distance, int cc) {
		// set position
		x = psx;
		y = psy;
		// set extension
		dx = pdx;
		dy = pdy;
		// check side condition for extension
		assert (dx != 0 && dy == 0) || (dx == 0 && dy != 0) : "Segment needs to extend into exactly one direction" ;
		// set distance
		dist = distance;
		partition = false;
		// initialize boolean flags as false
		seen = false;
		// determine color
		initColor(distance, cc);
		// all fields initialized
	}

	/**
	 * Determine and set the color for this segment
	 * @param distance to exit
	 * @param cc obscure
	 */
	private void initColor(int distance, int cc) {
		int add = (getExtensionX() != 0) ? 1 : 0;
		// 7 in binary is 0...0111
		// use AND to get last 3 digits of distance
		distance /= 4;
		int part1 = distance & 7; 
		// mod used to limit the number of colors to 6
		int part2 = ((distance >> 3) ^ cc) % 6; 
		// compute rgb value, depends on distance and x direction
		int rgbValue = ((part1 + 2 + add) * 70)/8 + 80;
		switch (part2) {
		case 0: setColor(new Color(rgbValue, 20, 20)); break;
		case 1: setColor(new Color(20, rgbValue, 20)); break;
		case 2: setColor(new Color(20, 20, rgbValue)); break;
		case 3: setColor(new Color(rgbValue, rgbValue, 20)); break;
		case 4: setColor(new Color(20, rgbValue, rgbValue)); break;
		case 5: setColor(new Color(rgbValue, 20, rgbValue)); break;
		default: setColor(new Color(20, 20, 20)); break;
		}
	}

	/**
	 * Computes specific integer values for the X,Y directions.
	 * If x direction matters, it returns the inverse direction, either -1 or 1.
	 * If y direction matters, it returns the inverse direction, either -2 or 2.
	 * Possible return values limited to {-2,-1,1,2}.
	 * @return 
	 */
	public int getDir() {
		if (getExtensionX() != 0)
			return (getExtensionX() < 0) ? 1 : -1;
		return (getExtensionY() < 0) ? 2 : -2;
	}
	
	public int getDistance() {
		return this.dist ;
	}
	
	/**
	 * stores fields into the given document with the help of the MazeFileWriter
	 * @param doc
	 * @param mazeXML
	 * @param number
	 * @param i
	 */
	public void storeSeg(Document doc, Element mazeXML, int number, int i) {
		MazeFileWriter.appendChild(doc, mazeXML, "distSeg_" + number+ "_" + i, dist) ;
		MazeFileWriter.appendChild(doc, mazeXML, "dxSeg_" + number+ "_" + i, getExtensionX()) ;
		MazeFileWriter.appendChild(doc, mazeXML, "dySeg_" + number+ "_" + i, getExtensionY()) ;
		MazeFileWriter.appendChild(doc, mazeXML, "partitionSeg_" + number+ "_" + i, isPartition()) ;
		MazeFileWriter.appendChild(doc, mazeXML, "seenSeg_" + number+ "_" + i, isSeen()) ;
		MazeFileWriter.appendChild(doc, mazeXML, "xSeg_" + number+ "_" + i, getStartPositionX()) ;
		MazeFileWriter.appendChild(doc, mazeXML, "ySeg_" + number+ "_" + i, getStartPositionY()) ;
		MazeFileWriter.appendChild(doc, mazeXML, "colSeg_" + number+ "_" + i, getColor().getRGB()) ;
	}

	/**
	 * Equals method that checks if the other object matches in dimensions and content.
	 * @param other provides fully functional cells object to compare its content
	 */
	public boolean equals(Object other){
		// trivial special cases
		if (this == other)
			return true ;
		if (null == other)
			return false ;
		if (getClass() != other.getClass())
			return false ;
		// general case
		final Seg o = (Seg)other ; // type cast safe after checking class objects
		// compare all fields
		if ((x != o.x) || (dx != o.dx) ||(y != o.y) || (dy != o.dy)) 
			return false ; 
		if ((dist != o.dist) || (partition != o.partition) ||(seen != o.seen) || (col.getRGB() != o.col.getRGB())) 
			return false ;
		// all fields are equal, so both objects are equal
		return true ;
	}
	
	/**
	 * We override the equals method, so it is good practice to do this
	 * for the hashCode method as well.
	 */
	public int hashCode() {
		assert false : "hashCode not designed";
		return 42; // any arbitrary constant will do
	}
	/**
	 * @return the partition
	 */
	public boolean isPartition() {
		return partition;
	}

	/**
	 * @param partition the partition to set
	 */
	public boolean setPartition(boolean partition) {
		this.partition = partition;
		return partition;
	}
	/**
	 * Sets partition bit to true for cases where the segment touches the border of the maze
	 * and has an extension of 0.
	 * Method is used in BSPBuilder.
	 */
	public void updatePartitionIfBorderCase(int width, int height) {
		if (((x == 0 || x == width ) && dx == 0) ||
				((y == 0 || y == height) && dy == 0)) {
			partition = true;
		}
	}
	/**
	 * @return the seen
	 */
	public boolean isSeen() {
		return seen;
	}

	/**
	 * @param seen the seen to set
	 */
	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	/**
	 * @return the col
	 */
	public Color getColor() {
		return col;
	}

	/**
	 * @param col the color to set
	 */
	public void setColor(Color color) {
		/* for debugging: 
		 * use random color settings such that all segments look different
		int r = SingleRandom.getRandom().nextIntWithinInterval(20, 240) ;
		int g = SingleRandom.getRandom().nextIntWithinInterval(20, 240) ;
		int b = SingleRandom.getRandom().nextIntWithinInterval(20, 240) ;
		this.col = new Color(r,g,b);
		return ;	
		 */
		this.col = color;
	}

	/**
	 * @return the x
	 */
	public int getStartPositionX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getStartPositionY() {
		return y;
	}

	/**
	 * @return the dx
	 */
	public int getExtensionX() {
		return dx;
	}

	/**
	 * @return the dy
	 */
	public int getExtensionY() {
		return dy;
	}
	
	public int getEndPositionY() {
		return getStartPositionY() + getExtensionY();
	}

	public int getEndPositionX() {
		return getStartPositionX() + getExtensionX();
	}
	
}
