package org.optim4j.ns;

/**
 * Optimization completion condition.
 * 
 * @author Avijit Basak
 * 
 */
@FunctionalInterface
public interface CompletionCondition {

	/**
	 * Checks if the optimization process is complete.
	 * 
	 * @param agent {@link Agent}
	 * @return {@link Boolean}
	 */
	boolean isComplete(Agent agent);

}
