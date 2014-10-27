package vamix.function;

/**
 * The Interface for working with the AbstractWorker class.
 * Enables the function classes to be updated by the worker classes.
 * Specifies the basic life cycle of the function classes.
 * 
 * - LIFE CYCLE -
 * 1. The functions are instantiated setting up all the variables needed.
 * 2. Then the execute method is called starting the corresponding worker class.
 * 3. The worker class updates the function class with doProcess.
 * 4. The worker class finishes the process and calls doDone on the function class.
 * 
 * *. The GUI can call cancel on the function class which will cancel the worker class.
 * 
 * 
 * @see #AbstractWorker
 * 
 * @author Callum Fitt-Simpson
 * 
 */
public interface IFunction {
	
	/**
	 * Starts the function.
	 * Creates a corresponding worker class and executes it.
	 */
	public void execute();

	/**
	 * Called by the corresponding worker class to update the function class.
	 * The function class can then update the GUI.
	 */
	public void doProcess(String intermediateValue);

	/**
	 * Called by the corresponding worker class to tell the function class it has finished.
	 * The function class can then tell the user through the GUI.
	 */
	public void doDone(int exitValue);
	
	/**
	 * Used to cancel the functional process running in the background.
	 */
	public void cancel();

}
