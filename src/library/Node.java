package library;

import java.util.List;

/**
 * The Node class represents a single Node in a program Tree.
 * 
 * @author Roma
 * 
 */
public abstract class Node {
	/**
	 * Return type of this node
	 */
	private int returnType;

	/**
	 * Current depth of program tree
	 */
	private int depth;

	/**
	 * This nodes parent in the program tree
	 */
	private Node parent;

	/**
	 * The name of this node
	 */
	private String name;

	/**
	 * The number of children this node has
	 */
	protected int numArgs;

	/**
	 * position of this node in the tree
	 */
	private int position;

	/**
	 * Make a new node with the specified number of arguments, return type and name
	 * 
	 * @param retType
	 *            The return type of this node
	 * @param numArgs
	 *            The number of argument this node takes
	 * @param name
	 *            The name of this node
	 */
	public Node(int retType, int numArgs, String name) {
		this.returnType = retType;
		this.parent = null;
		this.name = name;
		this.numArgs = numArgs;
	}

	/**
	 * Gets a new copy of the node. Guaranteed to be new and not come from the cache
	 * 
	 * @param config
	 *            config to generate the node with
	 * @return the new node
	 */
	public abstract <T extends Node> T getNew(GPConfig config);

	/**
	 * Get the return type of this node
	 * 
	 * @return the return type of this node
	 */
	public int getReturnType() {
		return returnType;
	}

	/**
	 * Set the current depth
	 * 
	 * @param d
	 *            current depth
	 */
	protected void setDepth(int d) {
		depth = d;
	}

	/**
	 * Get the current depth of the tree
	 * 
	 * @return current depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * The name of the node
	 * 
	 * @return the name of the node
	 */
	public String getName() {
		return name;
	}

	/**
	 * The number of children this node needs
	 * 
	 * @return number of child slots
	 */
	public int getNumArgs() {
		return numArgs;
	}

	/**
	 * The Node above this one in the program tree
	 * 
	 * @return Nodes parent in a program
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * Set the parent of this node
	 * 
	 * @param n
	 *            The node that is this ones parent
	 */
	public void setParent(Node n) {
		parent = n;
	}

	/**
	 * Evaluate this node and its subtree where out is the running value being computed Doing it this way allows for
	 * computation orderings that do work on the way down the tree
	 * 
	 * @param out
	 *            The running value being computed
	 */
	public abstract void evaluate(ReturnData out);

	/**
	 * Compute the size of the subtree
	 * 
	 * @return size of the subtree
	 */
	public abstract int computeSize();

	/**
	 * Compute the depth of the longest subtree, and set the value for depth of current node
	 * 
	 * @param curDepth
	 *            depth at so far
	 * @return depth of longest subtree
	 */
	public abstract int computeDepth(int curDepth);

	/**
	 * Add this node and all its children to the List (double dispatch trick)
	 * 
	 * @param list
	 *            the lsit to add to
	 */
	public abstract void addTreeToVector(List<Node> list);

	/**
	 * Add all the nodes in the subtree that are of the type typeNum (double dispatch trick)
	 * 
	 * @param list
	 *            the list to add the nodes to
	 * @param typeNum
	 *            the type that the nodes need to be to be added
	 */
	public abstract void addTreeToVector(List<Node> list, int typeNum);

	/**
	 * Print this node to s
	 * 
	 * @param s
	 *            The StringBuffer to print to
	 */
	public abstract void print(StringBuffer s);

	/**
	 * Return a copy of this node
	 * 
	 * @param conf
	 *            the config to copy it using
	 * @return a copy of this node
	 */
	public abstract Node copy(GPConfig conf);

	/**
	 * Sets the kind of a node (this is used only for memory management)
	 * 
	 * @param kind
	 *            The kind of the node - a unique int per type
	 */
	public abstract Node setKind(int kind);

	/**
	 * Returns the kind of the node. Must be the same as the last setKind
	 * 
	 * @return the kind of the node
	 */
	public abstract int getKind();

	/**
	 * Generate this node based on a string
	 * 
	 * @param s
	 *            generate this node based on this string represenation of it
	 * @param conf
	 *            the config to generate it using
	 * @return the generated Node
	 */
	public abstract <T extends Node> T generate(String s, GPConfig conf);

	/**
	 * Return a new copy of this node
	 * 
	 * @param conf
	 *            the config to generate it using
	 * @return the generated Node
	 */
	public abstract <T extends Node> T generate(GPConfig conf);

	public String toString() {
		StringBuffer s = new StringBuffer();
		this.print(s);
		return s.toString();
	}

	/**
	 * Gets a node at a specified position
	 * 
	 * @param i
	 *            position to get node at
	 * @return the node at this position
	 */
	public abstract Node getNode(int i);

	/**
	 * work out the numbering of each node
	 * @param parent the number of your parent
	 * @return the number of your largest child
	 */
	public abstract int computePositions(int parent);
	
	/** 
	 * Set the position of this node
	 * @param pos position of this node
	 */
	protected void setPosition(int pos){
		this.position = pos;
	}
	
	/**
	 * get the position of this node
	 * @return position of this node
	 */
	protected int getPosition(){
		return this.position;
	}
}
