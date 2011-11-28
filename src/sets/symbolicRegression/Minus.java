package sets.symbolicRegression;


import library.Function;
import library.GPConfig;
import library.Node;
import library.NodeFactory;
import library.ReturnData;

public class Minus extends Function {

	private static int KIND;
	
	public Minus() {
		super(ReturnDouble.TYPENUM, 2, "-");
		for(int i = 0 ; i < numArgs; i++){
			setArgNReturnType(i, ReturnDouble.TYPENUM);
		}
	}

	@Override
	public Minus getNew(GPConfig config) {
		return new Minus();
	}

	@Override
	public void evaluate(ReturnData out) {
		ReturnDouble d = (ReturnDouble) out;
		getArgN(0).evaluate(d);
		double d1 = d.value();
		getArgN(1).evaluate(d);
		d.setValue(d1 - d.value());
	}

	@Override
	public Minus copy(GPConfig conf) {
		Minus a = (Minus)NodeFactory.newNode(getKind(), conf);
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
	public  Minus generate(String s, GPConfig conf) {
		if(s.startsWith(getName())){
			return (Minus) NodeFactory.newNode(getKind(), conf);
		}
		return null;
	}

	@Override
	public Minus generate(GPConfig conf) {
		return (Minus) NodeFactory.newNode(getKind(), conf);
	}

}
