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
 * Function with draws an Oval. It has a child which represents its color, as
 * well as 3 other drawing node children
 * 
 * @author roma
 * 
 */
public class Oval extends Function {

	/**
	 * Fields to store how to draw this specific oval
	 */
	private int x, width, y, height;

	public Oval(GPConfig conf)

	{
		//This node returns a ReturnImage, has 4 children and is called "oval"
		super(ReturnImage.TYPENUM, 4, "oval");
		
		//generate a random position for the oval inside the image
		x = conf.randomNumGenerator.nextInt(ImageFitness.xSize + 1);
		width = conf.randomNumGenerator.nextInt(ImageFitness.xSize - x + 1);
		y = conf.randomNumGenerator.nextInt(ImageFitness.ySize + 1);
		height = conf.randomNumGenerator.nextInt(ImageFitness.ySize - y + 1);

		//Set the first child to be a color
		setArgNReturnType(0, ReturnColor.TYPENUM);
		
		//Rest of the children are drawing nodes
		for (int i = 1; i < numArgs; i++) {
			setArgNReturnType(i, ReturnImage.TYPENUM);
		}
	}

	public Oval generate(String name, GPConfig conf) {
		
		int x, width, y, height;
		//Create a scanner to read the string
		Scanner scan = new Scanner(name);
		//Make letters and commas whitespace
		scan.useDelimiter("[a-z,A-Z]+");
		//read in the constants
		x = scan.nextInt();
		width = scan.nextInt();
		y = scan.nextInt();
		height = scan.nextInt();
		
		//Get a new oval from the factory 
		Oval o = new Oval(conf);
		//assign the values from the string
		o.x = x;
		o.width = width;
		o.y = y;
		o.height = height;
		
		//return the new oval
		return o;
	}

	public void evaluate(ReturnData out) {
		// Cast is safe as we specified what the type we expect is in the
		// constructor
		ReturnImage i = (ReturnImage) out;

		// Create a new ReturnColor to evaluate the color this oval should be
		ReturnColor c = new ReturnColor();
		// evaluate the color subtree
		getArgN(0).evaluate(c);

		// get the graphics to draw on
		Graphics g = i.getGraphics();
		// Set the color to the color from the color subtree
		g.setColor(c.getColor());
		// Draw the oval on the canvas. Parents draw before children. This
		// should allow a program with a bad root node which is hard to replace
		// to still give a good result.
		g.fillOval(x, y, width, height);

		//evaluate all the drawing children
		for (int j = 1; j < numArgs; j++) {
			getArgN(j).evaluate(out);
		}
	}

	public void print(StringBuilder s) {
		// Parenthesis as this is a function
		s.append(" ( ");
		// Infix print this functions name with its fixed values
		s.append(String
				.format("%sx%dx%dy%dy%d", getName(), x, width, y, height));

		// print the children separated by spaces
		for (int i = 0; i < numArgs; i++) {
			s.append(" ");
			getArgN(i).print(s);
		}
		// close parenthesis to show the end of the children
		s.append(" ) ");
	}

	public void init(Node n) {
		// Initilise this with the values from the passed in oval
		Oval o = (Oval) n;
		x = o.x;
		width = o.width;
		y = o.y;
		height = o.height;
	}

	public void reinit(GPConfig conf) {
		// this node has state so needs reinitialising
		// Generate a new random oval inside the image bounds
		x = conf.randomNumGenerator.nextInt(ImageFitness.xSize + 1);
		width = conf.randomNumGenerator.nextInt(ImageFitness.xSize - x + 1);
		y = conf.randomNumGenerator.nextInt(ImageFitness.ySize + 1);
		height = conf.randomNumGenerator.nextInt(ImageFitness.ySize - y + 1);

	}

	@Override
	public Oval getNew(GPConfig config) {
		// Return a new Oval
		return new Oval(config);
	}

}
