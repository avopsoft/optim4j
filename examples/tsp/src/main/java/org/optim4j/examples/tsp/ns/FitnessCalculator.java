package org.optim4j.examples.tsp.ns;

/**
 * Interface for fitness calculator.
 * 
 * @author AvijitBasak
 */
@FunctionalInterface
public interface FitnessCalculator {

	public double calculate(TravelRoute tspAgent);

}
