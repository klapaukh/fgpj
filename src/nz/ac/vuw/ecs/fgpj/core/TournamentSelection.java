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
 * Tournament Selection
 * @author Roman Klapaukh
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
