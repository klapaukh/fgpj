package sets.symbolicRegression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import library.GPConfig;
import library.GeneticProgram;
import library.ParallelFitness;
import library.Population;
import library.TournamentSelection;

public class SymbMain {

	public static void main(String[] args){
		GPConfig conf= new GPConfig(1,1,8,0.6, 0.38, 0.02);
		conf.setLogFile("run-log.txt");
		conf.loggingFrequency(10000);
		conf.selectionOperator = new TournamentSelection(5);
		
		conf.addTerminal(new X());
		conf.addTerminal(new RandomInt(1,5,conf));
		conf.addFunction(new Add());
		conf.addFunction(new Times());
		conf.addFunction(new Minus());
		conf.addFunction(new Divide());
		
		conf.fitnessObject = new ParallelFitness<SymbolicFitness>(new SymbolicFitness(),4,20);
		
		Population p = new Population(500, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		
		p.evolve(1000);
		
		GeneticProgram s = p.getBest();
		System.out.println(s.getFitness());
		System.out.println(s);
	}
}
