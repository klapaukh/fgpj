package library;

import java.io.Serializable;
import java.util.Vector;

public abstract class Node implements Generable<Node>, Serializable {
	private int returnType; // The type number of the return type for this node

	private int depth; // The depth of this node in the program tree

	private Node parent; // The parent of this node in the program tree

	private String name; // How this node gets printed out in the program

	// code

	protected int maxArgs; // The maximum number of arguments this node can

	// accept

	protected GPConfig config; // Configuration object;

	public Node(int type, int numArgs, String n, GPConfig conf) {
		returnType = type;
		parent = null;
		name = n;
		maxArgs = numArgs;
		config = conf;
	}

	public Node(Node n) {
		returnType = n.returnType;
		parent = n.parent;
		name = n.name;
		maxArgs = n.maxArgs;
		config = n.config;
	}
	public abstract Node getNew(GPConfig config);

	public int getReturnType() {
		return returnType;
	}

	public void setDepth(int d) {
		depth = d;
	}

	public int getDepth() {
		return depth;
	}

	public String getName() {
		return name;
	}

	public int getMaxArgs() {
		return maxArgs;
	}

	public Node getParent() {
		return parent;
	}

	public Node getRootNode() {
		if (parent == null)
			return this;
		else {
			return parent.getRootNode();
		}
	}

	public void setParent(Node n) {
		parent = n;
	}

	public abstract void evaluate(ReturnData out);

	public abstract int computeSize();

	public abstract int computeDepth(int curDepth);

	public abstract void addToVector(Vector<Node> vec);

	public abstract void addToVector(Vector<Node> vec, int typeNum);

	public abstract void print(StringBuffer s);

	public abstract Node copy();
	
	/**
	 * Sets the kind of a node (this is used only for memory management)
	 * @param kind The kind of the node - a unique int per type
	 */
	public abstract Node setKind(int kind);
	
	/**
	 * Returns the kind of the node. Must be the same as the last setKind
	 * @return the kind of the node
	 */
	public abstract int getKind();
}
