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
import java.util.List;

/**
 * This is the selection Operator. This is an implementation of roulette wheel. It can be exteded to be replaced
 * 
 * @author Roman Klapaukh
 * 
 */
public class Selection {

	/**
	 * Select an index from the population based on Roulette wheel. Bigger fitness means greater chance of selection.
	 * 
	 * @param pop the population of programs
	 * @param config the config
	 * @return the index of the selected program
	 */
	public int select(List<GeneticProgram> pop, GPConfig config) {
		double totalFitness = 0;

		for (int i = 0; i < pop.size(); i++) {
			totalFitness += pop.get(i).getFitness();
		}

		double tmpRand = config.randomNumGenerator.nextDouble();
		double randValue = totalFitness * tmpRand;

		double cumulFitness = totalFitness;
		
		int i ;
		for (i = 0; i < pop.size(); i++) {
			cumulFitness -= pop.get(i).getFitness();

			if (cumulFitness <= randValue) break;
		}

		if (i >= pop.size()) {
			i = pop.size() - 1;
		}

		return i;
	}

}
