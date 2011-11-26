package sets.lines;

import library.GPConfig;
import library.Node;
import library.NodeFactory;
import library.ReturnData;
import library.Terminal;

public class Null extends Terminal {

	/**
	 * 
	 */
	public static int kind;
	public static final long serialVersionUID = 5747126155380314948L;

	public Null(GPConfig conf) {
		super(ReturnImage.TYPENUM, "null");
	}

	public Terminal generate(GPConfig conf) {
		return (Terminal) NodeFactory.newNode(getKind(), conf);
		// return new Null(conf);
	}

	public Terminal generate(String name, GPConfig conf) {
		if(name.startsWith(getName()))
			return generate(conf);
		return null;
	}

	public void evaluate(ReturnData out) {
		if (out.getTypeNum() != (ReturnImage.TYPENUM)) throw new IllegalArgumentException("Incorrect ReturnData type");

	}

	public void print(StringBuffer s) {
		s.append(getName());
	}

	public Node copy(GPConfig config) {
		return NodeFactory.newNode(getKind(), config);
		// return new Null(config);
	}

	@Override
	public Null getNew(GPConfig config) {
		return new Null(config);
	}

	@Override
	public Null setKind(int kind) {
		Null.kind = kind;
		return this;
	}

	@Override
	public int getKind() {
		return kind;
	}
}
