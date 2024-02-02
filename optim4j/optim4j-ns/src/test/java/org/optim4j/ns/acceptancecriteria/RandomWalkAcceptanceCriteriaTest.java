package org.optim4j.ns.acceptancecriteria;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.optim4j.ns.Agent;

class RandomWalkAcceptanceCriteriaTest {

	@Test
	@DisplayName("Test when neighbor is better than current agent.")
	void testAcceptable1() {
		RandomWalkAcceptanceCriteria randomWalkAcceptanceCriteria = new RandomWalkAcceptanceCriteria();
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
		Assertions.assertTrue(randomWalkAcceptanceCriteria.isAcceptable(current, neighbor));
	}

	@Test
	@DisplayName("Test when neighbor is not better than current agent.")
	void testAcceptable2() {
		RandomWalkAcceptanceCriteria randomWalkAcceptanceCriteria = new RandomWalkAcceptanceCriteria();
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
		Assertions.assertTrue(randomWalkAcceptanceCriteria.isAcceptable(current, neighbor));
	}

}
