package org.optim4j.ns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of neighborhood search optimization process.
 * <p>
 * The implementation accepts a solution agent which is a local optima of the
 * problem domain. The optimization process transforms the same using destroy
 * and repair heuristic over multiple iterations. The new solution in each
 * iteration is accepted based on provided acceptance criteria. The process
 * continues until the completion condition is satisfied.
 * </p>
 * 
 * @author Avijit Basak
 *
 * @param <A> A valid solution agent
 * @param <T> Partially destroyed solution agent
 */
public class NeighborhoodSearchOptimizer<A extends Agent, T> implements Optimizer<A> {

	/**
	 * Completion condition of the optimization process.
	 */
	private CompletionCondition completionCondition;

	/**
	 * Acceptance criteria for the new neighbor solution agent.
	 */
	private AcceptanceCriteria acceptanceCriteria;

	/**
	 * Observer for the optimization process.
	 */
	private Observer[] observers;

	/**
	 * Repairer to construct a valid solution agent from partially destroyed agent.
	 */
	private Repairer<T, A> repairer;

	/**
	 * Destroyer to destroy a valid solution agent.
	 */
	private Destroyer<A, T> destroyer;

	/** instance of logger. **/
	private static final Logger LOGGER = LoggerFactory.getLogger(NeighborhoodSearchOptimizer.class);

	/**
	 * Constructs an instance of neighborhood search optimizer.
	 * 
	 * @param acceptanceCriteria  An acceptance criteria
	 * @param completionCondition Completion condition of optimization
	 * @param repairer            Repairer to construct a valid agent from partially
	 *                            destroyed agent
	 * @param destroyer           Destroyer to destroy a valid agent to a partial
	 *                            solution
	 * @param observers           Observers to get notifications of optimization
	 *                            process
	 */
	public NeighborhoodSearchOptimizer(AcceptanceCriteria acceptanceCriteria, CompletionCondition completionCondition,
			Repairer<T, A> repairer, Destroyer<A, T> destroyer, Observer... observers) {
		this.acceptanceCriteria = acceptanceCriteria;
		this.completionCondition = completionCondition;
		this.repairer = repairer;
		this.destroyer = destroyer;
		this.observers = observers;
	}

	/**
	 * Executes the optimization process. Accepts a solution agent as input
	 * optimizes the same using destroy and repair process through multiple
	 * iterations following neighborhood search methodology and returns an optimum
	 * result.
	 * 
	 * @param A A valid solution agent representing a local optima
	 * @return An optimized solution agent
	 */
	public A optimize(A currentAgent) {
		LOGGER.info("Input Solution Agent: " + currentAgent.toString());
		int generation = 0;
		A bestAgent = currentAgent;
		while (!completionCondition.isComplete(currentAgent)) {
			LOGGER.debug("Current Solution Agent: " + currentAgent.toString());

			// Notify all registered observers.
			for (Observer observer : observers) {
				observer.notify(bestAgent, generation++);
			}

			// Generate the neighbor.
			A neighbour = this.repairer.repair(this.destroyer.destroy(currentAgent));
			LOGGER.debug("New neighbor: " + neighbour.toString());

			// Check if neighbor is acceptable compared to current agent.
			if (acceptanceCriteria.isAcceptable(currentAgent, neighbour)) {
				LOGGER.debug("Neighbor Agent is acceptable.");
				currentAgent = neighbour;
				if (currentAgent.compareTo(bestAgent) > 0) {
					LOGGER.debug("Neighbor Agent is better than last best Agent.");
					bestAgent = currentAgent;
				}
			}
		}
		LOGGER.info("Optimized Solution Agent: " + bestAgent.toString());
		return bestAgent;
	}

}
