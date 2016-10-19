package falstad;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import falstad.BasicRobot;
import falstad.MazeApplication;
import falstad.Robot.Direction;
import falstad.SingleRandom;


//These are very basic tests for getters and simple battery tests
public class BasicRobotTest {
	BasicRobot robot;
	MazeApplication a;
	
	@Before
	public void setUp() throws Exception{
		SingleRandom.setSeed(17);
		robot = new BasicRobot();
		a = new MazeApplication("Prim"); // uses prim because we know prim is perfect
		a.repaint();	
	}
	
	
	/** 4*3=12, so it should take 12 battery for a full rotation
	 */
	@Test
	public void testGetEnergyForFullRotation() {
		
		assertTrue(12 == robot.getEnergyForFullRotation());
	}

	@Test
	public void testGetEnergyForStepForward() {
		
		assertTrue(robot.getEnergyForStepForward() == 5);
	}

	@Test
	public void testHasRoomSensor(){
		assertTrue(robot.hasRoomSensor());
	}
	
	@Test
	public void testHasDistanceSensorBackward(){
		assertTrue(robot.hasDistanceSensor(Direction.BACKWARD));
	}
	
	@Test
	public void testHasDistanceSensorForward(){

		assertTrue(robot.hasDistanceSensor(Direction.FORWARD));
	}
	@Test
	public void testHasDistanceSensorLeft(){

		assertTrue(robot.hasDistanceSensor(Direction.LEFT));
	}
	@Test
	public void testHasDistanceSensorRight(){

		assertTrue(robot.hasDistanceSensor(Direction.RIGHT));
	}
	
}