package sets.symbolicRegression;

import library.Function;
import library.GPConfig;
import library.Node;
import library.NodeFactory;
import library.ReturnData;

public class Tan extends Function{

	private static int KIND;

	public Tan() {
		super(ReturnDouble.TYPENUM, 1, "Tan");
		for (int i = 0; i < numArgs; i++) {
			setArgNReturnType(i, ReturnDouble.TYPENUM);
		}
	}

	@Override
	public Tan getNew(GPConfig config) {
		return new Tan();
	}

	@Override
	public void evaluate(ReturnData out) {
		ReturnDouble d = (ReturnDouble) out;
		getArgN(0).evaluate(d);
		d.setValue(Math.tan(d.value()));
	}

	@Override
	public Tan copy(GPConfig conf) {
		Tan a = (Tan) NodeFactory.newNode(getKind(), conf);
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
	public Tan generate(String s, GPConfig conf) {
		if (s.startsWith(getName())) {
			return (Tan) NodeFactory.newNode(getKind(), conf);
		}
		return null;
	}

	@Override
	public Tan generate(GPConfig conf) {
		return (Tan) NodeFactory.newNode(getKind(), conf);
	}

}
