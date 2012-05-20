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
 * Implements a Sin function node for GP with ReturnDoubles. It has one subtree
 * which must return a ReturnDouble
 * 
 * @author roma
 * 
 */
public class Sin extends Function {

	public Sin() {
		// This node returns a ReturnDouble, has one subtree and is called "Sin"
		super(ReturnDouble.TYPENUM, 1, "Sin");
		for (int i = 0; i < numArgs; i++) {
			//Set the expected return value of each child to ReturnDouble 
			setArgNReturnType(i, ReturnDouble.TYPENUM);
		}
	}

	@Override
	public Sin getNew(GPConfig config) {
		//Return a new Sin
		return new Sin();
	}

	@Override
	public void evaluate(ReturnData out) {
		//Cast is safe as we specified our return type
		ReturnDouble d = (ReturnDouble) out;
		//Evaluate the subtree
		getArgN(0).evaluate(d);
		//Set the result to be sin(subtreeResult)
		d.setValue(Math.sin(d.value()));
	}

}
