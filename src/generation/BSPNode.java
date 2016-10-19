/**
 * 
 */
package generation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import falstad.MazeFileWriter;

/**
 * BSPNodes are used to build a binary tree, where internal nodes keep track of lower and upper bounds of (x,y) coordinates.
 * Leaf nodes carry a list of segments. A BSP tree is a data structure to search for a set of segments to put on display in the FirstPersonDrawer.
 * 
 * Superclass for BSPBranch and Leaf nodes that carry further data. 
 * 
 * This code is refactored code from Maze.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 *  
 */
public class BSPNode {
	/* lower and upper bounds for (x,y) coordinates of segments carried in leaf nodes. */
    private int xl;
	private int yl;
	private int xu;
	private int yu;    
    
	/**
	 * Store the content of a BSPNode including data of branches and leaves as special cases.
	 * @param root is the node considered
	 * @param doc document to add data to
	 * @param mazeXML element to add data to
	 * @param number is an index number for this node in the XML format
	 * @return the highest used index number, in this case the given number
	 */
    public int store(Document doc, Element mazeXML, int number) {
    	// xlBSPNode elements
    	MazeFileWriter.appendChild(doc, mazeXML, "xlBSPNode_" + number, xl) ;
    	// ylBSPNode elements
    	MazeFileWriter.appendChild(doc, mazeXML, "ylBSPNode_" + number, yl) ;
    	// xuBSPNode elements
    	MazeFileWriter.appendChild(doc, mazeXML, "xuBSPNode_" + number, xu) ;
    	// yuBSPNode elements
    	MazeFileWriter.appendChild(doc, mazeXML, "yuBSPNode_" + number, yu) ;
    	// isleafBSPNode elements
    	MazeFileWriter.appendChild(doc, mazeXML, "isleafBSPNode_" + number, isIsleaf()) ;
    	
    	return number ; // unchanged
    }

    

	/**
	 * @return tells if object is a leaf node
	 */
	public boolean isIsleaf() {
		return false ;
	}



	/**
	 * Updates internal fields for upper and lower bounds of (x,y) coordinates
	 * @param x used to update xl and xu
	 * @param y used to update yl and yu
	 */
	protected void fix_bounds(int x, int y) {
		// update fields in super class BSPNode
		xl = Math.min(xl, x);
		yl = Math.min(yl, y);
		xu = Math.max(xu, x);
		yu = Math.max(yu, y);
		
	}



	/**
	 * @return the xl
	 */
	public int getLowerBoundX() {
		return xl;
	}



	/**
	 * @param xl the xl to set
	 */
	public void setLowerBoundX(int xl) {
		this.xl = xl;
	}



	/**
	 * @return the yl
	 */
	public int getLowerBoundY() {
		return yl;
	}



	/**
	 * @param yl the yl to set
	 */
	public void setLowerBoundY(int yl) {
		this.yl = yl;
	}



	/**
	 * @return the xu
	 */
	public int getUpperBoundX() {
		return xu;
	}



	/**
	 * @param xu the xu to set
	 */
	public void setUpperBoundX(int xu) {
		this.xu = xu;
	}



	/**
	 * @return the yu
	 */
	public int getUpperBoundY() {
		return yu;
	}



	/**
	 * @param yu the yu to set
	 */
	public void setUpperBoundY(int yu) {
		this.yu = yu;
	}
}