package library;

public class TermSet extends NodeVector<Node> {

	private GPConfig config;

	private NodeVector<Node> theSet;

	public TermSet(GPConfig conf) {
		config = conf;
		theSet = new NodeVector<Node>();
		theSet.setGPConfig(config);
	}

	public void addNodeToSet(int returnType, Generable<Node> g) {
		NodeVector<Node>.Element elem = theSet.new Element();
		elem.returnType = returnType;
		elem.g = g;

		theSet.addElement(elem);
	}

	public Terminal getRandomNode() {
		return (Terminal)theSet.getRandomElement();
	}

	public Terminal getRandomNode(int returnType) {
		return (Terminal)theSet.getRandomTypedElement(returnType);
	}

	public Node getNodeByName(String name) {
		Node newNode;
		int i;

		for (i = 0; i < theSet.size(); i++) {
			newNode = theSet.getElement(i).g.generate(name, config);

			if (newNode != null) {
				return newNode;
			}
		}

		throw new RuntimeException(
				"FuncSet::getNodeByName Error could not build function for token "
						+ name);
	}

	public Node getNodeByNumber(int position) {
		Node newNode;

		if (theSet.size() > position) {
			newNode = theSet.getElement(position).g.generate("", config);
			if (newNode != null) {
				return newNode;
			}
		}

		throw new RuntimeException(
				"Error could not build function for the token");

	}

	public int getNodeReturnType(int position) {
		NodeVector<Node>.Element elem = theSet.getElement(position);

		if (elem != null)
			return elem.returnType;
		else
			return -1;
	}

	public Generable<Node> getGenFunction(int position) {
		NodeVector<Node>.Element elem = theSet.getElement(position);

		if (elem != null)
			return elem.g;
		else
			return null;
	}

	public int size() {
		return theSet.size();
	}

}
