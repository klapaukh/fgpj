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

public class Rect extends Function {

	public static final long serialVersionUID = 6154965376115885411L;

	private int x, width, y, height;

	public Rect(GPConfig conf)

	{

		super(ReturnImage.TYPENUM, 4, "rect");
		x = conf.randomNumGenerator.nextInt(ImageFitness.xSize + 1);
		width = conf.randomNumGenerator.nextInt(ImageFitness.xSize - x + 1);
		y = conf.randomNumGenerator.nextInt(ImageFitness.ySize + 1);
		height = conf.randomNumGenerator.nextInt(ImageFitness.ySize - y + 1);

		setArgNReturnType(0, ReturnColor.TYPENUM);
		for (int i = 1; i < numArgs; i++) {
			setArgNReturnType(i, ReturnImage.TYPENUM);
		}
	}

	public Rect generate(String name, GPConfig conf) {

		int x, width, y, height;
		Scanner scan = new Scanner(name);
		scan.useDelimiter("[a-z,A-Z]+");
		x = scan.nextInt();
		width = scan.nextInt();
		y = scan.nextInt();
		height = scan.nextInt();
		Rect r = ((Rect) NodeFactory.newNode(getKind(), conf));
		r.x = x;
		r.width = width;
		r.y = y;
		r.height = height;
		return r;
	}

	public void evaluate(ReturnData out) {
		if (out.getTypeNum() != (ReturnImage.TYPENUM))
			throw new IllegalArgumentException("Incorrect ReturnData type");
		ReturnImage i = (ReturnImage) out;
		ReturnColor c = new ReturnColor();
		getArgN(0).evaluate(c);

		Graphics g = i.getGraphics();
		g.setColor(c.getColor());
		g.fillRect(x, y, width, height);

		for (int j = 1; j < numArgs; j++) {
			getArgN(j).evaluate(out);
		}
	}

	public void print(StringBuilder s) {

		s.append(" ( ");
		s.append(String.format("%sx%dx%dy%dy%d", getName(), x, width, y, height));

		for (int i = 0; i < numArgs; i++) {
			s.append(" ");
			getArgN(i).print(s);
		}

		s.append(" ) ");
	}

	@Override
	public void init(Node n) {
		Rect r = (Rect) n;
		x = r.x;
		width = r.width;
		y = r.y;
		height = r.height;
	}

	@Override
	public Rect getNew(GPConfig config) {
		return new Rect(config);
	}

}
