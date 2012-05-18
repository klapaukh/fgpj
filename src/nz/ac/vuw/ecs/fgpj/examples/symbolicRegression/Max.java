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
		super(ReturnDouble.TYPENUM, 2, "Max");
		for (int i = 0; i < numArgs; i++) {
			setArgNReturnType(i, ReturnDouble.TYPENUM);
		}
	}

	@Override
	public Max getNew(GPConfig config) {
		return new Max();
	}

	@Override
	public void evaluate(ReturnData out) {
		ReturnDouble d = (ReturnDouble) out;
		getArgN(0).evaluate(d);
		double d1 = d.value();
		getArgN(1).evaluate(d);
		d.setValue(Math.max(d.value(), d1));
	}

}
