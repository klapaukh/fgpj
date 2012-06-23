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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import nz.ac.vuw.ecs.fgpj.core.Fitness;
import nz.ac.vuw.ecs.fgpj.core.GPConfig;
import nz.ac.vuw.ecs.fgpj.core.GeneticProgram;

/**
 * An implementation of the Fitness interface. This class judges the quality of
 * a program by how close to a target image is the resulting image. It requires
 * a ppm image in P3 format as a target to compare against.
 * 
 * 
 * It has a static x and y size field. While having it static seems silly, it
 * allows for node classes like Line and Oval to be able to adjust their values
 * according to the scale. This is not an ideal implementation though. It would
 * be better to have the nodes limited from 0 - 1 and the Return image class
 * scale them to the appropriate size. This however may lead to the impression
 * that the image could be scaled up and down. This is not the case, as the
 * spacing between lines would change, changing significantly how the image
 * looks.
 * 
 * @author roma
 * 
 */
public class ImageFitness extends Fitness {

	/**
	 * This represents the width of the image.
	 */
	public static int xSize = 1000;

	/**
	 * This represents the height of the image
	 */
	public static int ySize = 1000;

	// The filename of the image that is the target
	private String filename;

	// array of the color of each pixel as [x][y] and then the third index is
	// the color rgb 0-2 respectively.
	private int[][][] pixels;

	/**
	 * Create a new ImageFitness with the specified filename pointing to the
	 * target image
	 * 
	 * @param filename
	 *            PPM image in P3 format that is the "ideal" target
	 */
	public ImageFitness(String filename) {
		this.filename = filename;
	}

	public void initFitness() {
		Scanner scan;

		// Open the image file for reading
		try {
			scan = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(filename
					+ " cannot be read. No target to compare to");
		}

		int y, x, c;
		// Ensure that it is a P3 ppm image (check magic number)
		String type = scan.next();
		if (!type.equals("P3")) {
			throw new IllegalArgumentException(filename
					+ " image is not a ppm image of type P3");
		}
		xSize = scan.nextInt();
		ySize = scan.nextInt();

		int depth = scan.nextInt();

		// Read all colors for each pixel
		pixels = new int[xSize][ySize][3];
		for (y = 0; y < ySize; y++) {
			for (x = 0; x < xSize; x++) {
				for (c = 0; c < 3; c++) {
					pixels[x][y][c] = (int) (depth != 255 ? 255 * ((float) scan
							.nextInt() / depth) : scan.nextInt());

				}
			}
		}

		// close the file
		scan.close();
	}

	public boolean isDirty() {
		// Fitness function never changes
		return false;
	}

	public void assignFitness(GeneticProgram p, GPConfig config) {

		double totalFitness = 0;

		// Create a new blank image of the required size
		ReturnImage im[] = new ReturnImage[] { new ReturnImage(xSize, ySize) };

		p.evaluate(im);

		byte[] pd = im[0].getData();
		for (int y = 0; y < ySize; y++) {
			for (int x = 0; x < xSize; x++) {
				totalFitness += Math.abs((0xFF & pd[(y * xSize + x) * 3 + 2])
						- pixels[x][y][0]);
				totalFitness += Math.abs((0xFF & pd[(y * xSize + x) * 3 + 1])
						- pixels[x][y][1]);
				totalFitness += Math.abs((0xFF & pd[(y * xSize + x) * 3 + 0])
						- pixels[x][y][2]);
			}
		}

//		Color c;
//		for (int y = 0; y < ySize; y++) {
//			for (int x = 0; x < xSize; x++) {
//				c = im[0].getData(x, y);
//				totalFitness += Math.abs((c.getRed()) - pixels[x][y][0]);
//				totalFitness += Math.abs((c.getGreen()) - pixels[x][y][1]);
//				totalFitness += Math.abs((c.getBlue()) - pixels[x][y][2]);
//			}
//		}
		p.setFitness(totalFitness);
	}

	public boolean solutionFound(List<GeneticProgram> pop) {
		// no picture can ever be good enough to terminate early
		return false;
	}

	public int compare(double gp1, double gp2) {
		// Have to reverse the comparison, because lower fitness is better
		return -Double.compare(gp1, gp2);
	}

	/**
	 * Print out a given problem as an image to a file. The file produced will
	 * be a ppm file in P3 form.
	 * 
	 * @param gp
	 *            The genetic program to draw
	 * @param fname
	 *            The file name to draw it to.
	 */
	public void getResult(GeneticProgram gp, String fname) {

		PrintStream file;
		try {
			file = new PrintStream(new File(fname));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		// Set up ppm header

		file.print("P3\n# best.pnm\n");
		StringBuilder s = new StringBuilder();
		gp.print(s);
		file.print("#");
		file.println(s);
		file.print(xSize);
		file.print(" ");
		file.print(ySize);
		file.print("\n");
		file.print("255\n");

		ReturnImage[] im = new ReturnImage[] { new ReturnImage(xSize, ySize) };

		gp.evaluate(im);

		Color p;
		// Write each pixel
		for (int y = 0; y < ySize; y++) {
			for (int x = 0; x < xSize; x++) {
				p = im[0].getData(x, y);
				file.print(p.getRed());
				file.print(" ");
				file.print(p.getGreen());
				file.print(" ");
				file.print(p.getBlue());
				file.print(" ");
			}
			file.println();
		}

		file.println();
		file.close();

	}

	public void finish() {
		// No clean up is required
	}
}
