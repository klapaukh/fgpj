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
 * Represents subtraction. Returns the difference between two subtrees
 * 
 * @author roma
 * 
 */
public class Minus extends Function {

	public Minus() {
		//Minus returns a return double, has two children and is represented by a "-"
		super(ReturnDouble.TYPENUM, 2, "-");
		for (int i = 0; i < numArgs; i++) {
			//set the return type of each child to ReturnDouble
			setArgNReturnType(i, ReturnDouble.TYPENUM);
		}
	}

	@Override
	public Minus getNew(GPConfig config) {
		//return a new minus instance
		return new Minus();
	}

	@Override
	public void evaluate(ReturnData out) {
		//cast is safe as we specified what type we expected 
		ReturnDouble d = (ReturnDouble) out;
		
		//evaluate the first subtree
		getArgN(0).evaluate(d);
		//save the result
		double d1 = d.value();
		//evaluate the second subtree
		getArgN(1).evaluate(d);
		//Set the result to being the difference
		d.setValue(d1 - d.value());
	}

}
