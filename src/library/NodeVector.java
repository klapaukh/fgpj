package library;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a special List that allows for easy access to nodes by type, name, index and random value. It is aware of
 * node identity so its generate methods return clones.
 * 
 * @author Roma
 * 
 * @param <T>
 *            The type of Node that this NodeVector stores
 */
public class NodeVector<T extends Node> {

	private List<T> vec;

	/**
	 * Create a new NodeVector
	 */
	public NodeVector() {
		vec = new ArrayList<T>();
	}

	/**
	 * Add to the list
	 * 
	 * @param g
	 *            the node to add
	 */
	public void add(T g) {
		vec.add(g);
	}

	/**
	 * Get a new instance of the type that the name is
	 * 
	 * @param name
	 *            The name of the node
	 * @param conf
	 *            the GPConfig to generate the node with
	 * @return the generated node
	 */
	public T generateNodeByName(String name, GPConfig conf) {
		for (int i = 0; i < size(); i++) {
			if (vec.get(i).getName().equals(name)) {
				return vec.get(i).generate(name, conf);
			}
		}

		throw new RuntimeException("Could not find node " + name);
	}

	/**
	 * Generate a clone of the node at the position
	 * 
	 * @param pos
	 *            Index of node to generate
	 * @param config
	 *            the config to generate the node with
	 * @return the generated node
	 */
	public T generate(int pos, GPConfig config) {
		return vec.get(pos).generate(config);
	}

	/**
	 * 
	 * Get the size of the list
	 * 
	 * @return the size of the list
	 */
	public int size() {
		return vec.size();
	}

	/**
	 * Generate a random node in the list
	 * 
	 * @param config
	 *            the config to generate the node with
	 * @return a new random node
	 */
	public T generateRandomNode(GPConfig config) {
		int pos = (int) Math.abs(config.randomNumGenerator.nextLong() % vec.size());
		return vec.get(pos).generate(config);
	}

	/**
	 * Generate a new random node of a given type
	 * 
	 * @param returnType
	 *            the return type that the new node must have
	 * @param config
	 *            the config used to create the node
	 * @return the new random node
	 */
	public T generateRandomNode(int returnType, GPConfig config) {
		T ret = null;
		int numNodes = 0;
		for (int i = 0; i < vec.size(); i++) {
			if (vec.get(i).getReturnType() == returnType) {
				numNodes++;
			}
		}
		if (numNodes != 0) {
			int pos = Math.abs(config.randomNumGenerator.nextInt() % numNodes);
			numNodes = 0;
			for (int i = 0; i < vec.size(); i++) {
				if (vec.get(i).getReturnType() == returnType) {
					if (numNodes == pos) {
						return vec.get(i).generate(config);
					}
					numNodes++;
				}
			}
		}

		return ret;
	}
}
