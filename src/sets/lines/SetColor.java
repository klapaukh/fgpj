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
	public static final long serialVersionUID = -7950230195747877164L;
	private Color col;
	public GPConfig config;

	public SetColor(GPConfig conf) {
		super(ReturnColor.TYPENUM, "setcolor", conf);
		col = new Color((int) Math.abs(conf.randomNumGenerator.randNum() % 256), (int) Math.abs(conf.randomNumGenerator.randNum() % 256),
				(int) Math.abs(conf.randomNumGenerator.randNum() % 256), (int) Math.abs(conf.randomNumGenerator.randNum() % 256));

	}

	public SetColor(Color c, GPConfig conf) {
		super(ReturnColor.TYPENUM, "setcolor", conf);
		col = c;
	}

	public Terminal generate(String name, GPConfig conf) {
		if (name.equals("")){
//			return (Terminal)NodeFactory.newNode(getName());
			return new SetColor(conf);
		}
		else if (name.startsWith(getName())) {
			int r, g, b, a;
			name = name.substring(getName().length());
			Scanner scan = new Scanner(name);
			scan.useDelimiter("[a-z,A-Z]+");
			r = scan.nextInt();
			g = scan.nextInt();
			b = scan.nextInt();
			a = scan.nextInt();

			return new SetColor(new Color(r, g, b, a), conf);
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

	private Node init(Color col){
		this.col = col;
		return this;
	}
	
	public Node copy() {
//		return ((SetColor)NodeFactory.newNode(this.getName())).init(col);
		return new SetColor(col, config);
	}

	@Override
	public Node getNew(GPConfig config) {
		return new SetColor(config);
	}

}
