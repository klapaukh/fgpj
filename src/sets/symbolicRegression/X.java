package sets.symbolicRegression;

import library.GPConfig;
import library.Node;
import library.NodeFactory;
import library.ReturnData;
import library.Terminal;

public class X extends Terminal{

	private static double value;
	private static int KIND;
	
	public X(){
		super(ReturnDouble.TYPENUM,"X");
	}
	
	@Override
	public X getNew(GPConfig config) {
		return new X();
	}

	@Override
	public void evaluate(ReturnData out) {
		ReturnDouble d = (ReturnDouble)out;
		d.setValue(X.value);
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
	public X generate(String s, GPConfig conf) {
		if(s.startsWith(getName()))
			return (X) NodeFactory.newNode(getKind(), conf);
		return null;
	}

	@Override
	public X generate(GPConfig conf) {
		return (X) NodeFactory.newNode(getKind(), conf);
	}
	
	public static void setValue(double val){
		X.value = val;
	}

}
