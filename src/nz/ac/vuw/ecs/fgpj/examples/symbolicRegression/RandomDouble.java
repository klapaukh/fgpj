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
import nz.ac.vuw.ecs.fgpj.core.Node;
import nz.ac.vuw.ecs.fgpj.core.NodeFactory;
import nz.ac.vuw.ecs.fgpj.core.ReturnData;
import nz.ac.vuw.ecs.fgpj.core.Terminal;

/**
 * Implements a random double terminal. Will always return the same value once it has been created. Used for constants.
 * 
 * @author roma
 * 
 */
public class RandomDouble extends Terminal {

	private double value;
	private double min, max;

	public RandomDouble(double min, double max, GPConfig conf) {
		super(ReturnDouble.TYPENUM, "RandomDouble");
		this.min = min;
		this.max = max;

		double range = max - min;
		value = conf.randomNumGenerator.nextDouble() * (range + 1) + min;
	}

	public void print(StringBuilder s) {
		s.append(String.format("%sx%f", getName(), value));
	}

	@Override
	public RandomDouble getNew(GPConfig config) {
		return new RandomDouble(min, max, config);
	}

	@Override
	public void evaluate(ReturnData out) {
		((ReturnDouble) out).setValue(value);
	}

	public void init(Node n) {
		RandomDouble v = (RandomDouble)n;
		this.value = v.value;
	}

	@Override
	public RandomDouble generate(String s, GPConfig conf) {
		String v = s.substring(getName().length() + 1);
		RandomDouble res = ((RandomDouble) NodeFactory.newNode(getKind(), conf));
		res.value = Double.parseDouble(v);
		return res;
	}

}
