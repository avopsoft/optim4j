package org.optim4j.ns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of adaptive large neighborhood search algorithm.
 * 
 * <p>
 * This implementation accepts a solution agent which is a local optima of the
 * problem domain. The optimization process transforms the same using destroy
 * and repair heuristics over multiple iterations. The new solution in each
 * iteration is accepted based on provided acceptance criteria. The process
 * continues until the completion condition is satisfied.
 * </p>
 * 
 * @author Avijit Basak
 *
 * @param <A> an input solution agent type
 * @param <T> a partially destroyed solution agent type
 */
public class AdaptiveLargeNeighborhoodSearchOptimizer<A extends Agent, T> implements Optimizer<A> {

	/**
	 * Completion condition of the optimization process.
	 */
	private final CompletionCondition completionCondition;

	/**
	 * Acceptance criteria for the new neighbor solution agent.
	 */
	private final AcceptanceCriteria acceptanceCriteria;

	/**
	 * Observer of the optimization process.
	 */
	private final Observer<A, T> observer;

	/**
	 * A score manager for destroyers and repairers.
	 */
	private final RepairerDestroyerManager<T, A> repairerDestroyerManager;

	/**
	 * The period by which the probabilities need to be updated for repairers and
	 * destroyers.
	 */
	private final int updatePeriod;

	/**
	 * Default period by which the probabilities need to be updated for repairers
	 * and destroyers.
	 */
	private static final int DEFAULT_UPDATE_PERIOD = 10;

	/**
	 * The period by which the scores of repairer and destroyer heuristics need to
	 * be reset.
	 */
	private final int scoreResetPeriod;

	/**
	 * Default period by which the scores of repairer and destroyer heuristics need
	 * to be reset.
	 */
	private static final int DEFAULT_SCORE_RESET_PERIOD = 50;

	/**
	 * Instance of logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AdaptiveLargeNeighborhoodSearchOptimizer.class);

	/**
	 * Constructs an instance of adaptive large neighborhood search optimizer.
	 * 
	 * @param acceptanceCriteria  acceptance criteria for newly generated solution
	 *                            agent
	 * @param completionCondition optimization completion condition
	 * @param repairers           list of repairers to be used for this optimization
	 * @param destroyers          list of destroyers to be used for this
	 *                            optimization
	 * @param repairerScores      scores for repairers
	 * @param destroyerScores     scores for destroyers
	 * @param updatePeriod        period by which the probabilities need to be
	 *                            updated for repairers and destroyers
	 * @param scoreResetPeriod    period by which the scores of repairer and
	 *                            destroyer heuristics need to be reset
	 * @param observer            observer to get notifications of optimization
	 *                            process
	 * 
	 * @throws NullPointerException     if acceptance criteria or completion
	 *                                  condition or repairer scores or destroyer
	 *                                  scores is null
	 * @throws IllegalArgumentException if list of repairers or destroyers is null
	 *                                  or empty
	 */
	public AdaptiveLargeNeighborhoodSearchOptimizer(AcceptanceCriteria acceptanceCriteria,
			CompletionCondition completionCondition, List<Repairer<T, A>> repairers, List<Destroyer<A, T>> destroyers,
			Scores repairerScores, Scores destroyerScores, int updatePeriod, int scoreResetPeriod,
			Observer<A, T> observer) {
		/*
		 * Validate input arguments.
		 */
		Objects.requireNonNull(acceptanceCriteria, "Acceptance criteria cannot be Null");
		Objects.requireNonNull(completionCondition, "Completion condition cannot be Null");
		Objects.requireNonNull(repairerScores, "repairer scores cannot be Null");
		Objects.requireNonNull(destroyerScores, "destroyer scores cannot be Null");
		if (repairers == null || repairers.isEmpty()) {
			throw new IllegalArgumentException("Need to provide at least one repairer.");
		}
		if (destroyers == null || destroyers.isEmpty()) {
			throw new IllegalArgumentException("Need to provide at least one destroyer.");
		}
		if (updatePeriod < DEFAULT_UPDATE_PERIOD) {
			throw new IllegalArgumentException("Update period cannot be lesser than " + DEFAULT_UPDATE_PERIOD);
		}

		this.acceptanceCriteria = acceptanceCriteria;
		this.completionCondition = completionCondition;
		this.repairerDestroyerManager = new DefaultRepairerDestroyerManager(repairers, destroyers, repairerScores,
				destroyerScores);
		this.updatePeriod = updatePeriod;
		this.scoreResetPeriod = scoreResetPeriod;
		this.observer = observer;
	}

	/**
	 * Constructs an instance of adaptive large neighborhood search optimizer.
	 * 
	 * @param acceptanceCriteria  acceptance criteria for newly generated solution
	 *                            agent
	 * @param completionCondition optimization completion condition
	 * @param repairers           list of repairers to be used for this optimization
	 * @param destroyers          list of destroyers to be used for this
	 *                            optimization
	 * @param observer            observer to get notifications of optimization
	 *                            process
	 * 
	 * @throws NullPointerException     if acceptance criteria or completion
	 *                                  condition is null
	 * @throws IllegalArgumentException if list of repairers or destroyers is null
	 *                                  or empty
	 */
	public AdaptiveLargeNeighborhoodSearchOptimizer(AcceptanceCriteria acceptanceCriteria,
			CompletionCondition completionCondition, List<Repairer<T, A>> repairers, List<Destroyer<A, T>> destroyers,
			Observer<A, T> observer) {
		/*
		 * Validate input arguments.
		 */
		Objects.requireNonNull(acceptanceCriteria, "Acceptance criteria cannot be Null");
		Objects.requireNonNull(completionCondition, "Completion condition cannot be Null");
		if (repairers == null || repairers.isEmpty()) {
			throw new IllegalArgumentException("Need to provide at least one repairer.");
		}
		if (destroyers == null || destroyers.isEmpty()) {
			throw new IllegalArgumentException("Need to provide at least one destroyer.");
		}

		this.acceptanceCriteria = acceptanceCriteria;
		this.completionCondition = completionCondition;
		this.repairerDestroyerManager = new DefaultRepairerDestroyerManager(repairers, destroyers);
		this.updatePeriod = DEFAULT_UPDATE_PERIOD;
		this.scoreResetPeriod = DEFAULT_SCORE_RESET_PERIOD;
		this.observer = observer;
	}

	/**
	 * Constructs an instance of adaptive large neighborhood search optimizer.
	 * 
	 * @param acceptanceCriteria       acceptance criteria for newly generated
	 *                                 solution agent
	 * @param completionCondition      optimization completion condition
	 * @param repairerDestroyerManager instance of repairer destroyer manager
	 * @param updatePeriod             period by which the probabilities need to be
	 *                                 updated for repairers and destroyers
	 * @param scoreResetPeriod         period by which the scores of repairer and
	 *                                 destroyer heuristics need to be reset
	 * @param observer                 observer to get notifications of optimization
	 *                                 process
	 * 
	 * @throws NullPointerException     if acceptance criteria, completion condition
	 *                                  or repairer destroyer manager is null
	 * @throws IllegalArgumentException if list of repairers or destroyers is null
	 *                                  or empty
	 */
	public AdaptiveLargeNeighborhoodSearchOptimizer(AcceptanceCriteria acceptanceCriteria,
			CompletionCondition completionCondition, RepairerDestroyerManager<T, A> repairerDestroyerManager,
			int updatePeriod, int scoreResetPeriod, Observer<A, T> observer) {
		/*
		 * Validate input arguments.
		 */
		Objects.requireNonNull(acceptanceCriteria, "Acceptance criteria cannot be Null");
		Objects.requireNonNull(completionCondition, "Completion condition cannot be Null");
		Objects.requireNonNull(repairerDestroyerManager, "RepairerDestroyerManager cannot be null");
		if (updatePeriod < DEFAULT_UPDATE_PERIOD) {
			throw new IllegalArgumentException("Update period cannot be lesser than " + DEFAULT_UPDATE_PERIOD);
		}

		this.acceptanceCriteria = acceptanceCriteria;
		this.completionCondition = completionCondition;
		this.repairerDestroyerManager = repairerDestroyerManager;
		this.updatePeriod = updatePeriod;
		this.scoreResetPeriod = scoreResetPeriod;
		this.observer = observer;
	}

	/**
	 * Executes the optimization process. Accepts a solution agent as input
	 * optimizes the same using multiple destroy and repair heuristics based on
	 * their performance score following ALNS methodology. The new solution agent at
	 * each iteration is accepted based on result of acceptance criteria evaluation.
	 * An optimum solution is returned once the completion condition is satisfied.
	 * 
	 * @param agent a valid solution agent representing a local optima
	 * 
	 * @return the optimized solution agent
	 */
	public A optimize(A agent) {

		LOGGER.info("Input Solution Agent: {}", agent);

		int generation = 0;
		A bestAgent = agent;

		/*
		 * Check if the completion condition is satisfied. If not continue to generate a
		 * new neighbor following the ALNS algorithm.
		 */
		while (!completionCondition.isComplete(agent)) {
			LOGGER.debug("Generation: {}", generation);

			/*
			 * If generation is divisible by score reset period then reset the repairer and
			 * destroyer scores.
			 */
			if (generation % scoreResetPeriod == 0) {
				LOGGER.info("Reset repairer and destroyer heuristic scores.");
				repairerDestroyerManager.resetScores();
			} else if (generation % updatePeriod == 0) {
				/*
				 * If generation is divisible by update period then update score boundaries.
				 */
				LOGGER.info("Update score boundaries");
				repairerDestroyerManager.updateScoreBoundaries();
			}

			/*
			 * Find repairer and destroyer.
			 */
			final Repairer<T, A> repairer = repairerDestroyerManager.getRepairer();
			LOGGER.debug("Selected repairer: {}", repairer);
			final Destroyer<A, T> destroyer = repairerDestroyerManager.getDestroyer();
			LOGGER.debug("Selected destroyer: {}", destroyer);

			/*
			 * Find the neighbor solution.
			 */
			LOGGER.trace("Find the neighbor solution.");
			final A neighbour = repairer.repair(destroyer.destroy(agent));
			LOGGER.debug("New Neighbor: {}", neighbour);

			/*
			 * Update the repairer and destroyer scores and replace the new current agent
			 * and best agent by new neighbor based on it's score and acceptability.
			 */
			LOGGER.debug("Update repairer and destroyer scores.");
			if (neighbour.compareTo(bestAgent) >= 0) {
				LOGGER.debug(
						"Neighbor Agent better than last best Agent. Replace current and best Agent by neighbor Agent.");
				LOGGER.info("New Best Agent: {}", agent);
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
			} else {
				LOGGER.debug("Neighbor Agent is not acceptable.");
				repairerDestroyerManager.updateScoresWhenNeighborNotAcceptable(repairer, destroyer);
			}

			/*
			 * Notify the observer about the the best agent, current agent, acceptance
			 * criteria, selected repairer and selected destroyer.
			 */
			if (observer != null) {
				observer.notify(bestAgent, agent, acceptanceCriteria, repairer, destroyer);
			}
			generation++;
		}
		LOGGER.info("Optimum Solution Agent: {}", bestAgent);
		return bestAgent;
	}

	/**
	 * Repairer destroyer manager for adaptive large neighborhood search.
	 * 
	 * @author Avijit Basak
	 *
	 * @param <T> Partially destroyed agent type
	 * @param <A> Agent type
	 */
	public interface RepairerDestroyerManager<T, A extends Agent> {

		/**
		 * Reset scores of repairers and destroyers.
		 */
		void resetScores();

		/**
		 * Updates score boundaries for repairers and destroyers.
		 */
		void updateScoreBoundaries();

		/**
		 * Updates repairer and destroyer scores when the neighbor agent is better than
		 * best agent.
		 * 
		 * @param repairer  selected repairer
		 * @param destroyer selected destroyer
		 */
		void updateScoresWhenNeighborBetterThanBest(Repairer<T, A> repairer, Destroyer<A, T> destroyer);

		/**
		 * Updates repairer and destroyer scores when the neighbor agent is better than
		 * current agent.
		 * 
		 * @param repairer  selected repairer
		 * @param destroyer selected destroyer
		 */
		void updateScoresWhenNeighborBetterThanCurrent(Repairer<T, A> repairer, Destroyer<A, T> destroyer);

		/**
		 * Updates repairer and destroyer scores when the neighbor agent is acceptable
		 * although it is not better than best or current agent.
		 * 
		 * @param repairer  selected repairer
		 * @param destroyer selected destroyer
		 */
		void updateScoresWhenNeighborAcceptable(Repairer<T, A> repairer, Destroyer<A, T> destroyer);

		/**
		 * Updates repairer and destroyer scores when the neighbor agent is not
		 * acceptable.
		 * 
		 * @param repairer  selected repairer
		 * @param destroyer selected destroyer
		 */
		void updateScoresWhenNeighborNotAcceptable(Repairer<T, A> repairer, Destroyer<A, T> destroyer);

		/**
		 * Selects a repairer.
		 * 
		 * @return the selected repairer
		 * 
		 * @throws IllegalArgumentException if list of repairers is empty
		 */
		Repairer<T, A> getRepairer();

		/**
		 * Selects a destroyer.
		 * 
		 * @return the selected destroyer
		 * 
		 * @throws IllegalArgumentException if list of destroyers is empty
		 */
		Destroyer<A, T> getDestroyer();

	}

	/**
	 * Manager of repairers and destroyers and their score.
	 */
	private class DefaultRepairerDestroyerManager implements RepairerDestroyerManager<T, A> {

		/**
		 * Map of repairer scores.
		 */
		private final Map<Repairer<T, A>, Double> repairerScoreMap = new HashMap<>();

		/**
		 * Map of destroyer scores.
		 */
		private final Map<Destroyer<A, T>, Double> destroyerScoreMap = new HashMap<>();

		/**
		 * Map of score boundary and repairers.
		 */
		private final Map<ScoreBoundary, Repairer<T, A>> repairerScoreBoundaries = new HashMap<>();

		/**
		 * Map of score boundary and destroyers.
		 */
		private final Map<ScoreBoundary, Destroyer<A, T>> destroyerScoreBoundaries = new HashMap<>();

		/**
		 * Repairer scores.
		 */
		private final Scores repairerScores;

		/**
		 * Destroyer scores.
		 */
		private final Scores destroyerScores;

		/**
		 * Default initial score of repairers and destroyers.
		 */
		private static final double DEFAULT_INITIAL_SCORE = 25;

		/**
		 * Default score increment when neighbor is better than best agent.
		 */
		private static final double DEFAULT_SCORE_INCREMENT_WHEN_NEIGHBOR_BETTER_THAN_BEST = 100;

		/**
		 * Default score increment when neighbor is better than current agent.
		 */
		private static final double DEFAULT_SCORE_INCREMENT_WHEN_NEIGHBOR_BETTER_THAN_CURRENT = 75;

		/**
		 * Default score increment when neighbor is not better than current or best
		 * agent but still acceptable according to acceptance criteria.
		 */
		private static final double DEFAULT_SCORE_INCREMENT_WHEN_NEIGHBOR_ACCEPTABLE = 50;

		/**
		 * Default score decrement when neighbor is not acceptable according to
		 * acceptance criteria.
		 */
		private static final double DEFAULT_SCORE_DECREMENT_WHEN_NEIGHBOR_NOT_ACCEPTABLE = 0;

		/**
		 * Initializes repairers and destroyers with user provided scores.
		 * 
		 * @param repairers       list of repairers
		 * @param destroyers      list of destroyers
		 * @param repairerScores  scores of repairers
		 * @param destroyerScores scores of destroyers
		 */
		private DefaultRepairerDestroyerManager(List<Repairer<T, A>> repairers, List<Destroyer<A, T>> destroyers,
				Scores repairerScores, Scores destroyerScores) {
			this.repairerScores = repairerScores;
			this.destroyerScores = destroyerScores;
			repairers.stream().forEach(repairer -> repairerScoreMap.put(repairer, repairerScores.initialScore));
			destroyers.stream().forEach(destroyer -> destroyerScoreMap.put(destroyer, destroyerScores.initialScore));
		}

		/**
		 * Initializes repairers and destroyers with default scores.
		 * 
		 * @param repairers  list of repairers
		 * @param destroyers list of destroyers
		 */
		private DefaultRepairerDestroyerManager(List<Repairer<T, A>> repairers, List<Destroyer<A, T>> destroyers) {
			this.repairerScores = new Scores(DEFAULT_INITIAL_SCORE,
					DEFAULT_SCORE_INCREMENT_WHEN_NEIGHBOR_BETTER_THAN_BEST,
					DEFAULT_SCORE_INCREMENT_WHEN_NEIGHBOR_BETTER_THAN_CURRENT,
					DEFAULT_SCORE_INCREMENT_WHEN_NEIGHBOR_ACCEPTABLE,
					DEFAULT_SCORE_DECREMENT_WHEN_NEIGHBOR_NOT_ACCEPTABLE);
			this.destroyerScores = new Scores(DEFAULT_INITIAL_SCORE,
					DEFAULT_SCORE_INCREMENT_WHEN_NEIGHBOR_BETTER_THAN_BEST,
					DEFAULT_SCORE_INCREMENT_WHEN_NEIGHBOR_BETTER_THAN_CURRENT,
					DEFAULT_SCORE_INCREMENT_WHEN_NEIGHBOR_ACCEPTABLE,
					DEFAULT_SCORE_DECREMENT_WHEN_NEIGHBOR_NOT_ACCEPTABLE);
			repairers.stream().forEach(repairer -> repairerScoreMap.put(repairer, repairerScores.initialScore));
			destroyers.stream().forEach(destroyer -> destroyerScoreMap.put(destroyer, destroyerScores.initialScore));
		}

		/**
		 * Reset scores of repairers and destroyers.
		 */
		public void resetScores() {
			LOGGER.debug("reset scores of repairers and destroyers.");
			for (Repairer<T, A> repairer : repairerScoreMap.keySet()) {
				repairerScoreMap.put(repairer, this.repairerScores.initialScore);
			}
			for (Destroyer<A, T> destroyer : destroyerScoreMap.keySet()) {
				destroyerScoreMap.put(destroyer, this.destroyerScores.initialScore);
			}
			updateScoreBoundaries();
		}

		/**
		 * Updates score boundaries for repairers and destroyers.
		 */
		public void updateScoreBoundaries() {
			final Set<Repairer<T, A>> repairers = repairerScoreMap.keySet();
			final Set<Destroyer<A, T>> destroyers = destroyerScoreMap.keySet();

			LOGGER.trace("Update score boundaries of repairers.");
			Double minScore = Double.valueOf(0);
			Double maxScore = Double.valueOf(0);
			for (Repairer<T, A> repairer : repairers) {
				maxScore = minScore + repairerScoreMap.get(repairer);
				ScoreBoundary scoreBoundary = new ScoreBoundary(minScore, maxScore);
				repairerScoreBoundaries.put(scoreBoundary, repairer);
				minScore = maxScore;
			}

			LOGGER.trace("Update score boundaries of destroyers.");
			minScore = Double.valueOf(0);
			maxScore = Double.valueOf(0);
			for (Destroyer<A, T> destroyer : destroyers) {
				maxScore = minScore + destroyerScoreMap.get(destroyer);
				ScoreBoundary scoreBoundary = new ScoreBoundary(minScore, maxScore);
				destroyerScoreBoundaries.put(scoreBoundary, destroyer);
				minScore = maxScore;
			}
		}

		/**
		 * Selects a repairer using roulette wheel selection based on their score.
		 * 
		 * @return the selected repairer
		 * 
		 * @throws IllegalArgumentException if list of repairers is empty
		 */
		public Repairer<T, A> getRepairer() {
			LOGGER.trace("Calculate total score for repairers.");
			final Optional<Double> totalScore = repairerScoreBoundaries.keySet().stream()
					.map(scoreBoundary -> scoreBoundary.maxScore).reduce((Double t, Double u) -> t > u ? t : u);
			LOGGER.trace("Generate random score.");
			if (totalScore.isEmpty()) {
				throw new IllegalArgumentException("No repairer found.");
			}
			final double randomScore = Math.random() * totalScore.get();
			LOGGER.debug("Generated random score: {}", randomScore);
			for (Entry<ScoreBoundary, Repairer<T, A>> entry : repairerScoreBoundaries.entrySet()) {
				if (entry.getKey().isBetween(randomScore)) {
					return entry.getValue();
				}
			}
			throw new IllegalArgumentException("No repairer found.");
		}

		/**
		 * Selects a destroyer using roulette wheel selection based on their score.
		 * 
		 * @return the selected destroyer
		 * 
		 * @throws IllegalArgumentException if list of destroyers is empty
		 */
		public Destroyer<A, T> getDestroyer() {
			LOGGER.trace("Calculate total score for destroyers.");
			final Optional<Double> totalScore = destroyerScoreBoundaries.keySet().stream()
					.map(scoreBoundary -> scoreBoundary.maxScore).reduce((Double t, Double u) -> t > u ? t : u);
			LOGGER.trace("Generate random score.");
			if (totalScore.isEmpty()) {
				throw new IllegalArgumentException("No destroyer found.");
			}
			final double randomScore = Math.random() * totalScore.get();
			LOGGER.debug("Generated random score: {}", randomScore);
			for (Entry<ScoreBoundary, Destroyer<A, T>> entry : destroyerScoreBoundaries.entrySet()) {
				if (entry.getKey().isBetween(randomScore)) {
					return entry.getValue();
				}
			}
			throw new IllegalArgumentException("No destroyer found.");
		}

		/**
		 * Updates repairer and destroyer scores when the neighbor agent is better than
		 * best agent.
		 * 
		 * @param repairer  selected repairer
		 * @param destroyer selected destroyer
		 */
		public void updateScoresWhenNeighborBetterThanBest(Repairer<T, A> repairer, Destroyer<A, T> destroyer) {
			this.repairerScoreMap.put(repairer,
					repairerScores.scoreIncrementWhenNeighborBetterThanBest + this.repairerScoreMap.get(repairer));
			this.destroyerScoreMap.put(destroyer,
					destroyerScores.scoreIncrementWhenNeighborBetterThanBest + this.destroyerScoreMap.get(destroyer));
		}

		/**
		 * Updates repairer and destroyer scores when the neighbor agent is better than
		 * current agent.
		 * 
		 * @param repairer  selected repairer
		 * @param destroyer selected destroyer
		 */
		public void updateScoresWhenNeighborBetterThanCurrent(Repairer<T, A> repairer, Destroyer<A, T> destroyer) {
			this.repairerScoreMap.put(repairer,
					repairerScores.scoreIncrementWhenNeighborBetterThanCurrent + this.repairerScoreMap.get(repairer));
			this.destroyerScoreMap.put(destroyer, destroyerScores.scoreIncrementWhenNeighborBetterThanCurrent
					+ this.destroyerScoreMap.get(destroyer));
		}

		/**
		 * Updates repairer and destroyer scores when the neighbor agent is acceptable
		 * although it is not better than best or current agent.
		 * 
		 * @param repairer  selected repairer
		 * @param destroyer selected destroyer
		 */
		public void updateScoresWhenNeighborAcceptable(Repairer<T, A> repairer, Destroyer<A, T> destroyer) {
			this.repairerScoreMap.put(repairer,
					repairerScores.scoreIncrementWhenNeighborAcceptable + this.repairerScoreMap.get(repairer));
			this.destroyerScoreMap.put(destroyer,
					destroyerScores.scoreIncrementWhenNeighborAcceptable + this.destroyerScoreMap.get(destroyer));
		}

		/**
		 * Updates repairer and destroyer scores when the neighbor agent is not
		 * acceptable.
		 * 
		 * @param repairer  selected repairer
		 * @param destroyer selected destroyer
		 */
		public void updateScoresWhenNeighborNotAcceptable(Repairer<T, A> repairer, Destroyer<A, T> destroyer) {
			this.repairerScoreMap.put(repairer,
					this.repairerScoreMap.get(repairer) - repairerScores.scoreDecrementWhenNeighborNotAcceptable);
			this.destroyerScoreMap.put(destroyer, Math.max(DEFAULT_INITIAL_SCORE,
					this.destroyerScoreMap.get(destroyer) - destroyerScores.scoreDecrementWhenNeighborNotAcceptable));
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

			/**
			 * Constructs score boundary based on provided min and max score.
			 * 
			 * @param minScore minimum score
			 * @param maxScore maximum score
			 */
			private ScoreBoundary(double minScore, double maxScore) {
				this.minScore = minScore;
				this.maxScore = maxScore;
			}

			/**
			 * Checks if the provided score is in between min and max scores.
			 * 
			 * @param score input score
			 * @return true/false
			 */
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

			/**
			 * Compares two score boundaries and returns true if both are equal.
			 */
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

			/**
			 * Provides the enclosing instance.
			 * 
			 * @return the instance of encapsulating repairer destroyer manager.
			 */
			private DefaultRepairerDestroyerManager getEnclosingInstance() {
				return DefaultRepairerDestroyerManager.this;
			}

			@Override
			public String toString() {
				return "ScoreBoundary [minScore=" + minScore + ", maxScore=" + maxScore + "]";
			}

		}

	}

	/**
	 * An encapsulation of all types of scores for repairers and destroyers.
	 */
	public static class Scores {

		/**
		 * Initial score.
		 */
		private final double initialScore;

		/**
		 * Score increment when the neighbor is better than best agent.
		 */
		private final double scoreIncrementWhenNeighborBetterThanBest;

		/**
		 * Score increment when the neighbor is better than current agent.
		 */
		private final double scoreIncrementWhenNeighborBetterThanCurrent;

		/**
		 * Score increment when the neighbor is not better than either best or current
		 * agent but is acceptable according to acceptance criteria.
		 */
		private final double scoreIncrementWhenNeighborAcceptable;

		/**
		 * Score decrement when the neighbor is not acceptable according to acceptance
		 * criteria.
		 */
		private final double scoreDecrementWhenNeighborNotAcceptable;

		/**
		 * Initializes the instance with provided values.
		 * 
		 * @param initialScore                                initial score
		 * @param scoreIncrementWhenNeighborBetterThanBest    score when neighbor is
		 *                                                    better than best agent
		 * @param scoreIncrementWhenNeighborBetterThanCurrent score when neighbor is
		 *                                                    better than current agent
		 * @param scoreIncrementWhenNeighborAcceptable        score when neighbor is not
		 *                                                    better than either current
		 *                                                    or best agent but is still
		 *                                                    acceptable according to
		 *                                                    acceptance criteria
		 * @param scoreDecrementWhenNeighborNotAcceptable     score when neighbor is not
		 *                                                    acceptable according to
		 *                                                    acceptance criteria
		 */
		public Scores(double initialScore, double scoreIncrementWhenNeighborBetterThanBest,
				double scoreIncrementWhenNeighborBetterThanCurrent, double scoreIncrementWhenNeighborAcceptable,
				double scoreDecrementWhenNeighborNotAcceptable) {
			this.initialScore = initialScore;
			this.scoreIncrementWhenNeighborBetterThanBest = scoreIncrementWhenNeighborBetterThanBest;
			this.scoreIncrementWhenNeighborBetterThanCurrent = scoreIncrementWhenNeighborBetterThanCurrent;
			this.scoreIncrementWhenNeighborAcceptable = scoreIncrementWhenNeighborAcceptable;
			this.scoreDecrementWhenNeighborNotAcceptable = scoreDecrementWhenNeighborNotAcceptable;
		}
	}

}
