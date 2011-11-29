package sets.lines;

import java.awt.Graphics;
import java.util.Scanner;

import library.Function;
import library.GPConfig;
import library.NodeFactory;
import library.ReturnData;

public class Rect extends Function {

	/**
	 * 
	 */
	public static int kind;
	public static final long serialVersionUID = 6154965376115885411L;

	private int x1, x2, y1, y2;

	public Rect(GPConfig conf)

	{

		super(ReturnImage.TYPENUM, 4, "rect");
		x1 = conf.randomNumGenerator.nextInt(ImageFitness.SIZE +1);
		x2 = conf.randomNumGenerator.nextInt(ImageFitness.SIZE +1);
		y1 = conf.randomNumGenerator.nextInt(ImageFitness.SIZE +1);
		y2 = conf.randomNumGenerator.nextInt(ImageFitness.SIZE +1);

		setArgNReturnType(0, ReturnColor.TYPENUM);
		for (int i = 1; i < numArgs; i++) {
			setArgNReturnType(i, ReturnImage.TYPENUM);
		}
	}

	public Rect(int initx1, int initx2, int inity1, int inity2, GPConfig conf)

	{
		super(ReturnImage.TYPENUM, 4, "rect");
		x1 = (initx1);
		x2 = (initx2);
		y1 = (inity1);
		y2 = (inity2);

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
		g.fillRect(x1, y1, x2, y2);

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

	private Rect init(int initx1, int initx2, int inity1, int inity2) {
		x1 = (initx1);
		x2 = (initx2);
		y1 = (inity1);
		y2 = (inity2);
		return this;
	}

	public Rect copy(GPConfig config) {
		Rect tmp = ((Rect) NodeFactory.newNode(getKind(),config)).init(x1, x2, y1, y2);
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