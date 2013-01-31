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
import nz.ac.vuw.ecs.fgpj.core.ReturnData;
import nz.ac.vuw.ecs.fgpj.core.Terminal;

/**
 * Implements a random double terminal. Will always return the same value once
 * it has been created. Used for constants.
 * 
 * @author roma
 * 
 */
public class RandomDouble extends Terminal {

	/**
	 * Field to store the constant double that a specific instance represents
	 */
	private double value;

	/**
	 * Stores the range of values an instance can produce. This is needed for
	 * creating new instances later down the line
	 */
	private double min, max;

	/**
	 * Create a new RandomDouble that represents a constant double value in the
	 * random from min to max inclusive.
	 * 
	 * @param min
	 *            The minimum value this double can be
	 * @param max
	 *            The maximin value this double can be
	 * @param conf
	 *            The GPConfig used in this run
	 */
	public RandomDouble(double min, double max, GPConfig conf) {
		super(ReturnDouble.TYPENUM, "RandomDouble");
		this.min = min;
		this.max = max;

		double range = max - min;
		// Generate a random number in the allowed range
		value = conf.randomNumGenerator.nextDouble() * (range + 1) + min;
	}

	public void print(StringBuilder s) {
		// Need to override the default print implementation to include the
		// value that this instance represents. It is important that there is no
		// white space in the resulting
		// string appended as tokenising later will be by whitespace
		s.append(String.format("%sx%f", getName(), value));
	}

	@Override
	public RandomDouble getNew(GPConfig config) {
		// Create a new RandomDouble. As it is in the same GP run as this one,
		// the min and max are still the same
		return new RandomDouble(min, max, config);
	}

	@Override
	public void evaluate(ReturnData out) {
		// Cast is safe as we specified what type we expect.
		// The result is simply the value we store
		((ReturnDouble) out).setValue(value);
	}

	public void init(Node n) {
		// This node has state and so needs to override the default blank init
		// to copy the originals state.
		RandomDouble v = (RandomDouble) n;
		// The only state is the value, so copy it over
		this.value = v.value;
	}

	public void reinit(GPConfig conf) {
		// Need to regenerate a new random value for this node to represent
		double range = max - min;
		// Generate a random number in the allowed range
		value = conf.randomNumGenerator.nextDouble() * (range + 1) + min;
	}

	@Override
	public RandomDouble generate(String s, GPConfig conf) {
		// Need to override the default generate from string as we also need to
		// read in the value we represent. The print() method defines how the
		// string is printed

		// First parse out the name
		String v = s.substring(getName().length() + 1);

		// Create a new node of the same type as this one. We pass in ourselves
		// so the factory knows what sort of node to return.
		RandomDouble res = new RandomDouble(min,max,conf);

		// Read in the value represented and assign it
		res.value = Double.parseDouble(v);

		// return the newly created object that represents the String
		return res;
	}

}
