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
import java.awt.Color;

import nz.ac.vuw.ecs.fgpj.core.ReturnData;

/**
 * Return type to represent a color
 * 
 * @author roma
 * 
 */
public class ReturnColor extends ReturnData {
	/**
	 * Field to store the color this represents
	 */
	private Color col;

	/**
	 * Typenum is a unique number that represents the type of this return data
	 */
	public static final int TYPENUM = 3;

	/**
	 * Create a new ReturnColor
	 */
	public ReturnColor() {
		// notify supertype of what typenum this is
		super(TYPENUM);
	}

	/**
	 * Set the color this will return
	 * 
	 * @param c
	 *            The color to return
	 */
	public void setColor(Color c) {
		col = c;
	}

	/**
	 * Gets the stored color
	 * 
	 * @return The color that was previously assigned (null if it wasn't)
	 */
	public Color getColor() {
		return col;
	}

}
