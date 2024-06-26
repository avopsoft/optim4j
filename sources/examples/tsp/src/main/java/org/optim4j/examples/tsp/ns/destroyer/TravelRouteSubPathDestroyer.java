package org.optim4j.examples.tsp.ns.destroyer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.optim4j.examples.tsp.ns.Node;
import org.optim4j.examples.tsp.ns.PartiallyDestroyedTravelRoute;
import org.optim4j.examples.tsp.ns.TravelRoute;
import org.optim4j.ns.Destroyer;

/**
 * A destroyer heuristic of travel route which destroys a subpath of routes.
 * 
 * @author Avijit Basak
 */
public class TravelRouteSubPathDestroyer implements Destroyer<TravelRoute, PartiallyDestroyedTravelRoute> {

	private int noOfEdgesToBeRemoved;

	private Random random = new Random();

	public TravelRouteSubPathDestroyer(int noOfEdgesToBeRemoved) {
		this.noOfEdgesToBeRemoved = noOfEdgesToBeRemoved;
	}

	@Override
	public PartiallyDestroyedTravelRoute destroy(TravelRoute travelRoute) {
		final List<Node> representation = travelRoute.getRepresentation();
		int nodeCount = representation.size();

		int startIndex = random.nextInt(representation.size());
		int endIndex = (startIndex + noOfEdgesToBeRemoved) <= nodeCount - 1 ? startIndex + noOfEdgesToBeRemoved
				: (startIndex + noOfEdgesToBeRemoved - nodeCount);

		if (startIndex <= endIndex) {
			final List<Node> newRepresentation = new ArrayList<>(representation.subList(0, startIndex));
			newRepresentation.addAll(representation.subList(endIndex + 1, nodeCount));

			return new PartiallyDestroyedTravelRoute(
					new TravelRoute(newRepresentation, travelRoute.getFitnessCalculator()),
					representation.subList(startIndex, endIndex + 1));
		} else {
			final List<Node> destroyedNodes = new ArrayList<>(representation.subList(startIndex, nodeCount));
			destroyedNodes.addAll(representation.subList(0, endIndex + 1));

			return new PartiallyDestroyedTravelRoute(new TravelRoute(representation.subList(endIndex + 1, startIndex),
					travelRoute.getFitnessCalculator()), destroyedNodes);
		}
	}

}
