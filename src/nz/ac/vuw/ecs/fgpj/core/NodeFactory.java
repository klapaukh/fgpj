package nz.ac.vuw.ecs.fgpj.core;
/*
FGPJ Genetic Programming library
Copyright (C) 2011  Roman Klapaukh

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
import java.util.LinkedList;
import java.util.Queue;

/**
 * NodeFactory recycles nodes. This is an attempt to try to reduce the amount of memory thrashing that goes on with
 * constant allocation of new nodes for the GP trees.
 * 
 * @author Roman Klapaukh
 * 
 */
public class NodeFactory {

	private Queue<Node>[] nodes;
	private static final NodeFactory n = new NodeFactory();
	private int hit, miss;
	private int kinds = 0;

	private NodeFactory() {
		hit = miss = 0;
	}

	/**
	 * Returns a node to the factory. There must be no external aliases of this Node.
	 * 
	 * @param node
	 *            The node to return to the factory
	 */
	public static void delete(Node node) {
		if (node == null) return;
		node.setParent(null);
		node.setDepth(0);
		if (node instanceof Function) {
			// need to maul it's children
			Function f = (Function) node;
			for (int i = 0; i < f.numArgs; i++) {
				delete(f.getArgN(i));
			}
			f.unhook();
		}
		n.nodes[node.getKind()].offer(node);
	}

	/**
	 * Get a new node of the specified kind
	 * @param kind The kind of node to get
	 * @param conf The config to create the node with
	 * @return A new node
	 */
	public static Node newNode(int kind, GPConfig conf) {
		Queue<Node> l = n.nodes[kind];
		if (l.size() == 1) {
			n.miss++;
			return l.peek().getNew(conf);
		} else {
			n.hit++;
			return l.poll();
		}
	}

	/**
	 * Add a new kind of node to the Factory, so that it can be generated.
	 * 
	 * @param node New kind of node to add
	 * @param config the config to create new nodes with
	 */
	@SuppressWarnings("unchecked")
	public static void teach(Node node, GPConfig config) {
		node.setKind(n.kinds++);
		Queue<Node>[] t = n.nodes;
		n.nodes = new Queue[n.kinds];
		int i = 0;
		for (; t != null && i < t.length; i++) {
			n.nodes[i] = t[i];
		}
		n.nodes[i] = new LinkedList<Node>();
		n.nodes[i].add(node.getNew(config));
	}

	/**
	 * Print a performance report for the cache
	 */
	public static void report() {
		System.out.printf("%s cache hits: %.2f\n", "%", (double) (100.0 * n.hit / (double) (n.hit + n.miss)));
		System.out.println(n.kinds + " different kinds");
		for (int i = 0; i < n.nodes.length; i++) {
			System.out.printf("\t%s:%d\n", n.nodes[i].peek().getName(), n.nodes[i].size());
		}
	}
}
