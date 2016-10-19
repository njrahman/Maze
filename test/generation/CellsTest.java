package generation;

import static org.junit.Assert.*;
import falstad.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests individual methods of Cells class. 
 * 
 * 
 * @author Peter Kemper
 *
 */
public class CellsTest{

	// private variables
	private int width = 4;
	private int height = 4;
	private Cells cells;  // setup makes this a width x height cells object
	private Cells cells1; // setup makes this a 1x1 cells object 
	
	/**
	 * We create a default (width x height) cells object that is not initialized and a (1x1) cells1 object.
	 */
	@Before
	public void setUp() {
		cells = new Cells(width, height);
		cells1 = new Cells(1, 1);
	}

	/**
	 * Nothing needed to clean up variables after each test
	 * @throws Exception
	 */
	/*
	@After
	public void tearDown() throws Exception {
	}
	*/
	/**
	 * Test case: See if constructor used in setUp delivers anything
	 * <p>
	 * Method under test: own set up
	 * <p>
	 * It is correct if the cells field is not null.
	 */
	@Test
	public final void testCells() {
		assertNotNull(cells) ;
		assertNotNull(cells1) ;
	}

	/**
	 * Test case: Check if constructor that takes existing array really 
	 * copies values and resets values with the initialize method.
	 * <p>
	 * Method under test: Cells(int[][] input), getValueOfCell(int i, int j)
	 * <p>
	 * Correct behavior: constructor delivers a cells object where
	 * all internal positions are set as given. After initialization
	 * those values must be set differently.
	 */
	@Test
	public final void testCellsConstructorWithArray() {
		// constructor with arrays should use initial values from array
		// in this case, values are set to specific numbers
		int[][] a = new int[width][height] ;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				a[i][j] = i*height+j ;
			}
		}
		cells = new Cells(a) ;
		assertTrue(cells != null) ;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				assertTrue(i*height+j == cells.getValueOfCell(i, j)) ;
			}
		}
		// initialize method should reset values such that walls are up everywhere
		// means old values are gone, new values can not be 0
		cells.initialize();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				assertFalse(i*height+j == cells.getValueOfCell(i, j)) ;
				assertFalse(0 == cells.getValueOfCell(i, j)) ;
			}
		}
	}
	/**
	 * Test case: See if the two constructor methods work in a consistent manner
	 * <p>
	 * Method under test: Cells(int[][] input), Cells(width,height), equals(Object other)
	 * <p>
	 * Correct behavior:
	 * It is correct if each constructor delivers a cells object and that both 
	 * are equal if of same dimension and of same content
	 */
	@Test
	public final void testCellsBothConstructors() {
		// constructor with arrays should use initial values from array
		// in this case, values are set to specific numbers
		cells = new Cells(new int[width][height]) ;
		assertTrue(cells != null) ;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				assertTrue(0 == cells.getValueOfCell(i, j)) ;
			}
		}
		// constructor with width and height
		// should have values for outside bounds being set and 
		// inner walls being up, such that values can not be 0 anywhere after initialization
		cells1 = new Cells(width,height) ;
		assertTrue(cells1 != null) ;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				assertTrue(0 == cells.getValueOfCell(i, j)) ;
			}
		}
		// both constructor methods should deliver same maze before initialization
		assertTrue(cells1.equals(cells)) ;
		// let's initialize one cells object and see if values change
		cells.initialize();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				assertFalse(0 == cells.getValueOfCell(i, j)) ;
			}
		}
		// both cells should be different as cells1 is not initialized yet
		assertFalse(cells1.equals(cells)) ;
		cells1.initialize() ;
		assertTrue(0 != cells1.getValueOfCell(0, 0)) ;
		// check equals method
		assertTrue(cells1.equals(cells1)) ; // reflexive
		assertFalse(cells1.equals(null)) ; // by definition, false for null
		assertFalse(cells1.equals(this)) ; // by definition, false for different types
		// both constructor methods should deliver same maze after initialization
		assertTrue(cells1.equals(cells)) ;
		// check if dimensions matter, cells of different dimensions can not be equal
		cells1 = new Cells(new int[width+1][height+1]) ;
		assertFalse(cells1.equals(cells)) ;
		cells1.initialize() ;
		assertFalse(cells1.equals(cells)) ;
	}


	/** 
	 * Test case: Correctness of masks in Constants.java 
	 * <p>
	 * Method under test: none, we test a constraint across constants
	 * <p>
	 * Correct behavior:
	 * It will be correct if the mask = {0,2,4,1} matches with right, bottom, left and top directions
	 */
	@Test
	public final void testGetMasks() {
		assertEquals(Constants.MASKS[0], Constants.CW_RIGHT);
		assertEquals(Constants.MASKS[1], Constants.CW_BOT);
		assertEquals(Constants.MASKS[2], Constants.CW_LEFT);
		assertEquals(Constants.MASKS[3], Constants.CW_TOP);
	}

	/** 
	 * Test case: Correctness of the canGo method 
	 * <p>
	 * Method under test: canGo(int x, int y, CardinalDirection dir) 
	 * <p>
	 * Correct behavior: 
	 * checks if adjacent cells (x,y) and its neighbor (x+dx,y+dy) are not separated by a border
	 * and (x+dx,y+dy) has not been visited before.
	 */
	@Test
	public final void testCanGo() {
		assertTrue(width > 1) ;
		assertTrue(height > 1) ;
		// the initial 4x4 cells have walls up everywhere, but borders only on the outside
		// canGo is expected to be ok as the visited flags is not set yet and
		// there are no borders set internally
		cells.initialize();
		// origin (0,0) is at top left corner
		// x coordinate grows to the right in horizontal direction
		// y coordinate grows to the bottom in the vertical direction
		// at origin, we can not go up or left but down or right
		Wall wall = new Wall(0,0,CardinalDirection.East) ;
		assertTrue(cells.canGo(wall)); // right
		wall.setWall(0,0,CardinalDirection.South);
		assertTrue(cells.canGo(wall)); // down
		wall.setWall(0,0,CardinalDirection.West);
		assertFalse(cells.canGo(wall)); // left
		wall.setWall(0,0,CardinalDirection.North);
		assertFalse(cells.canGo(wall)); // up
		// at position (1,1) we can go in each direction
		wall.setWall(1,1,CardinalDirection.East);
		assertTrue(cells.canGo(wall));
		wall.setWall(1,1,CardinalDirection.South);
		assertTrue(cells.canGo(wall));
		wall.setWall(1,1,CardinalDirection.West);
		assertTrue(cells.canGo(wall));
		wall.setWall(1,1,CardinalDirection.North);
		assertTrue(cells.canGo(wall));
		// at the bottom right corner we can go North or West only
		wall.setWall(width-1,height-1,CardinalDirection.East);
		assertFalse(cells.canGo(wall));
		wall.setWall(width-1,height-1,CardinalDirection.South);
		assertFalse(cells.canGo(wall));
		wall.setWall(width-1,height-1,CardinalDirection.West);
		assertTrue(cells.canGo(wall));
		wall.setWall(width-1,height-1,CardinalDirection.North);
		assertTrue(cells.canGo(wall));
		// check if visited flag impacts canGo decision
		// at position (1,1) to right (2,1) we can still go
		// but not anymore if we set the visited flag
		cells.setCellAsVisited(2, 1);
		wall.setWall(1,1,CardinalDirection.East);
		assertFalse(cells.canGo(wall)); 
		cells.setCellAsVisited(0, 1);
		wall.setWall(1,1,CardinalDirection.West);
		assertFalse(cells.canGo(wall)); 
		cells.setCellAsVisited(1, 2);
		wall.setWall(1,1,CardinalDirection.South);
		assertFalse(cells.canGo(wall)); 
		cells.setCellAsVisited(1, 0);
		wall.setWall(1,1,CardinalDirection.North);
		assertFalse(cells.canGo(wall)); 	
	}
	/** 
	 * Test case: Correctness of methods for exit position 
	 * <p>
	 * Method under test: setExitPosition(int x, int y) and isExitPosition(int x, int y, int bit)
	 * <p>
	 * Correct behavior: 
	 * it sets a given bit to zero in a given cell
	 */
	@Test
	public final void testExitPosition() {
		cells.initialize();
		// top left corner
		assertFalse(cells.isExitPosition(0, 0));
		cells.setExitPosition(0,0);
		assertTrue(cells.isExitPosition(0, 0));
		// top right corner
		assertFalse(cells.isExitPosition(width-1, 0));
		cells.setExitPosition(width-1,0);
		assertTrue(cells.isExitPosition(width-1, 0));
		// bottom left corner
		assertFalse(cells.isExitPosition(0, height-1));
		cells.setExitPosition(0, height-1);
		assertTrue(cells.isExitPosition(0, height-1));
		// bottom right corner
		assertFalse(cells.isExitPosition(width-1, height-1));
		cells.setExitPosition(width-1, height-1);
		assertTrue(cells.isExitPosition(width-1, height-1));
		// top middle position
		assertFalse(cells.isExitPosition(0, 2));
		cells.setExitPosition(0, 2);
		assertTrue(cells.isExitPosition(0, 2));
		// side middle position
		assertFalse(cells.isExitPosition(2, 0));
		cells.setExitPosition(2, 0);
		assertTrue(cells.isExitPosition(2, 0));
		// side middle position
		assertFalse(cells.isExitPosition(2, height-1));
		cells.setExitPosition(2, height-1);
		assertTrue(cells.isExitPosition(2, height-1));
		// wrong position
		assertFalse(cells.isExitPosition(2, 2));
		cells.setExitPosition(2, 2);
		assertFalse(cells.isExitPosition(2, 2));
		
	}

	/** 
	 * Test case: Correctness of the setCellAsVisited method 
	 * <p>
	 * Method under test: setCellAsVisited(int x, int y) 
	 * <p>
	 * Correct behavior: 
	 * Method sets visited flag to zero for a given cell.
	 */
	@Test
	public final void testSetCellAsVisitedToZero() {
		// initial values are 0
		assertTrue(cells.hasMaskedBitsFalse(0, 0, Constants.CW_VISITED));
		//cells.setBitToOne(0, 0, Constants.CW_VISITED);
		cells.initialize(); // sets all visited flags to 1
		assertTrue(cells.hasMaskedBitsTrue(0, 0, Constants.CW_VISITED));

		cells.setCellAsVisited(0, 0);
		assertTrue(cells.hasMaskedBitsFalse(0, 0, Constants.CW_VISITED));
		
		//cells.setBitToOne(0, 0, Constants.CW_VISITED);
		cells.initialize(); // sets all visited flags to 1
		assertTrue(cells.hasMaskedBitsTrue(0, 0, Constants.CW_VISITED));
	}
	


	/** 
	 * Test case: Correctness of the setInRoomToOne method 
	 * <p>
	 * Method under test: setInRoomToOne(int x, int y) 
	 * <p>
	 * Correct behavior: 
	 * it sets the InRoom bit to one for a given cell and direction
	 */
	@Test
	public final void testSetInRoomToOne() {
		assertFalse(cells.isInRoom(1, 1));
		cells.setInRoomToOne(1,1);
		assertTrue(cells.isInRoom(1, 1));
	}


	/** 
	 * Test case: Correctness of the initialize method 
	 * <p>
	 * Method under test: initialize() 
	 * <p>
	 * Correct behavior: 
	 * Initialize maze such that all cells have not been visited (CW_VIRGIN), all walls are up (CW_ALL),
	 * and borders are set as a rectangle (CW_*_BOUND).
	 */
	@Test
	public final void testInitialize() {
		cells.initialize();
		assertTrue(cells.hasMaskedBitsTrue(0, 0, Constants.CW_TOP_BOUND)); 
		assertTrue(cells.hasMaskedBitsTrue(0, 0, Constants.CW_LEFT_BOUND));
		assertFalse(cells.hasMaskedBitsTrue(0, 0, Constants.CW_BOT_BOUND));
		assertFalse(cells.hasMaskedBitsTrue(0, 0, Constants.CW_RIGHT_BOUND));
		assertTrue(cells.hasMaskedBitsTrue(0, 0, Constants.CW_ALL_BOUNDS));
		assertTrue(cells.hasMaskedBitsTrue(0, 0, Constants.CW_ALL));
		assertTrue(cells.hasMaskedBitsTrue(0, 0, Constants.CW_VISITED));
		
		Cells cell3 = new Cells(0,0);//tests empty grid
		cell3.initialize();
		assertNotNull(cell3);//above line did not throw error
	}

	/** 
	 * Test case: Correctness of the areaOverlapsWithRoom method 
	 * <p>
	 * Method under test: areaOverlapsWithRoom(int rx, int ry, int rxl, int ryl) 
	 * <p>
	 * Correct behavior: 
	 * Checks if there is a cell in the given area that belongs to a room.
	 * The first corner is at the upper left position, the second corner is at the lower right position.
	 */
	@Test
	public final void testAreaOverlapsWithRoom() {
		cells.initialize();
		assertFalse(cells.areaOverlapsWithRoom(1,1,0,1));
	}

	/** 
	 * Test case: Correctness of the addWall, deleteWall methods 
	 * <p>
	 * Method under test: deleteWall(int x, int y, CardinalDirection cd) 
	 * Method under test: addWall(int x, int y, CardinalDirection cd) 
	 * <p>
	 * Correct behavior: 
	 * it deletes a wall between to adjacent cells (x,y) and (x+dx,y+dy).
	 */
	@Test
	public final void testAddAndDeleteWall() {
		// initially all walls are up
		cells.initialize();
		assertTrue(cells.hasMaskedBitsTrue(0,0,Constants.CW_RIGHT));
		assertTrue(cells.hasMaskedBitsTrue(1,0,Constants.CW_LEFT));
		
		// define wall between (0,0) and (1,0) which is east from (0,0)
		// deleteWall removes wall in both cells (0,0) and (1,0)
		Wall wall = new Wall(0, 0, CardinalDirection.East) ;
		cells.deleteWall(wall);
		assertTrue(cells.hasMaskedBitsFalse(0,0,Constants.CW_RIGHT));
		assertTrue(cells.hasMaskedBitsFalse(1,0,Constants.CW_LEFT));
		
		// addWall as internal wall adds it to both cells (0,0) and (1,0)
		cells.addWall(wall, true);
		assertTrue(cells.hasMaskedBitsTrue(0,0,Constants.CW_RIGHT));
		assertTrue(cells.hasMaskedBitsTrue(1,0,Constants.CW_LEFT));
		
		// deleteWall removes wall in both cells (0,0) and (1,0)
		cells.deleteWall(wall);
		assertTrue(cells.hasMaskedBitsFalse(0,0,Constants.CW_RIGHT));
		assertTrue(cells.hasMaskedBitsFalse(1,0,Constants.CW_LEFT));
		
		// addWall but not as internal wall adds it only to cells (0,0) 
		cells.addWall(wall, false);
		assertTrue(cells.hasMaskedBitsTrue(0,0,Constants.CW_RIGHT));
		assertTrue(cells.hasMaskedBitsFalse(1,0,Constants.CW_LEFT));
		
		// deleteWall removes wall in both cells (0,0) and (1,0)
		// robust against deleting non-existing wall at cell(1,0) 
		cells.deleteWall(wall);
		assertTrue(cells.hasMaskedBitsFalse(0,0,Constants.CW_RIGHT));
		assertTrue(cells.hasMaskedBitsFalse(1,0,Constants.CW_LEFT));


	}

	
	/** 
	 * Test case: Correctness of the markAreaAsRoom method 
	 * <p>
	 * Method under test: markAreaAsRoom(int rw, int rh, int rx, int ry, int rxl, int ryl, Random r) 
	 * Method under test: isInRoom(int x, int y) 
	 * <p>
	 * Correct behavior: 
	 * it marks a given area as a room on the maze and positions up to five doors randomly.
	 * The first corner is at the upper left position, the second corner is at the lower right position.
	 * Assumes that given area is located on the map and does not intersect with any existing room.
	 * The walls of a room are declared as borders to prevent the generation mechanism from tearing them down.
	 * rw is the room width, rh is the room height, rx is 1st corner, x coordinate, ry is 1st corner, y coordinate, 
	 * rxl is 2nd corner, x coordinate, ryl is 2nd corner, y coordinate
	 */
	@Test
	public final void testMarkAreaAsRoom() {
		Cells C = new Cells(10,10);
		C.initialize();
		C.markAreaAsRoom(4,4, 1,1, 4,4);
		assertTrue(C.areaOverlapsWithRoom(1,1,5,5));
		assertFalse(C.areaOverlapsWithRoom(6,6,8,8));
		assertTrue(C.isInRoom(3, 3));
		assertFalse(C.isInRoom(4, 8));

		Cells cell1 = new Cells(10, 10);
		cell1.markAreaAsRoom(5, 5, 2, 2, 7, 7);
		for(int x = 2; x < 8; x++){
			for(int y = 2; y < 8; y++){
				assertTrue(cell1.isInRoom(x, y));
			}
		}
		assertFalse(cell1.isInRoom(9, 9));

		Cells cell2 = new Cells(10, 10);
		cell2.initialize();
		cell2.markAreaAsRoom(5, 5, 1, 1, 6, 6);
		Wall wall = new Wall(5, 5, CardinalDirection.East.getDirection(0, 1)) ;
		assertTrue(cell2.canGo(wall));
		wall.setWall(5, 5, CardinalDirection.South);
		assertTrue(cell2.canGo(wall));
	}

	/** 
	 * Test case: Correctness of the hasMaskedBitsTrue method 
	 * <p>
	 * Method under test: hasMaskedBitsTrue(int x, int y, int bitmask) 
	 * <p>
	 * Correct behavior: 
	 * it gets methods (is..., has...) for various flags
	 */
	@Test
	public final void testHasMaskedBitsTrue() {
		cells.initialize();
		assertTrue(cells.hasMaskedBitsTrue(0, 0, Constants.CW_VISITED));
		
		cells.setCellAsVisited(0, 0);
		assertFalse(cells.hasMaskedBitsTrue(0, 0, Constants.CW_VISITED));
	}

	/** 
	 * Test case: Correctness of the isInRoom method 
	 * <p>
	 * Method under test: isInRoom(int x, int y) 
	 * <p>
	 * Correct behavior: 
	 * it tells if InRoom flag is set for given cell
	 */
	@Test
	public final void testIsInRoom() {
		// initial setting must be such that position is not inside a room
		assertFalse(cells.isInRoom(0, 0));
	}

	/** 
	 * Test case: Correctness of the hasWall, hasNoWall methods 
	 * <p>
	 * Method under test: hasWall(int x, int y, CardinalDirection d) 
	 * Method under test: hasNoWall(int x, int y, CardinalDirection) 
	 * <p>
	 * Correct behavior: 
	 * it tells if cell has a wall in the given direction
	 */ 
	@Test
	public final void testHasWall() {
		cells.initialize();
		assertTrue(cells.hasWall(0, 0, CardinalDirection.North));
		assertTrue(cells.hasWall(0, 0, CardinalDirection.East));
		assertTrue(cells.hasWall(0, 0, CardinalDirection.South));
		assertTrue(cells.hasWall(0, 0, CardinalDirection.West));
		assertFalse(cells.hasNoWall(0, 0, CardinalDirection.North));
		assertFalse(cells.hasNoWall(0, 0, CardinalDirection.East));
		assertFalse(cells.hasNoWall(0, 0, CardinalDirection.South));
		assertFalse(cells.hasNoWall(0, 0, CardinalDirection.West));
		
		Wall wall = new Wall(0,0,CardinalDirection.East);
		cells.deleteWall(wall);
		assertFalse(cells.hasWall(0, 0, CardinalDirection.East));
		assertTrue(cells.hasNoWall(0, 0, CardinalDirection.East));
		assertFalse(cells.hasWall(1, 0, CardinalDirection.West));
		assertTrue(cells.hasNoWall(1, 0, CardinalDirection.West));

		wall.setWall(0,0,CardinalDirection.South);
		cells.deleteWall(wall);
		assertFalse(cells.hasWall(0, 0, CardinalDirection.South));
		assertTrue(cells.hasNoWall(0, 0, CardinalDirection.South));
		assertFalse(cells.hasWall(0, 1, CardinalDirection.North));
		assertTrue(cells.hasNoWall(0, 1, CardinalDirection.North));
		
	}

	/** 
	 * Test case: Correctness of the hasMaskedBitsFalse method 
	 * <p>
	 * Method under test: hasMaskedBitsFalse(int x, int y,int bitmask) 
	 * <p>
	 * Correct behavior: 
	 * it tells if masked bit is false
	 */
	@Test
	public final void testHasMaskedBitsFalse() {
		cells.initialize();
		assertFalse(cells.hasMaskedBitsFalse(0, 0, Constants.CW_RIGHT));
		
		Wall wall = new Wall(0,0,CardinalDirection.East);
		cells.deleteWall(wall);
		assertTrue(cells.hasMaskedBitsFalse(0, 0, Constants.CW_RIGHT));
	}

	/** 
	 * Test case: Correctness of the toString method 
	 * <p>
	 * Method under test: toString() 
	 * <p>
	 * Correct behavior: 
	 * it dumps internal data into a string, intended usage is for debugging purposes. 
	 * Maze is represent as a matrix of integer values.
	 */
	@Test
	public final void testToString() {
		Cells cell1 = new Cells(1, 1);
		assertEquals(cell1.toString(), cell1.toString(), " i:0 j:0=0\n"); 
		cell1.initialize();
		assertEquals(cell1.toString(), cell1.toString(), " i:0 j:0=511\n"); 
	}

}
