package org.optim4j.ns;

/**
 * Optimization completion condition.
 */
@FunctionalInterface
public interface CompletionCondition {

	/**
	 * Checks if the optimization process is complete.
	 * 
	 * @param agent an {@link Agent}
	 * @return completion status of the optimization process
	 */
	boolean isComplete(Agent agent);

}
