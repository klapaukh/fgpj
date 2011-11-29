package sets.symbolicRegression.Bad;

import library.GPConfig;
import library.Node;
import library.NodeFactory;
import library.ReturnData;
import library.Terminal;

public class UnsafeX extends Terminal{

	private static double value;
	private static int KIND;
	
	public UnsafeX(){
		super(UnsafeReturnDouble.TYPENUM,"X");
	}
	
	@Override
	public UnsafeX getNew(GPConfig config) {
		return new UnsafeX();
	}

	@Override
	public void evaluate(ReturnData out) {
		UnsafeReturnDouble d = (UnsafeReturnDouble)out;
		d.setValue(UnsafeX.value);
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
	public UnsafeX generate(String s, GPConfig conf) {
		if(s.startsWith(getName()))
			return (UnsafeX) NodeFactory.newNode(getKind(), conf);
		return null;
	}

	@Override
	public UnsafeX generate(GPConfig conf) {
		return (UnsafeX) NodeFactory.newNode(getKind(), conf);
	}
	
	public static void setValue(double val){
		UnsafeX.value = val;
	}

}
