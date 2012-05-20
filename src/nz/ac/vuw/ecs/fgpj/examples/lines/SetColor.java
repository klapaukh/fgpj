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

/**
 * This is a terminal that represents a color (RGBA) that is used to draw a
 * shape
 * 
 * @author roma
 * 
 */
public class SetColor extends Terminal {

	public static final long serialVersionUID = -7950230195747877164L;
	/**
	 * The color this Terminal represents
	 */
	private Color col;

	public SetColor(GPConfig conf) {
		// It returns a color, and is called "setcolor"
		super(ReturnColor.TYPENUM, "setcolor");
		// pick a new random color
		col = new Color(
				(int) Math.abs(conf.randomNumGenerator.nextLong() % 256),
				(int) Math.abs(conf.randomNumGenerator.nextLong() % 256),
				(int) Math.abs(conf.randomNumGenerator.nextLong() % 256),
				(int) Math.abs(conf.randomNumGenerator.nextLong() % 256));
	}

	public SetColor generate(String name, GPConfig conf) {
		int r, g, b, a;
		//remove the name from the string
		name = name.substring(getName().length());
		//set up a scanner to split the string
		Scanner scan = new Scanner(name);
		//use letters and commas for whitesapce
		scan.useDelimiter("[a-z,A-Z]+");
		//Read in the color components
		r = scan.nextInt();
		g = scan.nextInt();
		b = scan.nextInt();
		a = scan.nextInt();

		//Get a new SetColor node
		SetColor c = NodeFactory.newNode(this, conf);
		//Set its color to be the one from the string
		c.col = new Color(r, g, b, a);
		//return the constructed color
		return c;
	}

	public void evaluate(ReturnData out) {
		//Cast is safe as we specify what we expect
		ReturnColor i = (ReturnColor) out;

		//Set the color
		i.setColor(col);
	}

	public void print(StringBuilder s) {
		//print yourself with constants to the stringbuilder
		s.append(String.format("%sred%dgreen%dblue%dalpha%d", getName(),
				col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha()));
	}

	public void init(Node n) {
		//copy the color from the passed in node
		SetColor col = (SetColor) n;
		this.col = col.col;
	}

	public void reinit(GPConfig conf) {
		// pick a new random color
		col = new Color(
				(int) Math.abs(conf.randomNumGenerator.nextLong() % 256),
				(int) Math.abs(conf.randomNumGenerator.nextLong() % 256),
				(int) Math.abs(conf.randomNumGenerator.nextLong() % 256),
				(int) Math.abs(conf.randomNumGenerator.nextLong() % 256));
	}

	@Override
	public SetColor getNew(GPConfig config) {
		//return a new SetColor
		return new SetColor(config);
	}
}
