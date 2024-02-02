package org.optim4j.ns.acceptancecriteria;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.optim4j.ns.Agent;

class SimulatedAnnealingAcceptanceCriteriaTest {

	@Test
	@DisplayName("Constructs a simulated annealing acceotance criteria with valid input criteria.")
	void testSimulatedAnnealingAcceptanceCriteriaCreationWithValidParams() {
		Assertions.assertDoesNotThrow(new Executable() {

			@Override
			public void execute() throws Throwable {
				new SimulatedAnnealingAcceptanceCriteria(100.0, .9);
			}
		});
	}

	@Test
	@DisplayName("Constructs a simulated annealing acceotance criteria with invalid temperature.")
	void testSimulatedAnnealingAcceptanceCriteriaCreationWithInvalidInitialTemperature() {
		Assertions.assertThrows(IllegalArgumentException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				new SimulatedAnnealingAcceptanceCriteria(1, .9);
			}
		});
	}

	@Test
	@DisplayName("Constructs a simulated annealing acceotance criteria with invalid decay rate.")
	void testSimulatedAnnealingAcceptanceCriteriaCreationWithInvalidDecayRate() {
		Assertions.assertThrows(IllegalArgumentException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				new SimulatedAnnealingAcceptanceCriteria(100.0, 1);
			}
		});
	}

	@Test
	@DisplayName("Tests simulated annealing acceptance criteria where neighbor agent is better than current agent.")
	void testIsAcceptableForHundredPercentProbability() {
		SimulatedAnnealingAcceptanceCriteria simulatedAnnealingAcceptanceCriteria = new SimulatedAnnealingAcceptanceCriteria(
				100.0, .9);
		Agent current = new Agent() {

			@Override
			public int compareTo(Agent o) {
				return (int) (this.evaluate() - o.evaluate());
			}

			@Override
			public double evaluate() {
				return 10;
			}
		};
		Agent neighbor = new Agent() {

			@Override
			public int compareTo(Agent o) {
				return (int) (this.evaluate() - o.evaluate());
			}

			@Override
			public double evaluate() {
				return 20;
			}
		};

		Assertions.assertTrue(simulatedAnnealingAcceptanceCriteria.isAcceptable(current, neighbor));
	}

}
