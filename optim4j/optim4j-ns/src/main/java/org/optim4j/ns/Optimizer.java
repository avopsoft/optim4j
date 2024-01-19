package org.optim4j.ns;

public interface Optimizer<A extends Agent> {

	A optimize(A agent);

}
