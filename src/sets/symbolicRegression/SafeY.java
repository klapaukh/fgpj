package sets.symbolicRegression;

import library.Function;
import library.GPConfig;
import library.GeneticProgram;
import library.Node;
import library.NodeFactory;
import library.ReturnData;
import library.Terminal;

public class SafeY extends Terminal {
	private double value;
	private static int KIND;

	public SafeY() {
		super(ReturnDouble.TYPENUM, "SafeY");
		value = Double.NaN;
	}

	@Override
	public SafeY getNew(GPConfig config) {
		return new SafeY();
	}

	@Override
	public void evaluate(ReturnData out) {
		ReturnDouble d = (ReturnDouble) out;
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
	public SafeY generate(String s, GPConfig conf) {
		if (s.startsWith(getName())) return (SafeY) NodeFactory.newNode(getKind(), conf);
		return null;
	}

	@Override
	public SafeY generate(GPConfig conf) {
		return (SafeY) NodeFactory.newNode(getKind(), conf);
	}

	public static void setValue(double val, GeneticProgram p) {
		for (int i = 0; i < p.numRoots(); i++) {
			Node n = p.getRoot(i);
			setValue(val,n);
		}
	}
	
	private static void setValue(double val, Node n){
		if(n instanceof SafeY){
			((SafeY) n).value = val;
		}else if(n instanceof Function){
			for( int i =0 ;i < n.getNumArgs() ;i ++){
				setValue(val,((Function) n).getArgN(i));
			}
		}
	}
}
