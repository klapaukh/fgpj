package sets.symbolicRegression;

import library.GPConfig;
import library.Node;
import library.NodeFactory;
import library.ReturnData;
import library.Terminal;

public class Y extends Terminal {
	private static int KIND;

	public Y() {
		super(ReturnDouble.TYPENUM, "SafeY");
	}

	@Override
	public Y getNew(GPConfig config) {
		return new Y();
	}

	@Override
	public void evaluate(ReturnData out) {
		ReturnDouble d = (ReturnDouble) out;
		d.setValue(d.getY());
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
	public Y generate(String s, GPConfig conf) {
		if (s.startsWith(getName()))
			return (Y) NodeFactory.newNode(getKind(), conf);
		return null;
	}

	@Override
	public Y generate(GPConfig conf) {
		return (Y) NodeFactory.newNode(getKind(), conf);
	}

}
