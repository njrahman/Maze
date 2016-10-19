package generation;

import static org.junit.Assert.*;
import generation.Order.Builder;
import falstad.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MazeFactoryTest {

	private MazeFactory mazeFactory;
	private StubOrderTest stubOrder;
	private MazeConfiguration configuration;
	
	@Before
	public void setUp() throws Exception {
		boolean det = false;
		mazeFactory = new MazeFactory(det);
		stubOrder = new StubOrderTest(1, Builder.DFS, det);
		mazeFactory.order(stubOrder);
		mazeFactory.waitTillDelivered();
		configuration = stubOrder.getConfiguration();
		
	}

	@After
	public void tearDown() throws Exception {
	}


	/**
	 * Makes sure none of the objects are equal to null
	 */
	@Test
	public void doesSetupWork(){
		assertNotNull(mazeFactory);
		assertNotNull(configuration);
		assertNotNull(stubOrder);
	}	
	
	/**
	 * Checks the maze for an exit using the distance class
	 */
	@Test
	public void doesMazeHaveExit(){
		int findExit = 0;    // initializes variable, could be anything, 0 is easiest
		Distance mazeDistance = configuration.getMazedists(); // gets distance from exit
		
		for (int i = 0; i < configuration.getWidth(); i++) {
			for (int j = 0; j < configuration.getHeight(); j++) {
				int distance = mazeDistance.getDistance(i, j);
				if (distance == 1){  // if distance from exit is 1 then there is an exit, so you increment the counter
					findExit ++;
				}
			}
			
		}
		assertEquals(1,findExit); // there should only be 1 exits so that means the correct answer for findExit is 1
	}
	
	@Test 
	public void isMazeExitAccessible(){
		
	}
	
	/**
	 * checks to see if the dimensions are correct in accordance with the constants class
	 */
	@Test
	public void areDimensionsCorrect(){
		
		int skillLevel = stubOrder.getSkillLevel();
		
		int xDimension = Constants.SKILL_X[skillLevel]; // retrieves the dimensions for that specific skill level
		int yDimension = Constants.SKILL_Y[skillLevel];
		
		int width = configuration.getWidth();           // actual dimensions of the maze created
		int height = configuration.getHeight();
		
		assertEquals(width, xDimension);		//the dimensions of the actual maze must equal the dimensions obtained from Constants
		assertEquals(height, yDimension);
		
	}
}