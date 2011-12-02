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

public class ImageFitness implements Fitness {
	public static int xSize =1000;
	public static int ySize = 1000;

	private String filename;
	private int[][][] pixels;

	// private double power;

	public ImageFitness(String filename) {
		this.filename = filename;
	}

	public void initFitness() {
		Scanner scan;
		try {
			scan = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(filename + " cannot be read. No target to compare to");
		}

		int y, x, c;
		String type = scan.next();
		if (!type.equals("P3")) {
			throw new IllegalArgumentException(filename + " image is not a ppm image of type P3");
		}
		xSize = scan.nextInt();
		ySize = scan.nextInt();

		int depth = scan.nextInt();

		pixels = new int[xSize][ySize][3];
		for (y = 0; y < ySize; y++) {
			for (x = 0; x < xSize; x++) {
				for (c = 0; c < 3; c++) {
					pixels[x][y][c] = scan.nextInt();
				}
			}
		}

		// close the file
		scan.close();
	}

	public void assignFitness(List<GeneticProgram> pop, GPConfig config) {
		int i;

		double totalFitness;

		// outerloop - selects a program
		for (i = 0; i < pop.size(); i++) {
			// initialise fitness to zero
			totalFitness = 0;

			ReturnImage im[] = new ReturnImage[] { new ReturnImage(xSize, ySize) };

			pop.get(i).evaluate(im);

			Color p;
			for (int y = 0; y < ySize; y++) {
				for (int x = 0; x < xSize; x++) {
					p = im[0].getData(x, y);
					totalFitness += Math.abs((p.getRed()) - pixels[x][y][0]);
					totalFitness += Math.abs((p.getGreen()) - pixels[x][y][1]);
					totalFitness += Math.abs((p.getBlue()) - pixels[x][y][2]);
				}
			}

			pop.get(i).setFitness(totalFitness);
		}
	}

	public boolean solutionFound(List<GeneticProgram> pop) {
		return false;
	}

	public int compare(GeneticProgram gp1, GeneticProgram gp2) {
		return -Double.compare(gp1.getFitness(), gp2.getFitness());
	}

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
	}
}
