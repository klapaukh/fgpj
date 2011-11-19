package library;

import java.util.LinkedList;
import java.util.Queue;

public class NodeFactory {

	private Queue<Node>[] nodes;
	private static final NodeFactory n = new NodeFactory();
	private GPConfig conf;
	private int hit, miss;
	private int kinds = 0;

	private NodeFactory() {
		hit = miss = 0;
	}

	public static void delete(Node node) {
		if (node == null)
			return;
		node.setParent(null);
		node.setDepth(0);
		if (node instanceof Function) {
			// need to maul it's children
			Function f = (Function) node;
			for (int i = 0; i < f.maxArgs; i++) {
				delete(f.getArgN(i));
			}
			f.unhook();
		}
		n.nodes[node.getKind()].offer(node);

		// all done
	}

	public static Node newNode(int kind) {

		Queue<Node> l = n.nodes[kind];
		if (l.size() == 1) {
			n.miss++;
			return l.peek().getNew(n.conf);
		} else {
			n.hit++;
			return l.poll();
		}
	}

	public static void setConfig(GPConfig conf) {
		n.conf = conf;
	}

	@SuppressWarnings("unchecked")
	public static void teach(Node node) {
		node.setKind(n.kinds++);
		Queue<Node>[] t = n.nodes;
		n.nodes = new Queue[n.kinds];
		int i=0;
		for (; t != null && i < t.length; i++) {
			n.nodes[i] = t[i];
		}
		n.nodes[i] = new LinkedList<Node>();
		n.nodes[i].add(node.getNew(node.config));
	}

	public static void report() {
		System.out.printf("%s cache hits: %.2f\n", "%",(double)(100.0 * n.hit / (double)(n.hit + n.miss)));
		System.out.println(n.kinds + " different kinds");
		for (int i = 0; i < n.nodes.length; i++) {
			System.out.printf("\t%d:%d\n", i,n.nodes[i].size());
		}
	}
}
