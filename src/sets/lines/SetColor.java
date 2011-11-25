package sets.lines;

import java.awt.Color;
import java.util.Scanner;

import library.GPConfig;
import library.Node;
import library.NodeFactory;
import library.ReturnData;
import library.Terminal;

public class SetColor extends Terminal {
	/**
	 * 
	 */
	public static int kind;
	public static final long serialVersionUID = -7950230195747877164L;
	private Color col;

	public SetColor(GPConfig conf) {
		super(ReturnColor.TYPENUM, "setcolor", conf);
		if (conf == null) {
			System.out.println("BOO");
		}
		col = new Color((int) Math.abs(conf.randomNumGenerator.nextLong() % 256),
				(int) Math.abs(conf.randomNumGenerator.nextLong() % 256), (int) Math.abs(conf.randomNumGenerator
						.nextLong() % 256), (int) Math.abs(conf.randomNumGenerator.nextLong() % 256));

	}

	public SetColor(Color c, GPConfig conf) {
		super(ReturnColor.TYPENUM, "setcolor", conf);
		if (conf == null) {
			System.out.println("BOO");
		}
		col = c;
	}

	public SetColor generate(GPConfig conf) {
		return (SetColor) NodeFactory.newNode(getKind(), conf);
		// return new SetColor(conf);
	}

	public SetColor generate(String name, GPConfig conf) {
		if (name.equals("")) {
			return (SetColor) NodeFactory.newNode(getKind(), conf);
			// return new SetColor(conf);
		} else if (name.startsWith(getName())) {
			int r, g, b, a;
			name = name.substring(getName().length());
			Scanner scan = new Scanner(name);
			scan.useDelimiter("[a-z,A-Z]+");
			r = scan.nextInt();
			g = scan.nextInt();
			b = scan.nextInt();
			a = scan.nextInt();

			return ((SetColor) NodeFactory.newNode(getKind(), conf)).init(new Color(r, g, b, a));
			// return new SetColor(new Color(r, g, b, a), conf);
		}

		return null;
	}

	public void evaluate(ReturnData out) {
		if (out.getTypeNum() != (ReturnColor.TYPENUM)) throw new IllegalArgumentException("Incorrect ReturnData type");
		ReturnColor i = (ReturnColor) out;

		i.setColor(col);
	}

	public void print(StringBuffer s) {

		s.append(String.format("%sred%dgreen%dblue%dalpha%d", getName(), col.getRed(), col.getGreen(), col.getBlue(),
				col.getAlpha()));
	}

	private SetColor init(Color col) {
		this.col = col;
		return this;
	}

	public Node copy() {
		return ((SetColor) NodeFactory.newNode(this.getKind(), config)).init(col);
		// return new SetColor(col, config);
	}

	@Override
	public SetColor getNew(GPConfig config) {
		return new SetColor(config);
	}

	@Override
	public SetColor setKind(int kind) {
		SetColor.kind = kind;
		return this;
	}

	@Override
	public int getKind() {
		return kind;
	}
}
