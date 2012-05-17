package nz.ac.vuw.ecs.fgpj.examples.symbolicRegression;

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
import nz.ac.vuw.ecs.fgpj.core.GPConfig;
import nz.ac.vuw.ecs.fgpj.core.GeneticProgram;
import nz.ac.vuw.ecs.fgpj.core.ParallelFitness;
import nz.ac.vuw.ecs.fgpj.core.Population;
import nz.ac.vuw.ecs.fgpj.core.TournamentSelection;

/**
 * The main entry class for the Symbolic Regression example. This class is
 * intended to show a basic common use case of the GP library, as well as show
 * of how simple the parallelism features are to use.
 * 
 * This is the set up to find the formula to match a given list of points. This
 * class contains only a main method as all of the core computation and work is
 * taken care of by other classes.
 * 
 * @author roma
 * 
 */
public class SymbMain {

	/**
	 * The main method. This will run the GP algorithm for the symbolic
	 * regression example
	 * 
	 * @param args
	 *            The values passed in this array are ignored
	 */
	public static void main(String[] args) {

		// Flag to determine if the fitness function should be run in parallel
		boolean parallel = true;

		// The GPConfig is the settings for the GP algorithm. It controls all
		// the parameters and how the algorithm behaves
		// Here we set the following parameters in order
		// numParts = 1. Our trees only have one root, as there is only 1 result
		// for each input value
		// minDepth = 1. Program trees must have depth at least 1. Or else they
		// might be silly
		// maxDepth = 5. Program trees cannot be deeper than 5 levels. This is
		// because the larger the tree is
		// the more complexity, and the slower it runs. We assume that 5 is deep
		// enough for any
		// problem this example will be used on
		// mutationRate = 0.6. Each generation 60% of the new population will
		// come from mutation
		// crossoverRate = 0.38. Each generation 38% of the new population will
		// come from crossover
		// elitismRate = 0.02. Each generation 2% of the population will come
		// from elitism. While elitism is
		// is good in that it preserves good programs, too much of it will not
		// allow
		// many new programs to be created.
		GPConfig conf = new GPConfig(1, 1, 5, 0.6, 0.38, 0.02);

		// Each generation basic statistics about that generation will be logged
		// to this file.
		conf.setLogFile("run-log.txt");

		// Every 10,000 generations, write the current population to a file
		conf.loggingFrequency(10000);

		// For selection, use tournament selection with a pool of size 5. We use
		// tournament selection
		// instead of roulette wheel, because the fitness function uses error as
		// the fitness which is, lower is better. Roulette wheel is good for the
		// opposite case,
		// however, its general statement doesn't work as well for this case.
		conf.selectionOperator = new TournamentSelection(5);

		// Add the terminals
		// This is the X terminal. It takes a value from the input. Essentially,
		// as X is the only input variable the program will be a function of at most 1 variable
		conf.addTerminal(new X());
		//Add a random double terminal that ranges from [1,5]
		conf.addTerminal(new RandomDouble(1, 5, conf));
		conf.addTerminal(new RandomInt(1, 5, conf)); //same as above but integer
		
		//Add mathematical operators to the function set
		//commented out functions are available in the library and can be safely uncommented
		conf.addFunction(new Add());
		conf.addFunction(new Times());
		conf.addFunction(new Minus());
		conf.addFunction(new Divide());
		conf.addFunction(new Exp());
		conf.addFunction(new Sin());
//		conf.addFunction(new Min());
//		conf.addFunction(new Max());
//		conf.addFunction(new Tan());
		
		if (parallel) {
			conf.fitnessObject = new ParallelFitness<SymbolicFitness>(
					new SymbolicFitness(), 4, 21);
		} else {
			conf.fitnessObject = new SymbolicFitness();
		}

		Population p = new Population(100, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
		// p.readFromFile("population.txt")
		p.generateInitialPopulation();

		long start = System.currentTimeMillis();
		if (p.evolve(500)) {
			System.out.println("Early Termination");
		}
		long end = System.currentTimeMillis();

		GeneticProgram s = p.getBest();
		System.out.println(s.getFitness());
		System.out.println(s);

		System.out.println(end - start);
	}
}
