package nz.ac.vuw.ecs.fgpj.examples.lines;

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
import nz.ac.vuw.ecs.fgpj.core.ParallelFitness;
import nz.ac.vuw.ecs.fgpj.core.Population;
import nz.ac.vuw.ecs.fgpj.core.TournamentSelection;

public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		// Create the setting for a new GP run
		// 1 root node, min program depth 1, and max depth 8
		// Mutation rate = 70%, crossover 28%, elitism 2%
		GPConfig symConfig = new GPConfig(1, 1, 8, 0.7, 0.28, 0.02);

		// output the statistics about each generation to run-log.txt
		symConfig.setLogFile("run-log.txt");

		// Use tournament selection with a tournament sizeof 5
		symConfig.selectionOperator = new TournamentSelection(5);

		// Record intermediate best image results every 100 generations and save
		// them to the current directory
		symConfig.configModifier = new ImageLogMod(100, ".");

		// Declare a population, with 100 individuals and using symConfig as the
		// config
		Population pop = new Population(100, symConfig);

		// The programs return images of type ReturnImage
		pop.setReturnType(ReturnImage.TYPENUM);

		// Write out the whole population every 100,000 generations
		symConfig.loggingFrequency(100000);

		// Add the terminals we need
		symConfig.addTerminal(new Null());
		symConfig.addTerminal(new SetColor(symConfig));

		// Add the functions we need
		symConfig.addFunction(new Line(symConfig));
		symConfig.addFunction(new Rect(symConfig));
		symConfig.addFunction(new Oval(symConfig));

		// Set the fitness Function to be used
		// We are using a the ParallelFitness function to parallelise the
		// ImageFitness class which is the actual fitness function.
		// It is set up to copy an image called sample.pnm in the working
		// directory. It must have the magic number P3
		// It is set up to use 4 threads and job sizes of 20 programs
		symConfig.fitnessObject = new ParallelFitness<ImageFitness>(
				new ImageFitness("sample.pnm"), 4, 20);

		if (args.length == 1) {
			// If there is one command line argument assume its a pop
			// file name
			// Read the  population from a file
			try {
				pop.readFromFile(args[0]);
			} catch (Exception err) {
				//if it fails print a stack trace and just quit
				//there isn't really a reason to keep going anymore
				err.printStackTrace();
				System.exit(1);
			}
		} else {
			//If there is not population file to read from, generate a new one
			pop.generateInitialPopulation();
		}

		//Run for 20,000 generations
		pop.evolve(20000);

		System.out.println("Best program");
		System.out.println("Fitness: " + pop.getBest().getFitness());
		System.out.println("Code: ");
		System.out.println(pop.getBest());

		//Save the resulting best image to res.pnm. It will be a P3 file
		((ParallelFitness<ImageFitness>) (symConfig.fitnessObject)).fitness
				.getResult(pop.getBest(), "res.pnm");

	}

}
