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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.ac.vuw.ecs.fgpj.core.Fitness;
import nz.ac.vuw.ecs.fgpj.core.GPConfig;
import nz.ac.vuw.ecs.fgpj.core.GeneticProgram;

/**
 * SymbolicFitness attempts to be a general fitness function using Root Mean
 * Squared error for the fitness. It should be able to generalise to most single
 * variable symbolic regression problems. It also is thread safe with regards to
 * being used with the ParalellFitness class.
 * 
 * @author roma
 * 
 */
public class SymbolicFitness extends Fitness {
	// This hashmap is a listing of all the test cases. It maps the input value
	// to the expected output. E.g. if I wanted to train a classifier on x^2 I
	// would put in examples like 1 -> 1, 2 -> 4, 3 -> 9
	private Map<Double, Double> values;

	@Override
	public int compare(double arg0, double arg1) {
		// Must multiply by -1 as smaller is better rather than larger is better
		// as in normal doubles
		return -1 * Double.compare(arg0, arg1);
	}

	@Override
	/**
	 * This populates the values hash map will all of the test cases that will be used for training 
	 */
	public void initFitness() {
		// Create the hashmap
		values = new HashMap<Double, Double>();

		// Make a set of test points using the function f()
		for (double i = 0; i < 100; i += 1) {
			double x = i;
			values.put(x, f(x));
		}
	}

	/**
	 * Example function to learn. This can be changed, or even removed entirely
	 * if initFitness is fixed to use a different data source. It is static to
	 * allow a test set to be built up in the main class. Realisitically you
	 * would be getting data from a file, for both the train and test sets
	 * 
	 * @param x
	 *            The x value to run the function on
	 * @return The expected return value given the input x for ground truth
	 */
	public static double f(double x) {
		return x * Math.tan(x + 3);
	}

	public boolean isDirty() {
		// Fitness function never changes
		return false;
	}

	@Override
	public void assignFitness(GeneticProgram p, GPConfig config) {
		// Create space for the return values and variables
		ReturnDouble d[] = new ReturnDouble[] { new ReturnDouble() };

		// total error starts at zero
		double error = 0;

		// Test each program on every point in the hash map and sum the squared
		// error
		for (Map.Entry<Double, Double> e : values.entrySet()) {
			d[0].setX(e.getKey());
			p.evaluate(d);
			error += Math.pow(d[0].value() - e.getValue(), 2);
		}
		// Make into RMS error and assign to the program
		error /= values.size();
		p.setFitness(Math.sqrt(error));

	}

	@Override
	public boolean solutionFound(List<GeneticProgram> pop) {
		for (GeneticProgram p : pop) {
			// There is a solution if any program has a fitness of 0
			if (Double.compare(p.getFitness(), 0) == 0) {
				return true;
			}
		}
		// otherwise, they can still get better
		return false;
	}

	@Override
	public void finish() {
		// There is no required clean up for this fitness function.
	}

}
