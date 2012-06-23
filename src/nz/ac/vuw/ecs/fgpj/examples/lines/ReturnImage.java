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
import java.awt.image.DataBufferByte;

import nz.ac.vuw.ecs.fgpj.core.ReturnData;

/**
 * This represents an Image as a return value. This is what nodes draw on to
 * build up the image as they go
 * 
 * @author roma
 * 
 */
public class ReturnImage extends ReturnData {

	/**
	 * Internal image to draw on
	 */
	private BufferedImage im;

	/**
	 * Typenum is a unique number that serves as the type of the return data
	 */
	public static final int TYPENUM = 2;

	/**
	 * width of the image
	 */
	private final int xSize;

	/**
	 * Height of the image
	 */
	private final int ySize;

	/**
	 * Create a new ReturnImage with a given width and height
	 * 
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 */
	public ReturnImage(int width, int height) {
		// notify supertype of what the typenum is
		super(TYPENUM);

		xSize = (width);
		ySize = (height);
		// Allocate an image to actually draw on
		im = new BufferedImage(xSize, ySize, BufferedImage.TYPE_3BYTE_BGR);
	}

	/**
	 * Returns a graphics on which you can actually draw on
	 * 
	 * @return Graphics to draw on
	 */
	public Graphics getGraphics() {
		return im.getGraphics();
	}

	/**
	 * Return the color of a specific point in the image
	 * 
	 * @param x
	 *            x coordinate in the image
	 * @param y
	 *            y coordinate in the image
	 * @return color a the point (x,y)
	 */
	public Color getData(int x, int y) {
		return new Color(im.getRGB(x, y));
	}

	/**
	 * The image data for the whole image
	 * 
	 * @return The colors for the whole image
	 */
	public byte[] getData() {
		return ((DataBufferByte) im.getRaster().getDataBuffer()).getData();

	}

}
