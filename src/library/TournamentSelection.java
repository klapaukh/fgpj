package library;

import java.util.Arrays;
import java.util.List;


/**
 * Tournament Selection
 * @author Roma
 *
 */
public class TournamentSelection extends Selection {

	private int[] tournament;
	
	public TournamentSelection(int size) {
		this.tournament = new int[size];
	}

	public int select(List<GeneticProgram> pop, GPConfig config) {
		for(int i= 0 ; i < tournament.length;i++){
			tournament[i] = config.randomNumGenerator.nextInt(pop.size());
		}
		Arrays.sort(tournament);
		return tournament[tournament.length-1];
	}

	public int getSize() {
		return tournament.length;
	}

	public void setSize(int size) {
		this.tournament = new int[size];
	}

}
