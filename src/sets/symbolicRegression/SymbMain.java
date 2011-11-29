package sets.symbolicRegression;

import library.GPConfig;
import library.GeneticProgram;
import library.ParallelFitness;
import library.Population;
import library.TournamentSelection;

public class SymbMain {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		boolean parallel = true;

		GPConfig conf = new GPConfig(1, 1, 5, 0.6, 0.38, 0.02);
		conf.setLogFile("run-log.txt");
		conf.loggingFrequency(10000);
		conf.selectionOperator = new TournamentSelection(5);

		conf.addTerminal(new X());
		conf.addTerminal(new RandomDouble(1, 5, conf));
		conf.addFunction(new Add());
		conf.addFunction(new Times());
		conf.addFunction(new Minus());
		conf.addFunction(new Divide());
		conf.addFunction(new Exp());
		conf.addFunction(new Sin());

		if (parallel) {
			conf.fitnessObject = new ParallelFitness<SymbolicFitness>(new SymbolicFitness(), 4, 21);
		} else {
			conf.fitnessObject = new SymbolicFitness();
		}

		Population p = new Population(100, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
		// p.readFromFile("population.txt")
		p.generateInitialPopulation();

		if (p.evolve(500)) {
			System.out.println("Early Termination");
		}

		GeneticProgram s = p.getBest();
		System.out.println(s.getFitness());
		System.out.println(s);

		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}
}
