package sets.symbolicRegression;

import library.GPConfig;
import library.GeneticProgram;
import library.ParallelFitness;
import library.Population;
import library.TournamentSelection;

public class SymbMain {

	public static void main(String[] args){
		
		boolean parallel = true;
		
		GPConfig conf= new GPConfig(1,3,4,0.6, 0.38, 0.02);
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

		String ss;
		if(parallel){
			ss = "1 Program0  ( * SafeX ( e ( + SafeX Randomx3 ) ) )  |";
		}else{
			ss = "1 Program0  ( * X ( e ( + X Randomx3 ) ) )  |";
		}
		GeneticProgram pr = p.getUnderlyingPopulation().get(0);
		pr.parseProgram(ss, conf);
		
		p.evolve(1000);
		
		GeneticProgram s = p.getBest();
		System.out.println(s.getFitness());
		System.out.println(s);

		
//		List<GeneticProgram> pp = new ArrayList<GeneticProgram>();
//		pp.add(pr);
//		((ParallelFitness<SafeSymbolicFitness>)conf.fitnessObject).fitness.assignFitness(pp, conf);
		
//		System.out.println(pr.getFitness());
	}
}
