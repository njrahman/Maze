package falstad;

import generation.CardinalDirection;
import generation.Cells;

public class BasicRobot implements Robot {
	
	
	protected CardinalDirection currentDirection;
	protected MazeController maze;
	protected Cells roboCells;
	protected static float batteryLevel;
	protected boolean hasStopped;
	protected int[] currentPosition;
	
	static int pathLength = 0;
	
	
	public boolean forwardDistanceSensor;
	public boolean backwardDistanceSensor;
	public boolean leftDistanceSensor;
	public boolean rightDistanceSensor;
	
	public BasicRobot() {
		
		this.maze = null;
		this.currentPosition = new int[2];
		this.currentPosition[0] = 0;
		this.currentPosition[1] = 0;
		batteryLevel = 2500;
		hasStopped = false;
		forwardDistanceSensor = true;
		backwardDistanceSensor = true;
		leftDistanceSensor = true;
		rightDistanceSensor = true;
		currentDirection = CardinalDirection.East;
	}

	@Override
	public void rotate(Turn turn) {
		switch(turn) {
		
			case RIGHT:
				// check battery, if there is enough to turn it completes the turn
				if (batteryLevel >= 3) {
					currentDirection = currentDirection.rotateClockwise();
					batteryLevel -= 3;
				}
				// else the robot has stopped
				else {
					hasStopped = true;
				}
				break;
			
			case LEFT:
				// check battery, if there is enough to turn it completes the turn
				if (batteryLevel >= 3) {
					currentDirection = currentDirection.rotateCounterClockwise();
					batteryLevel -= 3;
				}
				// else the robot has stopped
				else {
					hasStopped = true;
				}
				break;
			
			case AROUND:
				// check battery, if there is enough to turn it completes the turn
				if (batteryLevel >= 6) {
					currentDirection = currentDirection.oppositeDirection();
					batteryLevel -= 6;
				}
				// else the robot has stopped
				else {
					hasStopped = true;
				}
				break;
		}
	}

	@Override
	public void move(int distance, boolean manual) {
		
		while (distance > 0) { // This checks to make sure that the robot still is supposed to move
			this.currentPosition = this.maze.getCurrentPosition();
			
			if (batteryLevel >= 5) {
				if (manual == true) {
					distance = 1;
				}
				if (distanceToObstacle(Direction.FORWARD) > 0) {
					
					// create a switch to see which direction you are facing so you can change
					// your current position
					switch(currentDirection) {
					
						case West: // west is in the negative X direction so you decrement the x coordinate
							this.currentPosition[0]--;
							break;
						
						case East: // east is in the positive X direction so you increment the x coordinate
							this.currentPosition[0]++;
							break;
						
						case North: // north is in the negative Y direction so you decrement the y coordinate
							this.currentPosition[1]--;
							break;
						
						case South: // south is in the positive Y direction so you increment the y coordinate
							this.currentPosition[1]++;
							break;
					}
					// The maze sets its current position to match whatever the change makes 
					this.maze.setCurrentPosition(this.currentPosition[0], this.currentPosition[1]);
					this.batteryLevel -= 5; // battery ccost for a move is 5
					pathLength ++; // pathLength is incremented for each move
					distance--; // counter for while loop
				}
				else {
					hasStopped = true;
				}
			}
			else {
				hasStopped = true;
			}
		}
	}

	@Override
	public int[] getCurrentPosition() {
		return this.currentPosition;
	}

	@Override
	public void setMaze(MazeController maze) {
		
		this.maze = maze;
		this.currentPosition = this.maze.getCurrentPosition();
		// creates a new cells object and fills it with the maze cells
		roboCells = new Cells(this.maze.mazeConfig.getWidth(), this.maze.mazeConfig.getHeight());
		roboCells = this.maze.mazeConfig.getMazecells();

		int[] cd = new int[2];
		cd = currentDirection.getDirection();
		// Each of these conditionals checks the coordinates of the direction and sets the currentDirection to match that one
		if (cd[0] == 0 && cd[1] == -1) {
			currentDirection = CardinalDirection.North;
		}
		
		else if (cd[0] == 1 && cd[1] == 0) {
			currentDirection = CardinalDirection.East;
		}
		
		else if (cd[0] == 0 && cd[1] == 1) {
			currentDirection = CardinalDirection.South;
		}
		
		else {
			currentDirection = CardinalDirection.West;
		}
	}

	@Override
	public boolean isAtGoal() {
		return roboCells.isExitPosition(this.currentPosition[0], this.currentPosition[1]);
	}

	@Override
	public boolean canSeeGoal(Direction direction) throws UnsupportedOperationException {

		if (hasDistanceSensor(direction) == true) {
			if (distanceToObstacle(direction) == Integer.MAX_VALUE) {// This only returns true at the goal per the the method in distance
				return true;
			}
			
			else {
				return false;
			}
		}
		
		else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public boolean isInsideRoom() throws UnsupportedOperationException {
		return roboCells.isInRoom(this.currentPosition[0], this.currentPosition[1]);
	}

	@Override
	public boolean hasRoomSensor() {
		return true;
	}

	@Override
	public CardinalDirection getCurrentDirection() {
		return this.maze.getCurrentDirection();
	}

	@Override
	public float getBatteryLevel() {
		return batteryLevel;
	}
	// used for redrawing the final screen
	public static float getBatteryLevelStatic() {
		return batteryLevel;
	}

	@Override
	public void setBatteryLevel(float level) {
		batteryLevel = level;
	}

	@Override
	public float getEnergyForFullRotation() {
		return 12;
	}

	@Override
	public float getEnergyForStepForward() {
		return  5;
	}

	@Override
	public boolean hasStopped() {
		return hasStopped;
	}

	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		if (hasDistanceSensor(direction)) {
			// sensor cost = 1
			setBatteryLevel(batteryLevel - 1);
			//get direction for checking walls in that direction
			CardinalDirection cd;
			if (direction == Direction.LEFT) {
				cd = currentDirection.rotateCounterClockwise();
			}
			
			else if (direction == Direction.RIGHT) {
				cd = currentDirection.rotateClockwise();
			}
			
			else if (direction == Direction.BACKWARD) {
				cd = currentDirection.oppositeDirection();
			}
			
			else {
				cd = currentDirection;
			}
			
			// set a counter to see how many steps you can go before running into an obstacle
			int count = 0;
			int currX = this.maze.getCurrentPosition()[0]; // current x coordinate
			int currY = this.maze.getCurrentPosition()[1]; // current y coordinate

			while (true) {
				if (currX < 0 || currX >= roboCells.width || currY < 0 || currY >= roboCells.height) { // checks if out of bounds of maze
					return Integer.MAX_VALUE;
				}

				else {
					switch (cd) { // each one of the cases checks to see if there is a wall in the given direction and then increments the counter of the while loop while decrementing/incrementing the corresponding coordinate value
					case North:
						if (roboCells.hasWall(currX, currY, CardinalDirection.North)) {
							return count;
						}
						currY--;
						break;
					case South:
						if (roboCells.hasWall(currX, currY, CardinalDirection.South)) {
							return count;
						}
						currY++;
						break;
					case East:
						if (roboCells.hasWall(currX, currY, CardinalDirection.East)) {
							return count;
						}
						currX++;
						break;
					case West:
						if (roboCells.hasWall(currX, currY, CardinalDirection.West)) {
							return count;
						}
						currX--;
						break;
					}
					count++;
				}
			}
		}
		else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public boolean hasDistanceSensor(Direction direction) {
		
		if (direction == Direction.RIGHT){
			return rightDistanceSensor;
		}
		
		else if (direction == Direction.LEFT) {
			return leftDistanceSensor;
		}
		
		else if (direction == Direction.FORWARD) {
			return forwardDistanceSensor;
		}
		
		else {
			return backwardDistanceSensor;
		}
		
		
	}
	public static int getPathLength(){
		return pathLength;
	}

}
