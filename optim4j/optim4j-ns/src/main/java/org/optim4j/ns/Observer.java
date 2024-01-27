package org.optim4j.ns;

/**
 * Observes the optimization process and captures all notifications.
 * 
 * @author Avijit Basak
 * 
 */
public interface Observer {

	void notify(Agent agent, String name, int generation);
}
