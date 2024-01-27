package org.optim4j.examples.tsp.ns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.optim4j.ns.Agent;

public class TravelRoute implements Agent {

	private List<Node> representation;

	private FitnessCalculator fitnessCalculator;

	public TravelRoute(List<Node> representation, FitnessCalculator fitnessCalculator) {
		this.representation = Collections.unmodifiableList(representation);
		this.fitnessCalculator = fitnessCalculator;
	}

	public static List<Node> shuffledNodes(List<Node> representation) {
		Collections.shuffle(representation);
		return representation;
	}

	public TravelRoute remove(int nodeIndex) {
		return new TravelRoute(
				representation.stream().filter(rNode -> rNode.getIndex() != nodeIndex).collect(Collectors.toList()),
				fitnessCalculator);
	}

	public TravelRoute remove(Node node) {
		return new TravelRoute(
				representation.stream().filter(rNode -> !rNode.equals(node)).collect(Collectors.toList()),
				fitnessCalculator);
	}

	public TravelRoute insert(int index, Node node) {
		List<Node> newRepresentation = new ArrayList<>(representation);
		newRepresentation.add(index, node);
		return new TravelRoute(newRepresentation, this.fitnessCalculator);
	}

	public FitnessCalculator getFitnessCalculator() {
		return fitnessCalculator;
	}

	public List<Node> getRepresentation() {
		return representation;
	}

	public int compareTo(Agent agent) {
		return (int) (this.evaluate() - agent.evaluate());
	}

	public double evaluate() {
		return fitnessCalculator.calculate(this);
	}

	public int len() {
		return representation.size();
	}

}