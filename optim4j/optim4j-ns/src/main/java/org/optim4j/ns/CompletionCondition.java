package org.optim4j.ns;

@FunctionalInterface
public interface CompletionCondition {

	boolean isComplete(Agent agent);

}
