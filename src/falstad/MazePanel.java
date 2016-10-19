package falstad;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;

/**
 * Add functionality for double buffering to an AWT Panel class.
 * Used for drawing a maze.
 * 
 * @author pk
 *
 */
public class MazePanel extends Panel  {
	/* Panel operates a double buffer see
	 * http://www.codeproject.com/Articles/2136/Double-buffer-in-standard-Java-AWT
	 * for details
	 */
	private Image bufferImage ;
	/**
	 * Constructor. Object is not focusable.
	 */
	public MazePanel() {
		super() ;
		this.setFocusable(false) ;
	}
	
	@Override
	public void update(Graphics g) {
		paint(g) ;
	}
	public void update() {
		paint(getGraphics()) ;
	}
	

	/**
	 * Draws the buffer image to the given graphics object.
	 * This method is called when this panel should redraw itself.
	 */
	@Override
	public void paint(Graphics g) {
		if (null == g) {
			System.out.println("MazePanel.paint: no graphics object, skipping drawImage operation") ;
		}
		else {
			g.drawImage(bufferImage,0,0,null) ;	
		}
	}

	
	public void initBufferImage() {
		bufferImage = createImage(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		if (null == bufferImage)
		{
			System.out.println("Error: creation of buffered image failed, presumedly container not displayable");
		}
	}
	
	/**
	 * Obtains a graphics object that can be used for drawing. 
	 * Multiple calls to the method will return the same graphics object 
	 * such that drawing operations can be performed in a piecemeal manner 
	 * and accumulate. To make the drawing visible on screen, one
	 * needs to trigger a call of the paint method, which happens 
	 * when calling the update method. 
	 * @return graphics object to draw on
	 */
	public Graphics getBufferGraphics() {
		if (null == bufferImage)
			initBufferImage() ;
		if (null == bufferImage)
			return null ;
		return bufferImage.getGraphics() ;
	}

}
