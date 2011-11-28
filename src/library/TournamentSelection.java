package library;

import java.util.Arrays;
import java.util.List;


/**
 * Tournament Selection
 * @author Roma
 *
 */
public class TournamentSelection extends Selection {

	private int tournamentSize;
	
	public TournamentSelection(int size) {
		this.tournamentSize = size;
	}

	public int select(List<GeneticProgram> pop, GPConfig config) {
		int idx = config.randomNumGenerator.nextInt(pop.size());
		GeneticProgram fitness = pop.get(idx);
		for(int i= 1 ; i < tournamentSize;i++){
			int newIdx = config.randomNumGenerator.nextInt(pop.size());
			GeneticProgram newFitness = pop.get(newIdx);
			if(config.fitnessObject.compare(fitness, newFitness)<0){
				idx = newIdx;
				fitness = newFitness;
			}
			
		}
		return idx;
	}

	public int getSize() {
		return tournamentSize;
	}

	public void setSize(int size) {
		this.tournamentSize = size;
	}

}
