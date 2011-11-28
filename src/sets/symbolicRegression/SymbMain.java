package sets.symbolicRegression;

import library.GPConfig;
import library.GeneticProgram;
import library.ParallelFitness;
import library.Population;
import library.TournamentSelection;

public class SymbMain {

	public static void main(String[] args){
		long start = System.currentTimeMillis();
		boolean parallel = true;
		
		GPConfig conf= new GPConfig(1,1,8,0.6, 0.38, 0.02);
		conf.setLogFile("run-log.txt");
		conf.loggingFrequency(10000);
		conf.selectionOperator = new TournamentSelection(5);
		
		if(parallel){
			conf.addTerminal(new SafeX());
		}else{
			conf.addTerminal(new X());
		}
		conf.addTerminal(new RandomInt(1,5,conf));
		conf.addFunction(new Add());
		conf.addFunction(new Times());
		conf.addFunction(new Minus());
		conf.addFunction(new Divide());
		conf.addFunction(new Exp());
		
		if(parallel){
			conf.fitnessObject = new ParallelFitness<SafeSymbolicFitness>(new SafeSymbolicFitness(),4,21);
		}else{
			conf.fitnessObject = new SymbolicFitness();
		}
		
		Population p = new Population(500, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
		p.generateInitialPopulation();

		
		if(p.evolve(1000)){
			System.out.println("Early");
		}
		
		GeneticProgram s = p.getBest();
		System.out.println(s.getFitness());
		System.out.println(s);
		
		long end = System.currentTimeMillis();
		System.out.println(end - start );
	}
}
