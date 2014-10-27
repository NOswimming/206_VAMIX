package vamix.function.worker;

import java.util.List;

import javax.swing.SwingWorker;

import vamix.function.IFunction;

/**
 * The swing worker thread to work concurrently on tasks. It is designed to be
 * flexible and use able by all functions.
 */
public abstract class Worker extends SwingWorker<Void, String> {

	public static final int CANCELED_EXIT_VALUE = -10;

	protected Process process;
	protected int exitValue;
	protected IFunction function;
	
	public Worker() {}

	public Worker(IFunction function) {
		this.function = function;
	}

	@Override
	public abstract Void doInBackground();

	@Override
	protected void process(List<String> list) {
		for (String s : list) {
			function.doProcess(s);

		}
	}

	@Override
	public void done() {
		if (isCancelled()) {
			process.destroy();
			function.doDone(CANCELED_EXIT_VALUE);
		}
		exitValue = process.exitValue();
		function.doDone(exitValue);
	}
}
