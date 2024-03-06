package org.optim4j.examples.tsp.ns;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.optim4j.examples.tsp.ns.destroyer.TravelRouteRandomDestroyer;
import org.optim4j.examples.tsp.ns.destroyer.TravelRouteSubPathDestroyer;
import org.optim4j.examples.tsp.ns.destroyer.TravelRouteWorstEdgeDestroyer;
import org.optim4j.examples.tsp.ns.repairer.TravelRouteBasicGreedyRepairer;
import org.optim4j.examples.tsp.ns.repairer.TravelRouteRandomRepairer;
import org.optim4j.examples.tsp.ns.repairer.TravelRouteRegretNRepairer;
import org.optim4j.ns.AdaptiveLargeNeighborhoodSearchOptimizer;
import org.optim4j.ns.Destroyer;
import org.optim4j.ns.Repairer;
import org.optim4j.ns.acceptancecriteria.SimulatedAnnealingAcceptanceCriteria;
import org.optim4j.ns.completioncond.UnchangedBestFitness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Traveling salesman problem solution using adaptive large neighborhood search
 * optimizer.
 * 
 * @author Avijit Basak
 */
public class TSPAdaptiveLargeNeighborhoodSearchOptimizer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TSPAdaptiveLargeNeighborhoodSearchOptimizer.class);

	private static final String DATA_FILE = "data/zimbabwe.txt";

	public static void main(String[] args) throws IOException {
		List<Node> nodes = getTravelNodes();
		DistanceMatrix distanceMatrix = new DistanceMatrix(nodes);
		Collections.shuffle(nodes);
		TravelRoute travelRoute = new TravelRoute(nodes, new TravelRouteFitnessCalculator(distanceMatrix));
		LOGGER.info("Initial travel route: {}", travelRoute);

		List<Repairer<PartiallyDestroyedTravelRoute, TravelRoute>> repairers = new ArrayList<>();
		repairers.add(new TravelRouteBasicGreedyRepairer());
		repairers.add(new TravelRouteRegretNRepairer());
		repairers.add(new TravelRouteRandomRepairer());

		List<Destroyer<TravelRoute, PartiallyDestroyedTravelRoute>> destroyers = new ArrayList<>();
		destroyers.add(new TravelRouteRandomDestroyer());
		destroyers.add(new TravelRouteWorstEdgeDestroyer());
		destroyers.add(new TravelRouteSubPathDestroyer((int) (travelRoute.len() * .2)));

		AdaptiveLargeNeighborhoodSearchOptimizer<TravelRoute, PartiallyDestroyedTravelRoute> alnsOptimizer = new AdaptiveLargeNeighborhoodSearchOptimizer<>(
				new SimulatedAnnealingAcceptanceCriteria(100000, .99), new UnchangedBestFitness(5000), repairers,
				destroyers, new GraphicalObserver("TSP Optimizer", "generations", "cost"));
		TravelRoute optimizedTravelRoute = alnsOptimizer.optimize(travelRoute);
		LOGGER.info("Optimized route: {}", optimizedTravelRoute);
	}

	private static List<Node> getTravelNodes() throws IOException {
		List<Node> nodes = new ArrayList<>();
		CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(' ');
		try (CSVParser parser = new CSVParser(
				new InputStreamReader(
						TSPLargeNeighborhoodSearchOptimizer.class.getClassLoader().getResourceAsStream(DATA_FILE)),
				csvFormat);) {
			CSVRecord csvRecord = null;
			Iterator<CSVRecord> itr = parser.iterator();
			while (itr.hasNext()) {
				csvRecord = itr.next();
				Node node = new Node(Integer.parseInt(csvRecord.get(0)), Double.parseDouble(csvRecord.get(1)),
						Double.parseDouble(csvRecord.get(2)));
				nodes.add(node);
			}
		}
		return nodes;
	}

}
