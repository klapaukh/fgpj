package sets.symbolicRegression;

import library.Function;
import library.GPConfig;
import library.GeneticProgram;
import library.Node;
import library.NodeFactory;
import library.ReturnData;
import library.Terminal;

public class SafeX extends Terminal {
	private double value;
	private static int KIND;

	public SafeX() {
		super(ReturnDouble.TYPENUM, "SafeX");
		value = Double.NaN;
	}

	@Override
	public SafeX getNew(GPConfig config) {
		return new SafeX();
	}

	@Override
	public void evaluate(ReturnData out) {
		ReturnDouble d = (ReturnDouble) out;
		if(Double.isNaN(value)) System.err.println("Unset");
		d.setValue(value);
	}

	@Override
	public Node copy(GPConfig conf) {
		return NodeFactory.newNode(getKind(), conf);
	}

	@Override
	public Node setKind(int kind) {
		KIND = kind;
		return this;
	}

	@Override
	public int getKind() {
		return KIND;
	}

	@Override
	public SafeX generate(String s, GPConfig conf) {
		if (s.startsWith(getName())) return (SafeX) NodeFactory.newNode(getKind(), conf);
		return null;
	}

	@Override
	public SafeX generate(GPConfig conf) {
		return (SafeX) NodeFactory.newNode(getKind(), conf);
	}

	public static void setValue(double val, GeneticProgram p) {
		for (int i = 0; i < p.numRoots(); i++) {
			Node n = p.getRoot(i);
			setValue(val,n);
		}
	}
	
	private static void setValue(double val, Node n){
		if(n instanceof SafeX){
			((SafeX) n).value = val;
		}else if(n instanceof Function){
			for( int i =0 ;i < n.getNumArgs() ;i ++){
				setValue(val,((Function) n).getArgN(i));
			}
		}
	}
}
