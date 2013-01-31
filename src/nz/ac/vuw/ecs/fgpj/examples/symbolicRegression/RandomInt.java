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
 * Represents a random int terminal value. Unlike a X or Y value, this always
 * returns the same value (which is decided upon its creation). Used for
 * constants
 * 
 * @author roma
 * 
 */
public class RandomInt extends Terminal {

	/**
	 * The value is the actual value this instance of a RandomInt will return.
	 * We can't just have it return an actual random number each time otherwise
	 * it will not be repeatable.
	 */
	private int value;

	/**
	 * The range of allowed values this RandomInt is allowed to produce
	 */
	private int min, max;

	/**
	 * Create a new RandomInt that will return random numbers within a specified
	 * range.
	 * 
	 * @param min
	 *            The minimum value that can be returned
	 * @param max
	 *            The maximum value that can be returned
	 * @param conf
	 *            The GPConfig for this set up
	 */
	public RandomInt(int min, int max, GPConfig conf) {
		super(ReturnDouble.TYPENUM, "RandomInt");
		this.min = min;
		this.max = max;

		int range = max - min;
		// Generate a random value in the given range
		value = conf.randomNumGenerator.nextInt(range + 1) + min;
	}

	public void print(StringBuilder s) {
		// This method has to override the default print method as it has to
		// print out what value it returns as well as its name
		// It is important that there are no spaces in the representation as it
		// will be read by whitespace
		s.append(String.format("%sx%d", getName(), value));
	}

	@Override
	public RandomInt getNew(GPConfig config) {
		// Make a new one. It is same to pass in your min and max as they are
		// part of the project config and this is still the same run.
		return new RandomInt(min, max, config);
	}

	@Override
	public void evaluate(ReturnData out) {
		// Cast is safe as we specify what we expect.
		// It simply return the value it represents
		((ReturnDouble) out).setValue(value);
	}

	public void init(Node n) {
		// When this node is cloned using the supertypes clone method it does
		// not get the same value. As such this is needed to set the value to
		// the same as the ReturnInt being cloned.
		RandomInt v = (RandomInt) n;
		this.value = v.value;
	}
	
	public void reinit(GPConfig conf){
		//Need to generate a new random Int for this node to represent
		int range = max - min;
		// Generate a random value in the given range
		value = conf.randomNumGenerator.nextInt(range + 1) + min;
	}

	@Override
	public RandomInt generate(String s, GPConfig conf) {
		// Overrides the supertype generate from string method. This is because
		// it also prints out the number that it represents. Therefore this code
		// also reads back the number it represents

		//We specified how it writes itself in the print() function.
		
		// parse out the name
		String v = s.substring(getName().length() + 1);

		// Create an new instance of a RandomInt. Gives itself to the factory so
		// the factory knows what kind of new node to construct. NB This may not
		// call getNew(), and so cannot be replaced that way
		RandomInt r = new RandomInt(min,max,conf);
		// Set the value of the new node to be the value held in the string
		r.value = Integer.parseInt(v);
		// return the new RandomInt that has been created
		return r;
	}

}
