package org.optim4j.ns;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of neighborhood search optimization process.
 * <p>
 * This implementation accepts a solution agent which is a local optima of the
 * problem domain. The optimization process transforms the same using destroy
 * and repair heuristic over multiple iterations. The new solution in each
 * iteration is accepted based on provided acceptance criteria. The process
 * continues until the completion condition is satisfied.
 * </p>
 * 
 * @author Avijit Basak
 *
 * @param <A> a valid solution agent type
 * @param <T> partially destroyed solution agent type
 */
public class LargeNeighborhoodSearchOptimizer<A extends Agent, T> implements Optimizer<A> {

	/**
	 * Completion condition of the optimization process.
	 */
	private CompletionCondition completionCondition;

	/**
	 * Acceptance criteria for the new neighbor solution agent.
	 */
	private AcceptanceCriteria acceptanceCriteria;

	/**
	 * Observer of the optimization process.
	 */
	private Observer<A, T> observer;

	/**
	 * Repairer to construct a valid solution agent from partially destroyed agent.
	 */
	private Repairer<T, A> repairer;

	/**
	 * Destroyer to destroy a valid solution agent.
	 */
	private Destroyer<A, T> destroyer;

	/** Instance of logger. **/
	private static final Logger LOGGER = LoggerFactory.getLogger(LargeNeighborhoodSearchOptimizer.class);

	/**
	 * Constructs an instance of neighborhood search optimizer.
	 * 
	 * @param acceptanceCriteria  acceptance criteria for newly generated solution
	 *                            agent
	 * @param completionCondition optimization completion condition
	 * @param repairer            repairer to construct a valid agent from partially
	 *                            destroyed agent
	 * @param destroyer           destroyer to destroy a valid agent to a partial
	 *                            solution
	 * @param observer            observer to get notifications of optimization
	 *                            process
	 * 
	 * @throws NullPointerException if any input argument is null
	 */
	public LargeNeighborhoodSearchOptimizer(AcceptanceCriteria acceptanceCriteria, CompletionCondition completionCondition,
			Repairer<T, A> repairer, Destroyer<A, T> destroyer, Observer<A, T> observer) {
		/*
		 * Validate input arguments.
		 */
		Objects.requireNonNull(acceptanceCriteria, "Acceptance criteria cannot be Null");
		Objects.requireNonNull(completionCondition, "Completion condition cannot be Null");
		Objects.requireNonNull(repairer, "Repairer cannot be Null");
		Objects.requireNonNull(destroyer, "Destroyer cannot be Null");

		this.acceptanceCriteria = acceptanceCriteria;
		this.completionCondition = completionCondition;
		this.repairer = repairer;
		this.destroyer = destroyer;
		this.observer = observer;
	}

	/**
	 * Executes the optimization process. Accepts a solution agent as input
	 * optimizes the same using destroy and repair process through multiple
	 * iterations following neighborhood search methodology and returns an optimum
	 * result.
	 * 
	 * @param agent a valid solution agent representing a local optima
	 * 
	 * @return the optimized solution agent
	 */
	public A optimize(A agent) {

		LOGGER.info("Input Solution Agent: {}", agent);
		A bestAgent = agent;

		/*
		 * Check if the completion condition is satisfied. If not continue to generate a
		 * new neighbor following the Neighborhood Search algorithm.
		 */
		while (!completionCondition.isComplete(agent)) {
			LOGGER.debug("Current Solution Agent: {}", agent);

			/*
			 * Generate the neighbor.
			 */
			A neighbour = this.repairer.repair(this.destroyer.destroy(agent));
			LOGGER.debug("New neighbor: {}", neighbour);

			/*
			 * Check if neighbor is acceptable compared to current agent. Replace the
			 * current agent and best agent depending on it's fitness and acceptability.
			 */
			if (acceptanceCriteria.isAcceptable(agent, neighbour)) {
				LOGGER.debug("Neighbor Agent is acceptable.");
				agent = neighbour;
				if (agent.compareTo(bestAgent) > 0) {
					LOGGER.debug("Neighbor Agent is better than last best Agent.");
					LOGGER.info("New Best Agent: {}", agent);
					bestAgent = agent;
				}
			}

			/*
			 * Notify the registered observer.
			 */
			if (observer != null) {
				observer.notify(bestAgent, agent, acceptanceCriteria, repairer, destroyer);
			}
		}
		LOGGER.info("Optimized Solution Agent: {}", bestAgent);
		return bestAgent;
	}

}
