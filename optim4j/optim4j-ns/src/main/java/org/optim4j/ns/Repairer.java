package org.optim4j.ns;

@FunctionalInterface
public interface Repairer<T, A extends Agent> {

	public A repair(T t);
}
