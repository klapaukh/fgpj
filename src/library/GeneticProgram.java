package library;

import java.util.Scanner;
import java.util.Vector;

public class GeneticProgram {
	private GPConfig config; // Configuration object for this program

	private int programID; // ID number of this program

	private double fitness; // The fitness of this program

	private double adjustFitness; // The adjusted fitness

	private int[] depth; // Depth of this program

	private int[] size; // Size of this program

	private int returnType; // Return type of this program

	private int numParts; // The number of programs this program contains

	private Node[] root; // The root node of the program tree

	public GeneticProgram(GPConfig conf) {
		config = conf;
		programID = 0;
		fitness = 0.0;
		returnType = -1;
		numParts = conf.getNumParts();
		root = new Node[numParts];
		for (int i = 0; i < numParts; i++)
			root[i] = null;
		depth = new int[numParts];
		size = new int[numParts];
	}

	public GeneticProgram(GeneticProgram g) {

		config = g.config;
		programID = g.programID;
		fitness = g.fitness;
		adjustFitness = g.adjustFitness;
		returnType = g.returnType;
		numParts = g.numParts;
		root = new Node[numParts];
		depth = new int[numParts];
		size = new int[numParts];
		for (int i = 0; i < numParts; i++) {
			root[i] = g.root[i].copy();
			depth[i] = g.depth[i];
			size[i] = g.size[i];
		}

	}

	public void setProgramID(int id) {
		programID = id;
	}

	public int getProgramID() {
		return programID;
	}

	// The evaluate method evaluates(executes) this program
	public void evaluate(ReturnData out[]) {
		for (int i = 0; i < numParts; i++) {
			if (root[i] == null)
				throw new RuntimeException("Root node is NULL");
		}
		for (int i = 0; i < numParts; i++) {
			if (out[i].getTypeNum() != root[i].getReturnType()) {
				// int j = out[i].getTypeNum();
				throw new RuntimeException(
						"Return type of root node does not match return type of out");
			}
		}

		for (int i = 0; i < numParts; i++) {
			root[i].evaluate(out[i]);
		}
	}

	// Sets the root of the tree to point to the new tree value.
	// If the old tree is no longer to be used it must be deleted
	// explicitly by calling deleteTree() below.
	public void setRoot(Node value, int place) {
		if (place < 0 || place >= numParts) {
			throw new IllegalArgumentException("Invalid place value");
		}
		root[place] = value;

		if (root[place] != null)
			computeSizeAndDepth(place);
	}

	public Node getRoot(int place) {
		if (place < 0 || place >= numParts)
			throw new IllegalArgumentException("Invalid place value");
		return root[place];
	}

	// Delete the current tree.
	public void deleteTree(int place) {
		if (place < 0 || place >= numParts)
			throw new IllegalArgumentException("Invalid place of value");
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
		for (int i = 0; i < numParts; i++) {
			if (root[i] == null) {
				throw new RuntimeException("Root " + i + " is null");
			}
			size[i] = root[i].computeSize();
			depth[i] = root[i].computeDepth(0);
		}
	}

	public void computeSizeAndDepth(int place) {
		if (place < 0 || place >= numParts) {
			throw new RuntimeException("Invalid place value: " + place);
		}

		if (root[place] == null)
			throw new RuntimeException("Root " + place + " is null");

		size[place] = root[place].computeSize();
		depth[place] = root[place].computeDepth(0);
	}

	// Gets a random node from the program tree
	public Node getRandomNode(int p) {
		if (p < 0 || p >= numParts) {
			throw new RuntimeException("Invalid p value: " + p);
		}
		Vector<Node> nodeList = new Vector<Node>();
		int place = p;
		//int place = (int) (config.randomNumGenerator.randNum() % numParts);
		//p = place;

		root[place].addToVector(nodeList);

		if (nodeList.size() != 0) {
			int index = (int) Math.abs(config.randomNumGenerator.randNum() % nodeList
					.size());
			return nodeList.get(index);
		} else
			return null;
	}

	// Get a random node from the program tree that returns typeNum
	public Node getRandomNode(int typeNum, int p) {
		if (p < 0 || p >= numParts) {
			throw new RuntimeException("Invalid p value: " + p);
		}
		Vector<Node> nodeList = new Vector<Node>();
		int index;
		int place = p;
		//int place = (int) (config.randomNumGenerator.randNum() % numParts);
		

		root[place].addToVector(nodeList, typeNum);

		if (nodeList.size() != 0) {
			index = (int) Math.abs(config.randomNumGenerator.randNum() % nodeList
					.size());
			return nodeList.get(index);
		} else
			return null;
	}

	public void print(StringBuffer s) {
		if (root == null)
			throw new RuntimeException("Root node is NULL");

		s.append(numParts);
		s.append(" ");
		for (int i = 0; i < numParts; i++) {
			s.append("Program");
			s.append(i);
			s.append(" ");
			root[i].print(s);
			s.append(" | ");
		}

	}

	public void setReturnType(int type) {
		returnType = type;
	}

	public int getReturnType() {
		return returnType;
	}

	public void setFitness(double f) {
		// If we get a fitness < 0 then there is a problem
		// this is usually caused by a double overflowing and
		// wrapping to a negative value
		if (f < 0.0) {
			System.err
					.println("GeneticProgram::setFitness : Warning: Fitness overflow "
							+ f);
		} else
			fitness = f;
	}

	public double getFitness() {
		return fitness;
	}

	public void setAdjFitness(double f) {
		adjustFitness = f;
	}

	public double getAdjFitness() {
		return adjustFitness;
	}

	// The copy method will create a new genetic
	// program which is a copy of the current program
	public GeneticProgram copy() {
		GeneticProgram tmp = new GeneticProgram(config);

		Node rootTmp;

		// tmp.setFitness(fitness);
		// tmp.setAdjFitness(adjustFitness);
		// tmp.setReturnType(returnType);

		for (int i = 0; i < numParts; i++) {
			if (root[i] != null) {
				rootTmp = root[i].copy();
				tmp.setRoot(rootTmp, i);
			} else {
				System.err
						.println("**** Warning GeneticProgram::copy() ******");
				System.err.println("Copying program will NULL root");

				tmp.setRoot(null, i);
			}
		}
		tmp.computeSizeAndDepth();

		return tmp;
	}

	/***************************************************************************
	 * parseProgram based on function parseTree originally written by Peter
	 * Wilson
	 **************************************************************************/
	public void parseProgram(String programString) {
		Node[] tmpRoot = null;
		Scanner scan = new Scanner(programString);

		try {
			tmpRoot = buildTree(scan);
		} catch (Exception e) {
			System.err.println("GeneticProgram::parseProgram\nError program: ");
			System.err.println(programString);
			e.printStackTrace();
			throw new RuntimeException("Parsing failed");
		}

		for (int i = 0; i < numParts; i++) {
			deleteTree(i);
			setRoot(tmpRoot[i], i);
		}
	}

	private Node[] buildTree(Scanner scan) {
		Node[] tmpRoot = null;// new Node*[numParts];
		Node arg = null;

		String token = scan.next();

		// token should be a number
		numParts = Integer.parseInt(token);
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
				tmpRoot[i] = config.funcSet.getNodeByName(token);
				token = scan.next();
				while (!token.equals(")")) {
					arg = buildTreeNode(scan, token);
					((Function) (tmpRoot[i])).setArgN(count, arg);
					count++;
					token = scan.next();
				}
				token = scan.next();
			} else // Must be terminal
			{
				tmpRoot[i] = config.termSet.getNodeByName(token);
				token = scan.next(); // read extra |
			}
		}

		// If we get an error it will usually be because of a terminal
		// or function being missing from the nodesets.

		return tmpRoot;
	}

	private Node buildTreeNode(Scanner scan, String token) {
		Node tmpRoot = null;
		Node arg = null;

		int count = 0;

		if (token.equalsIgnoreCase("|")) {
			/* End of program */
		} else if (token.equalsIgnoreCase("(")) // Start of function
		{
			token = scan.next();
			tmpRoot = config.funcSet.getNodeByName(token);
			token = scan.next();
			while (!token.equals(")")) {
				arg = buildTreeNode(scan, token);
				((Function) (tmpRoot)).setArgN(count, arg);
				count++;
				token = scan.next();
			}
		} else // Must be terminal
		{
			tmpRoot = config.termSet.getNodeByName(token);
		}

		// If we get an error it will usually be because of a terminal
		// or function being missing from the nodesets.

		return tmpRoot;
	}

}
