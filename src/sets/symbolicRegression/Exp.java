package sets.symbolicRegression;

import library.Function;
import library.GPConfig;
import library.Node;
import library.NodeFactory;
import library.ReturnData;

public class Exp extends Function{

	private static int KIND;

	public Exp() {
		super(ReturnDouble.TYPENUM, 1, "e");
		for (int i = 0; i < numArgs; i++) {
			setArgNReturnType(i, ReturnDouble.TYPENUM);
		}
	}

	@Override
	public Exp getNew(GPConfig config) {
		return new Exp();
	}

	@Override
	public void evaluate(ReturnData out) {
		ReturnDouble d = (ReturnDouble) out;
		getArgN(0).evaluate(d);
		d.setValue(Math.exp(d.value()));
	}

	@Override
	public Exp copy(GPConfig conf) {
		Exp a = (Exp) NodeFactory.newNode(getKind(), conf);
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
	public Exp generate(String s, GPConfig conf) {
		if (s.startsWith(getName())) {
			return (Exp) NodeFactory.newNode(getKind(), conf);
		}
		return null;
	}

	@Override
	public Exp generate(GPConfig conf) {
		return (Exp) NodeFactory.newNode(getKind(), conf);
	}

}
