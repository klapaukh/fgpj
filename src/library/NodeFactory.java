package library;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
public class NodeFactory {
//
//	private Map<String, List<Node>> nodes;
//	private static final NodeFactory n = new NodeFactory();
//	private GPConfig conf;
//	private int hit,miss;
//	
//	private NodeFactory(){
//		nodes = new HashMap<String,List<Node>>();
//		hit = miss = 0;
//	}
//
//	public static void delete(Node node){
//		if(node == null) return;
//		List<Node> l = n.nodes.get(node.getName());
//		node.setParent(null);
//		node.setDepth(0);
//		l.add(node);
//		if(node instanceof Function){
//			//need to maul it's children
//			Function f = (Function) node;
//			for(int i=0;i<f.maxArgs;i++){
//				delete(f.getArgN(i));
//			}
//			f.unhook();
//		}
//		
//		//all done
//	}
//	
////	public static Node newNode(String name){
////		
////		List<Node> l = n.nodes.get(name);
////		if(l.size() == 1){
////			n.miss++;
////			return l.get(0).getNew(n.conf);
////		}else{
////			n.hit++;
////			return l.remove(l.size()-1);
////		}
////	}
//	
//	public static void setConfig(GPConfig conf){
//		n.conf = conf;
//	}
//	
//	public static void teach(Node node){
//		List<Node> l = n.nodes.get(node.getName());
//		if(l == null){
//			l = new LinkedList<Node>();
//			l.add(node);
//			n.nodes.put(node.getName(), l);
//		}
//	}
//	
//	public static void report(){
//		System.out.println("% cache hits: " + (100*n.hit/(n.hit+n.miss)));
//	}
}
