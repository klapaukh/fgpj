package library;

import java.util.List;

/**
 * The Function class represents a function in the GP program tree
 * 
 * @author Roma
 * 
 */
public abstract class Function extends Node {

	private Node args[];

	private int argReturnTypes[];

	/**
	 * Make a new Function
	 * 
	 * @param type
	 *            return type of the function
	 * @param numArgs
	 *            number of arguments the function takes
	 * @param name
	 *            name of the function
	 */
	public Function(int type, int numArgs, String name) {
		super(type, numArgs, name);
		args = null;
		argReturnTypes = null;

		// If the number of arguments this function accepts is greater than zero (which is must be as it is a function
		// and not a Terminal
		// then we need to allocate the space to store pointers to the arguments
		// and to store their return types.
		args = new Node[numArgs];
		argReturnTypes = new int[numArgs];

		for (int i = 0; i < numArgs; i++) {
			args[i] = null;
			argReturnTypes[i] = -1;
		}
	}

	/**
	 * Set the Nth argument
	 * 
	 * @param N
	 *            the argument to set
	 * @param node
	 *            the node that is the argument
	 */
	public void setArgN(int N, Node node) {
		if (node == null) throw new IllegalArgumentException("Node is NULL");

		if (node.getReturnType() != argReturnTypes[N])
			throw new IllegalArgumentException("Incorrect return type for argument " + N);

		args[N] = node;
		node.setParent(this);
	}

	/**
	 * Get the argument at position N
	 * 
	 * @param N
	 *            The argument to get
	 * @return root of subtree at position N
	 */
	public Node getArgN(int N) {
		return args[N];
	}

	/**
	 * Set the return type of the Nth argument
	 * 
	 * @param N
	 *            position for which argument to set
	 * @param type
	 *            the type to set the argument to
	 */
	protected void setArgNReturnType(int N, int type) {
		if (type <= 0) throw new IllegalArgumentException("Invalid return type: " + type);

		argReturnTypes[N] = type;
	}

	/**
	 * Get the return type of the Nth argument
	 * 
	 * @param argument
	 *            to get type for
	 * @return Type of the argument
	 */
	public int getArgNReturnType(int N) {
		return argReturnTypes[N];
	}

	public int computeSize() {
		int size = 0;

		for (int i = 0; i < numArgs; i++) {
			size += args[i].computeSize();
		}

		return size + 1;
	}

	public int computeDepth(int curDepth) {
		setDepth(curDepth + 1);

		int retDepth = 0;
		int maxDepth = 0;

		for (int i = 0; i < numArgs; i++) {
			if ((retDepth = args[i].computeDepth(getDepth())) > maxDepth) maxDepth = retDepth;
		}

		return maxDepth + 1;
	}

	public void addTreeToVector(List<Node> list) {
		list.add(this);

		for (int i = 0; i < numArgs; i++) {
			args[i].addTreeToVector(list);
		}
	}

	public void addTreeToVector(List<Node> list, int typeNum) {
		if (getReturnType() == typeNum) list.add(this);

		for (int i = 0; i < numArgs; i++) {
			args[i].addTreeToVector(list, typeNum);
		}
	}

	public void print(StringBuffer s) {
		s.append(" ( ");
		s.append(getName());

		for (int i = 0; i < numArgs; i++) {
			s.append(" ");
			args[i].print(s);
		}

		s.append(" ) ");
	}

	/**
	 * Set child at position i to be null
	 * 
	 * @param i
	 *            position to set to null
	 */
	public void unhook(int i) {
		args[i] = null;
	}

	/**
	 * Set all children to null
	 * 
	 */
	public void unhook() {
		for (int i = 0; i < numArgs; i++) {
			args[i] = null;
		}
	}
	
	public int computePositions(int parent){
		int pos = parent+1;
		this.setPosition(pos);
		for( int i =0 ;i <args.length;i++){
			pos = args[i].computePositions(pos);
		}
		return parent +1;
	}
	
	public Node getNode(int node){
		if(this.getPosition() == node){
			return this;
		}
		for( int i =0 ;i <args.length;i++){
			Node n  = args[i].getNode(node);
			if(n != null){
				return n;
			}
		}
		return null;
	}
}
