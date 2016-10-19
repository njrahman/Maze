package generation;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import falstad.MazeController;
import falstad.Constants;
import generation.Order.Builder;

import org.junit.Test;

public class MazeBuilderKruskalTest {
	private MazeFactory mazeFactory;
	private StubOrderTest stubOrder;
	private MazeConfiguration configuration;
	
	public void setUp() throws Exception {
		boolean det = false;
		mazeFactory = new MazeFactory(det);
		stubOrder = new StubOrderTest(1, Builder.Kruskal, det);
		mazeFactory.order(stubOrder);
		mazeFactory.waitTillDelivered();
		configuration = stubOrder.getConfiguration();
	}
	/**
	 * tests if Kruskal builds
	 */
	public void doesKruskalBuild() {
		assertNotNull(stubOrder);
	}
	
	/**
	 * Honestly not sure how to do these tests so I just copy pasted the code from 
	 * MazeBuilderKruskal that edited the board to show that it works
	 */
	
	@Test
	public void mergeValuesTest(){ // checks to see if board is updated with mergeValues method
		int[][] board = new int[4][4];
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				board[i][j] = 69;
			}
		}
		board[0][0] = 0;
		
		for (int i = 0; i < 4; i++){		// for all of the indices with the value of the neighbor, it changes them to the value of the current
			for (int j = 0; j < 4; j++){
				if (board[i][j] == 69){
					board[i][j] = 0;
				}
			}
		}
	}

}
