package org.optim4j.ns.acceptancecriteria;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.optim4j.ns.Agent;

class OldBachelorAcceptanceCriteriaTest {

	@Test
	@DisplayName("Create the acceptance criteria with valid inputs.")
	void testOldBachelorAcceptanceCriteriaWithValidInputs() {
		Assertions.assertDoesNotThrow(new Executable() {
			@Override
			public void execute() throws Throwable {
				new OldBachelorAcceptanceCriteria(10, .9, 1.1);
			}
		});
	}

	@Test
	@DisplayName("Create the acceptance criteria with invalid threshold.")
	void testOldBachelorAcceptanceCriteriaWithInvalidThreshold() {
		Assertions.assertThrows(IllegalArgumentException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				new OldBachelorAcceptanceCriteria(0, .9, 1.1);
			}
		});
	}

	@Test
	@DisplayName("Create the acceptance criteria with invalid reduction factor.")
	void testOldBachelorAcceptanceCriteriaWithInvalidReductionFactor() {
		Assertions.assertThrows(IllegalArgumentException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				new OldBachelorAcceptanceCriteria(10, 1.1, 1.1);
			}
		});
	}

	@Test
	@DisplayName("Create the acceptance criteria with invalid increment factor.")
	void testOldBachelorAcceptanceCriteriaWithInvalidIncrementFactor() {
		Assertions.assertThrows(IllegalArgumentException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				new OldBachelorAcceptanceCriteria(10, .9, .9);
			}
		});
	}

	@Test
	@DisplayName("Test when neighbor is not better than current agent by specified threshold.")
	void testNotAcceptable() {
		OldBachelorAcceptanceCriteria thresholdAcceptanceCriteria = new OldBachelorAcceptanceCriteria(10, .9, 1.1);
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
		OldBachelorAcceptanceCriteria thresholdAcceptanceCriteria = new OldBachelorAcceptanceCriteria(10, .9, 1.1);
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
