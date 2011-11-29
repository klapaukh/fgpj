package sets.symbolicRegression;


import library.Function;
import library.GPConfig;
import library.Node;
import library.NodeFactory;
import library.ReturnData;

public class Max extends Function {

	private static int KIND;
	
	public Max() {
		super(ReturnDouble.TYPENUM, 2, "Max");
		for(int i = 0 ; i < numArgs; i++){
			setArgNReturnType(i, ReturnDouble.TYPENUM);
		}
	}

	@Override
	public Max getNew(GPConfig config) {
		return new Max();
	}

	@Override
	public void evaluate(ReturnData out) {
		ReturnDouble d = (ReturnDouble) out;
		getArgN(0).evaluate(d);
		double d1 = d.value();
		getArgN(1).evaluate(d);
		d.setValue(Math.max(d.value() , d1));
	}

	@Override
	public Max copy(GPConfig conf) {
		Max a = (Max)NodeFactory.newNode(getKind(), conf);
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
	public  Max generate(String s, GPConfig conf) {
		if(s.startsWith(getName())){
			return (Max) NodeFactory.newNode(getKind(), conf);
		}
		return null;
	}

	@Override
	public Max generate(GPConfig conf) {
		return (Max) NodeFactory.newNode(getKind(), conf);
	}

}
