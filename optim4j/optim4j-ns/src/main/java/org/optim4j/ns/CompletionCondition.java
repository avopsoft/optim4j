package org.optim4j.ns;

/**
 * Optimization completion condition.
 */
@FunctionalInterface
public interface CompletionCondition {

	/**
	 * Checks if the optimization process is complete.
	 * 
	 * @param agent {@link Agent}
	 * @return If optimization process is complete or not
	 */
	boolean isComplete(Agent agent);

}
