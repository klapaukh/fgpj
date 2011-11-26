package library;

import java.util.Comparator;
import java.util.List;

public abstract class Fitness implements Comparator<GeneticProgram>{
	
	protected GPConfig config;

	public Fitness(GPConfig conf) {
		config = conf;
	}

	public abstract void initFitness();

	/**
	 * assignFitness evaluates a population of programs and assigns their raw
	 * fitnesses.
	 * 
	 * Pure virtual function. This class must be subclassed and evaluate must be
	 * implemented in the subclass.
	 */
	public abstract void assignFitness(List<GeneticProgram> pop);


	/**
	 * best() returns a double represnting the best possible fitness attainable.
	 * worst() returns a double representing the worst possible fitness
	 * attainable.
	 */
	public abstract double best();

	public abstract double worst();

	/**
	 * solutionFound returns true if the solution has been found, false
	 * otherwise.
	 * 
	 * Pure virtual function. The class must be subclassed and solutionFound
	 * must be implemented in the subclass.
	 */
	public abstract boolean solutionFound(List<GeneticProgram> pop);

}
