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

import nz.ac.vuw.ecs.fgpj.core.Function;
import nz.ac.vuw.ecs.fgpj.core.GPConfig;
import nz.ac.vuw.ecs.fgpj.core.ReturnData;

/**
 * Returns the max of two subtrees.
 * 
 * @author roma
 * 
 */
public class Max extends Function {

	public Max() {
		// A max returns a ReturnDouble, has two children and is called "Max"
		super(ReturnDouble.TYPENUM, 2, "Max");
		// Set the expected return type of each child to being ReturnDouble
		for (int i = 0; i < numArgs; i++) {
			setArgNReturnType(i, ReturnDouble.TYPENUM);
		}
	}

	@Override
	public Max getNew(GPConfig config) {
		// Get a new Max
		return new Max();
	}

	@Override
	public void evaluate(ReturnData out) {
		// Cast is safe as we say what we expect in the constructor
		ReturnDouble d = (ReturnDouble) out;
		//Evaluate the first subtree
		getArgN(0).evaluate(d);
		//save the  result
		double d1 = d.value();
		//evaluate the second subtree
		getArgN(1).evaluate(d);
		// return the max of the two subtrees
		d.setValue(Math.max(d.value(), d1));
	}

}
