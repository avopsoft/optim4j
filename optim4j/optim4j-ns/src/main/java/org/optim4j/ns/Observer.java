package org.optim4j.ns;

/**
 * Observes the optimization process and captures all notifications.
 * 
 * @author Avijit Basak
 * 
 */
public interface Observer {

	/**
	 * Notify the listener about the current best agent.
	 * 
	 * @param agent      Current best agent
	 * @param generation Generation number
	 */
	void notify(Agent agent, int generation);
}
