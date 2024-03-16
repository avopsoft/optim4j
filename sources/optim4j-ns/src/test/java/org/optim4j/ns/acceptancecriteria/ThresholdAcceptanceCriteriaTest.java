package org.optim4j.ns.acceptancecriteria;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.optim4j.ns.Agent;

class ThresholdAcceptanceCriteriaTest {

	@Test
	@DisplayName("Create the acceptance criteria with valid inputs.")
	void testThresholdAcceptanceCriteriaWithValidInputs() {
		Assertions.assertDoesNotThrow(new Executable() {
			@Override
			public void execute() throws Throwable {
				new ThresholdAcceptanceCriteria(10, .9);
			}
		});
	}

	@Test
	@DisplayName("Create the acceptance criteria with invalid threshold.")
	void testThresholdAcceptanceCriteriaWithInvalidThreshold() {
		Assertions.assertThrows(IllegalArgumentException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				new ThresholdAcceptanceCriteria(0, .9);
			}
		});
	}

	@Test
	@DisplayName("Create the acceptance criteria with invalid reduction factor.")
	void testThresholdAcceptanceCriteriaWithInvalidReductionFactor() {
		Assertions.assertThrows(IllegalArgumentException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				new ThresholdAcceptanceCriteria(10, 1);
			}
		});
	}

	@Test
	@DisplayName("Test when neighbor is not better than current agent by specified threshold.")
	void testNotAcceptable() {
		ThresholdAcceptanceCriteria thresholdAcceptanceCriteria = new ThresholdAcceptanceCriteria(10, .9);
		Agent current = new Agent() {

			@Override
			public int compareTo(Agent o) {
				return (int) (this.evaluate() - o.evaluate());
			}

			@Override
			public double evaluate() {
				return 20;
			}
		};
		Agent neighbor = new Agent() {

			@Override
			public int compareTo(Agent o) {
				return (int) (this.evaluate() - o.evaluate());
			}

			@Override
			public double evaluate() {
				return 10;
			}
		};
		Assertions.assertTrue(thresholdAcceptanceCriteria.isAcceptable(current, neighbor));
	}

	@Test
	@DisplayName("Test when neighbor is better than current agent.")
	void testAcceptable() {
		ThresholdAcceptanceCriteria thresholdAcceptanceCriteria = new ThresholdAcceptanceCriteria(10, .9);
		Agent current = new Agent() {

			@Override
			public int compareTo(Agent o) {
				return (int) (this.evaluate() - o.evaluate());
			}

			@Override
			public double evaluate() {
				return 20;
			}
		};
		Agent neighbor = new Agent() {

			@Override
			public int compareTo(Agent o) {
				return (int) (this.evaluate() - o.evaluate());
			}

			@Override
			public double evaluate() {
				return 5;
			}
		};
		Assertions.assertFalse(thresholdAcceptanceCriteria.isAcceptable(current, neighbor));
	}

}
