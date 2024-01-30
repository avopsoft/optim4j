package org.optim4j.ns;

/**
 * Observer of the optimization process to capture all notifications.
 */
public interface Observer {

	/**
	 * Accepts notification of the current best agent.
	 * 
	 * @param agent      Current best agent
	 * @param generation Generation number
	 */
	void notify(Agent agent, int generation);
}
