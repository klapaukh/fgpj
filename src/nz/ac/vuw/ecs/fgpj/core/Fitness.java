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
	 * return if a good enough solution has been found in a set of programs that have had their fitness tested
	 * 
	 * @param pop
	 *            population of evaluated programs
	 * @return true is a good enough solution is in the list
	 */
	public abstract boolean solutionFound(List<GeneticProgram> pop);
	
	
	/**
	 * GP has now finished, any resources can be freed
	 */
	public abstract void finish();

}
