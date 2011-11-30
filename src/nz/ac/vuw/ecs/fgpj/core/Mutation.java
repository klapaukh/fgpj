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
 * The mutation operator. Extending it and overriding mutate allows users to create custom operators
 * 
 * @author Roman Klapaukh
 * 
 */
public class Mutation {
	
	/**
	 * Mutate a genetic program
	 * 
	 * @param gp The genetic program to mutate
	 * @param config config to use
	 */
	public void mutate(GeneticProgram gp, GPConfig config) {

		// Get a random node and the parent of that node
		// need to get parent index here as well
		int p = (int) Math.abs(config.randomNumGenerator.nextInt() % config.getNumRoots());
		Node tmp = gp.getRandomNode(p, config);
		Function parent = (Function) (tmp.getParent());

		// Generate a new tree to replace the old one
		Node newTree = config.programGenerator.createGrowProgram(tmp.getDepth(), config.maxDepth() ,
				tmp.getReturnType(), config);


		// If parent is not NULL then newTree is not at the root of
		// the tree
		if (parent != null) {
			int i;
			for (i = 0; i < parent.getNumArgs(); i++) {
				if (tmp == parent.getArgN(i)) break;
			}

			parent.setArgN(i, newTree);
		} else {
			// Need to use root index here
			gp.setRoot(newTree, p);
		}

		// Delete the old tree
		gp.deleteTree(tmp);
		// Recompute the size and depth
		gp.computeSizeAndDepth();
	}

}
