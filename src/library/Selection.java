package library;

import java.util.List;

/**
 * This is the selection Operator. This is an implementation of roulette wheel. It can be exteded to be replaced
 * 
 * @author Roma
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
