package sets.symbolicRegression.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import sets.symbolicRegression.Add;
import sets.symbolicRegression.Divide;
import sets.symbolicRegression.Exp;
import sets.symbolicRegression.Minus;
import sets.symbolicRegression.RandomDouble;
import sets.symbolicRegression.ReturnDouble;
import sets.symbolicRegression.Sin;
import sets.symbolicRegression.Times;
import sets.symbolicRegression.X;
import sets.symbolicRegression.Y;

import library.GPConfig;
import library.GeneticProgram;
import library.ParallelFitness;
import library.Population;
import library.TournamentSelection;

public class SymImageMain {
	
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
		((ParallelFitness<MathImageFitness>) (c.fitnessObject)).fitness.getResult(p, size,size, c);
	}
	
	public static void main(String[] args) {

		
		long start = System.currentTimeMillis();

		GPConfig conf = new GPConfig(3, 1, 8, 0.7, 0.28, 0.02);
		conf.setLogFile("run-log.txt");
		conf.loggingFrequency(10000);
		conf.selectionOperator = new TournamentSelection(5);

		conf.addTerminal(new X());
		conf.addTerminal(new Y());
		conf.addTerminal(new RandomDouble(1, 5, conf));

		conf.addFunction(new Add());
		conf.addFunction(new Times());
		conf.addFunction(new Minus());
		conf.addFunction(new Divide());
		conf.addFunction(new Exp());
		conf.addFunction(new Sin());

		conf.fitnessObject = new ParallelFitness<MathImageFitness>(new MathImageFitness("sample.pnm"), 4, 21);
		
		
		if(args.length != 0){
			enlarge(args, conf);
			System.exit(0);
		}

		Population p = new Population(100, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
		p.generateInitialPopulation();

		p.evolve(500);
		
		

		GeneticProgram s = p.getBest();
		System.out.println(s.getFitness());
		System.out.println(s);

		((ParallelFitness<MathImageFitness>)conf.fitnessObject).fitness.outputResults(s, "res.pnm", conf);
				
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}
}
