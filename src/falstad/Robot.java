package falstad;

import generation.CardinalDirection;
import generation.MazeConfiguration;


/**
 * This interface specifies methods to operate a robot that is inside 
 * a maze at a particular location and looking in a particular direction.
 * An implementing class will support a robot with certain sensors. 
 * A robot needs to be given an existing maze to be operational.
 * It provides an operating platform for a robotdriver that experiences a maze (the real world) 
 * through the sensors and actors of this robot interface.
 * 
 * Note that a robot may be very limited in its mobility, e.g. only 90 degree left or right turns, 
 * which makes sense in the artificial terrain of a maze, and its sensing capability, 
 * e.g. only a sensor on its front or left to detect remote obstacles. 
 * Left/right is a notion relative to the robot's direction 
 * or relative to the underlying maze. 
 * To avoid a confusion, the latter is considered a direction in an absolute sense 
 * and it may be better to describe it as a cardinal direction 
 * north, south, east, west than up, down, right, left. 
 * 
 * A robot comes with a battery level that is depleted during operations 
 * such that a robot may actually stop if it runs out of energy.
 * This interface supports energy consideration. 
 * A robot may also stop when hitting an obstacle. 
 * 
 * WARNING: the use of CW_BOT/CW_TOP and CardinalDirection in Cells and 
 * Mazebuilder is does not directly match with the MapDrawer 
 * which draws position (0,0) at the lower left corner, such that 
 * x values grow towards the right, y values grow towards the top and
 * direction SOUTH is towards the top of the display. 
 * Or in other words, the maze is drawn upside down by the MapDrawer but
 * East and West are as one expects it (East to the right, West to the left).
 *  
 * The rotation is calculated with polar coordinates (angle) towards a 
 * cartesian coordinate system where a southbound direction is (dx,dy)=(0,1).
 * 
 * Implementing classes: movable robots with distance sensors of different kind. 
 * 
 * Collaborators: a maze class to be explored, a robotdriver class that operates robot
 * 
 * @author peterkemper
 *
 */
public interface Robot {
	/** 
	 * Describes all possible turns that a robot can do when it rotates on the spot.
	 * Left is 90 degrees left, right is 90 degrees right, turn around is 180 degrees.
	 */
	public enum Turn { LEFT, RIGHT, AROUND };
	/**
	 * Describes all possible directions from the point of view of the robot,
	 * i.e., relative to its current forward position.
	 * Mind the difference between the robot's point of view
	 * and cardinal directions in terms of north,south,east,west.
	 */
	public enum Direction { LEFT, RIGHT, FORWARD, BACKWARD };
	/**
	 * Turn robot on the spot for amount of degrees. 
	 * If robot runs out of energy, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level. 
	 * @param direction to turn and relative to current forward direction. 
	 */
	void rotate(Turn turn);
	/**
	 * Moves robot forward a given number of steps. A step matches a single cell.
	 * If the robot runs out of energy somewhere on its way, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level. 
	 * If the robot hits an obstacle like a wall, it depends on the mode of operation
	 * what happens. If an algorithm drives the robot, it remains at the position in front 
	 * of the obstacle and also hasStopped() == true as this is not supposed to happen.
	 * This is also helpful to recognize if the robot implementation and the actual maze
	 * do not share a consistent view on where walls are and where not.
	 * If a user manually operates the robot, this behavior is inconvenient for a user,
	 * such that in case of a manual operation the robot remains at the position in front
	 * of the obstacle but hasStopped() == false and the game can continue.
	 * @param distance is the number of cells to move in the robot's current forward direction 
	 * @param manual is true if robot is operated manually by user, false otherwise
	 * @precondition distance >= 0
	 */
	void move(int distance, boolean manual);
	/**
	 * Provides the current position as (x,y) coordinates for the maze cell as an array of length 2 with [x,y].
	 * @postcondition 0 <= x < width, 0 <= y < height of the maze. 
	 * @return array of length 2, x = array[0], y=array[1]
	 * @throws Exception if position is outside of the maze
	 */
	int[] getCurrentPosition() throws Exception  ;
	/**
	 * Provides the robot with a reference to the maze it is currently in.
	 * The robot memorizes the maze such that this method is most likely called only once
	 * and for initialization purposes. The maze serves as the main source of information
	 * for the robot about the current position, the presence of walls, the reaching of an exit.
	 * @param maze is the current maze
	 * @precondition maze != null, maze refers to a fully operational, configured maze configuration
	 */
	void setMaze(MazeController maze) ;
	/**
	 * Tells if current position is at the goal (the exit). Used to recognize termination of a search.
	 * @return true if robot is at the goal, false otherwise
	 */
	boolean isAtGoal() ;
	/**
	 * Tells if a sensor can identify the goal in given direction relative to 
	 * the robot's current forward direction from the current position.
	 * @return true if the goal (here: exit of the maze) is visible in a straight line of sight
	 * @throws UnsupportedOperationException if robot has no sensor in this direction
	 */
	boolean canSeeGoal(Direction direction) throws UnsupportedOperationException ;
	/**
	 * Tells if current position is inside a room. 
	 * @return true if robot is inside a room, false otherwise
	 * @throws UnsupportedOperationException if not supported by robot
	 */	
	boolean isInsideRoom() throws UnsupportedOperationException ;
	/**
	 * Tells if the robot has a room sensor.
	 */
	boolean hasRoomSensor() ;	
	/**
	 * Provides the current cardinal direction.
	 * 
	 * @return cardinal direction is robot's current direction in absolute terms
	 */	
	CardinalDirection getCurrentDirection() ;
	/**
	 * Returns the current battery level.
	 * The robot has a given battery level (energy level) that it draws energy from during operations. 
	 * The particular energy consumption is device dependent such that a call for distance2Obstacle may use less energy than a move forward operation.
	 * If battery level <= 0 then robot stops to function and hasStopped() is true.
	 * @return current battery level, level is > 0 if operational. 
	 */
	float getBatteryLevel() ;
	/**
	 * Sets the current battery level.
	 * The robot has a given battery level (energy level) that it draws energy from during operations. 
	 * The particular energy consumption is device dependent such that a call for distance2Obstacle may use less energy than a move forward operation.
	 * If battery level <= 0 then robot stops to function and hasStopped() is true.
	 * @param level is the current battery level
	 * @precondition level >= 0 
	 */
	void setBatteryLevel(float level) ;
	/**
	 * Gives the energy consumption for a full 360 degree rotation.
	 * Scaling by other degrees approximates the corresponding consumption. 
	 * @return energy for a full rotation
	 */
	float getEnergyForFullRotation() ;
	/**
	 * Gives the energy consumption for moving forward for a distance of 1 step.
	 * For simplicity, we assume that this equals the energy necessary 
	 * to move 1 step backwards and that scaling by a larger number of moves is 
	 * approximately the corresponding multiple.
	 * @return energy for a single step forward
	 */
	float getEnergyForStepForward() ;
	/**
	 * Tells if the robot has stopped for reasons like lack of energy, hitting an obstacle, etc.
	 * @return true if the robot has stopped, false otherwise
	 */
	boolean hasStopped() ;
	/**
	 * Tells the distance to an obstacle (a wall or border) 
	 * in a direction as given and relative to the robot's current forward direction.
	 * Distance is measured in the number of cells towards that obstacle, 
	 * e.g. 0 if current cell has a wall in this direction, 
	 * 1 if it is one step forward before directly facing a wall,
	 * Integer.MaxValue if one looks through the exit into eternity.
	 * @return number of steps towards obstacle if obstacle is visible 
	 * in a straight line of sight, Integer.MAX_VALUE otherwise
	 * @throws UnsupportedOperationException if not supported by robot
	 */
	int distanceToObstacle(Direction direction) throws UnsupportedOperationException ;
	/**
	 * Tells if the robot has a distance sensor for the given direction.
	 * Since this interface is generic and may be implemented with robots 
	 * that are more or less equipped. The purpose is to allow for a flexible
	 * robot driver to adapt its driving strategy according the features it
	 * finds supported by a robot.
	 */
	boolean hasDistanceSensor(Direction direction) ;

}