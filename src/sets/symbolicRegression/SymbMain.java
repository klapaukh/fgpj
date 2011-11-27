package sets.symbolicRegression;

import library.GPConfig;
import library.GeneticProgram;
import library.Population;
import library.TournamentSelection;

public class SymbMain {

	public static void main(String[] args){
		GPConfig conf= new GPConfig(1,2,5,0.6, 0.38, 0.02);
		conf.setLogFile("run-log.txt");
		conf.loggingFrequency(1);
		conf.selectionOperator = new TournamentSelection(5);
		
		conf.addTerminal(new X());
		conf.addTerminal(new RandomInt(conf));
		conf.addFunction(new Add());
		conf.addFunction(new Times());
		
		conf.fitnessObject = new SymbolicFitness();
		
		Population p = new Population(100, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		
		p.evolve(100);
		
		
		GeneticProgram s = p.getBest();
		System.out.println(s.getFitness());
		System.out.println(s);
	}
}
