package generation;
import java.util.ArrayList;

import generation.CardinalDirection;
/**
 * This class has the responsibility to create a maze of given dimensions (width, height) 
* together with a solution based on a distance matrix.
* The MazeBuilder implements Runnable such that it can be run a separate thread.
* The MazeFactory has a MazeBuilder and handles the thread management.   

* 
* The maze is built with a randomized version of Kruskal's algorithm. 
* This means a spanning tree is expanded into a set of cells by removing walls from the maze.
* Algorithm leaves walls in tact that carry the border flag.
* Borders are used to keep the outside surrounding of the maze enclosed and 
* to make sure that rooms retain outside walls and do not end up as open stalls. 
*   
* @author Noor Rahman
*/


public class MazeBuilderKruskal extends MazeBuilder implements Runnable{
	private int[][] board;

	public MazeBuilderKruskal() {
		super();
		System.out.println("MazeBuilderKruskal uses Kruskal's algorithm to generate maze.");
	}
	
	public MazeBuilderKruskal(boolean det) {
		super(det);
		System.out.println("MazeBuilderKruskal uses Kruskal's algorithm to generate maze.");
	}
	/**
	 *This method creates a 2d array as to keep track of the values of each cell, this is used to
	 *create a way to figure out which cells are not part of the same set without using a set.
	 *Then it picks a wall out of the list of walls and if its neighbor is within bounds, it
	 *merges the value of the neighbor to be the same is the one of currWall inn the array.
	 */
	protected void generatePathways() {
		board = new int[width][height];
		int counter = 1; 						
		
		for (int i = 0; i < width; i++){		//creates a 2d array and fills it with unique numbers for each index
			for (int j = 0; j < height; j++){
				board[i][j] = counter;
				counter++;
			}
		}
	
		final ArrayList<Wall> candidates = new ArrayList<Wall>(); //creates an array list because the number of walls changes with difficulty so the list may need to expand
		createListOfWalls(candidates);							  // fills array list using the method
		
		while(!candidates.isEmpty()){     // While the list of walls is not empty                                              
			
			Wall currWall = extractWallFromCandidateSetRandomly(candidates); // grab a random wall
			int currX = currWall.getX();                                     // grab that wall's x coordinate
			int currY = currWall.getY();                                     // grab that wall's y coordinate
			int neighX = currWall.getNeighborX();                            // grab neighbor's x coordinate
			int neighY = currWall.getNeighborY();							 // grab neighbor's y coordinate
			
			if (neighX >= 0 && neighY >= 0 && neighX < width && neighY < height){   // makes sure that the neighbor is in bounds
				if (board[currX][currY] != board[neighX][neighY]){                  // if the values of the current index and its neighbor are not equal
					changeNeighborValue(currWall, currX, currY, neighX, neighY); 	// then update the neighbor's value to that of the current index
				}
			}
		}
	}
	
	/**
	 * This method takes in the coordinates of a wall and its neighbor, deletes the wall
	 * and then updates the board array so that the neighbor and all indices in the array
	 * with the same value as the neighbor are changed to the value of the current index.
	 * 
	 * 
	 * @param wall
	 * @param currX
	 * @param currY
	 * @param neighX
	 * @param neighY
	 */
	private void changeNeighborValue(Wall wall, int currX, int currY, int neighX, int neighY){ // the integer values are passed because if you take them after the deletion of the wall then they change
		
		cells.deleteWall(wall); // self explanatory(deletes from both sides)
		
		int current = board[currX][currY]; 		//stores indices,
		int neighbor = board[neighX][neighY];
		
		for (int i = 0; i < width; i++){		// for all of the indices with the value of the neighbor, it changes them to the value of the current
			for (int j = 0; j < height; j++){
				if (board[i][j] == neighbor){
					board[i][j] = current;
				}
			}
		}
	}
		
	/**
	 * Pick a random position in the list of candidates, remove the candidate from the list and return it
	 * @param candidates
	 * @return candidate from the list, randomly chosen
	 */
	private Wall extractWallFromCandidateSetRandomly(ArrayList<Wall> candidates) {
		return candidates.remove(random.nextIntWithinInterval(0, candidates.size()-1)); 
	}
	/**
	 * Simply goes through all the walls in the maze and adds them to a list if they are not borders.
	 * @param walls
	 */
	private void createListOfWalls(ArrayList<Wall> walls) {
		
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				for (CardinalDirection cd : CardinalDirection.values()) {
					Wall wall = new Wall(i, j, cd);
					if (cells.canBreak(wall)){        // If the wall is not a border, it is added to the list
						walls.add(wall);
					}
				}
			}
		}
	}
	

}



