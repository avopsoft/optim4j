package org.optim4j.ns.observer;

import org.optim4j.ns.AcceptanceCriteria;
import org.optim4j.ns.Agent;
import org.optim4j.ns.Destroyer;
import org.optim4j.ns.Observer;
import org.optim4j.ns.Repairer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A logger based observer of optimization process.
 * 
 * @author Avijit Basak
 *
 * @param <A> the solution agent type
 * @param <T> partially destroyed solution agent
 */
public class OptimizationLogger<A extends Agent, T> implements Observer<A, T> {

	/**
	 * The current generation.
	 */
	private int generation;

	/**
	 * Instance of logger.
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(OptimizationLogger.class);

	/**
	 * Logs notification of the current optimization status i.e. best agent, current
	 * agent, acceptance criteria, selected repairer, selected destroyer of current
	 * generation.
	 * 
	 * @param bestAgent          current best agent
	 * @param currentAgent       current agent
	 * @param acceptanceCriteria acceptance criteria
	 * @param selectedRepairer   selected repairer
	 * @param selectedDestroyer  selected destroyer
	 */
	@Override
	public void notify(Agent bestAgent, Agent currentAgent, AcceptanceCriteria acceptanceCriteria,
			Repairer<T, A> selectedRepairer, Destroyer<A, T> selectedDestroyer) {
		LOGGER.info("Current generation: {}", generation++);
		LOGGER.info("Best agent fitness: {}", bestAgent.evaluate());
		LOGGER.info("Current agent fitness: {}", currentAgent.evaluate());
		LOGGER.info("Acceptance criteria: {}", acceptanceCriteria);
		LOGGER.info("Selected repairer: {}", selectedRepairer);
		LOGGER.info("Selected destroyer: {}", selectedDestroyer);
	}

}
