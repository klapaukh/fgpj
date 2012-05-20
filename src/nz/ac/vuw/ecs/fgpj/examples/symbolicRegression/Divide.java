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
 * The division operator. In the case of division by zero, it has the result zero.
 * 
 * @author roma
 * 
 */
public class Divide extends Function {

	public Divide() {
		//Divide returns a ReturnDouble, has to children and is represented by "/"
		super(ReturnDouble.TYPENUM, 2, "/");
		for (int i = 0; i < numArgs; i++) {
			//Set the expected return type of each child to ReturnDouble.
			setArgNReturnType(i, ReturnDouble.TYPENUM);
		}
	}

	@Override
	public Divide getNew(GPConfig config) {
		//Return a new Divide
		return new Divide();
	}

	@Override
	public void evaluate(ReturnData out) {
		//cast is safe as we specified the type we expect
		ReturnDouble d = (ReturnDouble) out;

		//Evaluate the first subtree
		getArgN(0).evaluate(d);
		//save the value
		double d1 = d.value();
		//evaluate the second subtree
		getArgN(1).evaluate(d);
		
		//Set the right return value
		if (d.value() == 0) {
			//if the bottom part is zero, just return zero
			d.setValue(0);
		} else {
			//actually divide
			d.setValue(d1 / d.value());
		}
	}

}
