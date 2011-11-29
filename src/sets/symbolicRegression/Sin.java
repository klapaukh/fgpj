package sets.symbolicRegression;

import library.Function;
import library.GPConfig;
import library.Node;
import library.NodeFactory;
import library.ReturnData;

public class Sin extends Function{

	private static int KIND;

	public Sin() {
		super(ReturnDouble.TYPENUM, 1, "Sin");
		for (int i = 0; i < numArgs; i++) {
			setArgNReturnType(i, ReturnDouble.TYPENUM);
		}
	}

	@Override
	public Sin getNew(GPConfig config) {
		return new Sin();
	}

	@Override
	public void evaluate(ReturnData out) {
		ReturnDouble d = (ReturnDouble) out;
		getArgN(0).evaluate(d);
		d.setValue(Math.sin(d.value()));
	}

	@Override
	public Sin copy(GPConfig conf) {
		Sin a = (Sin) NodeFactory.newNode(getKind(), conf);
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
	public Sin generate(String s, GPConfig conf) {
		if (s.startsWith(getName())) {
			return (Sin) NodeFactory.newNode(getKind(), conf);
		}
		return null;
	}

	@Override
	public Sin generate(GPConfig conf) {
		return (Sin) NodeFactory.newNode(getKind(), conf);
	}

}
