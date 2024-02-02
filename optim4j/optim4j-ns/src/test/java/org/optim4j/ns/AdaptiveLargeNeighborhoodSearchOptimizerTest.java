package org.optim4j.ns;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AdaptiveLargeNeighborhoodSearchOptimizerTest {

	private class Solution implements Agent {

		@Override
		public int compareTo(Agent o) {
			return (int) (this.evaluate() - o.evaluate());
		}

		@Override
		public double evaluate() {
			return Math.random();
		}

	}

	private class PartiallyDestroyedSolution {

	}

	@Test
	@DisplayName("Test constructor with all valid parameters.")
	void testAdaptiveLargeNeighborhoodSearchOptimizerConstructorWithValidParameters() {
		List<Repairer<PartiallyDestroyedSolution, Solution>> repairers = new ArrayList<>();
		repairers.add(
				new Repairer<AdaptiveLargeNeighborhoodSearchOptimizerTest.PartiallyDestroyedSolution, AdaptiveLargeNeighborhoodSearchOptimizerTest.Solution>() {

					@Override
					public Solution repair(PartiallyDestroyedSolution t) {
						return new Solution();
					}
				});
		List<Destroyer<Solution, PartiallyDestroyedSolution>> destroyers = new ArrayList<>();
		destroyers.add(
				new Destroyer<AdaptiveLargeNeighborhoodSearchOptimizerTest.Solution, AdaptiveLargeNeighborhoodSearchOptimizerTest.PartiallyDestroyedSolution>() {

					@Override
					public PartiallyDestroyedSolution destroy(Solution agent) {
						return new PartiallyDestroyedSolution();
					}
				});

		Assertions.assertDoesNotThrow(new Executable() {

			@Override
			public void execute() throws Throwable {
				new AdaptiveLargeNeighborhoodSearchOptimizer<>((current, neighbour) -> true, agent -> true, repairers,
						destroyers,
						(bestAgent, currentAgent, acceptanceCriteria, selectedRepairer, selectedDestroyer) -> {
						});
			}
		});
	}

	@Test
	@DisplayName("Test constructor with all invalid acceptance criteria.")
	void testAdaptiveLargeNeighborhoodSearchOptimizerInvalidAcceptanceCriteria() {
		List<Repairer<PartiallyDestroyedSolution, Solution>> repairers = new ArrayList<>();
		repairers.add(
				new Repairer<AdaptiveLargeNeighborhoodSearchOptimizerTest.PartiallyDestroyedSolution, AdaptiveLargeNeighborhoodSearchOptimizerTest.Solution>() {

					@Override
					public Solution repair(PartiallyDestroyedSolution t) {
						return new Solution();
					}
				});
		List<Destroyer<Solution, PartiallyDestroyedSolution>> destroyers = new ArrayList<>();
		destroyers.add(
				new Destroyer<AdaptiveLargeNeighborhoodSearchOptimizerTest.Solution, AdaptiveLargeNeighborhoodSearchOptimizerTest.PartiallyDestroyedSolution>() {

					@Override
					public PartiallyDestroyedSolution destroy(Solution agent) {
						return new PartiallyDestroyedSolution();
					}
				});

		Assertions.assertThrows(NullPointerException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				new AdaptiveLargeNeighborhoodSearchOptimizer<>(null, agent -> true, repairers, destroyers,
						(bestAgent, currentAgent, acceptanceCriteria, selectedRepairer, selectedDestroyer) -> {
						});
			}
		});
	}

	@Test
	@DisplayName("Test constructor with all invalid completion condition.")
	void testAdaptiveLargeNeighborhoodSearchOptimizerInvalidCompletionCondition() {
		List<Repairer<PartiallyDestroyedSolution, Solution>> repairers = new ArrayList<>();
		repairers.add(
				new Repairer<AdaptiveLargeNeighborhoodSearchOptimizerTest.PartiallyDestroyedSolution, AdaptiveLargeNeighborhoodSearchOptimizerTest.Solution>() {

					@Override
					public Solution repair(PartiallyDestroyedSolution t) {
						return new Solution();
					}
				});
		List<Destroyer<Solution, PartiallyDestroyedSolution>> destroyers = new ArrayList<>();
		destroyers.add(
				new Destroyer<AdaptiveLargeNeighborhoodSearchOptimizerTest.Solution, AdaptiveLargeNeighborhoodSearchOptimizerTest.PartiallyDestroyedSolution>() {

					@Override
					public PartiallyDestroyedSolution destroy(Solution agent) {
						return new PartiallyDestroyedSolution();
					}
				});

		Assertions.assertThrows(NullPointerException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				new AdaptiveLargeNeighborhoodSearchOptimizer<>((current, neighbor) -> true, null, repairers, destroyers,
						(bestAgent, currentAgent, acceptanceCriteria, selectedRepairer, selectedDestroyer) -> {
						});
			}
		});
	}

	@Test
	@DisplayName("Test constructor with all blank repairer list.")
	void testAdaptiveLargeNeighborhoodSearchOptimizerBlankRepairerList() {
		List<Destroyer<Solution, PartiallyDestroyedSolution>> destroyers = new ArrayList<>();
		destroyers.add(
				new Destroyer<AdaptiveLargeNeighborhoodSearchOptimizerTest.Solution, AdaptiveLargeNeighborhoodSearchOptimizerTest.PartiallyDestroyedSolution>() {

					@Override
					public PartiallyDestroyedSolution destroy(Solution agent) {
						return new PartiallyDestroyedSolution();
					}
				});

		Assertions.assertThrows(IllegalArgumentException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				new AdaptiveLargeNeighborhoodSearchOptimizer<>((current, neighbor) -> true, agent -> true,
						new ArrayList<>(), destroyers,
						(bestAgent, currentAgent, acceptanceCriteria, selectedRepairer, selectedDestroyer) -> {
						});
			}
		});
	}

	@Test
	@DisplayName("Test constructor with all blank destroyer.")
	void testAdaptiveLargeNeighborhoodSearchOptimizerConstructorWithBlankDestroyerList() {
		List<Repairer<PartiallyDestroyedSolution, Solution>> repairers = new ArrayList<>();
		repairers.add(
				new Repairer<AdaptiveLargeNeighborhoodSearchOptimizerTest.PartiallyDestroyedSolution, AdaptiveLargeNeighborhoodSearchOptimizerTest.Solution>() {

					@Override
					public Solution repair(PartiallyDestroyedSolution t) {
						return new Solution();
					}
				});
		Assertions.assertThrows(IllegalArgumentException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				new AdaptiveLargeNeighborhoodSearchOptimizer<>((current, neighbour) -> true, agent -> true, repairers,
						new ArrayList<>(),
						(bestAgent, currentAgent, acceptanceCriteria, selectedRepairer, selectedDestroyer) -> {
						});
			}
		});
	}

	@Test
	@DisplayName("Test constructor with all valid parameters.")
	void testAdaptiveLargeNeighborhoodSearchOptimizerConstructorWithNullObserver() {
		List<Repairer<PartiallyDestroyedSolution, Solution>> repairers = new ArrayList<>();
		repairers.add(
				new Repairer<AdaptiveLargeNeighborhoodSearchOptimizerTest.PartiallyDestroyedSolution, AdaptiveLargeNeighborhoodSearchOptimizerTest.Solution>() {

					@Override
					public Solution repair(PartiallyDestroyedSolution t) {
						return new Solution();
					}
				});
		List<Destroyer<Solution, PartiallyDestroyedSolution>> destroyers = new ArrayList<>();
		destroyers.add(
				new Destroyer<AdaptiveLargeNeighborhoodSearchOptimizerTest.Solution, AdaptiveLargeNeighborhoodSearchOptimizerTest.PartiallyDestroyedSolution>() {

					@Override
					public PartiallyDestroyedSolution destroy(Solution agent) {
						return new PartiallyDestroyedSolution();
					}
				});

		Assertions.assertDoesNotThrow(new Executable() {

			@Override
			public void execute() throws Throwable {
				new AdaptiveLargeNeighborhoodSearchOptimizer<>((current, neighbour) -> true, agent -> true, repairers,
						destroyers, null);
			}
		});
	}

	@Test
	void testOptimize() {
		List<Repairer<PartiallyDestroyedSolution, Solution>> repairers = new ArrayList<>();
		repairers.add(
				new Repairer<AdaptiveLargeNeighborhoodSearchOptimizerTest.PartiallyDestroyedSolution, AdaptiveLargeNeighborhoodSearchOptimizerTest.Solution>() {

					@Override
					public Solution repair(PartiallyDestroyedSolution t) {
						return new Solution();
					}
				});
		List<Destroyer<Solution, PartiallyDestroyedSolution>> destroyers = new ArrayList<>();
		destroyers.add(
				new Destroyer<AdaptiveLargeNeighborhoodSearchOptimizerTest.Solution, AdaptiveLargeNeighborhoodSearchOptimizerTest.PartiallyDestroyedSolution>() {

					@Override
					public PartiallyDestroyedSolution destroy(Solution agent) {
						return new PartiallyDestroyedSolution();
					}
				});

		AdaptiveLargeNeighborhoodSearchOptimizer<Solution, PartiallyDestroyedSolution> adaptiveLargeNeighborhoodSearchOptimizer = new AdaptiveLargeNeighborhoodSearchOptimizer<>(
				(current, neighbour) -> true, new CompletionCondition() {
					private int generation;

					@Override
					public boolean isComplete(Agent agent) {
						return generation++ == 10;
					}
				}, repairers, destroyers,
				(bestAgent, currentAgent, acceptanceCriteria, selectedRepairer, selectedDestroyer) -> {
				});
		Assertions.assertDoesNotThrow(new Executable() {

			@Override
			public void execute() throws Throwable {
				adaptiveLargeNeighborhoodSearchOptimizer.optimize(new Solution());
			}
		});
		;
	}

}
