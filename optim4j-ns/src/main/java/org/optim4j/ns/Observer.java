package org.optim4j.ns;

/**
 * Observer of the optimization process to capture all notifications.
 * 
 * @author Avijit Basak
 */
public interface Observer<A extends Agent, T> {

	/**
	 * Accepts notification of the current optimization status i.e. best agent,
	 * current agent, acceptance criteria, selected repairer, selected destroyer of
	 * current generation.
	 * 
	 * @param bestAgent          current best agent
	 * @param currentAgent       current agent
	 * @param acceptanceCriteria acceptance criteria
	 * @param selectedRepairer   selected repairer
	 * @param selectedDestroyer  selected destroyer
	 */
	void notify(Agent bestAgent, Agent currentAgent, AcceptanceCriteria acceptanceCriteria,
			Repairer<T, A> selectedRepairer, Destroyer<A, T> selectedDestroyer);
}
