package org.optim4j.examples.tsp.ns;

import java.util.List;

/**
 * Fitness calculator for travel route.
 * 
 * @author Avijit Basak
 */
public class TravelRouteFitnessCalculator implements FitnessCalculator {

	private final DistanceMatrix distanceMatrix;

	public TravelRouteFitnessCalculator(DistanceMatrix distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}

	private double calculateTotalDistance(List<Node> nodes) {
		double totalDistance = 0.0;
		int index1 = 0;
		int index2 = 0;
		for (int i = 0; i < nodes.size(); i++) {
			index1 = i;
			index2 = (i == nodes.size() - 1) ? 0 : i + 1;
			totalDistance += calculateNodeDistance(nodes.get(index1), nodes.get(index2));
		}
		return totalDistance;
	}

	private double calculateNodeDistance(Node node1, Node node2) {
		return distanceMatrix.getDistance(node1, node2);
	}

	public double calculate(TravelRoute tspAgent) {
		return -calculateTotalDistance(tspAgent.getRepresentation());
	}

}
