package nz.ac.vuw.ecs.fgpj.core;

/*
 FGPJ Genetic Programming library
 Copyright (C) 2011  Roman Klapaukh

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.util.Comparator;
import java.util.List;

/**
 * This is the interface for all fitness functions
 * 
 * @author Roman Klapaukh
 * 
 */
public abstract class Fitness implements Comparator<GeneticProgram> {

	public abstract void initFitness();

	/**
	 * Calculate and assign the raw fitness values for all programs in pop
	 * 
	 * @param pop
	 *            List of programs that need fitness testing
	 */
	public void assignFitness(List<GeneticProgram> pop, GPConfig config) {
		for (GeneticProgram p : pop) {
			if (isDirty() || p.lastOperation() != GeneticProgram.ELITISM) {
				// Skip values which are just elitismed and where the fitness
				// won't have changed
				assignFitness(p, config);
			}
		}
	}

	/**
	 * Returns if the fitness function has changed. If it is non dirty it allows
	 * calculation of elitism selected program without actually evaluating by
	 * using the old value
	 * 
	 * @return true if the old elitism fitness would still be valid
	 */
	public abstract boolean isDirty();

	/**
	 * Calculate and assign the fitness for a single program
	 * 
	 * @param p
	 *            Program to assign the fitness to
	 * @param config
	 *            GPConfig for this run
	 */
	public abstract void assignFitness(GeneticProgram p, GPConfig config);

	/**
	 * return if a good enough solution has been found in a set of programs that
	 * have had their fitness tested
	 * 
	 * @param pop
	 *            population of evaluated programs
	 * @return true is a good enough solution is in the list
	 */
	public abstract boolean solutionFound(List<GeneticProgram> pop);

	public int compare(GeneticProgram p1, GeneticProgram p2) {
		return compare(p1.getFitness(), p2.getFitness());
	}

	/**
	 * Compare two fitnesses
	 * 
	 * @param fitness1
	 *            Fitness 1
	 * @param fitness2
	 *            Fitness 2
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second.
	 */
	public abstract int compare(double fitness1, double fitness2);

	/**
	 * GP has now finished, any resources can be freed
	 */
	public abstract void finish();

}
