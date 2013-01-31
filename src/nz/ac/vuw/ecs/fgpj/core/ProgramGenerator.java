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
import java.util.List;

/**
 * Generates programs. Can generate both grow and full programs. Default behaviour is generate 50-50 but can be changed
 * 
 * @author Roman Klapaukh
 * 
 */
public class ProgramGenerator {
	private NodeVector<Node> growTable[];

	private NodeVector<Node> fullTable[];

	public ProgramGenerator() {
	}

	/**
	 * Generate the initial population. They have depth between mindepth and maxdepth
	 * 
	 * @param pop
	 *            The list of programs to set trees for
	 * @param expectedReturnType
	 *            the array of return types for each root
	 * @param config
	 *            the config
	 */
	public void generateInitialPopulation(List<GeneticProgram> pop, int expectedReturnType[], GPConfig config) {
		generateTables(config);
		if (config.minDepth() > config.maxDepth()) throw new RuntimeException("minDepth is greater than maxDepth");
		if (pop.size() > 1) {
			GeneticProgram prog = pop.get(0);
			for (int i = 0; i < config.getNumRoots(); i++) {
				if (prog.getReturnType(i) == -1) {
					throw new IllegalStateException("Return types for population not set");
				}
			}
		}

		int numIndividualsForRamping = pop.size() / 2;
		int numSizes = config.maxDepth() - config.minDepth() + 1;
		int sizeInc = numIndividualsForRamping < numSizes ? numSizes
				/ (numIndividualsForRamping != 0 ? numIndividualsForRamping : 1) : 1;
		int minSize = numIndividualsForRamping > numSizes ? config.minDepth() : config.maxDepth()
				- (numIndividualsForRamping - 1);

		int indiv;
		if (minSize < config.minDepth()) {
			throw new RuntimeException("minSize < minDepth");
		}

		int maxSize = minSize;
		for (indiv = 0; indiv < numIndividualsForRamping; indiv++) {
			if (maxSize < config.maxDepth() && (indiv % sizeInc) == 0) {
				maxSize++;
			}

			for (int i = 0; i < config.getNumRoots(); i++) {
				Node tmp = createGrowProgram(1, maxSize, pop.get(indiv).getReturnType(i), config);
				pop.get(indiv).setRoot(tmp, i);
				pop.get(indiv).computeSizeAndDepth(i);
				if (pop.get(indiv).getDepth(i) < config.minDepth() || pop.get(indiv).getDepth(i) > config.maxDepth()) {
					i--;
				}
			}
		}

		maxSize = minSize;
		for (; indiv < pop.size(); indiv++) {
			if (maxSize < config.maxDepth() && (indiv % sizeInc) == 0) {
				maxSize++;
			}
			for (int i = 0; i < config.getNumRoots(); i++) {
				Node tmp = createFullProgram(1, config.maxDepth(), pop.get(indiv).getReturnType(i), config);
				pop.get(indiv).setRoot(tmp, i);
				pop.get(indiv).computeSizeAndDepth(i);
				if (pop.get(indiv).getDepth(i) < config.minDepth() || pop.get(indiv).getDepth(i) > config.maxDepth()) {
					i--;
				}
			}
		}
	}

	/**
	 * Create a program from curDept to maxDepth of full size
	 * 
	 * @param curDepth
	 *            Current depth
	 * @param maxDepth
	 *            max program depth
	 * @param expectedReturnType
	 *            return type of this subtree
	 * @param config
	 *            config
	 * @return Node that fits the requested description
	 */
	public Node createFullProgram(int curDepth, int maxDepth, int expectedReturnType, GPConfig config) {
		int depth = maxDepth - curDepth;
		Node node = fullTable[depth].generateRandomNode(expectedReturnType, config);

		if (node == null) {
			System.err.println("Warning, unable to create Full program for this set of Functions and Terminals");
			return createGrowProgram(curDepth, maxDepth, expectedReturnType, config);
		}

		if (node.getNumArgs() > 0) {
			Function func = (Function) (node);

			for (int i = 0; i < func.getNumArgs(); i++) {
				func.setArgN(i, createFullProgram(curDepth + 1, maxDepth, func.getArgNReturnType(i), config));
			}
		}

		return node;
	}

	/**
	 * Grow a program from curDept to maxDepth of random size
	 * 
	 * @param curDepth
	 *            Current depth
	 * @param maxDepth
	 *            max program depth
	 * @param expectedReturnType
	 *            return type of this subtree
	 * @param config
	 *            config
	 * @return Node that fits the requested description
	 */
	public Node createGrowProgram(int curDepth, int maxDepth, int expectedReturnType, GPConfig config) {
		int depth = maxDepth - curDepth;
		Node node = growTable[depth].generateRandomNode(expectedReturnType, config);

		if (node.getNumArgs() > 0) {
			Function func = (Function) (node);

			for (int i = 0; i < node.getNumArgs(); i++) {
				func.setArgN(i, createGrowProgram(curDepth + 1, maxDepth, func.getArgNReturnType(i), config));
			}
		}
		return node;
	}

	/**
	 * Generate the tables used for making programs
	 * 
	 * @param config
	 *            config to use
	 */
	@SuppressWarnings("unchecked")
	private void generateTables(GPConfig config) {

		int numFunctions = config.funcSet.size();
		int numTerminals = config.termSet.size();

		int maxDepth = config.maxDepth();

		growTable = (NodeVector<Node>[]) new NodeVector[maxDepth];
		fullTable = (NodeVector<Node>[]) new NodeVector[maxDepth];

		for (int i = 0; i < maxDepth; i++) {
			growTable[i] = new NodeVector<Node>();
			fullTable[i] = new NodeVector<Node>();
		}

		// Add in the terminals at the top of the matrix, this is the top of the tree
		for (int i = 0; i < numTerminals; i++) {
			Node n = config.termSet.generate(i, config);

			growTable[0].add(n);
			fullTable[0].add(n);
		}

		// grow table creation, by level
		for (int curDepth = 1; curDepth < maxDepth; curDepth++) {

			// Add the terminals - they can be at any level
			for (int i = 0; i < numTerminals; i++) {
				Node n = config.termSet.generate(i, config);
				growTable[curDepth].add(n);
			}

			// Add the functions
			for (int i = 0; i < numFunctions; i++) {
				// Try every function
				Function tmpFunc = config.funcSet.generate(i, config);
				boolean valid = true;

				// For each of its arguments
				for (int arg = 0; valid && arg < tmpFunc.getNumArgs(); arg++) {
					boolean found = false;
					int argNReturnType = tmpFunc.getArgNReturnType(arg);

					// Can you find something in the level below to attach it to
					for (int tSize = 0; !found && tSize < growTable[curDepth - 1].size(); tSize++) {
						Node tmpNode = growTable[curDepth - 1].generate(tSize, config);

						if (argNReturnType == tmpNode.getReturnType()) {
							found = true;
						}
						tmpNode.delete();
					}

					if (!found) {
						valid = false;
					}
				}

				if (valid) {
					Node n = config.funcSet.generate(i, config);
					growTable[curDepth].add(n);
				}
			}
		}

		// full table creation
		for (int curDepth = 1; curDepth < maxDepth; curDepth++) {
			// Add the functions
			for (int i = 0; i < numFunctions; i++) {
				Function tmpFunc = config.funcSet.generate(i, config);
				boolean valid = true;

				for (int arg = 0; valid && arg < tmpFunc.getNumArgs(); arg++) {
					boolean found = false;
					int argNReturnType = tmpFunc.getArgNReturnType(arg);

					for (int tSize = 0; !found && tSize < fullTable[curDepth - 1].size(); tSize++) {
						Node tmpNode = fullTable[curDepth - 1].generate(tSize, config);

						if (argNReturnType == tmpNode.getReturnType()) {
							found = true;
						}
						tmpNode.delete();
					}

					if (!found) {
						valid = false;
						break;
					}
				}

				if (valid) {
					Node n = config.funcSet.generate(i, config);
					fullTable[curDepth].add(n);
				}
			}
			if (fullTable[curDepth].size() == 0) {
				// add functions, but now allow for terminal to appear
				for (int i = 0; i < numFunctions; i++) {
					Function tmpFunc = config.funcSet.generate(i, config);
					boolean valid = true;

					for (int arg = 0; valid && arg < tmpFunc.getNumArgs(); arg++) {
						boolean found = false;
						int argNReturnType = tmpFunc.getArgNReturnType(arg);

						for (int tSize = 0; !found && tSize < fullTable[curDepth - 1].size(); tSize++) {
							Node tmpNode = fullTable[curDepth - 1].generate(tSize, config);

							if (argNReturnType == tmpNode.getReturnType()) {
								found = true;
							}
							tmpNode.delete();
						}

						if (!found) {
							// OK, we are desperate, give us a terminal
							for (int tSize = 0; !found && tSize < fullTable[0].size(); tSize++) {
								Node tmpNode = fullTable[0].generate(tSize, config);

								if (argNReturnType == tmpNode.getReturnType()) {
									found = true;
									fullTable[curDepth - 1].add(tmpNode);
								} else {
									tmpNode.delete();
								}
							}
						}

						if (!found) {
							valid = false;
							break;
						}
					}

					if (valid) {
						Node n = config.funcSet.generate(i, config);
						fullTable[curDepth].add(n);
					}
				}
			}
		}

	}
}
