package org.optim4j.examples.tsp.ns;

import java.util.List;

/**
 * The distance matrix to calculate distances between two locations.
 * 
 * @author Avijit Basak
 */
public class DistanceMatrix {

	private final double[][] distances;

	public DistanceMatrix(List<Node> nodes) {
		int len = nodes.size();
		this.distances = new double[len][len];
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				distances[i][j] = Math.pow((Math.pow(nodes.get(i).getX() - nodes.get(j).getX(), 2)
						+ Math.pow(nodes.get(i).getY() - nodes.get(j).getY(), 2)), .5);
			}
		}
	}

	public double getDistance(Node node1, Node node2) {
		return distances[node1.getIndex() - 1][node2.getIndex() - 1];
	}

}