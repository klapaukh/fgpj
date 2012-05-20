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
 * This class represents a single input value called X. It does not store the
 * value it returns directly and takes its value from the return double being
 * passed though. This is because this is a fast mechanism for doing this that
 * is also thread safe allowing us to use the ParallelFitness function.
 * 
 * @author roma
 * 
 */
public class X extends Terminal {

	public X() {
		//We return a ReturnDouble and print ourselves as "X"
		super(ReturnDouble.TYPENUM, "X");
	}

	@Override
	public X getNew(GPConfig config) {
		//calls own constructor
		return new X();
	}

	@Override
	public void evaluate(ReturnData out) {
		//Safely can case to ReturnDouble as we specified that is what we expect
		ReturnDouble d = (ReturnDouble) out;
		//We just return the value give to use by the ReturnDouble 
		d.setValue(d.getX());
	}

}
