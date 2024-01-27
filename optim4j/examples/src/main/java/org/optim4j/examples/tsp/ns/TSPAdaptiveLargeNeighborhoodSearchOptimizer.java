package org.optim4j.examples.tsp.ns;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.optim4j.ns.AdaptiveLargeNeighborhoodSearchOptimizer;
import org.optim4j.ns.AdaptiveRepairerDestroyerManager;
import org.optim4j.ns.Destroyer;
import org.optim4j.ns.Repairer;
import org.optim4j.ns.acceptancecriteria.SimulatedAnnealingAcceptanceCriteria;
import org.optim4j.ns.completioncond.UnchangedBestFitness;

public class TSPAdaptiveLargeNeighborhoodSearchOptimizer {

	public static void main(String[] args) throws IOException {
		List<Node> nodes = getTravelNodes(args[0]);
		DistanceMatrix distanceMatrix = DistanceMatrix.getInstance();
		distanceMatrix.initialize(nodes);
		Collections.shuffle(nodes);
		TravelRoute travelRoute = new TravelRoute(nodes, new TravelRouteFitnessCalculator());

		List<Repairer<PartiallyDestroyedTravelRoute, TravelRoute>> repairers = new ArrayList<>();
		repairers.add(new TravelRouteBasicGreedyRepairer());
		repairers.add(new TravelRouteRegretNRepairer());
		repairers.add(new TravelRouteRandomRepairer());

		List<Destroyer<TravelRoute, PartiallyDestroyedTravelRoute>> destroyers = new ArrayList<>();
		destroyers.add(new TravelRouteRandomDestroyer());
		destroyers.add(new TravelRouteWorstEdgeDestroyer());
		destroyers.add(new TravelRouteSubPathDestroyer((int) (travelRoute.len() * .2)));

		AdaptiveRepairerDestroyerManager<PartiallyDestroyedTravelRoute, TravelRoute> adaptiveRepairerDestroyerManager = new AdaptiveRepairerDestroyerManager<>(
				repairers, destroyers);

		AdaptiveLargeNeighborhoodSearchOptimizer<TravelRoute, PartiallyDestroyedTravelRoute> alnsOptimizer = new AdaptiveLargeNeighborhoodSearchOptimizer<>(
				new SimulatedAnnealingAcceptanceCriteria(Double.MAX_VALUE, .99), new UnchangedBestFitness(5000),
				new GraphicalObserver("TSP Optimizer", "generations", "cost"), "TSP", adaptiveRepairerDestroyerManager);
		TravelRoute optimizedTravelRoute = alnsOptimizer.optimize(travelRoute);
		System.out.println(optimizedTravelRoute);
	}

	private static List<Node> getTravelNodes(String filePath) throws IOException {
		List<Node> nodes = new ArrayList<Node>();
		CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(' ');
		try (CSVParser parser = new CSVParser(new FileReader(filePath), csvFormat);) {
			CSVRecord record = null;
			Iterator<CSVRecord> itr = parser.iterator();
			while (itr.hasNext()) {
				record = itr.next();
				Node node = new Node(Integer.parseInt(record.get(0)), Double.parseDouble(record.get(1)),
						Double.parseDouble(record.get(2)));
				nodes.add(node);
			}
		}
		return nodes;
	}

}
