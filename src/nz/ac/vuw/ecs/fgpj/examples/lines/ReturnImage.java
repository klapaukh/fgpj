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
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import nz.ac.vuw.ecs.fgpj.core.ReturnData;


public class ReturnImage extends ReturnData {

	private BufferedImage im;

	public String f;
	public static final int TYPENUM = 2;
	public final int xSize;
	public final int ySize;

	public ReturnImage(int width, int height) {
		super(TYPENUM);
		xSize = (width);
		ySize = (height);
		// Allocate memory
		im = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
	}

	public Graphics getGraphics() {
		return im.getGraphics();
	}

	public Color getData(int x, int y) {
		return new Color(im.getRGB(x, y));
	}

}
