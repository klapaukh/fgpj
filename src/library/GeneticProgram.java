package library;

import java.util.Scanner;
import java.util.Vector;

public class GeneticProgram {

	/**
	 * The fitness of this program
	 */
	private double fitness;

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
		this.numRoots = numRoots;
		fitness = 0.0;
		returnType = new int[numRoots];
		root = new Node[numRoots];
		depth = new int[numRoots];
		size = new int[numRoots];

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
		if (root[place] != null) {
			computeSizeAndDepth(place);
		}
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
	 * Recursively deletes the tree whose root is pointed to by theRoot. This returns all nodes in the subtree to the
	 * NodeFactory
	 * 
	 * @param theRoot
	 *            root node to delete from
	 */
	public void deleteTree(Node theRoot) {
		NodeFactory.delete(theRoot);
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
	 *  Gets a random node from the program tree
	 * @param p which subtree to select from
	 * @param config the config to use for random numbers
	 * @return a random node in the subtree
	 */
	public Node getRandomNode(int p, GPConfig config) {
		int node = config.randomNumGenerator.nextInt() % size[p];
		return root[p].getNode(node);
	}

	// Get a random node from the program tree that returns typeNum
	public Node getRandomNode(int typeNum, int p, GPConfig config) {
		Vector<Node> nodeList = new Vector<Node>();
		int index;
		int place = p;
		// int place = (int) (config.randomNumGenerator.randNum() % numParts);

		root[place].addTreeToVector(nodeList, typeNum);

		if (nodeList.size() != 0) {
			index = (int) Math.abs(config.randomNumGenerator.nextLong() % nodeList.size());
			return nodeList.get(index);
		} else return null;
	}

	public void print(StringBuffer s) {
		if (root == null) throw new RuntimeException("Root node is NULL");

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

	public void setReturnType(int root, int type) {
		returnType[root] = type;
	}

	public int getReturnType(int root) {
		return returnType[root];
	}

	public void setFitness(double f) {
		// If we get a fitness < 0 then there is a problem
		// this is usually caused by a double overflowing and
		// wrapping to a negative value
		if (f < 0.0) {
			System.err.println("GeneticProgram::setFitness : Warning: Fitness overflow " + f);
		} else fitness = f;
	}

	public double getFitness() {
		return fitness;
	}

	// The copy method will create a new genetic
	// program which is a copy of the current program
	public GeneticProgram copy(GPConfig config) {
		GeneticProgram tmp = new GeneticProgram(numRoots);

		Node rootTmp;

		// tmp.setFitness(fitness);
		// tmp.setAdjFitness(adjustFitness);
		for (int i = 0; i < numRoots; i++) {
			tmp.setReturnType(i, returnType[i]);
		}

		for (int i = 0; i < numRoots; i++) {
			if (root[i] != null) {
				rootTmp = root[i].copy(config);
				tmp.setRoot(rootTmp, i);
			} else {
				System.err.println("**** Warning GeneticProgram::copy() ******");
				System.err.println("Copying program with NULL root");

				tmp.setRoot(null, i);
			}
		}
		tmp.computeSizeAndDepth();

		return tmp;
	}

	/***************************************************************************
	 * parseProgram based on function parseTree originally written by Peter Wilson
	 **************************************************************************/
	public void parseProgram(String programString, GPConfig config) {
		Node[] tmpRoot = null;
		Scanner scan = new Scanner(programString);

		try {
			tmpRoot = buildTree(scan, config);
		} catch (Exception e) {
			System.err.println("GeneticProgram::parseProgram\nError program: ");
			System.err.println(programString);
			e.printStackTrace();
			throw new RuntimeException("Parsing failed");
		}

		for (int i = 0; i < numRoots; i++) {
			deleteTree(i);
			setRoot(tmpRoot[i], i);
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

			if (token.startsWith("Program")) {
				token = scan.next();
			}

			if (token.equalsIgnoreCase("|")) {
				// End of Program
			} else if (token.equalsIgnoreCase("(")) // Start of function
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
		StringBuffer s = new StringBuffer();
		this.print(s);
		return s.toString();
	}

}
