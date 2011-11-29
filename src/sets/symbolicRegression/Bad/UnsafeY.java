package sets.symbolicRegression.Bad;

import library.GPConfig;
import library.Node;
import library.NodeFactory;
import library.ReturnData;
import library.Terminal;

public class UnsafeY extends Terminal{

	private static double value;
	private static int KIND;
	
	public UnsafeY(){
		super(UnsafeReturnDouble.TYPENUM,"Y");
	}
	
	@Override
	public UnsafeY getNew(GPConfig config) {
		return new UnsafeY();
	}

	@Override
	public void evaluate(ReturnData out) {
		UnsafeReturnDouble d = (UnsafeReturnDouble)out;
		d.setValue(UnsafeY.value);
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
	public UnsafeY generate(String s, GPConfig conf) {
		if(s.startsWith(getName()))
			return (UnsafeY) NodeFactory.newNode(getKind(), conf);
		return null;
	}

	@Override
	public UnsafeY generate(GPConfig conf) {
		return (UnsafeY) NodeFactory.newNode(getKind(), conf);
	}
	
	public static void setValue(double val){
		UnsafeY.value = val;
	}

}
