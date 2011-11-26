package sets.symbolicRegression;


import library.Function;
import library.GPConfig;
import library.Node;
import library.NodeFactory;
import library.ReturnData;

public class Add extends Function {

	private static int KIND;
	
	public Add() {
		super(ReturnDouble.TYPENUM, 2, "+");
		for(int i = 0 ; i < numArgs; i++){
			setArgNReturnType(i, ReturnDouble.TYPENUM);
		}
	}

	@Override
	public Add getNew(GPConfig config) {
		return new Add();
	}

	@Override
	public void evaluate(ReturnData out) {
		ReturnDouble d = (ReturnDouble) out;
		ReturnDouble d1 = new ReturnDouble();
		getArgN(0).evaluate(d1);
		d.setValue(d1.value());
		getArgN(1).evaluate(d1);
		d.setValue(d.value() + d1.value());
	}

	@Override
	public Add copy(GPConfig conf) {
		Add a = (Add)NodeFactory.newNode(getKind(), conf);
		for (int i = 0; i < getNumArgs(); i++) {
			a.setArgN(i, getArgN(i).copy(conf));
		}
		return a;

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
	public  Add generate(String s, GPConfig conf) {
		if(s.startsWith(getName())){
			return (Add) NodeFactory.newNode(getKind(), conf);
		}
		return null;
	}

	@Override
	public Add generate(GPConfig conf) {
		return (Add) NodeFactory.newNode(getKind(), conf);
	}

}
