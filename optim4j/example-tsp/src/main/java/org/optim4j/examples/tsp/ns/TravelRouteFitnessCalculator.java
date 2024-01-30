package org.optim4j.examples.tsp.ns;

import java.util.List;

public class TravelRouteFitnessCalculator implements FitnessCalculator {

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
		DistanceMatrix distanceMatrix = DistanceMatrix.getInstance();
		double distance = distanceMatrix.getDistance(node1, node2);

		return distance;
	}

	public double calculate(TravelRoute tspAgent) {
		return -calculateTotalDistance(tspAgent.getRepresentation());
	}

}
