package falstad;

import falstad.Robot.Direction;
import falstad.Robot.Turn;
import generation.Distance;
import generation.Order;
import generation.CardinalDirection;

public class WallFollower implements RobotDriver {
	
	protected int[][] board;
	protected BasicRobot robot;
	protected Distance distance;
	protected int pathLength;
	protected MazeController maze;
	protected CardinalDirection cd;
	
	// Simple constructor for no bounds, sets everything to null except maze
	public WallFollower() {
		board = null;
		robot = null;
		distance = null;
		pathLength = 0;
		maze = new MazeController();
	}
	// copy paste of other constructor except with a builder
	public WallFollower(Order.Builder builder) {
		board = null;
		robot = null;
		distance = null;
		pathLength = 0;
		maze = new MazeController(builder);
	}

	@Override
	public void setRobot(Robot r) {
		robot = (BasicRobot) r;
	}

	@Override
	public void setDimensions(int width, int height) {
		board = new int[width][height];
	}

	@Override
	public void setDistance(Distance distance) {
		this.distance = distance;
	}

	@Override
	public boolean drive2Exit() throws Exception {
		// shows full map and solution so you can see robo run
		maze.showMaze = true;
		maze.showSolution = true;
		maze.mapMode = true;
		
		while (!robot.isAtGoal()) {//until the robo hits the goal this is true
			if (robot.batteryLevel != 0) {//makes sure robo still runs
				//moves forward without turning if there is open space, redraws maze then sleeps
				if (robot.distanceToObstacle(Direction.LEFT) == 0 && robot.distanceToObstacle(Direction.FORWARD) > 0) {
					robot.move(1, false);
					robot.maze.notifyViewerRedraw();
					Thread.sleep(100);
				}
				
				else {
					// turns left then moves forward, redraws then sleeps
					if (robot.distanceToObstacle(Direction.LEFT) > 0) {
						robot.rotate(Turn.LEFT);
						robot.move(1, false);
						robot.maze.notifyViewerRedraw();
						Thread.sleep(100);
					}
					// turns right then moves forward, redraws then sleeps
					else if (robot.distanceToObstacle(Direction.RIGHT) > 0) {
						robot.rotate(Turn.RIGHT);
						robot.move(1, false);
						robot.maze.notifyViewerRedraw();
						Thread.sleep(100);
					}
					// turns around then moves forward, redraws then sleeps
					else {
						robot.rotate(Turn.AROUND);
						robot.move(1, false);
						robot.maze.notifyViewerRedraw();
						Thread.sleep(100);
					}
				}
				//always increment pathlength
				pathLength++;
			}
			else {
				return false;
			}
		}
		// these conditionals check to see if the robot is next to the exit, if it is YOU WON!
		if (robot.canSeeGoal(Direction.LEFT)) {
			( (BasicRobot) robot).maze.state = Constants.StateGUI.STATE_FINISH;
			( (BasicRobot) robot).maze.notifyViewerRedraw();
		}
		
		else if (robot.canSeeGoal(Direction.RIGHT)) {
			( (BasicRobot) robot).maze.state = Constants.StateGUI.STATE_FINISH;
			( (BasicRobot) robot).maze.notifyViewerRedraw();
		}
		
		else if (robot.canSeeGoal(Direction.BACKWARD)) {
			( (BasicRobot) robot).maze.state = Constants.StateGUI.STATE_FINISH;
			( (BasicRobot) robot).maze.notifyViewerRedraw();
		}
		
		else if (robot.canSeeGoal(Direction.FORWARD)) {
			( (BasicRobot) robot).maze.state = Constants.StateGUI.STATE_FINISH;
			( (BasicRobot) robot).maze.notifyViewerRedraw();
		}
		
		return true;
	}

	@Override
	public float getEnergyConsumption() {
		return 2500 - robot.batteryLevel;
	}

	@Override
	public int getPathLength() {
		return pathLength;
	}

}
