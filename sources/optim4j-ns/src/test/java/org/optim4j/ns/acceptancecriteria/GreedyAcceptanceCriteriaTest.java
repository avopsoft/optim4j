package org.optim4j.ns.acceptancecriteria;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.optim4j.ns.Agent;

class GreedyAcceptanceCriteriaTest {

	@Test
	@DisplayName("Test when neighbor is not better than current agent.")
	void testNotAcceptable() {
		GreedyAcceptanceCriteria greedyAcceptanceCriteria = new GreedyAcceptanceCriteria();
		Agent current = new Agent() {

			@Override
			public int compareTo(Agent o) {
				return this.evaluate() > o.evaluate() ? 1 : (this.evaluate() < o.evaluate() ? -1 : 0);
			}

			@Override
			public double evaluate() {
				return 10;
			}
		};
		Agent neighbor = new Agent() {

			@Override
			public int compareTo(Agent o) {
				return this.evaluate() > o.evaluate() ? 1 : (this.evaluate() < o.evaluate() ? -1 : 0);
			}

			@Override
			public double evaluate() {
				return 5;
			}
		};
		Assertions.assertFalse(greedyAcceptanceCriteria.isAcceptable(current, neighbor));
	}

	@Test
	@DisplayName("Test when neighbor is better than current agent.")
	void testAcceptable() {
		GreedyAcceptanceCriteria greedyAcceptanceCriteria = new GreedyAcceptanceCriteria();
		Agent current = new Agent() {

			@Override
			public int compareTo(Agent o) {
				return this.evaluate() > o.evaluate() ? 1 : (this.evaluate() < o.evaluate() ? -1 : 0);
			}

			@Override
			public double evaluate() {
				return 10;
			}
		};
		Agent neighbor = new Agent() {

			@Override
			public int compareTo(Agent o) {
				return this.evaluate() > o.evaluate() ? 1 : (this.evaluate() < o.evaluate() ? -1 : 0);
			}

			@Override
			public double evaluate() {
				return 15;
			}
		};
		Assertions.assertTrue(greedyAcceptanceCriteria.isAcceptable(current, neighbor));
	}

}
