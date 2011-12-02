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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import nz.ac.vuw.ecs.fgpj.core.GPConfig;
import nz.ac.vuw.ecs.fgpj.core.GeneticProgram;
import nz.ac.vuw.ecs.fgpj.core.ParallelFitness;
import nz.ac.vuw.ecs.fgpj.core.Population;
import nz.ac.vuw.ecs.fgpj.core.TournamentSelection;


public class Main {

	private static void enlarge(String args[], GPConfig c) {
		if (args.length != 2) {
			System.err
					.println("Incorrect Usage\nTakes either a population archive, or a pnm output file and a new size\n");
			return;
		}
		String file = args[0];
		int size = Integer.parseInt(args[1]);
		Scanner scan;
		try {
			scan = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("File cannot be found to enlarge");
		}
		String line;
		scan.nextLine();
		scan.nextLine();
		line = scan.nextLine().substring(1);
		GeneticProgram p = new GeneticProgram(c);
		p.parseProgram(line, c);
		((ImageFitness) (c.fitnessObject)).getResult(p, "out.pnm");
	}

	public static void main(String[] args) {

		// Create the setting for a new GP run
		// 1 root node, min program depth 1, and max depth 8
		GPConfig symConfig = new GPConfig(1, 1, 8, 0.7, 0.28, 0.02);
		symConfig.setLogFile("run-log.txt");
		symConfig.selectionOperator = new TournamentSelection(5);
		symConfig.configModifier = new ImageLogMod(100, "/tmp/roma");
		
		// Declare a population, giving the size and a log file name
		Population pop = new Population(64,  symConfig);

		// Set the return type for our programs
//		pop.setReturnType(0,ReturnImage.TYPENUM);
		pop.setReturnType(ReturnImage.TYPENUM);

		// Write out the population every N generations
		symConfig.loggingFrequency(100000);

		// Add the terminals we need
		symConfig.addTerminal(new Null(symConfig));
		symConfig.addTerminal(new SetColor(symConfig));

		// Add the functions we need
		symConfig.addFunction(new Line(symConfig));
		symConfig.addFunction(new Rect(symConfig));
		symConfig.addFunction(new Oval(symConfig));

		// Set the fitness class to be used

		 symConfig.fitnessObject = new ParallelFitness<ImageFitness>(new ImageFitness("sample.pnm"),64,1);
//		symConfig.fitnessObject = new ImageFitness();
		// Initialise the fitness
		

		if (args.length == 2) {
			symConfig.fitnessObject = new ImageFitness("sample.pnm");
			enlarge(args, symConfig);
			System.exit(0);
		}

		if (args.length == 1) {
			// If there is one command line argument assume its a pop
			// file name
			// Reading a population from a file
			try {
				pop.readFromFile(args[0]);
			} catch (Exception err) {
				err.printStackTrace();
				System.exit(1);
			}
		} else {
			pop.generateInitialPopulation();
		}

		/* Do 20,000 generations (overnight on the test machine)*/

		pop.evolve(20000);
		
		System.out.println("Best program");
		System.out.println("Fitness " + pop.getBest().getFitness());
		System.out.println(pop.getBest());

		 ((ParallelFitness<ImageFitness>)(symConfig.fitnessObject)).fitness.getResult(pop.getBest(),"res.pnm");

	}

}
