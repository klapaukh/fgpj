package library;

import java.util.ArrayList;
import java.util.List;

public class NodeVector<T extends Node> {

	public class Element {
		public int returnType;

		public T g;
	}

	private List<Element> vec;

	protected GPConfig config;

	public NodeVector() {
		config = null;
		vec = new ArrayList<Element>();
	}
	
	public NodeVector(GPConfig conf) {
		config = conf;
		vec = new ArrayList<Element>();
	}

	public void setGPConfig(GPConfig conf) {
		config = conf;
	}
	
	public void addNodeToSet(int returnType, T g) {
		NodeFactory.teach(g);
		Element elem = new Element();
		elem.returnType = returnType;
		elem.g = g;

		addElement(elem);
	}
	
	
	public Node getNodeByName(String name) {
		Node newNode;
		int i;

		for (i = 0; i < size(); i++) {
			newNode = getElement(i).g.generate(name, config);

			if (newNode != null) {
				return newNode;
			}
		}

		throw new RuntimeException(
				"Error could not build function for token "
						+ name);
	}
	
	public void addElement(Element elem) {
		vec.add(elem);
	}

	public Element getElement(int pos) {
		if ((pos >= 0) && (pos < vec.size()))
			return vec.get(pos);
		else
			return null;
	}

	public T getRandomElement() {
		int pos = (int) Math.abs(config.randomNumGenerator.randNum() % vec.size());

		if ((pos >= 0) && (pos < vec.size()))
			return vec.get(pos).g.generate(config);
		else
			return null;
	}

	public T getRandomTypedElement(int returnType) {
		T ret = null;
		int pos;
		int i;

		List<Element>tmpStore = new ArrayList<Element>(vec.size());

		for (i = 0; i < vec.size(); i++) {
			if (vec.get(i).returnType == returnType) {
				tmpStore.add( vec.get(i));
			}
		}
		if (tmpStore.size() != 0) {
			pos = (int) Math.abs(config.randomNumGenerator.randNum() % tmpStore.size());
			ret = tmpStore.get(pos).g.generate(config);
		}

		return ret;
	}

	public int size() {
		return vec.size();
	}
	
	public Node getNodeByNumber(int position) {
		Node newNode;

		if (size() > position) {
			newNode = getElement(position).g.generate(config);
			if (newNode != null) {
				return newNode;
			}
		}

		throw new RuntimeException(
				"Error could not build function for the token");

	}

	public int getNodeReturnType(int position) {
		Element elem = getElement(position);

		if (elem != null)
			return elem.returnType;
		else
			return -1;
	}
	
	public T getGenFunction(int position) {
		Element elem = getElement(position);

		if (elem != null)
			return elem.g;
		else
			return null;
	}
	
	
	public T getRandomNode() {
		return getRandomElement();
	}

	public T getRandomNode(int returnType) {
		return getRandomTypedElement(returnType);
	}
}
