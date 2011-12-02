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
import nz.ac.vuw.ecs.fgpj.core.NodeFactory;
import nz.ac.vuw.ecs.fgpj.core.ReturnData;


public class Rect extends Function {

	public static int kind;
	public static final long serialVersionUID = 6154965376115885411L;

	private int x, width, y, height;

	public Rect(GPConfig conf)

	{

		super(ReturnImage.TYPENUM, 4, "rect");
		x = conf.randomNumGenerator.nextInt(ImageFitness.xSize +1);
		width = conf.randomNumGenerator.nextInt(ImageFitness.xSize - x +1);
		y = conf.randomNumGenerator.nextInt(ImageFitness.ySize +1);
		height = conf.randomNumGenerator.nextInt(ImageFitness.ySize - y +1);

		setArgNReturnType(0, ReturnColor.TYPENUM);
		for (int i = 1; i < numArgs; i++) {
			setArgNReturnType(i, ReturnImage.TYPENUM);
		}
	}

	public Rect(int initx1, int initx2, int inity1, int inity2, GPConfig conf)

	{
		super(ReturnImage.TYPENUM, 4, "rect");
		x = (initx1);
		width = (initx2);
		y = (inity1);
		height = (inity2);

		setArgNReturnType(0, ReturnColor.TYPENUM);
		for (int i = 1; i < numArgs; i++) {
			setArgNReturnType(i, ReturnImage.TYPENUM);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Rect generate(GPConfig conf) {
		return (Rect) NodeFactory.newNode(getKind(), conf);
		// return new Rect(conf);
	}

	@SuppressWarnings("unchecked")
	public Rect generate(String name, GPConfig conf) {

		int x1, x2, y1, y2;
			Scanner scan = new Scanner(name);
			scan.useDelimiter("[a-z,A-Z]+");
			x1 = scan.nextInt();
			x2 = scan.nextInt();
			y1 = scan.nextInt();
			y2 = scan.nextInt();
			return ((Rect) NodeFactory.newNode(getKind(), conf)).init(x1, x2, y1, y2);
			// return new Rect(x1, x2, y1, y2,conf);

	}

	public void evaluate(ReturnData out) {
		if (out.getTypeNum() != (ReturnImage.TYPENUM)) throw new IllegalArgumentException("Incorrect ReturnData type");
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

	private Rect init(int initx1, int initx2, int inity1, int inity2) {
		x = (initx1);
		width = (initx2);
		y = (inity1);
		height = (inity2);
		return this;
	}

	public Rect copy(GPConfig config) {
		Rect tmp = ((Rect) NodeFactory.newNode(getKind(),config)).init(x, width, y, height);
		// Function tmp = new Rect(x1, x2, y1, y2,config);

		for (int i = 0; i < getNumArgs(); i++) {
			tmp.setArgN(i, getArgN(i).copy(config));
		}

		return tmp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Rect getNew(GPConfig config) {
		return new Rect(config);
	}

	@Override
	public Rect setKind(int kind) {
		Rect.kind = kind;
		return this;
	}

	@Override
	public int getKind() {
		return kind;
	}

}
