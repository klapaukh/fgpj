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
import nz.ac.vuw.ecs.fgpj.core.ReturnData;

/**
 * This draws a rectangle on the image. It has a color child and three other
 * drawing children.
 *
 * @author roma
 *
 */
public class Rect extends Function {

	public static final long serialVersionUID = 6154965376115885411L;

	/**
	 * Coordinates for drawing this specific rectangle
	 */
	private int x, width, y, height;

	/**
	 * Create a new Rect
	 *
	 * @param conf
	 *            The current configuration
	 */
	public Rect(GPConfig conf)

	{
		// This node returns a ReturnImage, has 4 children and is called "rect"
		super(ReturnImage.TYPENUM, 4, "rect");

		// Generate a random rectangle inside the image
		x = conf.randomNumGenerator.nextInt(ImageFitness.xSize + 1);
		width = conf.randomNumGenerator.nextInt(ImageFitness.xSize - x + 1);
		y = conf.randomNumGenerator.nextInt(ImageFitness.ySize + 1);
		height = conf.randomNumGenerator.nextInt(ImageFitness.ySize - y + 1);

		// First child is a color
		setArgNReturnType(0, ReturnColor.TYPENUM);

		// Rest of the children are drawing nodes
		for (int i = 1; i < numArgs; i++) {
			setArgNReturnType(i, ReturnImage.TYPENUM);
		}
	}

	public Rect generate(String name, GPConfig conf) {
		// Read the Rect from the string generated in print
		int x, width, y, height;

		// scanner to parse the string
		Scanner scan = new Scanner(name);
		// Treat letters and commas as whitespace
		scan.useDelimiter("[a-z,A-Z]+");

		// read in values
		x = scan.nextInt();
		width = scan.nextInt();
		y = scan.nextInt();
		height = scan.nextInt();

		scan.close();
		// Create a new Rect from the factory
		Rect r = new Rect(conf);

		// Assign the fields to the new Rect
		r.x = x;
		r.width = width;
		r.y = y;
		r.height = height;

		// return the new Rect
		return r;
	}

	public void evaluate(ReturnData out) {
		// Cast is safe as we specified in the constructor
		ReturnImage i = (ReturnImage) out;

		// Create a ReturnColor to do the color subtree
		ReturnColor c = new ReturnColor();
		// evaluate the color this node should draw in
		getArgN(0).evaluate(c);

		// Get the graphics to draw on
		Graphics g = i.getGraphics();
		// Set the evaluated color
		g.setColor(c.getColor());
		// Draw the rect before evaluating the children. This should help deal
		// with the situation where the root node is bad, even though it is hard
		// to change
		g.fillRect(x, y, width, height);

		//Draw the drawing children
		for (int j = 1; j < numArgs; j++) {
			getArgN(j).evaluate(out);
		}
	}

	public void print(StringBuilder s) {
		//open parenthesis as it is a function
		s.append(" ( ");
		//Print the rect with all its constants
		s.append(String
				.format("%sx%dx%dy%dy%d", getName(), x, width, y, height));

		//print all the children separated by spaces
		for (int i = 0; i < numArgs; i++) {
			s.append(" ");
			getArgN(i).print(s);
		}

		//close parenthesis to show that this is finished
		s.append(" ) ");
	}

	@Override
	public void init(Node n) {
		//Copy over the fields from the passed in Rect to make this a clone
		Rect r = (Rect) n;
		x = r.x;
		width = r.width;
		y = r.y;
		height = r.height;
	}

	public void reinit(GPConfig conf) {
		// Node has state so needs to be reinitialised
		// Generate a random rectangle inside the image
		x = conf.randomNumGenerator.nextInt(ImageFitness.xSize + 1);
		width = conf.randomNumGenerator.nextInt(ImageFitness.xSize - x + 1);
		y = conf.randomNumGenerator.nextInt(ImageFitness.ySize + 1);
		height = conf.randomNumGenerator.nextInt(ImageFitness.ySize - y + 1);
	}

	@Override
	public Rect getNew(GPConfig config) {
		// Return a new Rect
		return new Rect(config);
	}

}
