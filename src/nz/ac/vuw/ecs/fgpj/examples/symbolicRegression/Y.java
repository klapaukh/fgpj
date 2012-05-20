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
import nz.ac.vuw.ecs.fgpj.core.ReturnData;
import nz.ac.vuw.ecs.fgpj.core.Terminal;

/**
 * This class represents a terminal that gets its value from the input data. It
 * is exactly like X in this way, however it can hold a different value. Using X
 * and Y together allows for functions of two values to be evolved.
 * 
 * @author roma
 * 
 */
public class Y extends Terminal {

	public Y() {
		// Creating a new Y. It returns a ReturnDouble and it prints itself as
		// "Y"
		super(ReturnDouble.TYPENUM, "Y");
	}

	@Override
	public Y getNew(GPConfig config) {
		//Just calls the constructor for this type
		return new Y();
	}

	@Override
	public void evaluate(ReturnData out) {
		// The return data is the type that we expect as an input so we cast it.
		// This is safe as we specified what input type we expect in our
		// constructor
		ReturnDouble d = (ReturnDouble) out;
		//The result is just the value that the Y input is
		d.setValue(d.getY());
	}

}
