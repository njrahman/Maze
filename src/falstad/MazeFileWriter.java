/**
 * 
 */
package falstad;


import generation.BSPNode;
import generation.Cells;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class provides functionality to write a maze to a file in an XML format.
 * The class design is not object-oriented as methods for this particular function are all collected here and
 * not distributed across classes that carry that information, e.g. BSPNode.
 * All methods are static. 
 * The XML format is a straightforward enumeration of elements and not particularly sophisticated. 
 * 
 *
 */
public class MazeFileWriter {

	/**
	 * Write maze content to a file
	 */
	public static void store(String filename, int width, int height, int rooms, int expected_partiters, BSPNode root, Cells cells, int[][] dists, int startX, int startY)
	{
		 try {
			 	// get a document 
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.newDocument();

				 
				// store data that characterizes the maze in the document
				storeMaze(width, height, rooms, expected_partiters, root, cells, dists, startX, startY, doc);
				
				// write the document content into resulting xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(filename));
		 
				transformer.transform(source, result);
			  } catch (ParserConfigurationException pce) {
				pce.printStackTrace();
			  } catch (TransformerException tfe) {
				tfe.printStackTrace();
			  }
	}
	/**
	 * Store given objects in the given document
	 * @param width
	 * @param height
	 * @param rooms
	 * @param expected_partiters
	 * @param root
	 * @param cells
	 * @param dists
	 * @param startX
	 * @param startY
	 * @param doc
	 */
	static void storeMaze(int width, int height, int rooms,
			int expected_partiters, BSPNode root, Cells cells, int[][] dists,
			int startX, int startY, Document doc) {
		Element mazeXML = doc.createElement("Maze");
		doc.appendChild(mazeXML);
		
		// store fields of Maze class
		MazeFileWriter.appendChild(doc, mazeXML, "sizeX", width) ;
		MazeFileWriter.appendChild(doc, mazeXML, "sizeY", height) ;
		MazeFileWriter.appendChild(doc, mazeXML, "roomNum", rooms) ;                 // TODO: check, unclear if this is truly necessary
		MazeFileWriter.appendChild(doc, mazeXML, "partiters", expected_partiters) ;  // TODO: check, unclear if this is truly necessary
		// cells
		int number = 0 ;		
		for ( int x = 0; x != width; x++) {
			for ( int y = 0; y != height; y++) {
				MazeFileWriter.appendChild(doc, mazeXML, "cell"+ "_" + Integer.toString(number), cells.getValueOfCell(x, y)) ;
				number++;
			}
		}
		// distances
		number = 0 ;		
		for ( int x = 0; x != width; x++) {
			for ( int y = 0; y != height; y++) {
				MazeFileWriter.appendChild(doc, mazeXML, "dists"+ "_" + Integer.toString(number), dists[x][y]) ;
				number++;
			}
		}
		// start position
		MazeFileWriter.appendChild(doc, mazeXML, "startX", startX) ;
		MazeFileWriter.appendChild(doc, mazeXML, "startY", startY) ;
		// BSPnodes
		if (null != root)
		{
			//Store the content of a BSPNode including data of branches and leaves as special cases.
			root.store(doc, mazeXML, 0);
		}
		else
		{
			System.out.println("MazeBuilderWriter.store: root node of BSP tree is null");
		}
		
	}
	
	/**
	 * Append an new element to mazeXML that carries the given name has a child node with the given value.
	 * @param doc document to add data to
	 * @param mazeXML element to add data to
	 * @param name specifies the XML element to write to
	 * @param value is the content for the XML element
	 */
	public static void appendChild(Document doc, Element mazeXML, String name, int value)
	{
		Element e = doc.createElement(name);
		e.appendChild(doc.createTextNode(Integer.toString(value)) );
		mazeXML.appendChild(e);
	}
	/**
	 * Append an new element to mazeXML that carries the given name has a child node with the given value.
	 * @param doc document to add data to
	 * @param mazeXML element to add data to
	 * @param name specifies the XML element to write to
	 * @param value is the content for the XML element
	 */
	public static void appendChild(Document doc, Element mazeXML, String name, boolean value)
	{
		Element e = doc.createElement(name);
		e.appendChild(doc.createTextNode(Boolean.toString(value)) );
		mazeXML.appendChild(e);
	}
}
