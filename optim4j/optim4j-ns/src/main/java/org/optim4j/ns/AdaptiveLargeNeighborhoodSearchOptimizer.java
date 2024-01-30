package org.optim4j.ns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of adaptive large neighborhood search algorithm.
 * 
 * <p>
 * The implementation accepts a solution agent which is a local optima of the
 * problem domain. The optimization process transforms the same using destroy
 * and repair heuristics over multiple iterations. The new solution in each
 * iteration is accepted based on provided acceptance criteria. The process
 * continues until the completion condition is satisfied.
 * </p>
 *
 * @param <A> An input solution agent type
 * @param <T> A partially destroyed solution agent type
 */
public class AdaptiveLargeNeighborhoodSearchOptimizer<A extends Agent, T> implements Optimizer<A> {

	/**
	 * Completion condition of the optimization process.
	 */
	private CompletionCondition completionCondition;

	/**
	 * Acceptance criteria for the new neighbor solution agent.
	 */
	private AcceptanceCriteria acceptanceCriteria;

	/**
	 * Observers of the optimization process.
	 */
	private Observer[] observers;

	/**
	 * A score manager for destroyers and repairers.
	 */
	private ScoreBasedRepairerDestroyerManager repairerDestroyerManager;

	/**
	 * Instance of logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AdaptiveLargeNeighborhoodSearchOptimizer.class);

	/**
	 * Constructs an instance of adaptive large neighborhood search optimizer.
	 * 
	 * @param acceptanceCriteria  An acceptance criteria for newly generated
	 *                            solution agent
	 * @param completionCondition Optimization completion condition
	 * @param repairers           A {@link List} of repairers to be used for this
	 *                            optimization
	 * @param destroyers          A {@link List} of destroyers to be used for this
	 *                            optimization
	 * @param repairerScores      A {@link Map} of repairer's scores for different
	 *                            options
	 * @param destroyerScores     A {@link Map} of destroyer's scores for different
	 *                            options
	 * @param observers           Optimization observers
	 */
	public AdaptiveLargeNeighborhoodSearchOptimizer(AcceptanceCriteria acceptanceCriteria,
			CompletionCondition completionCondition, List<Repairer<T, A>> repairers, List<Destroyer<A, T>> destroyers,
			Scores repairerScores, Scores destroyerScores, Observer... observers) {
		this.acceptanceCriteria = acceptanceCriteria;
		this.completionCondition = completionCondition;
		this.repairerDestroyerManager = new ScoreBasedRepairerDestroyerManager(repairers, destroyers, repairerScores,
				destroyerScores);
		this.observers = observers;
	}

	/**
	 * Constructs an instance of adaptive large neighborhood search optimizer.
	 * 
	 * @param acceptanceCriteria  An acceptance criteria for newly generated
	 *                            solution agent
	 * @param completionCondition Optimization completion condition
	 * @param repairers           A {@link List} of repairers to be used for this
	 *                            optimization
	 * @param destroyers          A {@link List} of destroyers to be used for this
	 *                            optimization
	 * @param observers           Optimization observers
	 */
	public AdaptiveLargeNeighborhoodSearchOptimizer(AcceptanceCriteria acceptanceCriteria,
			CompletionCondition completionCondition, List<Repairer<T, A>> repairers, List<Destroyer<A, T>> destroyers,
			Observer... observers) {
		this.acceptanceCriteria = acceptanceCriteria;
		this.completionCondition = completionCondition;
		this.repairerDestroyerManager = new ScoreBasedRepairerDestroyerManager(repairers, destroyers);
		this.observers = observers;
	}

	/**
	 * Executes the optimization process. Accepts a solution agent as input
	 * optimizes the same using multiple destroy and repair heuristics based on
	 * their performance score following ALNS methodology. The new solution agent at
	 * each iteration is accepted based on result of acceptance criteria evaluation.
	 * An optimum solution is returned once the completion condition is met.
	 * 
	 * @param agent A valid solution agent representing a local optima
	 * @return An optimized solution agent
	 */
	public A optimize(A agent) {
		LOGGER.info("Input Solution Agent: " + agent.toString());
		int generation = 0;
		A bestAgent = agent;
		while (!completionCondition.isComplete(agent)) {
			for (Observer observer : observers) {
				observer.notify(bestAgent, generation);
			}
			if (generation++ % 10 == 0) {
				LOGGER.info("Update score boundaries");
				repairerDestroyerManager.updateScoreBoundaries();
			}

			// Extract repairer and destroyer.
			LOGGER.trace("Find Destroyer and Repairer.");
			final Repairer<T, A> repairer = repairerDestroyerManager.getRepairer();
			LOGGER.debug("Selected repairer : " + repairer);
			final Destroyer<A, T> destroyer = repairerDestroyerManager.getDestroyer();
			LOGGER.debug("Selected destroyer : " + destroyer);

			// Find the neighbor solution.
			LOGGER.trace("Find the neighbor solution.");
			final A neighbour = repairer.repair(destroyer.destroy(agent));
			LOGGER.debug("New Neighbor: " + neighbour.toString());

			LOGGER.debug("Update repairer and destroyer scores.");
			// Update the repairer and destroyer scores.
			if (neighbour.compareTo(bestAgent) >= 0) {
				LOGGER.debug(
						"Neighbor Agent better than last best Agent. Replace current and best Agent by neighbor Agent.");
				repairerDestroyerManager.updateScoresWhenNeighborBetterThanBest(repairer, destroyer);
				agent = neighbour;
				bestAgent = neighbour;
			} else if (neighbour.compareTo(agent) >= 0) {
				LOGGER.debug("Neighbor Agent better than current Agent. Replace current Agent by neighbor Agent.");
				repairerDestroyerManager.updateScoresWhenNeighborBetterThanCurrent(repairer, destroyer);
				agent = neighbour;
			} else if (acceptanceCriteria.isAcceptable(agent, neighbour)) {
				LOGGER.debug("Neighbor Agent is acceptable. Replace current Agent by neighbor Agent.");
				repairerDestroyerManager.updateScoresWhenNeighborAcceptable(repairer, destroyer);
				agent = neighbour;
			}
		}
		LOGGER.info("Optimum Solution Agent: " + bestAgent.toString());
		return bestAgent;
	}

	/**
	 * Manager of repairers and destroyers and their score.
	 */
	private class ScoreBasedRepairerDestroyerManager {

		/**
		 * A {@link Map} of repairer scores.
		 */
		private Map<Repairer<T, A>, Double> repairerScoreMap = new HashMap<>();

		/**
		 * A {@link Map} of destroyer scores.
		 */
		private Map<Destroyer<A, T>, Double> destroyerScoreMap = new HashMap<>();

		/**
		 * A {@link Map} of score boundary and repairers.
		 */
		private Map<ScoreBoundary, Repairer<T, A>> repairerScoreBoundaries = new HashMap<>();

		/**
		 * A {@link Map} of score boundary and destroyers.
		 */
		private Map<ScoreBoundary, Destroyer<A, T>> destroyerScoreBoundaries = new HashMap<>();

		/**
		 * Repairer scores.
		 */
		private Scores repairerScores;

		/**
		 * Destroyer scores.
		 */
		private Scores destroyerScores;

		/**
		 * Initializes repairers and destroyers with user provided scores.
		 * 
		 * @param repairers       {@link List} of repairers
		 * @param destroyers      {@link List} of destroyers
		 * @param repairerScores  {@link Scores} of repairers
		 * @param destroyerScores {@link Scores} of destroyers
		 */
		private ScoreBasedRepairerDestroyerManager(List<Repairer<T, A>> repairers, List<Destroyer<A, T>> destroyers,
				Scores repairerScores, Scores destroyerScores) {
			this.repairerScores = repairerScores;
			this.destroyerScores = destroyerScores;
			repairers.stream().forEach(repairer -> repairerScoreMap.put(repairer, repairerScores.initialScore));
			destroyers.stream().forEach(destroyer -> destroyerScoreMap.put(destroyer, destroyerScores.initialScore));
		}

		/**
		 * Initializes repairers and destroyers with default scores.
		 * 
		 * @param repairers  {@link List} of repairers
		 * @param destroyers {@link List} of destroyers
		 */
		private ScoreBasedRepairerDestroyerManager(List<Repairer<T, A>> repairers, List<Destroyer<A, T>> destroyers) {
			this.repairerScores = new Scores(25.0, 100.0, 75.0, 50.0);
			this.destroyerScores = new Scores(25.0, 100.0, 75.0, 50.0);
			repairers.stream().forEach(repairer -> repairerScoreMap.put(repairer, repairerScores.initialScore));
			destroyers.stream().forEach(destroyer -> destroyerScoreMap.put(destroyer, destroyerScores.initialScore));
		}

		/**
		 * Updates score boundaries for repairers and destroyers.
		 */
		private void updateScoreBoundaries() {
			Set<Repairer<T, A>> repairers = repairerScoreMap.keySet();
			Set<Destroyer<A, T>> destroyers = destroyerScoreMap.keySet();

			LOGGER.trace("Update score boundaries of repairers.");
			Double cumulativeScore = Double.valueOf(0);
			for (Repairer<T, A> repairer : repairers) {
				ScoreBoundary scoreBoundary = new ScoreBoundary(cumulativeScore,
						cumulativeScore = (cumulativeScore + repairerScoreMap.get(repairer)));
				repairerScoreBoundaries.put(scoreBoundary, repairer);
			}

			LOGGER.trace("Update score boundaries of destroyers.");
			cumulativeScore = Double.valueOf(0);
			for (Destroyer<A, T> destroyer : destroyers) {
				ScoreBoundary scoreBoundary = new ScoreBoundary(cumulativeScore,
						cumulativeScore = (cumulativeScore + destroyerScoreMap.get(destroyer)));
				destroyerScoreBoundaries.put(scoreBoundary, destroyer);
			}
		}

		/**
		 * Selects a repairer using roulette wheel selection based on their score.
		 * 
		 * @return a repairer
		 */
		private Repairer<T, A> getRepairer() {
			LOGGER.trace("Calculate total score for repairers.");
			Optional<Double> totalScore = repairerScoreBoundaries.keySet().stream()
					.map(scoreBoundary -> scoreBoundary.maxScore).reduce((Double t, Double u) -> t > u ? t : u);
			LOGGER.trace("Generate random score.");
			double randomScore = Math.random() * totalScore.get();
			LOGGER.trace("Generated random score : " + randomScore);
			for (ScoreBoundary scoreBoundary : repairerScoreBoundaries.keySet()) {
				if (scoreBoundary.isBetween(randomScore)) {
					return repairerScoreBoundaries.get(scoreBoundary);
				}
			}
			throw new RuntimeException("Invalid random score generated.");
		}

		/**
		 * Selects a destroyer using roulette wheel selection based on their score.
		 * 
		 * @return a destroyer
		 */
		private Destroyer<A, T> getDestroyer() {
			LOGGER.trace("Calculate total score for destroyers.");
			Optional<Double> totalScore = destroyerScoreBoundaries.keySet().stream()
					.map(scoreBoundary -> scoreBoundary.maxScore).reduce((Double t, Double u) -> t > u ? t : u);
			LOGGER.trace("Generate random score.");
			double randomScore = Math.random() * totalScore.get();
			LOGGER.trace("Generated random score : " + randomScore);
			for (ScoreBoundary scoreBoundary : destroyerScoreBoundaries.keySet()) {
				if (scoreBoundary.isBetween(randomScore)) {
					return destroyerScoreBoundaries.get(scoreBoundary);
				}
			}
			throw new RuntimeException("Invalid random score generated.");
		}

		/**
		 * An encapsulation of score boundary for repairer and destroyer.
		 */
		private class ScoreBoundary {

			/**
			 * Minimum score
			 */
			private double minScore;

			/**
			 * Maximum score
			 */
			private double maxScore;

			private ScoreBoundary(double minScore, double maxScore) {
				this.minScore = minScore;
				this.maxScore = maxScore;
			}

			private boolean isBetween(double score) {
				return score >= minScore && score < maxScore;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + getEnclosingInstance().hashCode();
				result = prime * result + Objects.hash(maxScore, minScore);
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				ScoreBoundary other = (ScoreBoundary) obj;
				if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
					return false;
				return Double.doubleToLongBits(maxScore) == Double.doubleToLongBits(other.maxScore)
						&& Double.doubleToLongBits(minScore) == Double.doubleToLongBits(other.minScore);
			}

			private ScoreBasedRepairerDestroyerManager getEnclosingInstance() {
				return ScoreBasedRepairerDestroyerManager.this;
			}

			@Override
			public String toString() {
				return "ScoreBoundary [minScore=" + minScore + ", maxScore=" + maxScore + "]";
			}

		}

		/**
		 * Updates repairer and destroyer scores when the neighbor agent is better than
		 * best agent.
		 * 
		 * @param repairer  selected repairer
		 * @param destroyer selected destroyer
		 */
		private void updateScoresWhenNeighborBetterThanBest(Repairer<T, A> repairer, Destroyer<A, T> destroyer) {
			double existingRepairerScore = this.repairerScoreMap.get(repairer);
			double existingDestroyerScore = this.destroyerScoreMap.get(destroyer);
			this.repairerScoreMap.put(repairer,
					repairerScores.scoreIncrementWhenNeighborBetterThanBest + existingRepairerScore);
			this.destroyerScoreMap.put(destroyer,
					destroyerScores.scoreIncrementWhenNeighborBetterThanBest + existingDestroyerScore);
		}

		/**
		 * Updates repairer and destroyer scores when the neighbor agent is better than
		 * current agent.
		 * 
		 * @param repairer  selected repairer
		 * @param destroyer selected destroyer
		 */
		private void updateScoresWhenNeighborBetterThanCurrent(Repairer<T, A> repairer, Destroyer<A, T> destroyer) {
			double existingRepairerScore = this.repairerScoreMap.get(repairer);
			double existingDestroyerScore = this.destroyerScoreMap.get(destroyer);
			this.repairerScoreMap.put(repairer,
					repairerScores.scoreIncrementWhenNeighborBetterThanCurrent + existingRepairerScore);
			this.destroyerScoreMap.put(destroyer,
					destroyerScores.scoreIncrementWhenNeighborBetterThanCurrent + existingDestroyerScore);
		}

		/**
		 * Updates repairer and destroyer scores when the neighbor agent is acceptable
		 * although it is not better than best or current agent.
		 * 
		 * @param repairer  selected repairer
		 * @param destroyer selected destroyer
		 */
		private void updateScoresWhenNeighborAcceptable(Repairer<T, A> repairer, Destroyer<A, T> destroyer) {
			double existingRepairerScore = this.repairerScoreMap.get(repairer);
			double existingDestroyerScore = this.destroyerScoreMap.get(destroyer);
			this.repairerScoreMap.put(repairer,
					repairerScores.scoreIncrementWhenNeighborAcceptable + existingRepairerScore);
			this.destroyerScoreMap.put(destroyer,
					destroyerScores.scoreIncrementWhenNeighborAcceptable + existingDestroyerScore);
		}

	}

	/**
	 * An encapsulation of all types of scores for repairers and destroyers.
	 */
	public static class Scores {

		/**
		 * Initial score.
		 */
		private double initialScore;

		/**
		 * Score increment when the neighbor is better than best agent.
		 */
		private double scoreIncrementWhenNeighborBetterThanBest;

		/**
		 * Score increment when the neighbor is better than current agent.
		 */
		private double scoreIncrementWhenNeighborBetterThanCurrent;

		/**
		 * Score increment when the neighbor is not better than either best or current
		 * agent but is acceptable according to acceptance criteria.
		 */
		private double scoreIncrementWhenNeighborAcceptable;

		/**
		 * Initializes the instance with provided values.
		 * 
		 * @param initialScore                                initial score
		 * @param scoreIncrementWhenNeighborBetterThanBest    score when neighbor better
		 *                                                    than best agent
		 * @param scoreIncrementWhenNeighborBetterThanCurrent score when neighbor better
		 *                                                    than current agent
		 * @param scoreIncrementWhenNeighborAcceptable        score when neighbor not
		 *                                                    better than either current
		 *                                                    or best agent but is still
		 *                                                    acceptable according to
		 *                                                    acceptance criteria
		 */
		public Scores(double initialScore, double scoreIncrementWhenNeighborBetterThanBest,
				double scoreIncrementWhenNeighborBetterThanCurrent, double scoreIncrementWhenNeighborAcceptable) {
			this.initialScore = initialScore;
			this.scoreIncrementWhenNeighborBetterThanBest = scoreIncrementWhenNeighborBetterThanBest;
			this.scoreIncrementWhenNeighborBetterThanCurrent = scoreIncrementWhenNeighborBetterThanCurrent;
			this.scoreIncrementWhenNeighborAcceptable = scoreIncrementWhenNeighborAcceptable;
		}
	}

}
