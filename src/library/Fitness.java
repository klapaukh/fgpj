package library;

import java.util.Comparator;
import java.util.List;

public interface Fitness extends Comparator<GeneticProgram> {

	public abstract void initFitness();

	/**
	 * Calculate and assign the raw fitness values for all programs in pop
	 * 
	 * @param pop
	 *            List of programs that need fitness testing
	 */
	public abstract void assignFitness(List<GeneticProgram> pop, GPConfig config);

	/**
	 * The best possible fitness
	 * 
	 * @return best fitness possible
	 */
	public abstract double best();

	/**
	 * The worst possible fitness
	 * 
	 * @return worst possible fitness
	 */
	public abstract double worst();

	/**
	 * return if a good enough solution has been found in a set of programs that have had their fitness tested
	 * 
	 * @param pop
	 *            population of evaluated programs
	 * @return true is a good enough solution is in the list
	 */
	public abstract boolean solutionFound(List<GeneticProgram> pop);

}
