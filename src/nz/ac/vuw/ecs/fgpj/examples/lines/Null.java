package nz.ac.vuw.ecs.fgpj.examples.lines;

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
 * This is a null drawing node. It serves as the drawing terminal so that the
 * tree doesn't have to have infinite size.
 * 
 * @author roma
 * 
 */
public class Null extends Terminal {

	public static final long serialVersionUID = 5747126155380314948L;

	/**
	 * Create a new null drawing node
	 */
	public Null() {
		// Takes a Return image and is called "null"
		super(ReturnImage.TYPENUM, "null");
	}

	public void evaluate(ReturnData out) {
		// Doesn't do anything, hence why it is a null node
	}

	@Override
	public Null getNew(GPConfig config) {
		// return a new null
		return new Null();
	}

}
