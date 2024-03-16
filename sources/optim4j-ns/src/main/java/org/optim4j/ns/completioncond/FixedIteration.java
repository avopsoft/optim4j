package org.optim4j.ns.completioncond;

import org.optim4j.ns.Agent;
import org.optim4j.ns.CompletionCondition;

/**
 * A completion condition based on total number of optimization iterations.
 * 
 * @author Avijit Basak
 */
public final class FixedIteration implements CompletionCondition {

	/**
	 * Max number of iterations the optimization process continues.
	 */
	private final int maxIterationCount;

	/**
	 * The current iteration count.
	 */
	private int iterationCount;

	/**
	 * Constructs an instance with maximum iteration count.
	 * 
	 * @param maxIterationCount number of iteration for which the optimization
	 *                          process would continue
	 */
	public FixedIteration(int maxIterationCount) {
		this.maxIterationCount = maxIterationCount;
	}

	/**
	 * Checks if the configured number of iterations is over.
	 */
	@Override
	public boolean isComplete(Agent agent) {
		if (iterationCount++ == maxIterationCount) {
			return true;
		}
		return false;
	}

	/**
	 * Returns current iteration count.
	 * 
	 * @return iteration count
	 */
	public int getIterationCount() {
		return iterationCount;
	}
}
