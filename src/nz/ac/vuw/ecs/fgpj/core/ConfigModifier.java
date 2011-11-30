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
 * An interface to change the GP as it is running
 * 
 * @author Roman Klapaukh
 * 
 */
public interface ConfigModifier {

	/**
	 * Perform the change. Changing the number of root nodes in a program or the max depth of the program tree may result in serious errors. The
	 * population is sorted by fitness with pop.get(0), being the individual with the lowest fitness.
	 * 
	 * @param g
	 *            the config
	 * @param pop
	 *            the population
	 */
	public abstract void ModifyConfig(GPConfig g, Population pop);
}
