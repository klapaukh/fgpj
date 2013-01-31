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
 * This represents a line with a fixed location in an image. It has 4 children.
 * 3 of which are other draw operations and one is the color that this line is
 * drawn in.
 * 
 * @author roma
 * 
 */
public class Line extends Function {

	public static final long serialVersionUID = 6154965376115885411L;

	/**
	 * These are the coordinates that this line represents
	 */
	private int x1, x2, y1, y2;

	/**
	 * Create a line at a new valid place in the image
	 * 
	 * @param conf
	 *            The GPConfig being used for this run.
	 */
	public Line(GPConfig conf) {

		// Return a ReturnImage, has 4 children and is called line
		super(ReturnImage.TYPENUM, 4, "line");

		// Choose random coordinates for this line. Make sure they are within
		// the image
		x1 = conf.randomNumGenerator.nextInt(ImageFitness.xSize + 1);
		x2 = conf.randomNumGenerator.nextInt(ImageFitness.xSize + 1);
		y1 = conf.randomNumGenerator.nextInt(ImageFitness.ySize + 1);
		y2 = conf.randomNumGenerator.nextInt(ImageFitness.ySize + 1);

		//Set  the first child to return a color which will represent the color that this line is
		setArgNReturnType(0, ReturnColor.TYPENUM);
		for (int i = 1; i < numArgs; i++) {
			//Make all the other (3) children drawing operations
			setArgNReturnType(i, ReturnImage.TYPENUM);
		}
	}

	public Line generate(String name, GPConfig conf) {
		// Read the Line from the string generated in print
		int x1, x2, y1, y2;
		// create a new scanner over the name
		Scanner scan = new Scanner(name);
		// Treat letters and commas as whitespace
		scan.useDelimiter("[a-z,A-Z]+");
		// read in the coordinates
		x1 = scan.nextInt();
		x2 = scan.nextInt();
		y1 = scan.nextInt();
		y2 = scan.nextInt();
		// Create a new line from the factory
		Line l = new Line(conf);
		// assign the read values
		l.x1 = x1;
		l.x2 = x2;
		l.y1 = y1;
		l.y2 = y2;
		// return the new line
		return l;
	}

	public void evaluate(ReturnData out) {
		// Cast is safe as we specified in the constructor
		ReturnImage i = (ReturnImage) out;

		// Create a new ReturnColor to evaluate argument 0
		ReturnColor c = new ReturnColor();
		// Evaluate subtree 0
		getArgN(0).evaluate(c);

		// Get the graphics out of the return image
		Graphics g = i.getGraphics();
		// Set the color from subtree 0
		g.setColor(c.getColor());
		// Draw yourself. This forces a parent to draw itself before the
		// children. This makes the root node the first line drawn. In some ways
		// this is good as it means that it can get overridden later, and a bad
		// root node doesn't break everything
		g.drawLine(x1, y1, x2, y2);

		for (int j = 1; j < numArgs; j++) {
			// Evaluate the rest of the subtrees
			getArgN(j).evaluate(out);
		}
	}

	public void print(StringBuilder s) {
		// Print open parenthesis to show that it is a function with children
		s.append(" ( ");
		// print the function with all of its fixed arguments
		s.append(String.format("%sx%dx%dy%dy%d", getName(), x1, x2, y1, y2));

		for (int i = 0; i < numArgs; i++) {
			// print each child separated by a space
			s.append(" ");
			getArgN(i).print(s);
		}

		// Close parenthesis to show that the children are finished
		s.append(" ) ");
	}

	public void init(Node n) {
		// Copy accross the coordinates of the line
		Line l = (Line) n;
		x1 = l.x1;
		x2 = l.x2;
		y1 = l.y1;
		y2 = l.y2;
	}

	public void reinit(GPConfig conf) {
		// Need to create a new random position for the line
		// Choose random coordinates for this line. Make sure they are within
		// the image
		x1 = conf.randomNumGenerator.nextInt(ImageFitness.xSize + 1);
		x2 = conf.randomNumGenerator.nextInt(ImageFitness.xSize + 1);
		y1 = conf.randomNumGenerator.nextInt(ImageFitness.ySize + 1);
		y2 = conf.randomNumGenerator.nextInt(ImageFitness.ySize + 1);
	}

	@Override
	public Line getNew(GPConfig config) {
		// Create a new line
		return new Line(config);
	}

}
