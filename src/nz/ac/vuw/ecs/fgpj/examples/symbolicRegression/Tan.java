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
 * Implements the Tan function for ReturnDoubles. It computes the tangent of the
 * one subtree it has which returns a ReturnDouble. Note that tangent is
 * discontinuous
 * 
 * @author roma
 * 
 */
public class Tan extends Function {

	public Tan() {
		// This Function returns a Return double, has one child and is written
		// "Tan"
		super(ReturnDouble.TYPENUM, 1, "Tan");
		for (int i = 0; i < numArgs; i++) {
			// for each child, set the return type of the child to be
			// ReturnDouble
			setArgNReturnType(i, ReturnDouble.TYPENUM);
		}
	}

	@Override
	public Tan getNew(GPConfig config) {
		// Return a new Tan
		return new Tan();
	}

	@Override
	public void evaluate(ReturnData out) {
		// Cast is safe as we specified we are expecting this
		ReturnDouble d = (ReturnDouble) out;
		// Evaluate the subtree
		getArgN(0).evaluate(d);
		// Set the result to being tan(subtree)
		d.setValue(Math.tan(d.value()));
	}

}
