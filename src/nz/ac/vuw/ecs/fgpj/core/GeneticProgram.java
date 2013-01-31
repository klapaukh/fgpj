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
import java.util.Scanner;

/**
 * This is a GeneticProgram. It is a forest, where the number of trees is set in the GPConfig.
 * 
 * @author Roman Klapaukh
 * 
 */

public class GeneticProgram {

	/**
	 * The fitness of this program
	 */
	private double fitness;

	/**
	 * Fitness of the program before a genetic operator was applied
	 */
	private double lastFitness;

	/**
	 * Depth of this program
	 */
	private final int[] depth;

	/**
	 * Size of this program
	 */
	private final int[] size;

	/**
	 * Return type of this program
	 */
	private final int[] returnType;

	/**
	 * The root node of the program tree
	 */
	private final Node[] root;

	/** Number of parts **/
	private final int numRoots;

	/**
	 * Stores the number of elitisms used to get this program
	 */
	private long numElitisms;

	/**
	 * Stores the number of crossovers that were used to get this program along one path
	 */
	private long numCrossovers;

	/**
	 * Stores the number mutations that occurred to get this program along one path
	 */
	private long numMutations;

	/**
	 * Number of generations since this was not selected by elitism along one path
	 */
	private long lastChange;

	/**
	 * A place holder to show what the last GP operator was to make this program
	 */
	private int lastOperation;

	/**
	 * Constant to say that lastOperatoin was crossover
	 */
	public static final int CROSSOVER = 1;

	/**
	 * Constant to say that lastOperatoin was mutation
	 */
	public static final int MUTATION = 2;

	/**
	 * Constant to say that lastOperatoin was elitism
	 */
	public static final int ELITISM = 3;

	/**
	 * Create a new genetic program based on conf
	 * 
	 * @param conf
	 *            the config to use
	 */
	public GeneticProgram(GPConfig conf) {
		this(conf.getNumRoots());
	}

	/**
	 * Create a new genetic program with numParts roots
	 * 
	 * @param numRoots
	 *            number of root nodes
	 */
	public GeneticProgram(int numRoots) {
		this.lastOperation = 0;
		this.lastChange = 0;
		this.numCrossovers = 0;
		this.numMutations = 0;
		this.numElitisms = 0;
		this.numRoots = numRoots;
		fitness = Double.NaN;
		returnType = new int[numRoots];
		root = new Node[numRoots];
		depth = new int[numRoots];
		size = new int[numRoots];
		for (int i = 0; i < numRoots; i++) {
			returnType[i] = -1;
		}

	}

	/**
	 * The evaluate method evaluates (executes) this program
	 * 
	 * @param out
	 *            a pointer to the return value
	 */
	public void evaluate(ReturnData out[]) {
		for (int i = 0; i < numRoots; i++) {
			root[i].evaluate(out[i]);
		}
	}

	/**
	 * Sets the root of the tree to point to the new tree value. This does not delete the nodes
	 * 
	 * @param value
	 *            the new root node
	 * @param place
	 *            the number root it is
	 */
	public void setRoot(Node value, int place) {
		root[place] = value;
		value.setParent(null);
		computeSizeAndDepth(place);
	}

	/**
	 * Get the place root node
	 * 
	 * @param place
	 *            index of root to get
	 * @return the root at place
	 */
	public Node getRoot(int place) {
		return root[place];
	}

	/**
	 * Number of root nodes this program has
	 * 
	 * @return number of root nodes
	 */
	public int numRoots() {
		return numRoots;
	}

	/**
	 * Delete the current tree. Returns it to the node factory and sets the root to null
	 * 
	 * @param place
	 *            which root to delete
	 */
	public void deleteTree(int place) {
		deleteTree(root[place]);
		root[place] = null;
	}

	/**
	 * Recursively deletes the tree whose root is pointed to by theRoot. 
	 * 
	 * @param theRoot
	 *            root node to delete from
	 */
	public void deleteTree(Node theRoot) {
		theRoot.delete();
	}

	/**
	 * Get the depth of a tree
	 * 
	 * @param place
	 *            which tree to get the depth of
	 * @return the depth of the place tree
	 */
	public int getDepth(int place) {
		return depth[place];
	}

	/**
	 * Gets the size of the place tree
	 * 
	 * @param place
	 *            which tree to get the size of
	 * @return the size of the tree
	 */
	public int getSize(int place) {
		return size[place];
	}

	/**
	 * Computes the size and depth of each of the trees
	 */
	public void computeSizeAndDepth() {
		for (int i = 0; i < numRoots; i++) {
			size[i] = root[i].computeSize();
			depth[i] = root[i].computeDepth(0);
			root[i].computePositions(-1);
		}
	}

	/**
	 * Computes the size and depth of a given tree
	 * 
	 * @param place
	 *            which tree to compute
	 */
	public void computeSizeAndDepth(int place) {
		size[place] = root[place].computeSize();
		depth[place] = root[place].computeDepth(0);
		root[place].computePositions(-1);
	}

	/**
	 * Gets a random node from the program tree
	 * 
	 * @param p
	 *            which subtree to select from
	 * @param config
	 *            the config to use for random numbers
	 * @return a random node in the subtree
	 */
	public Node getRandomNode(int p, GPConfig config) {
		int node = Math.abs(config.randomNumGenerator.nextInt() % size[p]);
		Node n = root[p].getNode(node);
		return n;
	}

	/**
	 * Get a random node from the tree that has type typeNum
	 * 
	 * @param typeNum
	 *            The return type of the node
	 * @param p
	 *            the subtree to select from
	 * @param config
	 *            the config to use
	 * @return a random node from the tree p with type typeNum or null if there is none
	 * 
	 */
	public Node getRandomNode(int typeNum, int p, GPConfig config) {
		int node = Math.abs(config.randomNumGenerator.nextInt() % size[p]);
		Node n = root[p].getNode(node, typeNum);
		return n;
	}

	/**
	 * Prints the program to a string buffer. The program can be reconstructed from this string
	 * 
	 * @param s
	 *            the StringBuffer to print to
	 */
	public void print(StringBuilder s) {
		s.append(numRoots);
		s.append(" ");
		for (int i = 0; i < numRoots; i++) {
			s.append("Program");
			s.append(i);
			s.append(" ");
			root[i].print(s);
			s.append(" | ");
		}

	}

	/**
	 * Set the return type for a particular root node
	 * 
	 * @param root
	 *            the root whose return type will be se
	 * @param type
	 *            the return type of the root
	 */
	public void setReturnType(int root, int type) {
		returnType[root] = type;
	}

	/**
	 * Get the return type of a given root
	 * 
	 * @param root
	 *            which root to get the return type for
	 * @return the return type of the root
	 */
	public int getReturnType(int root) {
		return returnType[root];
	}

	/**
	 * Set the fitness. Assumes the fitness should be positive
	 * 
	 * @param f
	 *            the fitness of the node
	 */
	public void setFitness(double f) {
		fitness = f;
	}

	/**
	 * Get the fitness of this program
	 * 
	 * @return this program's fitness
	 */
	public double getFitness() {
		return fitness;
	}

	/**
	 * Return the number of mutations along one path of the tree that occurred to produce this program
	 * 
	 * @return number of mutations
	 */
	public long numMutations() {
		return numMutations;
	}

	/**
	 * Return the number of crossovers along one path of the tree that occurred to produce this program
	 * 
	 * @return number of crossovers
	 */
	public long numCrossovers() {
		return numCrossovers;
	}

	/**
	 * Return the number of elitisms along one path of the tree that were used to create this program
	 * 
	 * @return number of elitisms
	 */
	public long numElitisms() {
		return numElitisms;
	}

	/**
	 * Number of generations that this program has been continuously selected for by elitism
	 * 
	 * @return Number of generations that this program has not changed for
	 */
	public long lastChange() {
		return lastChange;
	}

	/**
	 * Set the number mutations along one path of the tree used to produce this program
	 * 
	 * @param num
	 *            number of mutations
	 */
	public void setNumMutations(long num) {
		this.numMutations = num;
	}

	/**
	 * Set the number of crossover along one path of the tree used to get this program
	 * 
	 * @param num
	 *            number of crossovers
	 */
	public void setNumCrossovers(long num) {
		this.numCrossovers = num;
	}

	/**
	 * Set the number of elitisms along one path of the tree used to produce this program
	 * 
	 * @param num
	 *            number of elitismss
	 */
	public void setNumElitisms(long num) {
		this.numElitisms = num;
	}

	/**
	 * Says that the program has been modified to be created. Not selected by elitism
	 */
	public void resetLastChange() {
		this.lastChange = 0;
	}

	/**
	 * Increments the last change counter to show that this program was selected by elitism
	 */
	public void incrementLastChange() {
		this.lastChange++;
	}

	/**
	 * Set what the last operation to get this program was. One of the constants CROSSOVER, MUTATION, ELITISM
	 * 
	 * @param lastOperator
	 *            the last operation used to generate this program
	 */
	public void setLastOperator(int lastOperator) {
		this.lastOperation = lastOperator;
	}

	/**
	 * Get what the last operation on this program was
	 * 
	 * @return the last operation on this program
	 */
	public int lastOperation() {
		return this.lastOperation;
	}

	/**
	 * Returns the fitness before a genetic operator was applied
	 * 
	 * @return last fitness
	 */
	public double lastFitness() {
		return lastFitness;
	}

	/**
	 * Set what the fitness of the parent was
	 * 
	 * @param fit
	 *            parents fitness
	 */
	public void setLastFitness(double fit) {
		this.lastFitness = fit;
	}

	/**
	 * The copy method will create a new genetic program which is a copy of the current program
	 * 
	 * @param config
	 *            the config to create with
	 * @return a clone of the current GeneticProgram
	 */
	public GeneticProgram copy(GPConfig config) {
		GeneticProgram tmp = new GeneticProgram(numRoots);
		tmp.setFitness(fitness);
		tmp.numCrossovers = this.numCrossovers;
		tmp.numElitisms = this.numElitisms;
		tmp.numMutations = this.numMutations;
		tmp.lastChange = this.lastChange;

		for (int i = 0; i < numRoots; i++) {
			tmp.setReturnType(i, returnType[i]);
		}

		for (int i = 0; i < numRoots; i++) {
			Node rootTmp = root[i].copy(config);
			tmp.setRoot(rootTmp, i);
		}
		tmp.computeSizeAndDepth();

		return tmp;
	}

	/**
	 * Read in a program from a string and make this that program
	 * 
	 * @param programString
	 *            The string specifying the program
	 * @param config
	 *            the config used to build the program
	 */
	public void parseProgram(String programString, GPConfig config) {

		try {
			Scanner scan = new Scanner(programString);
			Node[] tmpRoot = buildTree(scan, config);
			for (int i = 0; i < numRoots; i++) {
				deleteTree(i);
				setRoot(tmpRoot[i], i);
			}
		} catch (Exception e) {
			System.err.println("GeneticProgram::parseProgram\nError program: ");
			System.err.println(programString);
			e.printStackTrace();
			throw new RuntimeException("Parsing failed");
		}

	}

	private Node[] buildTree(Scanner scan, GPConfig config) {
		Node[] tmpRoot = null;
		Node arg = null;

		String token = scan.next();

		// token should be a number
		int numParts = Integer.parseInt(token);
		tmpRoot = new Node[numParts];

		token = scan.next();
		for (int i = 0; i < numParts; i++) {
			int count = 0;

			if (token.equalsIgnoreCase("|")) {
				// End of Program
				if (scan.hasNext())
					token = scan.next();
			}

			if (token.startsWith("Program")) {
				token = scan.next();
			}

			if (token.equalsIgnoreCase("(")) // Start of function
			{
				token = scan.next();
				tmpRoot[i] = config.funcSet.generateNodeByName(token, config);
				token = scan.next();
				while (!token.equals(")")) {
					arg = buildTreeNode(scan, token, config);
					((Function) (tmpRoot[i])).setArgN(count, arg);
					count++;
					token = scan.next();
				}
				token = scan.next();
			} else // Must be terminal
			{
				tmpRoot[i] = config.termSet.generateNodeByName(token, config);
				token = scan.next(); // read extra |
			}
		}

		// If we get an error it will usually be because of a terminal
		// or function being missing from the nodesets.

		return tmpRoot;
	}

	private Node buildTreeNode(Scanner scan, String token, GPConfig config) {
		Node tmpRoot = null;
		Node arg = null;

		int count = 0;

		if (token.equalsIgnoreCase("|")) {
			System.err.println("Probably an error");
			/* End of program */
		} else if (token.equalsIgnoreCase("(")) // Start of function
		{
			token = scan.next();
			tmpRoot = config.funcSet.generateNodeByName(token, config);
			token = scan.next();
			while (!token.equals(")")) {
				arg = buildTreeNode(scan, token, config);
				((Function) (tmpRoot)).setArgN(count, arg);
				count++;
				token = scan.next();
			}
		} else // Must be terminal
		{
			tmpRoot = config.termSet.generateNodeByName(token, config);
		}

		// If we get an error it will usually be because of a terminal
		// or function being missing from the nodesets.

		return tmpRoot;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		this.print(s);
		return s.toString();
	}

}
