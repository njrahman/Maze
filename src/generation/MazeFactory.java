package generation;

import generation.Factory;


/**
 * This class encapsulates how a maze is generated. 
 * It takes orders to produce a maze, delegates it to the matching maze builder 
 * that computes and delivers the maze. This class operates the worker thread
 * to do the computation in the background. The maze builder classes contribute
 * a run method to execute on the worker thread.
 */
public class MazeFactory implements Factory {
	// generation can be deterministic, i.e. same maze is generated each time for a given size
	private boolean deterministic;
	// factory keeps track of the current order, takes at most one order at a time
	private Order currentOrder;
	// factory has a MazeBuilder to do the work
	// note that subclasses are instantiated for specific algorithms such as Prim's
	// according to the given order
	private MazeBuilder builder;
	// 
	private Thread buildThread; // computations are performed in own separated thread with this.run()
	
	//////////////////////// Constructor ////////////////////////////////////////
	/**
	 * Constructor for a randomized maze generation
	 */
	public MazeFactory(){
		// nothing to do
	}
	/**
	 * Constructor with option to make maze generation deterministic or random
	 */
	public MazeFactory(boolean deterministic){
		this.deterministic = deterministic;
	}
	
	//////////////////////// Factory interface //////////////////////////////////
	@Override
	public boolean order(Order order) {
		// check if factory is busy
		if (null != buildThread && buildThread.isAlive()) {
			// order is currently processed, don't queue, just refuse
			System.out.println("MazeFactory.order: refusing to take order, too busy with current order");
			return false;
		}
		// idle, so accept order
		currentOrder = order;
		// set builder according to order
		switch (order.getBuilder()) {
		case DFS :
			builder = deterministic? new MazeBuilder(true) : new MazeBuilder();
			buildOrder();
			break;
		case Prim:
			builder = deterministic? new MazeBuilderPrim(true) : new MazeBuilderPrim();
			buildOrder();
			break;
		case Kruskal:
			builder = deterministic? new MazeBuilderKruskal(true) : new MazeBuilderKruskal();
			buildOrder();
			break;
		default:
			System.out.println("MazeFactory.order: missing implementation for requested algorithm: " + order.getBuilder());
			return false;
		}
		return true ;
	}
	@Override
	public void cancel() {
		System.out.println("MazeFactory.cancel: called");
		if (null != buildThread) {
			buildThread.interrupt() ;
			buildThread = null; // allow for next order to get through
		}
		else {
			System.out.println("MazeFactory.cancel: no thread to cancel");
		}
		// clean up happens in interrupt handling in run method
		builder = null;
		currentOrder = null;
	}
	@Override
	public void waitTillDelivered() {
		if (null != buildThread) {
			try {
				buildThread.join();
			} catch (Exception e) { 
				System.out.println("MazeBuilder.wailTillDelivered: join synchronization with builder thread lead to an exception") ;
			}
		}
		else {
			System.out.println("MazeBuilder.waitTillDelivered: no thread to wait for");
		}
		builder = null;
		currentOrder = null;
	}
	///////////////////////// private methods ///////////////////////////////////
	/**
	 * Provide the builder with necessary input and start its execution
	 */
	private void buildOrder() { 
		if (null == builder)
			return;
		System.out.println("MazeFactory.buildOrder: started") ;
		builder.buildOrder(currentOrder);
		buildThread = new Thread(builder);
		buildThread.start();
	}
}
