package org.optim4j.ns;

/**
 * Observer of the optimization process to capture all notifications.
 */
public interface Observer {

	/**
	 * Accepts notification of the current best agent.
	 * 
	 * @param agent      current best agent
	 * @param generation generation number
	 */
	void notify(Agent agent, int generation);
}
