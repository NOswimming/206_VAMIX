package vamix.function.worker;

import java.util.List;

import javax.swing.SwingWorker;

import vamix.function.IFunction;

/**
 * The template swing worker thread to work concurrently on tasks. It is designed to be
 * extended by the complete implementations.
 * It works by using the SwingWorker Class to create concurrent processes for long running tasks.
 * It then updates the appropriate Function class through use of the IFunction interface.
 * 
 * @see #ComponentManager
 * 
 * @author Callum Fitt-Simpson
 * 
 */
public abstract class AbstractWorker extends SwingWorker<Void, String> {

	public static final int CANCELLED_EXIT_VALUE = -10;

	protected Process process;
	protected int exitValue;
	protected IFunction function;
	
	public AbstractWorker() {}

	public AbstractWorker(IFunction function) {
		this.function = function;
	}

	/**
	 * To be overridden by implementing Worker Classes.
	 */
	@Override
	public abstract Void doInBackground();

	/**
	 * Updates the Function class with intermediate values
	 * by calling the doProcess method.
	 * 
	 * @see #IFunction#doProcess
	 */
	@Override
	protected void process(List<String> list) {
		for (String s : list) {
			function.doProcess(s);

		}
	}

	/**
	 * Updates the Function class that the process is finished
	 * by calling the doDone method.
	 * If cancelled it will return the cancelled exit value.
	 * 
	 * @see #IFunction#doDone #CANCELLED_EXIT_VALUE
	 */
	@Override
	public void done() {
		if (isCancelled()) {
			process.destroy();
			function.doDone(CANCELLED_EXIT_VALUE);
		}
		exitValue = process.exitValue();
		function.doDone(exitValue);
	}
}
