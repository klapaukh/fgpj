package sets.symbolicRegression;

import library.GPConfig;
import library.GeneticProgram;
import library.Population;

public class SymbMain {

	public static void main(String[] args){
		GPConfig conf= new GPConfig(1,3,4,0.4, 0.4, 0.2);
		conf.setLogFile("run-log.txt");
		conf.loggingFrequency(1);
		
		conf.addTerminal(new X());
		conf.addTerminal(new RandomDouble(conf));
		conf.addFunction(new Add());
		conf.addFunction(new Times());
		
		conf.fitnessObject = new SymbolicFitness();
		
		Population p = new Population(100, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		
		p.evolve(500);
		
		GeneticProgram s = p.getBest();
		System.out.println(s.getFitness());
		System.out.println(s);
	}
}
