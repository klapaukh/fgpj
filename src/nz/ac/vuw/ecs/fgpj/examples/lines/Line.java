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
import java.awt.Graphics;
import java.util.Scanner;

import nz.ac.vuw.ecs.fgpj.core.Function;
import nz.ac.vuw.ecs.fgpj.core.GPConfig;
import nz.ac.vuw.ecs.fgpj.core.Node;
import nz.ac.vuw.ecs.fgpj.core.NodeFactory;
import nz.ac.vuw.ecs.fgpj.core.ReturnData;

public class Line extends Function {

	/**
	 * 
	 */
	public static final long serialVersionUID = 6154965376115885411L;

	private int x1, x2, y1, y2;

	public Line(GPConfig conf) {

		super(ReturnImage.TYPENUM, 4, "line");
		x1 = conf.randomNumGenerator.nextInt(ImageFitness.xSize + 1);
		x2 = conf.randomNumGenerator.nextInt(ImageFitness.xSize + 1);
		y1 = conf.randomNumGenerator.nextInt(ImageFitness.ySize + 1);
		y2 = conf.randomNumGenerator.nextInt(ImageFitness.ySize + 1);

		setArgNReturnType(0, ReturnColor.TYPENUM);
		for (int i = 1; i < numArgs; i++) {
			setArgNReturnType(i, ReturnImage.TYPENUM);
		}
	}

	public Line generate(String name, GPConfig conf) {

		int x1, x2, y1, y2;
		Scanner scan = new Scanner(name);
		scan.useDelimiter("[a-z,A-Z]+");
		x1 = scan.nextInt();
		x2 = scan.nextInt();
		y1 = scan.nextInt();
		y2 = scan.nextInt();
		Line l = ((Line) NodeFactory.newNode(getKind(), conf));
		l.x1 = x1;
		l.x2 = x2;
		l.y1 = y1;
		l.y2 = y2;
		return l;
	}

	public void evaluate(ReturnData out) {
		if (out.getTypeNum() != (ReturnImage.TYPENUM))
			throw new IllegalArgumentException("Incorrect ReturnData type");
		ReturnImage i = (ReturnImage) out;
		ReturnColor c = new ReturnColor();
		getArgN(0).evaluate(c);

		Graphics g = i.getGraphics();
		g.setColor(c.getColor());
		g.drawLine(x1, y1, x2, y2);

		for (int j = 1; j < numArgs; j++) {
			getArgN(j).evaluate(out);
		}
	}

	public void print(StringBuilder s) {

		s.append(" ( ");
		s.append(String.format("%sx%dx%dy%dy%d", getName(), x1, x2, y1, y2));

		for (int i = 0; i < numArgs; i++) {
			s.append(" ");
			getArgN(i).print(s);
		}

		s.append(" ) ");
	}

	public void init(Node n) {
		Line l = (Line)n;
		x1 = l.x1;
		x2 = l.x2;
		y1 = l.y1;
		y2 = l.y2;
	}

	@Override
	public Line getNew(GPConfig config) {
		return new Line(config);
	}

}
