package generation;

import java.util.ArrayList;

/**
  * This class has the responsibility to create a maze of given dimensions (width, height) 
 * together with a solution based on a distance matrix.
 * The MazeBuilder implements Runnable such that it can be run a separate thread.
 * The MazeFactory has a MazeBuilder and handles the thread management.   

 * 
 * The maze is built with a randomized version of Prim's algorithm. 
 * This means a spanning tree is expanded into a set of cells by removing walls from the maze.
 * Algorithm leaves walls in tact that carry the border flag.
 * Borders are used to keep the outside surrounding of the maze enclosed and 
 * to make sure that rooms retain outside walls and do not end up as open stalls. 
 *   
 * @author Jones.Andrew, refactored pk
 */

public class MazeBuilderPrim extends MazeBuilder implements Runnable {
	
	public MazeBuilderPrim() {
		super();
		System.out.println("MazeBuilderPrim uses Prim's algorithm to generate maze.");
	}
	
	public MazeBuilderPrim(boolean det) {
		super(det);
		System.out.println("MazeBuilderPrim uses Prim's algorithm to generate maze.");
	}

	/**
	 * This method generates pathways into the maze by using Prim's algorithm to generate a spanning tree for an undirected graph.
	 * The cells are the nodes of the graph and the spanning tree. An edge represents that one can move from one cell to an adjacent cell.
	 * So an edge implies that its nodes are adjacent cells in the maze and that there is no wall separating these cells in the maze. 
	 */
	@Override
	protected void generatePathways() {
		// pick initial position (x,y) at some random position on the maze
		int x = random.nextIntWithinInterval(0, width-1);
		int y = random.nextIntWithinInterval(0, height-1);
		// create an initial list of all walls that could be removed
		// those walls lead to adjacent cells that are not part of the spanning tree yet.
		final ArrayList<Wall> candidates = new ArrayList<Wall>();
		updateListOfWalls(x, y, candidates);
		
		Wall curWall;
		// we need to consider each candidate wall and consider it only once
		while(!candidates.isEmpty()){
			// in order to have a randomized algorithm,
			// we randomly select and extract a wall from our candidate set
			// this also reduces the set to make sure we terminate the loop
			curWall = extractWallFromCandidateSetRandomly(candidates);
			// check if wall leads to a new cell that is not connected to the spanning tree yet
			if (cells.canGo(curWall))
			{
				// delete wall from maze, note that this takes place from both directions
				cells.deleteWall(curWall);
				// update current position
				x = curWall.getNeighborX();
				y = curWall.getNeighborY();
				
				cells.setCellAsVisited(x, y); // the flag is never reset, so this ensure we never go to (x,y) again
				updateListOfWalls(x, y, candidates); // checks to see if it has walls to new cells, if it does it adds them to the list
				// note that each wall can get added at most once. This is important for termination and efficiency
			}
		}
	}
	/**
	 * Pick a random position in the list of candidates, remove the candidate from the list and return it
	 * @param candidates
	 * @return candidate from the list, randomly chosen
	 */
	private Wall extractWallFromCandidateSetRandomly(final ArrayList<Wall> candidates) {
		return candidates.remove(random.nextIntWithinInterval(0, candidates.size()-1)); 
	}
	

	/**
	 * Updates a list of all walls that could be removed from the maze based on walls towards new cells
	 * @param x
	 * @param y
	 */
	private void updateListOfWalls(int x, int y, ArrayList<Wall> walls) {
		Wall wall = new Wall(x, y, CardinalDirection.East) ;
		for (CardinalDirection cd : CardinalDirection.values()) {
			wall.setWall(x, y, cd);
			if (cells.canGo(wall)) // 
			{
				walls.add(new Wall(x, y, cd));
			}
		}
	}

}