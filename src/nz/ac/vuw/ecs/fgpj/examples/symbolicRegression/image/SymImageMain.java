package nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.image;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import nz.ac.vuw.ecs.fgpj.core.GPConfig;
import nz.ac.vuw.ecs.fgpj.core.GeneticProgram;
import nz.ac.vuw.ecs.fgpj.core.ParallelFitness;
import nz.ac.vuw.ecs.fgpj.core.Population;
import nz.ac.vuw.ecs.fgpj.core.TournamentSelection;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.Add;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.Divide;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.Exp;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.Max;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.Min;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.Minus;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.RandomDouble;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.ReturnDouble;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.Sin;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.Tan;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.Times;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.X;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.Y;


public class SymImageMain {

	@SuppressWarnings("unchecked")
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
		scan.close();
		GeneticProgram p = new GeneticProgram(c);
		p.parseProgram(line, c);

		List<GeneticProgram> ll = new ArrayList<GeneticProgram>();
		ll.add(p);
		((ParallelFitness<MathImageFitness>) (c.fitnessObject)).fitness.initFitness();
		((ParallelFitness<MathImageFitness>) (c.fitnessObject)).fitness.assignFitness(ll, c);
		((ParallelFitness<MathImageFitness>) (c.fitnessObject)).fitness.getResult(p, size,size, c);

		System.out.println(p.getFitness());
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {


		long start = System.currentTimeMillis();

		GPConfig conf = new GPConfig(3, 1, 8, 0.7, 0.28, 0.02);
		conf.setLogFile("run-log.txt");
		conf.loggingFrequency(100000);
		conf.selectionOperator = new TournamentSelection(5);
		conf.configModifier = new ImageLoggerModifier(100,"/tmp");

		conf.addTerminal(new X());
		conf.addTerminal(new Y());
		conf.addTerminal(new RandomDouble(0, 10, conf));

		conf.addFunction(new Add());
		conf.addFunction(new Times());
		conf.addFunction(new Minus());
		conf.addFunction(new Divide());
		conf.addFunction(new Min());
		conf.addFunction(new Max());
		conf.addFunction(new Exp());
		conf.addFunction(new Sin());
		conf.addFunction(new Tan());

		conf.fitnessObject = new ParallelFitness<MathImageFitness>(new MathImageFitness("sample.pnm"), 4, 21);


		if(args.length != 0){
			enlarge(args, conf);
			System.exit(0);
		}

		Population p = new Population(100, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
		p.generateInitialPopulation();

		p.evolve(10000);



		GeneticProgram s = p.getBest();
		System.out.println(s.getFitness());
		System.out.println(s);

		((ParallelFitness<MathImageFitness>)conf.fitnessObject).fitness.outputResults(s, "res.pnm", conf);

		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}
}
