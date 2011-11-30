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
 * This is the default Crossover operator. Extending this class and overloading its crossover method allows for users to
 * define their own operator
 * 
 * @author Roman Klapaukh
 * 
 */
public class Crossover {

	/**
	 * Attempt to perform crossover on gp1 and gp2. The programs are modified in place, so copies should be used.
	 * Attempt at crossover may fail. Crossover may happen between trees with different root numbers.
	 * 
	 * @param gp1
	 *            Parent 1
	 * @param gp2
	 *            Parent 2
	 * @param numRetries
	 *            Number of tries to get successful crossover
	 * @param config
	 *            GPConfig to use
	 * @return True if crossover was successful
	 */
	public boolean crossover(GeneticProgram gp1, GeneticProgram gp2, int numRetries, GPConfig config) {
		Node point1 = null, point2 = null; // cross over points

		Function parent1 = null, parent2 = null; // parent of crossover point (null is root)

		boolean valid = false; // have we got a valid crossover

		int pos1 = -1, pos2 = -1;

		int depth1, depth2;
		int count = 0;

		// The following loop will continually try to find two valid crossover
		// points by selecting a node from each of the trees. It will try up
		// to numRetries times before giving up which will leave the
		// trees unchanged.
		int par1 = 0;
		int par2 = 0;
		while (!valid) {
			if (count >= numRetries) {
				return false;
			}

			point1 = point2 = null;

			par1 = Math.abs(config.randomNumGenerator.nextInt() % config.getNumRoots());
			par2 = Math.abs(config.randomNumGenerator.nextInt() % config.getNumRoots());

			// Get a random point in the tree of gp1
			point1 = gp1.getRandomNode(par1, config);
			parent1 = (Function) point1.getParent();

			// Get a random point in the tree which has the same return
			// type as point1
			point2 = gp2.getRandomNode(point1.getReturnType(), par2, config);

			// If there is no such node with the same return type then stop
			// and try again.
			if (point2 == null) {
				// Can't find valid node
				continue;
			}

			parent2 = (Function) point2.getParent();

			depth1 = parent1 == null ? 0 : parent1.getDepth();
			depth2 = parent2 == null ? 0 : parent2.getDepth();

			// Here we check that the crossover will produce trees which
			// do not violate the maximum and minimum depth
			int newDepth1 = depth1 + point2.traceDepth(0);
			int newDepth2 = depth2 + point1.traceDepth(0);
			if ((newDepth1 <= config.maxDepth()) && (newDepth2 <= config.maxDepth())) {
				valid = true;
			}
			count++;
		}

		// If we get to this point we have a valid pair of nodes to perform
		// crossover with

		// Here we find which child position point1 is in it's
		// parent node.
		if (parent1 != null) {
			for (pos1 = 0; pos1 < parent1.getNumArgs(); pos1++) {
				if (point1 == parent1.getArgN(pos1)) break;
			}
		}

		// Here we find which child position point2 is in it's
		// parent node.
		if (parent2 != null) {
			for (pos2 = 0; pos2 < parent2.getNumArgs(); pos2++) {
				if (point2 == parent2.getArgN(pos2)) break;
			}
		}

		if (pos1 >= 0) {
			// If point1 was not at the root of the tree
			// replace point1 with point2
			// update point2s parent node
			parent1.setArgN(pos1, point2);
		} else {
			// If point1 was at the root then make point2
			// the new root of gp1
			gp1.setRoot(point2, par1);
		}

		if (pos2 >= 0) {
			// If point1 was not at the root of the tree
			// replace point1 with point2
			// update point2s parent node
			parent2.setArgN(pos2, point1);
		} else {
			// If point1 was at the root then make point2
			// the new root of gp1
			gp2.setRoot(point1, par2);
		}

		// Force the updated programs to update their size and depth
		// information
		gp1.computeSizeAndDepth();
		gp2.computeSizeAndDepth();
		return true;
	}
}
