package org.optim4j.ns;

public class NeighborhoodSearchOptimizer<A extends Agent, T> implements Optimizer<A> {

	private CompletionCondition completionCondition;

	private AcceptanceCriteria acceptanceCriteria;

	private Repairer<T, A> repairer;

	private Destroyer<A, T> destroyer;

	private Observer observer;

	private String name;

	public NeighborhoodSearchOptimizer(AcceptanceCriteria acceptanceCriteria, CompletionCondition completionCondition,
			Repairer<T, A> repairer, Destroyer<A, T> destroyer, Observer observer, String name) {
		this.acceptanceCriteria = acceptanceCriteria;
		this.completionCondition = completionCondition;
		this.repairer = repairer;
		this.destroyer = destroyer;
		this.observer = observer;
		this.name = name;
	}

	public A optimize(A currentAgent) {
		int generation = 0;
		A bestAgent = currentAgent;
		while (!completionCondition.isComplete(currentAgent)) {
			observer.notify(bestAgent, name, generation++);
			A neighbour = this.repairer.repair(this.destroyer.destroy(currentAgent));
			if (acceptanceCriteria.isAcceptable(currentAgent, neighbour)) {
				currentAgent = neighbour;
				if (currentAgent.compareTo(bestAgent) > 0) {
					bestAgent = currentAgent;
				}
			}
		}
		return bestAgent;
	}

}
