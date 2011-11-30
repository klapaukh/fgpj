package nz.ac.vuw.ecs.fgpj.core;
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
/**
 * ReturnData represents values and their types in the program tree. Each type is assigned an unique integer
 * 
 * @author Roman Klapaukh
 * 
 */
public abstract class ReturnData {

	private int typeNum;

	/**
	 * Create a new Return data with type num
	 * 
	 * @param num
	 *            the type of this ReturnData
	 */
	public ReturnData(int num) {
		this.typeNum = num;
	}

	/**
	 * Get the type of this ReturnData
	 * 
	 * @return the type
	 */
	public int getTypeNum() {
		return typeNum;
	}

}
