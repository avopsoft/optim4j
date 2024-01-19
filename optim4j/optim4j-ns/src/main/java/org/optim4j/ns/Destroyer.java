package org.optim4j.ns;

@FunctionalInterface
public interface Destroyer<A extends Agent, T> {

	public T destroy(A agent);
}
