package nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.bad;

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
 * Essentially the same as SymbolicFitness from the symbolicFitness package,
 * just modified to use unsafex
 * 
 * @author roma
 * 
 */
public class UnsafeSymbolicFitness extends Fitness {

	Map<Double, Double> values;

	@Override
	public int compare(double arg0, double arg1) {
		return -1 * Double.compare(arg0, arg1);
	}

	@Override
	public void initFitness() {
		values = new HashMap<Double, Double>();
		for (double i = 0.1; i < 10; i += 0.1) {
			double x = i;
			values.put(x, f(x));
		}
	}

	private double f(double x) {
		// return x*Math.exp(x + 3);
		return x * Math.tan(x + 3);
	}

	@Override
	public void assignFitness(GeneticProgram p, GPConfig config) {
			UnsafeReturnDouble d[] = new UnsafeReturnDouble[] { new UnsafeReturnDouble() };
			double error = 0;
			for (Map.Entry<Double, Double> e : values.entrySet()) {
				UnsafeX.setValue(e.getKey());
				p.evaluate(d);
				error += Math.pow(d[0].value() - e.getValue(), 2);
			}
			error /= values.size();
			p.setFitness(Math.sqrt(error));
	}

	@Override
	public boolean solutionFound(List<GeneticProgram> pop) {
		for (GeneticProgram p : pop) {
			if (Double.compare(p.getFitness(), 0) == 0) {
				return true;
			}
		}
		return false;
	}

	public boolean isDirty(){
		//It never changes
		return false;
	}
	
	@Override
	public void finish() {

	}

}
