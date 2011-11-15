package library;

import java.util.ArrayList;
import java.util.List;

public class NodeVector<T> {

	public class Element {
		public int returnType;

		public Generable<T> g;
	}

	private List<Element> vec;

	private GPConfig config;

	public NodeVector() {
		config = null;
		vec = new ArrayList<Element>();
	}

	public void setGPConfig(GPConfig conf) {
		config = conf;
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
			return vec.get(pos).g.generate("", config);
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
			ret = tmpStore.get(pos).g.generate("", config);
		}

		return ret;
	}

	public int size() {
		return vec.size();
	}

}
