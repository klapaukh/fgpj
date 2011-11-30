package nz.ac.vuw.ecs.fgpj.examples.lines;
/*
FGPJ Genetic Programming library
Copyright (C) 2011  Roman Klapaukh

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
import java.awt.Color;
import java.util.Scanner;

import nz.ac.vuw.ecs.fgpj.core.GPConfig;
import nz.ac.vuw.ecs.fgpj.core.Node;
import nz.ac.vuw.ecs.fgpj.core.NodeFactory;
import nz.ac.vuw.ecs.fgpj.core.ReturnData;
import nz.ac.vuw.ecs.fgpj.core.Terminal;


public class SetColor extends Terminal {
	/**
	 * 
	 */
	public static int kind;
	public static final long serialVersionUID = -7950230195747877164L;
	private Color col;

	public SetColor(GPConfig conf) {
		super(ReturnColor.TYPENUM, "setcolor");
		if (conf == null) {
			System.out.println("BOO");
		}
		col = new Color((int) Math.abs(conf.randomNumGenerator.nextLong() % 256),
				(int) Math.abs(conf.randomNumGenerator.nextLong() % 256), (int) Math.abs(conf.randomNumGenerator
						.nextLong() % 256), (int) Math.abs(conf.randomNumGenerator.nextLong() % 256));

	}

	public SetColor(Color c, GPConfig conf) {
		super(ReturnColor.TYPENUM, "setcolor");
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

	public void print(StringBuilder s) {

		s.append(String.format("%sred%dgreen%dblue%dalpha%d", getName(), col.getRed(), col.getGreen(), col.getBlue(),
				col.getAlpha()));
	}

	private SetColor init(Color col) {
		this.col = col;
		return this;
	}

	public Node copy(GPConfig config) {
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
