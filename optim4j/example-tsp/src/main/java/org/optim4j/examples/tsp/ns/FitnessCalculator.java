package org.optim4j.examples.tsp.ns;

@FunctionalInterface
public interface FitnessCalculator {

	public double calculate(TravelRoute tspAgent);

}
