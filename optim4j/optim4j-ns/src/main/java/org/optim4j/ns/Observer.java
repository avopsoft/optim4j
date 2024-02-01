package org.optim4j.ns;

/**
 * Observer of the optimization process to capture all notifications.
 * 
 * @author Avijit Basak
 */
public interface Observer {

	/**
	 * Accepts notification of the current best agent.
	 * 
	 * @param bestAgent      current best agent
	 * @param currentAgent TODO
	 * @param generation generation number
	 */
	void notify(Agent bestAgent, Agent currentAgent, int generation);
}
