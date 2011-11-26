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
	
	/**Number of parts**/
	private final int numRoots;

	public GeneticProgram(GPConfig conf) {
		this(conf.getNumParts());
	}
	
	
	public GeneticProgram(int numParts) {
		this.numRoots = numParts;
		fitness = 0.0;
		returnType = new int[numParts];
		root = new Node[numParts];
		depth = new int[numParts];
		size = new int[numParts];

	}

	// The evaluate method evaluates(executes) this program
	public void evaluate(ReturnData out[]) {
		for (int i = 0; i < numRoots; i++) {
			if (out[i].getTypeNum() != root[i].getReturnType()) {
				// int j = out[i].getTypeNum();
				throw new RuntimeException("Return type of root node does not match return type of out");
			}
		}

		for (int i = 0; i < numRoots; i++) {
			root[i].evaluate(out[i]);
		}
	}

	// Sets the root of the tree to point to the new tree value.
	// If the old tree is no longer to be used it must be deleted
	// explicitly by calling deleteTree() below.
	public void setRoot(Node value, int place) {
		if (place < 0 || place >= numRoots) {
			throw new IllegalArgumentException("Invalid place value");
		}
		root[place] = value;

		if (root[place] != null) computeSizeAndDepth(place);
	}

	public Node getRoot(int place) {
		if (place < 0 || place >= numRoots) throw new IllegalArgumentException("Invalid place value");
		return root[place];
	}

	// Delete the current tree.
	public void deleteTree(int place) {
		if (place < 0 || place >= numRoots) throw new IllegalArgumentException("Invalid place of value");
		deleteTree(root[place]);
		root[place] = null;
	}

	// Recursively deletes the tree whose root is pointed to by theRoot.
	public void deleteTree(Node theRoot) {
		NodeFactory.delete(theRoot);
	}

	public int getDepth(int place) {
		return depth[place];
	}

	public int getSize(int place) {
		return size[place];
	}

	public void computeSizeAndDepth() {
		for (int i = 0; i < numRoots; i++) {
			if (root[i] == null) {
				throw new RuntimeException("Root " + i + " is null");
			}
			size[i] = root[i].computeSize();
			depth[i] = root[i].computeDepth(0);
		}
	}

	public void computeSizeAndDepth(int place) {
		if (place < 0 || place >= numRoots) {
			throw new RuntimeException("Invalid place value: " + place);
		}

		if (root[place] == null) throw new RuntimeException("Root " + place + " is null");

		size[place] = root[place].computeSize();
		depth[place] = root[place].computeDepth(0);
	}

	// Gets a random node from the program tree
	public Node getRandomNode(int p, GPConfig config) {
		if (p < 0 || p >= numRoots) {
			throw new RuntimeException("Invalid p value: " + p);
		}
		Vector<Node> nodeList = new Vector<Node>();
		int place = p;
		// int place = (int) (config.randomNumGenerator.randNum() % numParts);
		// p = place;

		root[place].addTreeToVector(nodeList);

		if (nodeList.size() != 0) {
			int index = (int) Math.abs(config.randomNumGenerator.nextLong() % nodeList.size());
			return nodeList.get(index);
		} else return null;
	}

	// Get a random node from the program tree that returns typeNum
	public Node getRandomNode(int typeNum, int p, GPConfig config) {
		if (p < 0 || p >= numRoots) {
			throw new RuntimeException("Invalid p value: " + p);
		}
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
					arg = buildTreeNode(scan, token,config);
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
