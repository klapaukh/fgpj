package nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.image;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import nz.ac.vuw.ecs.fgpj.core.Fitness;
import nz.ac.vuw.ecs.fgpj.core.GPConfig;
import nz.ac.vuw.ecs.fgpj.core.GeneticProgram;
import nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.ReturnDouble;



public class MathImageFitness implements Fitness {
	private int xSize;
	private int ySize;
	private final String filename;
	private final double min = 0, max = 10;

	private int[][][] pixels;

	public MathImageFitness(String filename) {
		this.filename = filename;
	}

	public void initFitness() {
		Scanner scan;
		try {
			scan = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("sample.pnm cannot be read. No target to copmare to");
		}

		int y, x, c;

		String type = scan.next();

		if (!type.equals("P3")) {
			throw new RuntimeException("File " + filename + " has type " + type + " but should be P3");
		}

		xSize = scan.nextInt();
		ySize = scan.nextInt();

		if (xSize != ySize) {
			System.err.println("Warning: Image is not square");
		}

		int depth = scan.nextInt();

		if (depth != 255) {
			System.err.println("WARNING: Image depth is not 255");
		}

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
		ReturnDouble im[] = { new ReturnDouble(), new ReturnDouble(), new ReturnDouble() };
		for (i = 0; i < pop.size(); i++) {
			// initialise fitness to zero
			totalFitness = 0;

			GeneticProgram p = pop.get(i);

			double xStep = (max - min) / xSize;
			double yStep = (max - min) / ySize;

			double yVal = min;
			for (int y = 0; y < ySize; y++, yVal += yStep) {
				double xVal = min;
				for (int x = 0; x < xSize; x++, xVal += xStep) {
					im[0].setX(xVal);
					im[1].setX(xVal);
					im[2].setX(xVal);
					im[0].setY(yVal);
					im[1].setY(yVal);
					im[2].setY(yVal);
					p.evaluate(im);
					totalFitness += Math.abs(toNumber(im[0].value()) - pixels[x][y][0]);
					totalFitness += Math.abs(toNumber(im[1].value()) - pixels[x][y][1]);
					totalFitness += Math.abs(toNumber(im[2].value()) - pixels[x][y][2]);
				}
			}

			pop.get(i).setFitness(totalFitness);
		}
	}

	public void outputResults(GeneticProgram program, String filename, GPConfig config) {
		PrintStream file;
		try {
			file = new PrintStream(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		file.print("P3\n# best.pnm\n");
		StringBuilder s = new StringBuilder();
		program.print(s);
		file.print("#");
		file.println(s);
		file.print(xSize);
		file.print(" ");
		file.print(ySize);
		file.print("\n");
		file.print("255\n");

		ReturnDouble[] im = new ReturnDouble[] { new ReturnDouble(), new ReturnDouble(), new ReturnDouble() };

		double xStep = (max - min) / xSize;
		double yStep = (max - min) / ySize;
		double yVal = min;
		for (int y = 0; y < ySize; y++, yVal += yStep) {
			double xVal = min;
			for (int x = 0; x < xSize; x++, xVal += xStep) {
				im[0].setX(xVal);
				im[1].setX(xVal);
				im[2].setX(xVal);
				im[0].setY(yVal);
				im[1].setY(yVal);
				im[2].setY(yVal);

				program.evaluate(im);

				file.print(toNumber(im[0].value()));
				file.print(" ");
				file.print(toNumber(im[1].value()));
				file.print(" ");
				file.print(toNumber(im[2].value()));
				file.print(" ");
			}
			file.println();
		}

		file.println();
		file.close();
	}

	public boolean solutionFound(List<GeneticProgram> pop) {
		return false;
	}

	public int compare(GeneticProgram gp1, GeneticProgram gp2) {
		return -Double.compare(gp1.getFitness(), gp2.getFitness());
	}

	public void getResult(GeneticProgram gp, int sizeX, int sizeY, GPConfig config) {
		getResult(gp,sizeX,sizeY,"out.pnm",config);
	}
	
	public void getResult(GeneticProgram gp, int sizeX, int sizeY, String fname, GPConfig config) {

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
		file.print(sizeX);
		file.print(" ");
		file.print(sizeY);
		file.print("\n");
		file.print("255\n");

		ReturnDouble[] im = new ReturnDouble[] { new ReturnDouble(), new ReturnDouble(), new ReturnDouble() };


		double xStep = (max - min) / sizeX;
		double yStep = (max - min) / sizeY;
		double yVal = min;
		for (int y = 0; y < sizeY; y++, yVal += yStep) {
			double xVal = min;
			for (int x = 0; x < sizeX; x++, xVal += xStep) {
				im[0].setX(xVal);
				im[1].setX(xVal);
				im[2].setX(xVal);
				im[0].setY(yVal);
				im[1].setY(yVal);
				im[2].setY(yVal);

				gp.evaluate(im);

				file.print(toNumber(im[0].value()));
				file.print(" ");
				file.print(toNumber(im[1].value()));
				file.print(" ");
				file.print(toNumber(im[2].value()));
				file.print(" ");
			}
			file.println();
		}

		file.println();
		file.close();

	}

	public void finish() {
	}
	
	private int toNumber(double i){
//		int comp = (int) Math.signum(Double.compare(i, 0));
//		switch(comp){
//		case 0: return 0;
//		case -1: i = Math.abs(i);
//		default:
//			int v= (int)( i % 512.0);
//			return v%256;
//		}
		if(Double.isInfinite(i) || Double.isNaN(i)){
			return 0;
		}
		int v = (int)i;
		return  Math.abs(v % 256);
	}
}
