package sets.symbolicRegression;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.Fitness;
import library.GPConfig;
import library.GeneticProgram;

public class SymbolicFitness implements Fitness {

	Map<Double, Double> values;

	@Override
	public int compare(GeneticProgram arg0, GeneticProgram arg1) {
		return Double.compare(arg0.getFitness(), arg1.getFitness());
	}

	@Override
	public void initFitness() {
		values = new HashMap<Double, Double>();
		for (int i = 0; i < 100; i++) {
			double x = Math.random() * 100;
			values.put(x, f(x));
		}
	}

	private double f(double x) {
		return 5 * x + 3 * x * x;
	}

	@Override
	public void assignFitness(List<GeneticProgram> pop, GPConfig config) {
		for (GeneticProgram p : pop) {
			ReturnDouble d[] = new ReturnDouble[] { new ReturnDouble() };
			double error = 0;
			for (Map.Entry<Double, Double> e : values.entrySet()) {
				X.setValue(e.getKey());
				p.evaluate(d);
				error += Math.abs(d[0].value() - e.getValue());
			}
			if (Double.compare(error, 0) == 0) {
				p.setFitness(0);
			} else {
				p.setFitness(1000000 / error);
			}
		}
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

	@Override
	public void finish() {

	}

}
