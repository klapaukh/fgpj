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
		return -1 * Double.compare(arg0.getFitness(), arg1.getFitness());
	}

	@Override
	public void initFitness() {
		values = new HashMap<Double, Double>();
		for (double i = 0; i < 50; i += 0.5) {
			double x = i;
			values.put(x, f(x));
		}
	}

	private double f(double x) {
		return (5*x)+(3*x*x);
	}

	@Override
	public void assignFitness(List<GeneticProgram> pop, GPConfig config) {
		for (GeneticProgram p : pop) {
			ReturnDouble d[] = new ReturnDouble[] { new ReturnDouble() };
			double error = 0;
			for (Map.Entry<Double, Double> e : values.entrySet()) {
				X.setValue(e.getKey());
				p.evaluate(d);
				error += Math.pow(d[0].value() - e.getValue(),2);
			}
			error /= values.size();
			p.setFitness(Math.sqrt(error));

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
