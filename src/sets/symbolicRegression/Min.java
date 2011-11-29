package sets.symbolicRegression;


import library.Function;
import library.GPConfig;
import library.Node;
import library.NodeFactory;
import library.ReturnData;

public class Min extends Function {

	private static int KIND;
	
	public Min() {
		super(ReturnDouble.TYPENUM, 2, "Min");
		for(int i = 0 ; i < numArgs; i++){
			setArgNReturnType(i, ReturnDouble.TYPENUM);
		}
	}

	@Override
	public Min getNew(GPConfig config) {
		return new Min();
	}

	@Override
	public void evaluate(ReturnData out) {
		ReturnDouble d = (ReturnDouble) out;
		getArgN(0).evaluate(d);
		double d1 = d.value();
		getArgN(1).evaluate(d);
		d.setValue(Math.min(d.value() , d1));
	}

	@Override
	public Min copy(GPConfig conf) {
		Min a = (Min)NodeFactory.newNode(getKind(), conf);
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
	public  Min generate(String s, GPConfig conf) {
		if(s.startsWith(getName())){
			return (Min) NodeFactory.newNode(getKind(), conf);
		}
		return null;
	}

	@Override
	public Min generate(GPConfig conf) {
		return (Min) NodeFactory.newNode(getKind(), conf);
	}

}
