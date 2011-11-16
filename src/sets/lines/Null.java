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
		super(ReturnImage.TYPENUM, "null", conf);
	}

	public Terminal generate(String name, GPConfig conf) {
		if (name.equals("")) {
			return (Terminal) NodeFactory.newNode(getKind());
//			return  new Null(conf);
		} else if (name.startsWith("null")) {
			return (Terminal) NodeFactory.newNode(getKind());
//			return new Null(conf);
		}
		return null;
	}

	public void evaluate(ReturnData out) {
		if (out.getTypeNum() != (ReturnImage.TYPENUM))
			throw new IllegalArgumentException("Incorrect ReturnData type");

	}

	public void print(StringBuffer s) {
		s.append(getName());
	}

	public Node copy() {
		return NodeFactory.newNode(getKind());
//		return new Null(config);
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
