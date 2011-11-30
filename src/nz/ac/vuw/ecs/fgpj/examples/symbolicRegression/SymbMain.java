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
