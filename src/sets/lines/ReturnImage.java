package sets.lines;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import library.GPConfig;
import library.ReturnData;

public class ReturnImage extends ReturnData {

	private BufferedImage im;

	public String f;
	public static final int TYPENUM = 2;
	public final int xSize;
	public final int ySize;

	public ReturnImage(int width, int height, GPConfig config) {
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
